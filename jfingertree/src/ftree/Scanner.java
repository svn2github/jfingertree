package ftree;

public interface Scanner<M, T>
{
	ScanResult<M> scan(T v, M i);
}
