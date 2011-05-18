package ftree;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Node3<M, T> implements Node<M, T>
{
  private M c;
  private T n1;
  private T n2;
  private T n3;

  public Node3(Measure<M, T> measure, T n1, T n2, T n3)
  {
    super();
    this.c = measure.sumMeasuresOf(n1, n2, n3);
    this.n1 = n1;
    this.n2 = n2;
    this.n3 = n3;
  }

  private Node3(M c, T n1, T n2, T n3)
  {
    super();
    this.c = c;
    this.n1 = n1;
    this.n2 = n2;
    this.n3 = n3;
  }

  public T getN1()
  {
    return n1;
  }

  public T getN2()
  {
    return n2;
  }

  public T getN3()
  {
    return n3;
  }
  
  @Override
  public M c()
  {
    return c;
  }
  
  @Override
  public T[] toArray()
  {
    return (T[]) new Object[] {n1, n2, n3};
  }
  
  @Override
  public T[] toReverseArray()
  {
    return (T[]) new Object[] {n3, n2, n1};
  }
  
  @Override
  public String toString()
  {
    return "(" + n1 + ", " + n2 + ", " + n3 + ")";
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
        return c < 3;
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
        else if (c == 2)
        {
          c++;
          return n3;
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
  
	public <N, U> Node<N, U> map(Measure<N, U> measure, Mapper<T, U> mapper)
	{
		U newN1 = mapper.map(n1);
		U newN2 = mapper.map(n2);
		U newN3 = mapper.map(n3);
		return new Node3<N, U>(measure, newN1, newN2, newN3);
	}

	public ScanResult<M> scan(Scanner<M, T> scanner, M i)
	{
		ScanResult<M> r1 = scanner.scan(n1, i);
		Object newN1 = r1.getV(); 
		ScanResult<M> r2 = scanner.scan(n2, r1.getI());
		Object newN2 = r2.getV();
		ScanResult<M> r3 = scanner.scan(n3, r2.getI());
		Object newN3 = r3.getV();
		return new ScanResult<M>(r3.getI(), new Node3<Void, Object>((Void) null, newN1, newN2, newN3));
	}

}
