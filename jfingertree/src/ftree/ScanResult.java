package ftree;

public class ScanResult<M>
{
	private M i;
	private Object v;

	public ScanResult(M i, Object v)
	{
		super();
		this.i = i;
		this.v = v;
	}

	public M getI()
	{
		return i;
	}

	public Object getV()
	{
		return v;
	}

}
