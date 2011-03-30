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


}
