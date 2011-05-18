package ftree;




public interface Node<M, T> extends Iterable<T>
{
  M c();
  T[] toArray();
  T[] toReverseArray();
  <N, U> Node<N, U> map(Measure<N, U> measure, Mapper<T, U> mapper);
  ScanResult<M> scan(Scanner<M, T> scanner, M i);
}
