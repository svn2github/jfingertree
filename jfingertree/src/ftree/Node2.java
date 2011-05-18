package ftree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Node2<M, T> implements Node<M, T>
{

	private M c;
	private T n1;
	private T n2;

	public Node2(Measure<M, T> measure, T n1, T n2)
	{
		super();
		this.c = measure.sumMeasures(n1, n2);
		this.n1 = n1;
		this.n2 = n2;
	}

	private Node2(M c, T n1, T n2)
	{
		super();
		this.c = c;
		this.n1 = n1;
		this.n2 = n2;
	}

	public T getN1()
	{
		return n1;
	}

	public T getN2()
	{
		return n2;
	}

	public M c()
	{
		return c;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{

			private int c;

			@Override
			public boolean hasNext()
			{
				return c < 2;
			}

			@Override
			public T next()
			{
				if (c == 0)
				{
					c++;
					return n1;
				}
				else if (c == 1)
				{
					c++;
					return n2;
				}
				else
				{
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public T[] toArray()
	{
		return (T[]) new Object[] { n1, n2 };
	}

	@Override
	public T[] toReverseArray()
	{
		return (T[]) new Object[] { n2, n1 };
	}

	@Override
	public String toString()
	{
		return "(" + n1 + ", " + n2 + ")";
	}
	
	public <N, U> Node<N, U> map(Measure<N, U> measure, Mapper<T, U> mapper)
	{
		U newN1 = mapper.map(n1);
		U newN2 = mapper.map(n2);
		return new Node2<N, U>(measure, newN1, newN2);
	}

	public ScanResult<M> scan(Scanner<M, T> scanner, M i)
	{
		ScanResult<M> r1 = scanner.scan(n1, i);
		Object newN1 = r1.getV(); 
		ScanResult<M> r2 = scanner.scan(n2, r1.getI());
		Object newN2 = r2.getV();
		return new ScanResult<M>(r2.getI(), new Node2<Void, Object>((Void) null, newN1, newN2));
	}
}
