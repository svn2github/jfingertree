package ftree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Single<M, T> extends FTree<M, T>
{

  private T v;

  protected Single(Measure<M, T> measure, T v)
  {
    super(measure, measure.measure(v));
    this.v = v;
  }

  @Override
  public boolean isEmpty()
  {
    return false;
  }

  public FTree<M, T> addLeft(T v)
  {
    return new Deep<M, T>(measure(), measure().sum(c(), measure().measure(v)), (T[]) new Object[] { v}, new Empty<M, Node<M, T>>(
        new NodeMeasure<M, T>(measure())), (T[]) new Object[] { this.v});
  };

  public FTree<M, T> addRight(T v)
  {
    return new Deep<M, T>(measure(), measure().sum(c(), measure().measure(v)), (T[]) new Object[] { this.v}, new Empty<M, Node<M, T>>(
        new NodeMeasure<M, T>(measure())), (T[]) new Object[] { v});
  };

  @Override
  public T leftHead()
  {
    return v;
  }

  @Override
  public FTree<M, T> leftTail()
  {
    return new Empty(measure());
  }

  @Override
  public T rightHead()
  {
    return v;
  }

  @Override
  public FTree<M, T> rightTail()
  {
    return new Empty(measure());
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((v == null) ? 0 : v.hashCode());
    return result;
  }

  @Override
  public FTree<M, T> append(FTree<M, T> ft)
  {
    return ft.addLeft(v);
  }

  @Override
  protected FTree<M, T> revappendDeep(Deep<M, T> ft)
  {
    return ft.addRight(v);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Single other = (Single) obj;
    if (v == null)
    {
      if (other.v != null)
        return false;
    }
    else if (!v.equals(other.v))
      return false;
    return true;
  }

  @Override
  protected FTree<M, T> append1(T v, FTree<M, T> ft)
  {
    return ft.addLeft(v).addLeft(this.v);
  }

  @Override
  protected FTree<M, T> revappendDeep1(T v, Deep<M, T> ft)
  {
    return ft.addRight(v).addRight(this.v);
  }

  @Override
  protected FTree<M, T> append2(T v1, T v2, FTree<M, T> ft)
  {
    return ft.addLeft(v2).addLeft(v1).addLeft(this.v);
  }

  @Override
  protected FTree<M, T> revappendDeep2(T v1, T v2, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2).addRight(this.v);
  }

  @Override
  protected FTree<M, T> append3(T v1, T v2, T v3, FTree<M, T> ft)
  {
    return ft.addLeft(v3).addLeft(v2).addLeft(v1).addLeft(this.v);
  }

  @Override
  protected FTree<M, T> revappendDeep3(T v1, T v2, T v3, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2).addRight(v3).addRight(this.v);
  }

  @Override
  protected FTree<M, T> append4(T v1, T v2, T v3, T v4, FTree<M, T> ft)
  {
    return ft.addLeft(v4).addLeft(v3).addLeft(v2).addLeft(v1).addLeft(this.v);
  }

  @Override
  protected FTree<M, T> revappendDeep4(T v1, T v2, T v3, T v4, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2).addRight(v3).addRight(v4).addRight(this.v);
  }

  @Override
  public String toString()
  {
    return "<" + v + ">";
  }

  @Override
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
    {

      private boolean hasNext = true;

      @Override
      public boolean hasNext()
      {
        return hasNext;
      }

      @Override
      public T next()
      {
        if (hasNext)
        {
          hasNext = false;
          return v;
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public String toStringWithMeasures()
  {
    return "<#" + c() + "# " + v + ">";
  }
}
