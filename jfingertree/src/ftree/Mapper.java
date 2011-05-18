package ftree;

public interface Mapper<T, U>
{
	U map(T v);
}
