package ftree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import jsr166y.ForkJoinPool;

public abstract class FTree<M, T> implements Iterable<T>
{
	private Measure<M, T> measure;
	private M c;

	protected FTree(Measure<M, T> measure, M c)
	{
		super();
		this.measure = measure;
		this.c = c;
	}

	public Measure<M, T> measure()
	{
		return measure;
	}

	public M cached()
	{
		return c;
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure)
	{
		return new Empty(measure);
	}

	public static <T> FTree<Void, T> treeOf()
	{
		return new Empty(Measure.nil());
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure, T v)
	{
		return new Single<M, T>(measure, v);
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure, T v1, T v2)
	{
		// paper: toTree().cons(v1).cons(v2)
		return new Deep(measure, measure.sumMeasures(v1, v2),
				(T[]) new Object[] { v1 }, new Empty(new NodeMeasure(measure)),
				(T[]) new Object[] { v2 });
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure, T v1, T v2,
			T v3)
	{
		return new Deep(measure, measure.sumMeasuresOf(v1, v2, v3),
				(T[]) new Object[] { v1, v2 }, new Empty(new NodeMeasure(measure)),
				(T[]) new Object[] { v3 });
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure, T v1, T v2,
			T v3, T v4)
	{
		return new Deep(measure, measure.sumMeasuresOf(v1, v2, v3, v4),
				(T[]) new Object[] { v1, v2, v3 }, new Empty(new NodeMeasure(measure)),
				(T[]) new Object[] { v4 });
	}

	public static <M, T> FTree<M, T> treeOf(Measure<M, T> measure, T[] vs)
	{
		FTree<M, T> tree = treeOf(measure);
		for (T v : vs)
		{
			tree = tree.addRight(v);
		}
		return tree;
	}

	public List<T> toList()
	{
		List<T> list = new ArrayList<T>();
		for (T t : this)
		{
			list.add(t);
		}
		return list;
	}

	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			FTree<M, T> ft = FTree.this;

			public boolean hasNext()
			{
				return !ft.isEmpty();
			}

			public T next()
			{
				T next = ft.leftHead();
				ft = ft.leftTail();
				return next;
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	public Iterator<T> descendingIterator()
	{
		return new Iterator<T>()
		{
			FTree<M, T> ft = FTree.this;

			public boolean hasNext()
			{
				return !ft.isEmpty();
			}

			public T next()
			{
				T next = ft.rightHead();
				ft = ft.rightTail();
				return next;
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	public abstract <N, U> FTree<N, U> map(Measure<N, U> newMeasure,
			Mapper<T, U> mapper);

	public abstract FTree<Void, Object> psum(M i, Scanner<M, T> scanner);

	public abstract FTree<Void, Object> ppsum(M i, Scanner<M, T> scanner,
			ExecutorService executor);

	public abstract FTree<M, T> addLeft(T v);

	public abstract FTree<M, T> addRight(T v);

	public abstract boolean isEmpty();

	public FTree<M, T> clear()
	{
		return new Empty<M, T>(measure);
	}

	public abstract T leftHead();

	public abstract FTree<M, T> leftTail();

	public abstract T rightHead();

	public abstract FTree<M, T> rightTail();

	public abstract FTree<M, T> append(FTree<M, T> ft);

	public abstract Split<M, T> split(Predicate<M> p, M i);

	public abstract String toStringWithMeasures();

	protected abstract FTree<M, T> revappendDeep(Deep<M, T> ft);

	protected abstract FTree<M, T> append1(T v, FTree<M, T> ft);

	protected abstract FTree<M, T> revappendDeep1(T v, Deep<M, T> ft);

	protected abstract FTree<M, T> append2(T v1, T v2, FTree<M, T> ft);

	protected abstract FTree<M, T> revappendDeep2(T v1, T v2, Deep<M, T> ft);

	protected abstract FTree<M, T> append3(T v1, T v2, T v3, FTree<M, T> ft);

	protected abstract FTree<M, T> revappendDeep3(T v1, T v2, T v3, Deep<M, T> ft);

	protected abstract FTree<M, T> append4(T v1, T v2, T v3, T v4, FTree<M, T> ft);

	protected abstract FTree<M, T> revappendDeep4(T v1, T v2, T v3, T v4,
			Deep<M, T> ft);

	public static void main(String[] args)
	{
		Measure<Integer, Integer> measure = Measure.intSum();
		FTree<Integer, Integer> ft = FTree.treeOf(measure);
		for (int i = 0; i < 1000000; i++)
		{
			ft = ft.addRight(i);
		}
//		System.out.println(ft);

		long mstart = System.currentTimeMillis();
		FTree<Void, Integer> manual = FTree.treeOf();
		int total = 0;
		for (Integer n : ft)
		{
			manual = manual.addRight(total += n);
		}
		long mend = System.currentTimeMillis();
//		System.out.println(manual);

		long pstart = System.currentTimeMillis();
		FTree<Void, Object> psum = ft.psum(0, new Scanner<Integer, Integer>()
		{
			public ScanResult<Integer> scan(Integer v, Integer i)
			{
				return new ScanResult(v + i, v + i);
			}
		});
		long pend = System.currentTimeMillis();
//		System.out.println(psum);

		ForkJoinPool executor = new ForkJoinPool();
		long ppstart = System.currentTimeMillis();
		FTree<Void, Object> ppsum = ft.ppsum(0, new Scanner<Integer, Integer>()
		{
			public ScanResult<Integer> scan(Integer v, Integer i)
			{
				return new ScanResult(v + i, v + i);
			}
		}, executor);
		long ppend = System.currentTimeMillis();
//		System.out.println(ppsum);
		
		
		System.out.println("equal? " + (manual.toList().equals(psum.toList()) && psum.toList().equals(ppsum.toList())));
		System.out.println("manual " + (mend - mstart) + " psum " + (pend - pstart) + " ppsum " + (ppend - ppstart));
		System.out.println(executor);
	}
}
