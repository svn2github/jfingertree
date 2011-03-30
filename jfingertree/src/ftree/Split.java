package ftree;

public class Split<M, T>
{

  private FTree<M, T> left;
  private T v;
  private FTree<M, T> right;

  public Split(FTree<M, T> left, T v, FTree<M, T> right)
  {
    super();
    this.left = left;
    this.v = v;
    this.right = right;
  }

  public FTree<M, T> getLeft()
  {
    return left;
  }
  
  public T getV()
  {
    return v;
  }
  
  public FTree<M, T> getRight()
  {
    return right;
  }
  
  @Override
  public String toString()
  {
    return "{" + left + ", " + v + ", " + right + "}";
  }
}
