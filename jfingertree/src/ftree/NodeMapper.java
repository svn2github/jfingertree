package ftree;

public class NodeMapper<M, T, N, U> implements Mapper<Node<M, T>, Node<N, U>>
{

	private Measure<N, U> measure;
  private Mapper<T, U> mapper;

  public NodeMapper(Measure<N, U> measure, Mapper<T, U> mapper)
  {
    super();
    this.measure = measure;
    this.mapper = mapper;
  }

	public Node<N, U> map(Node<M, T> node)
	{
		return node.map(measure, mapper);
	}

}
