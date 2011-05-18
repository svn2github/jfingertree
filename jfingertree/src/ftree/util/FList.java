package ftree.util;

import java.util.AbstractList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import ftree.FTree;
import ftree.Measure;
import ftree.Predicate;
import ftree.Split;

public class FList<T> extends AbstractList<T> implements Deque<T>, RandomAccess
{

  private FTree<Integer, T> ft = FTree.treeOf(Measure.<T> size());

  public FList()
  {
    super();
  }

  @Override
  public T get(int index)
  {
    Split<Integer, T> split = ft.split(Predicate.index(index), 0);
    if (split == null)
    {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    return split.getCenter();
  }

  public boolean add(T e)
  {
    ft = ft.addRight(e);
    return true;
  }

  public void add(int index, T e)
  {
    if (index == 0)
    {
      ft = ft.addLeft(e);
      return;
    }
    if (index < 0 || index > size())
    {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    Split<Integer, T> split = ft.split(Predicate.index(index), 0);
    if (split == null)
    {
      ft = ft.addRight(e);
    }
    else
    {
      ft = split.getLeft().addRight(e).addRight(split.getCenter()).append(split.getRight());
    }
  }

  @Override
  public T remove(int index)
  {
    if (index == 0)
    {
      return removeFirst();
    }
    if (index == size() - 1)
    {
      return removeLast();
    }
    if (index < 0 || index > size() - 1)
    {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    Split<Integer, T> split = ft.split(Predicate.index(index), 0);
    ft = split.getLeft().append(split.getRight());
    return split.getCenter();
  }

  @Override
  public int size()
  {
    return ft.cached();
  }

  @Override
  public String toString()
  {
    return ft.toList().toString();
  }

  public static void main(String[] args)
  {
    List<Integer> l = new FList<Integer>();
    l.add(0);
    l.add(1);
    l.add(2);
    System.out.println(l);
  }
  
  // Deque interface

  @Override
  public void addFirst(T e)
  {
    ft = ft.addLeft(e);
  }

  @Override
  public void addLast(T e)
  {
    ft = ft.addRight(e);
  }

  @Override
  public boolean offerFirst(T e)
  {
    addFirst(e);
    return true;
  }

  @Override
  public boolean offerLast(T e)
  {
    addLast(e);
    return true;
  }

  @Override
  public T removeFirst()
  {
    if (isEmpty())
    {
      throw new IndexOutOfBoundsException();      
    }
    T e = ft.leftHead();
    ft = ft.leftTail();
    return e;
  }

  @Override
  public T removeLast()
  {
    if (isEmpty())
    {
      throw new IndexOutOfBoundsException();      
    }
    T e = ft.rightHead();
    ft = ft.rightTail();
    return e;
  }

  @Override
  public T pollFirst()
  {
    if (isEmpty())
    {
      return null;
    }
    T e = ft.leftHead();
    ft = ft.leftTail();
    return e;
  }

  @Override
  public T pollLast()
  {
    if (isEmpty())
    {
      return null;
    }
    T e = ft.rightHead();
    ft = ft.rightTail();
    return e;
  }

  @Override
  public T getFirst()
  {
    if (isEmpty())
    {
      throw new IndexOutOfBoundsException();
    }
    return ft.leftHead();
  }

  @Override
  public T getLast()
  {
    if (isEmpty())
    {
      throw new IndexOutOfBoundsException();
    }
    return ft.rightHead();
  }

  @Override
  public T peekFirst()
  {
    return ft.leftHead();
  }

  @Override
  public T peekLast()
  {
    return ft.rightHead();
  }

  @Override
  public boolean removeFirstOccurrence(Object o)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeLastOccurrence(Object o)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean offer(T e)
  {
    return offerLast(e);
  }

  @Override
  public T remove()
  {
    return removeFirst();
  }

  @Override
  public T poll()
  {
    return pollFirst();
  }

  @Override
  public T element()
  {
    return getFirst();
  }

  @Override
  public T peek()
  {
    return peekFirst();
  }

  @Override
  public void push(T e)
  {
    addFirst(e);
  }

  @Override
  public T pop()
  {
    return removeFirst();
  }
  
  @Override
  public Iterator<T> iterator()
  {
  	return ft.iterator();
  }

  @Override
  public Iterator<T> descendingIterator()
  {
    return ft.descendingIterator();
  }
}
