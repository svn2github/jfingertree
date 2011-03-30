package ftree;

public abstract class Predicate<M>
{

  public static Predicate<Integer> index(final int i)
  {
    return new Predicate<Integer>()
    {
      @Override
      public boolean apply(Integer v)
      {
        return v > i;
      }
    };
  }

  public abstract boolean apply(M v);
}
