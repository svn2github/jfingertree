package ftree;

public class NodeMeasure<M, T> extends Measure<M, Node<M, T>>
{

  private Measure<M, T> measure;

  public NodeMeasure(Measure<M, T> measure)
  {
    super();
    this.measure = measure;
  }

  @Override
  public M measure(Node<M, T> t)
  {
    return t.c();
  }

  @Override
  public M empty()
  {
    return measure.empty();
  }

  @Override
  public M sum(M a, M b)
  {
    return measure.sum(a, b);
  }

  @Override
  public String toString()
  {
    return "node -> " + measure;
  }
}
