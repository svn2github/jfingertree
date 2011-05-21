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
	
	public static <M, T> FTree<M, T> fromIterable(Measure<M, T> measure, Iterable<T> iterable)
	{
		FTree<M, T> tree = treeOf(measure);
		for (T v : iterable)
		{
			tree = tree.addRight(v);
		}
		return tree;
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

	public FTree<Void, Object> psum()
	{
		M empty = measure().empty();
		return psum(empty, new Scanner<M, T>()
		{
			public ScanResult<M> scan(T v, M i)
			{
				M r = measure().sum(measure().measure(v), i);
				return new ScanResult<M>(r, r);
			}
		}).addLeft(empty);
	}

	public FTree<Void, Object> ppsum(ExecutorService executor)
	{
		M empty = measure().empty();
		return ppsum(empty, new Scanner<M, T>()
		{
			public ScanResult<M> scan(T v, M i)
			{
				M r = measure().sum(measure().measure(v), i);
				return new ScanResult<M>(r, r);
			}
		}, executor).addLeft(empty);
	}

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
    ft = ft.addRight(1).addRight(2).addRight(3).addRight(4).addRight(5);
    System.out.println(ft.split(new Predicate<Integer>()
		{
			
			@Override
			public boolean apply(Integer v)
			{
				return v > 3;
			}
		}, 0));
	}
}
