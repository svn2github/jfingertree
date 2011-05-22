package ftree;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Deep<M, T> extends FTree<M, T>
{

	private static Object[] reverse(Object[] t)
	{
		switch (t.length)
		{
		case 1:
			return t;
		case 2:
			return new Object[] { t[1], t[0] };
		case 3:
			return new Object[] { t[2], t[1], t[0] };
		case 4:
			return new Object[] { t[3], t[2], t[1], t[0] };
		}
		throw new IllegalStateException();
	}

	private static <M, T> FTree<M, T> treeOfDigits(Measure<M, T> measure,
			T[] digits)
	{
		switch (digits.length)
		{
		case 0:
			return treeOf(measure);
		case 1:
			return treeOf(measure, digits[0]);
		case 2:
			return treeOf(measure, digits[0], digits[1]);
		case 3:
			return treeOf(measure, digits[0], digits[1], digits[2]);
		case 4:
			return treeOf(measure, digits[0], digits[1], digits[2], digits[3]);
		}
		throw new IllegalStateException(String.valueOf(digits.length));
	}

	private static <M, T> FTree<M, T> reverseTreeOfDigits(Measure<M, T> measure,
			T[] digits)
	{
		switch (digits.length)
		{
		case 0:
			return treeOf(measure);
		case 1:
			return treeOf(measure, digits[0]);
		case 2:
			return treeOf(measure, digits[1], digits[0]);
		case 3:
			return treeOf(measure, digits[2], digits[1], digits[0]);
		case 4:
			return treeOf(measure, digits[3], digits[2], digits[1], digits[0]);
		}
		throw new IllegalStateException(String.valueOf(digits.length));
	}

	private static <M, T> T[] leftTail(T[] digits)
	{
		switch (digits.length)
		{
		case 2:
			return (T[]) new Object[] { digits[1] };
		case 3:
			return (T[]) new Object[] { digits[1], digits[2] };
		case 4:
			return (T[]) new Object[] { digits[1], digits[2], digits[3] };
		}
		throw new IllegalStateException();
	}

	private T[] pr;
	private FTree<M, Node<M, T>> m;
	private T[] sf;

	/*
	 * "Smart" constructor: may not be so smart when there is a cheaper way to
	 * calculate c (passing it using other constructor)
	 */
	private Deep(Measure<M, T> measure, T[] pr, FTree<M, Node<M, T>> m, T[] sf)
	{
		this(measure, measure.sum(measureDigits(measure, pr), m.cached(),
				measureDigits(measure, sf)), pr, m, sf);
	}

	public Deep(Measure<M, T> measure, M c, T[] pr, FTree<M, Node<M, T>> m, T[] sf)
	{
		super(measure, c);
		this.pr = pr;
		this.m = m;
		this.sf = sf;
	}

	private static <M, T> M measureDigits(Measure<M, T> measure, T[] d)
	{
		switch (d.length)
		{
		case 1:
			return measure.measure(d[0]);
		case 2:
			return measure.sum(measure.measure(d[0]), measure.measure(d[1]));
		case 3:
			return measure.sum(measure.measure(d[0]),
					measure.sum(measure.measure(d[1]), measure.measure(d[2])));
		case 4:
			return measure.sum(
					measure.measure(d[0]),
					measure.sum(measure.measure(d[1]),
							measure.sum(measure.measure(d[2]), measure.measure(d[3]))));
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	public FTree<M, T> addLeft(T v)
	{
		M newC = measure().sum(cached(), measure().measure(v));
		switch (pr.length)
		{
		case 1:
		{
			T[] newPr = (T[]) new Object[] { v, pr[0] };
			return new Deep<M, T>(measure(), newC, newPr, m, sf);
		}
		case 2:
		{
			T[] newPr = (T[]) new Object[] { v, pr[0], pr[1] };
			return new Deep<M, T>(measure(), newC, newPr, m, sf);
		}
		case 3:
		{
			T[] newPr = (T[]) new Object[] { v, pr[0], pr[1], pr[2] };
			return new Deep<M, T>(measure(), newC, newPr, m, sf);
		}
		case 4:
		{
			FTree<M, Node<M, T>> newM = m.addLeft(new Node3<M, T>(measure(), pr[1],
					pr[2], pr[3]));
			T[] newPr = (T[]) new Object[] { v, pr[0] };
			return new Deep<M, T>(measure(), newC, newPr, newM, sf);
		}
		}
		throw new IllegalStateException();
	};

	public FTree<M, T> addRight(T v)
	{
		M newC = measure().sum(cached(), measure().measure(v));
		switch (sf.length)
		{
		case 1:
		{
			T[] newSf = (T[]) new Object[] { v, sf[0] };
			return new Deep<M, T>(measure(), newC, pr, m, newSf);
		}
		case 2:
		{
			T[] newSf = (T[]) new Object[] { v, sf[0], sf[1] };
			return new Deep<M, T>(measure(), newC, pr, m, newSf);
		}
		case 3:
		{
			T[] newSf = (T[]) new Object[] { v, sf[0], sf[1], sf[2] };
			return new Deep<M, T>(measure(), newC, pr, m, newSf);
		}
		case 4:
		{
			FTree<M, Node<M, T>> newM = m.addRight(new Node3<M, T>(measure(), sf[3],
					sf[2], sf[1]));
			T[] newSf = (T[]) new Object[] { v, sf[0] };
			return new Deep<M, T>(measure(), newC, pr, newM, newSf);
		}
		}
		throw new IllegalStateException();
	};

	@Override
	public T leftHead()
	{
		return pr[0];
	}

	@Override
	public FTree<M, T> leftTail()
	{
		if (pr.length == 1)
		{
			if (m.isEmpty())
			{
				return reverseTreeOfDigits(measure(), sf);
			}
			return new Deep<M, T>(measure(), m.leftHead().toArray(), m.leftTail(), sf);
		}
		return new Deep(measure(), leftTail(pr), m, sf);
	}

	@Override
	public T rightHead()
	{
		return sf[0];
	}

	@Override
	public FTree<M, T> rightTail()
	{
		if (sf.length == 1)
		{
			if (m.isEmpty())
			{
				return treeOfDigits(measure(), pr);
			}
			return new Deep(measure(), pr, m.rightTail(), m.rightHead()
					.toReverseArray());
		}
		return new Deep(measure(), pr, m, leftTail(sf));
	}

	@Override
	public FTree<M, T> append(FTree<M, T> ft)
	{
		return ft.revappendDeep(this);
	}

	@Override
	protected FTree<M, T> revappendDeep(Deep<M, T> deep1)
	{
		FTree<M, Node<M, T>> newM = addDigits0(measure(), deep1.m, deep1.sf, pr, m);
		return new Deep<M, T>(measure(), deep1.pr, newM, sf);
	}

	protected FTree<M, T> append1(T v, FTree<M, T> ft)
	{
		return ft.revappendDeep1(v, this);
	}

	protected FTree<M, T> revappendDeep1(T v, Deep<M, T> deep1)
	{
		FTree<M, Node<M, T>> newM = addDigits1(measure(), deep1.m, deep1.sf, v, pr,
				m);
		return new Deep<M, T>(measure(), deep1.pr, newM, sf);
	}

	protected FTree<M, T> append2(T v1, T v2, FTree<M, T> ft)
	{
		return ft.revappendDeep2(v1, v2, this);
	}

	protected FTree<M, T> revappendDeep2(T v1, T v2, Deep<M, T> deep1)
	{
		FTree<M, Node<M, T>> newM = addDigits2(measure(), deep1.m, deep1.sf, v1,
				v2, pr, m);
		return new Deep<M, T>(measure(), deep1.pr, newM, sf);
	}

	protected FTree<M, T> append3(T v1, T v2, T v3, FTree<M, T> ft)
	{
		return ft.revappendDeep3(v1, v2, v3, this);
	}

	protected FTree<M, T> revappendDeep3(T v1, T v2, T v3, Deep<M, T> deep1)
	{
		FTree<M, Node<M, T>> newM = addDigits3(measure(), deep1.m, deep1.sf, v1,
				v2, v3, pr, m);
		return new Deep<M, T>(measure(), deep1.pr, newM, sf);
	}

	protected FTree<M, T> append4(T v1, T v2, T v3, T v4, FTree<M, T> ft)
	{
		return ft.revappendDeep4(v1, v2, v3, v4, this);
	}

	protected FTree<M, T> revappendDeep4(T v1, T v2, T v3, T v4, Deep<M, T> deep1)
	{
		FTree<M, Node<M, T>> newM = addDigits4(measure(), deep1.m, deep1.sf, v1,
				v2, v3, v4, pr, m);
		return new Deep<M, T>(measure(), deep1.pr, newM, sf);
	}

	private static <M, T> FTree<M, Node<M, T>> addDigits0(Measure<M, T> measure,
			FTree<M, Node<M, T>> m1, T[] d1, T[] d2, FTree<M, Node<M, T>> m2)
	{
		switch (d1.length)
		{
		case 1:
			switch (d2.length)
			{
			case 1:
				return m1.append1(new Node2(measure, d1[0], d2[0]), m2);
			case 2:
				return m1.append1(new Node3(measure, d1[0], d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node2(measure, d1[0], d2[0]), new Node2(measure,
						d2[1], d2[2]), m2);
			case 4:
				return m1.append2(new Node3(measure, d1[0], d2[0], d2[1]), new Node2(
						measure, d2[2], d2[3]), m2);
			}
		case 2:
			switch (d2.length)
			{
			case 1:
				return m1.append1(new Node3(measure, d1[1], d1[0], d2[0]), m2);
			case 2:
				return m1.append2(new Node2(measure, d1[1], d1[0]), new Node2(measure,
						d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node3(measure, d1[1], d1[0], d2[0]), new Node2(
						measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append2(new Node3(measure, d1[1], d1[0], d2[0]), new Node3(
						measure, d2[1], d2[2], d2[3]), m2);
			}
		case 3:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node2(measure, d1[2], d1[1]), new Node2(measure,
						d1[0], d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, d2[0], d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 4:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[3], d1[2], d1[1]), new Node2(
						measure, d1[0], d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node2(
						measure, d1[0], d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1
						.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
								measure, d1[0], d2[0], d2[1]),
								new Node2(measure, d2[2], d2[3]), m2);
			}
		default:
			throw new IllegalStateException();
		}
	}

	private static <M, T> FTree<M, Node<M, T>> addDigits1(Measure<M, T> measure,
			FTree<M, Node<M, T>> m1, T[] d1, T v, T[] d2, FTree<M, Node<M, T>> m2)
	{
		switch (d1.length)
		{
		case 1:
			switch (d2.length)
			{
			case 1:
				return m1.append1(new Node3(measure, d1[0], v, d2[0]), m2);
			case 2:
				return m1.append2(new Node2(measure, d1[0], v), new Node2(measure,
						d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node3(measure, d1[0], v, d2[0]), new Node2(
						measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append2(new Node3(measure, d1[0], v, d2[0]), new Node3(
						measure, d2[1], d2[2], d2[3]), m2);
			}
		case 2:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node2(measure, d1[1], d1[0]), new Node2(measure,
						v, d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[1], d1[0], v), new Node2(
						measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node3(measure, d1[1], d1[0], v), new Node3(
						measure, d2[0], d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[1], d1[0], v), new Node2(
						measure, d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 3:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, v, d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, v, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v, d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 4:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node2(
						measure, d1[0], v), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v, d2[0]), new Node3(measure, d2[1], d2[2], d2[3]),
						m2);
			}
		default:
			throw new IllegalStateException();
		}
	}

	private static <M, T> FTree<M, Node<M, T>> addDigits2(Measure<M, T> measure,
			FTree<M, Node<M, T>> m1, T[] d1, T v1, T v2, T[] d2,
			FTree<M, Node<M, T>> m2)
	{
		switch (d1.length)
		{
		case 1:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node2(measure, d1[0], v1), new Node2(measure, v2,
						d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[0], v1, v2), new Node2(measure,
						d2[0], d2[1]), m2);
			case 3:
				return m1.append2(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						d2[0], d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node2(measure,
						d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 2:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[1], d1[0], v1), new Node2(
						measure, v2, d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node2(
						measure, v2, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 3:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, v1, v2), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, d2[0]), new Node3(measure, d2[1], d2[2], d2[3]),
						m2);
			}
		case 4:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node2(
						measure, d1[0], v1), new Node2(measure, v2, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, d2[0], d2[1], d2[2]),
						m2);
			case 4:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node2(measure, d2[0], d2[1]),
						new Node2(measure, d2[2], d2[3]), m2);
			}
		default:
			throw new IllegalStateException();
		}
	}

	private static <M, T> FTree<M, Node<M, T>> addDigits3(Measure<M, T> measure,
			FTree<M, Node<M, T>> m1, T[] d1, T v1, T v2, T v3, T[] d2,
			FTree<M, Node<M, T>> m2)
	{
		switch (d1.length)
		{
		case 1:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[0], v1, v2), new Node2(measure,
						v3, d2[0]), m2);
			case 2:
				return m1.append2(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						v3, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node2(measure,
						v3, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						v3, d2[0], d2[1]), new Node2(measure, d2[2], d2[3]), m2);
			}
		case 2:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node2(
						measure, v2, v3), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, d2[0]), new Node3(measure, d2[1], d2[2], d2[3]),
						m2);
			}
		case 3:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node2(
						measure, v1, v2), new Node2(measure, v3, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node3(measure, d2[0], d2[1], d2[2]), m2);
			case 4:
				return m1.append4(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node2(measure, d2[0], d2[1]), new Node2(
						measure, d2[2], d2[3]), m2);
			}
		case 4:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node2(measure, v3, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, v3, d2[0], d2[1]), m2);
			case 3:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node2(measure, v3, d2[0]), new Node2(
						measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, v3, d2[0], d2[1]),
						new Node2(measure, d2[2], d2[3]), m2);
			}
		default:
			throw new IllegalStateException();
		}
	}

	private static <M, T> FTree<M, Node<M, T>> addDigits4(Measure<M, T> measure,
			FTree<M, Node<M, T>> m1, T[] d1, T v1, T v2, T v3, T v4, T[] d2,
			FTree<M, Node<M, T>> m2)
	{
		switch (d1.length)
		{
		case 1:
			switch (d2.length)
			{
			case 1:
				return m1.append2(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						v3, v4, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node2(measure,
						v3, v4), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						v3, v4, d2[0]), new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append3(new Node3(measure, d1[0], v1, v2), new Node3(measure,
						v3, v4, d2[0]), new Node3(measure, d2[1], d2[2], d2[3]), m2);
			}
		case 2:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node2(
						measure, v2, v3), new Node2(measure, v4, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, v4), new Node2(measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append3(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, v4), new Node3(measure, d2[0], d2[1], d2[2]), m2);
			case 4:
				return m1.append4(new Node3(measure, d1[1], d1[0], v1), new Node3(
						measure, v2, v3, v4), new Node2(measure, d2[0], d2[1]), new Node2(
						measure, d2[2], d2[3]), m2);
			}
		case 3:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node2(measure, v4, d2[0]), m2);
			case 2:
				return m1.append3(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node3(measure, v4, d2[0], d2[1]), m2);
			case 3:
				return m1.append4(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node2(measure, v4, d2[0]), new Node2(
						measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append4(new Node3(measure, d1[2], d1[1], d1[0]), new Node3(
						measure, v1, v2, v3), new Node3(measure, v4, d2[0], d2[1]),
						new Node2(measure, d2[2], d2[3]), m2);
			}
		case 4:
			switch (d2.length)
			{
			case 1:
				return m1.append3(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, v3, v4, d2[0]), m2);
			case 2:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node2(measure, v3, v4), new Node2(
						measure, d2[0], d2[1]), m2);
			case 3:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, v3, v4, d2[0]),
						new Node2(measure, d2[1], d2[2]), m2);
			case 4:
				return m1.append4(new Node3(measure, d1[3], d1[2], d1[1]), new Node3(
						measure, d1[0], v1, v2), new Node3(measure, v3, v4, d2[0]),
						new Node3(measure, d2[1], d2[2], d2[3]), m2);
			}
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public String toString()
	{
		return "<" + Arrays.toString(pr) + ", " + m + ", "
				+ Arrays.toString(reverse(sf)) + ">";
	}

	@Override
	public String toStringWithMeasures()
	{
		return "<#" + cached() + "# #" + measureDigits(measure(), pr) + "#"
				+ Arrays.toString(pr) + ", " + m.toStringWithMeasures() + ", #"
				+ measureDigits(measure(), sf) + "#" + Arrays.toString(reverse(sf))
				+ ">";
	}

	private static <T> T[][] splitDigits(T[] d, int p)
	{
		switch ((d.length << 2) + p)
		{
		case 4:
			return (T[][]) new Object[][] { new Object[0], new Object[0] };
		case 8:
			return (T[][]) new Object[][] { new Object[0], new Object[] { d[1] } };
		case 9:
			return (T[][]) new Object[][] { new Object[] { d[0] }, new Object[0] };
		case 12:
			return (T[][]) new Object[][] { new Object[0],
					new Object[] { d[1], d[2] } };
		case 13:
			return (T[][]) new Object[][] { new Object[] { d[0] },
					new Object[] { d[2] } };
		case 14:
			return (T[][]) new Object[][] { new Object[] { d[0], d[1] },
					new Object[0] };
		case 16:
			return (T[][]) new Object[][] { new Object[0],
					new Object[] { d[1], d[2], d[3] } };
		case 17:
			return (T[][]) new Object[][] { new Object[] { d[0] },
					new Object[] { d[2], d[3] } };
		case 18:
			return (T[][]) new Object[][] { new Object[] { d[0], d[1] },
					new Object[] { d[3] } };
		case 19:
			return (T[][]) new Object[][] { new Object[] { d[0], d[1], d[2] },
					new Object[0] };
		default:
			throw new IllegalStateException();
		}
	}

	public Split<M, T> split(Predicate<M> p, M i)
	{
		M r = i;
		for (int ipr = 0; ipr < pr.length; ipr++)
		{
			r = measure().sum(r, measure().measure(pr[ipr]));
			if (p.apply(r))
			{
				T[][] split = splitDigits(pr, ipr);
				if (split[1].length == 0)
				{
					if (m.isEmpty())
					{
						return new Split<M, T>(treeOfDigits(measure(), split[0]), pr[ipr],
								reverseTreeOfDigits(measure(), sf));
					}
					else
					{
						return new Split<M, T>(treeOfDigits(measure(), split[0]), pr[ipr],
								new Deep(measure(), m.leftHead().toArray(), m.leftTail(), sf));
					}
				}
				else
				{
					return new Split<M, T>(treeOfDigits(measure(), split[0]), pr[ipr],
							new Deep(measure(), split[1], m, sf));
				}
			}
		}
		M mpr = r;
		r = measure().sum(r, m.cached());
		if (p.apply(r))
		{
			Split<M, Node<M, T>> msplit = m.split(p, mpr);
			T[] vs = msplit.getCenter().toArray();
			mpr = measure().sum(mpr, msplit.getLeft().cached(),
					measure().measure(vs[0]));
			if (p.apply(mpr))
			{
				switch (vs.length)
				{
				case 2: // *n1* n2
				{
					if (msplit.getLeft().isEmpty())
					{
						return new Split<M, T>(treeOfDigits(measure(), pr), vs[0],
								new Deep(measure(), new Object[] { vs[1] }, msplit.getRight(),
										sf));
					}
					return new Split<M, T>(new Deep<M, T>(measure(), pr, msplit.getLeft()
							.rightTail(), msplit.getLeft().rightHead().toReverseArray()),
							vs[0], new Deep(measure(), new Object[] { vs[1] },
									msplit.getRight(), sf));
				}
				case 3: // *n1* n2 n3
				{
					if (msplit.getLeft().isEmpty())
					{
						return new Split<M, T>(treeOfDigits(measure(), pr), vs[0],
								new Deep(measure(), new Object[] { vs[1], vs[2] },
										msplit.getRight(), sf));
					}
					return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft()
							.rightTail(), msplit.getLeft().rightHead().toReverseArray()),
							vs[0], new Deep(measure(), new Object[] { vs[1], vs[2] },
									msplit.getRight(), sf));
				}
				}
			}
			mpr = measure().sum(mpr, measure().measure(vs[1]));
			if (p.apply(mpr))
			{
				switch (vs.length)
				{
				case 2: // n1 *n2*
				{
					if (msplit.getRight().isEmpty())
					{
						return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft(),
								new Object[] { vs[0] }), vs[1], reverseTreeOfDigits(measure(),
								sf));
					}
					return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft(),
							new Object[] { vs[0] }), vs[1], new Deep(measure(), msplit
							.getRight().leftHead().toArray(), msplit.getRight().leftTail(),
							sf));
				}
				case 3: // n1 *n2* n3
					return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft(),
							new Object[] { vs[0] }), vs[1], new Deep(measure(),
							new Object[] { vs[2] }, msplit.getRight(), sf));
				}
			}
			if (vs.length > 2)
			{
				mpr = measure().sum(mpr, msplit.getLeft().cached(),
						measure().measure(vs[2]));
				if (p.apply(mpr)) // n1 n2 *n3*
				{
					if (msplit.getRight().isEmpty())
					{
						return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft(),
								new Object[] { vs[1], vs[0] }), vs[2], reverseTreeOfDigits(
								measure(), sf));
					}
					return new Split<M, T>(new Deep(measure(), pr, msplit.getLeft(),
							new Object[] { vs[1], vs[0] }), vs[2], new Deep(measure(), msplit
							.getRight().leftHead().toArray(), msplit.getRight().leftTail(),
							sf));
				}
			}
		}
		for (int isf = sf.length - 1; isf > -1; isf--)
		{
			r = measure().sum(r, measure().measure(sf[isf]));
			if (p.apply(r))
			{
				T[][] split = splitDigits(sf, isf);
				if (split[1].length == 0)
				{
					if (m.isEmpty())
					{
						return new Split<M, T>(treeOfDigits(measure(), pr), sf[isf],
								reverseTreeOfDigits(measure(), split[0]));
					}
					else
					{
						return new Split<M, T>(new Deep(measure(), pr, m.rightTail(), m
								.rightHead().toReverseArray()), sf[isf], reverseTreeOfDigits(
								measure(), split[0]));
					}
				}
				else
				{
					return new Split<M, T>(new Deep(measure(), pr, m, split[1]), sf[isf],
							reverseTreeOfDigits(measure(), split[0]));
				}
			}
		}
		return null;
	}

	public <N, U> FTree<N, U> map(Measure<N, U> newMeasure, Mapper<T, U> mapper)
	{
		U[] newPr = (U[]) new Object[pr.length];
		N newC = newMeasure.empty();
		for (int d = 0; d < pr.length; d++)
		{
			U newD = mapper.map(pr[d]);
			newC = newMeasure.sum(newMeasure.measure(newD), newC);
			newPr[d] = newD;
		}
		FTree<N, Node<N, U>> newM = m.map(new NodeMeasure<N, U>(newMeasure),
				new NodeMapper<M, T, N, U>(newMeasure, mapper));
		U[] newSf = (U[]) new Object[sf.length];
		for (int d = sf.length - 1; d > -1; d--)
		{
			U newD = mapper.map(sf[d]);
			newC = newMeasure.sum(newMeasure.measure(newD), newC);
			newSf[d] = newD;
		}
		return new Deep<N, U>(newMeasure, newC, newPr, newM, newSf);
	}

	public FTree<Void, Object> psum(M i, final Scanner<M, T> scanner)
	{
		Object[] newPr = new Object[pr.length];
		for (int d = 0; d < pr.length; d++)
		{
			ScanResult<M> result = scanner.scan(pr[d], i);
			i = result.getI();
			newPr[d] = result.getV();
		}
		Object newM = m.psum(i, new Scanner<M, Node<M, T>>()
		{
			@Override
			public ScanResult<M> scan(Node<M, T> v, M i)
			{
				return v.scan(scanner, i);
			}
		});
		i = measure().sum(i, m.cached());
		Object[] newSf = new Object[sf.length];
		for (int d = sf.length - 1; d > -1; d--)
		{
			ScanResult<M> result = scanner.scan(sf[d], i);
			i = result.getI();
			newSf[d] = result.getV();
		}
		return new Deep<Void, Object>(Measure.nil(), null, newPr,
				(FTree<Void, Node<Void, Object>>) newM, newSf);
	}

	public FTree<Void, Object> ppsum(M i, final Scanner<M, T> scanner,
			final ExecutorService executor)
	{
		Object[] newPr = new Object[pr.length];
		for (int d = 0; d < pr.length; d++)
		{
			ScanResult<M> result = scanner.scan(pr[d], i);
			i = result.getI();
			newPr[d] = result.getV();
		}
		final M j = i;
		Future<Object> future = executor.submit(new Callable<Object>()
		{

			@Override
			public Object call() throws Exception
			{
				Object newM = m.ppsum(j, new Scanner<M, Node<M, T>>()
				{
					@Override
					public ScanResult<M> scan(Node<M, T> v, M i)
					{
						return v.scan(scanner, i);
					}
				}, executor);
				return newM;
			}
		});
		i = measure().sum(i, m.cached());
		Object[] newSf = new Object[sf.length];
		for (int d = sf.length - 1; d > -1; d--)
		{
			ScanResult<M> result = scanner.scan(sf[d], i);
			i = result.getI();
			newSf[d] = result.getV();
		}
		try
		{
			return new Deep<Void, Object>(Measure.nil(), null, newPr,
					(FTree<Void, Node<Void, Object>>) future.get(), newSf);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
