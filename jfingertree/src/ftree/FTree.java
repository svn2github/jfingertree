package ftree;

import java.util.ArrayList;
import java.util.List;

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

  protected Measure<M, T> measure()
  {
    return measure;
  }

  public M c()
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
    return new Deep(measure, measure.sumMeasures(v1, v2), (T[]) new Object[] { v1}, new Empty(new NodeMeasure(measure)), (T[]) new Object[] { v2});
  }

  public static <M, T> FTree<M, T> leftTreeOf(Measure<M, T> measure, T v1, T v2, T v3)
  {
    return new Deep(measure, measure.sumMeasuresOf(v1, v2, v3), (T[]) new Object[] { v1, v2}, new Empty(new NodeMeasure(measure)), (T[]) new Object[] { v3});
  }

  public static <M, T> FTree<M, T> leftTreeOf(Measure<M, T> measure, T v1, T v2, T v3, T v4)
  {
    return new Deep(measure, measure.sumMeasuresOf(v1, v2, v3, v4), (T[]) new Object[] { v1, v2, v3}, new Empty(new NodeMeasure(measure)), (T[]) new Object[] { v4});
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

  public abstract FTree<M, T> addLeft(T v);

  public abstract FTree<M, T> addRight(T v);

  public abstract boolean isEmpty();

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

  protected abstract FTree<M, T> revappendDeep4(T v1, T v2, T v3, T v4, Deep<M, T> ft);

  public static void main(String[] args)
  {
    Measure<Integer, Object> measure = Measure.<Object>size();
    FTree<Integer, Object> ft = FTree.treeOf(measure);
    for (int i = 0; i < 200; i++)
    {
      ft = ft.addLeft(i);
      List<Object> ftList = ft.toList();
      for (int j = 0; j < i; j++)
      {
        Split<Integer, Object> split = ft.split(Predicate.index(j), 0);
        if (!(split.getLeft().c() == j))
        {
          System.out.println("error");
        }
        if (!(split.getRight().c() == i - j))
        {
          System.out.println("error");
        }
        FTree<Integer, Object> appendSplit = split.getLeft().addRight(split.getCenter()).append(split.getRight());
        if (!(appendSplit.toList().equals(ftList)))
        {
          System.out.println(ft + " * " + split + " * " + appendSplit);
        }
      }
    }
//    for (int i = 0; i < 30; i++)
//    {
//      ft = ft.addLeft(i);
//    }
//    Split<Integer, Object> split = ft.split(Predicate.index(20), 0);
//    System.out.println(split);
  }
}
