package ftree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Empty<M, T> extends FTree<M, T>
{

  protected Empty(Measure<M, T> measure)
  {
    super(measure, measure.empty());
  }

  @Override
  public boolean isEmpty()
  {
    return true;
  }

  public FTree<M, T> addLeft(T v)
  {
    return new Single<M, T>(measure(), v);
  }

  public FTree<M, T> addRight(T v)
  {
    return new Single<M, T>(measure(), v);
  }

  @Override
  public T leftHead()
  {
    return null;
  }

  @Override
  public FTree<M, T> leftTail()
  {
    return null;
  }

  @Override
  public T rightHead()
  {
    return null;
  }

  @Override
  public FTree<M, T> rightTail()
  {
    return null;
  }

  @Override
  public FTree<M, T> append(FTree<M, T> ft)
  {
    return ft;
  }

  @Override
  protected FTree<M, T> revappendDeep(Deep<M, T> ft)
  {
    return ft;
  }

  @Override
  protected FTree<M, T> append1(T v, FTree<M, T> ft)
  {
    return ft.addLeft(v);
  }

  @Override
  protected FTree<M, T> revappendDeep1(T v, Deep<M, T> ft)
  {
    return ft.addRight(v);
  }

  @Override
  protected FTree<M, T> append2(T v1, T v2, FTree<M, T> ft)
  {
    return ft.addLeft(v2).addLeft(v1);
  }

  @Override
  protected FTree<M, T> revappendDeep2(T v1, T v2, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2);
  }

  @Override
  protected FTree<M, T> append3(T v1, T v2, T v3, FTree<M, T> ft)
  {
    return ft.addLeft(v3).addLeft(v2).addLeft(v1);
  }

  @Override
  protected FTree<M, T> revappendDeep3(T v1, T v2, T v3, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2).addRight(v3);
  }

  @Override
  protected FTree<M, T> append4(T v1, T v2, T v3, T v4, FTree<M, T> ft)
  {
    return ft.addLeft(v4).addLeft(v3).addLeft(v2).addLeft(v1);
  }

  @Override
  protected FTree<M, T> revappendDeep4(T v1, T v2, T v3, T v4, Deep<M, T> ft)
  {
    return ft.addRight(v1).addRight(v2).addRight(v3).addRight(v4);
  }

  @Override
  public String toString()
  {
    return "<>";
  }

  @Override
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
    {

      @Override
      public boolean hasNext()
      {
        return false;
      }

      @Override
      public T next()
      {
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
    return "<#" + c() + "#>";
  }

  @Override
  public Split<M, T> split(Predicate<M> p, M i)
  {
    return null;
  }
}
