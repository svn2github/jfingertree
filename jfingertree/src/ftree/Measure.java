package ftree;

public abstract class Measure<M, T>
{
  
  public static final <T> Measure<Void, T> nil()
  {
    return new Measure<Void, T>()
    {

      @Override
      public Void measure(T t)
      {
        return null;
      }

      @Override
      public Void empty()
      {
        return null;
      }

      @Override
      public Void sum(Void a, Void b)
      {
        return null;
      }
      
      @Override
      public String toString()
      {
        return "nil";
      }
    };
        
  }
  
  public static final <T> Measure<Integer, T> size()
  {
    return new Measure<Integer, T>()
    {

      @Override
      public Integer measure(T t)
      {
        return 1;
      }

      @Override
      public Integer empty()
      {
        return 0;
      }

      @Override
      public Integer sum(Integer a, Integer b)
      {
        return a + b;
      }
    };
  }

  public abstract M measure(T t);
  public abstract M empty();
  public abstract M sum(M a, M b);
  
  public M sumMeasures(T a, T b)
  {
    return sum(measure(a), measure(b));
  }
  
  public M sum(M a, M b, M c)
  {
    return sum(a, sum(b, c));
  }
  
  public M sum(M a, M b, M c, M d)
  {
    return sum(a, sum(b, sum(c, d)));
  }
  
  public M sumMeasuresOf(T a, T b, T c)
  {
    return sum(measure(a), measure(b), measure(c));
  }
  
  public M sumMeasuresOf(T a, T b, T c, T d)
  {
    return sum(measure(a), measure(b), measure(c), measure(d));
  }
  
}
