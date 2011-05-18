package ftree;

public interface NodePSum<M, T>
{
	Node<M, M> calc(Node<M, T> node, M i);
}
