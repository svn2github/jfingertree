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
    return (T[]) new Object[] {n1, n2};
  }

  @Override
  public String toString()
  {
    return "(" + n1 + ", " + n2 + ")";
  }
}
