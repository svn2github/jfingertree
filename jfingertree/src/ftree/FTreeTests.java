package ftree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FTreeTests
{

  @Test
  public void testIsEmpty()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    assertEquals("<>", ft.toString());
    assertTrue(ft.isEmpty());
    ft = ft.addRight("a");
    // assertTrue(ft instanceof Single);
    assertFalse(ft.isEmpty());
    ft = ft.addLeft("c");
    // assertTrue(ft instanceof Deep);
    assertFalse(ft.isEmpty());
  }

  @Test
  public void testAddRight()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    ft = ft.addRight("a");
    assertEquals("<a>", ft.toString());
    ft = ft.addRight("b");
    assertEquals("<[a], <>, [b]>", ft.toString());
    ft = ft.addRight("c");
    assertEquals("<[a], <>, [b, c]>", ft.toString());
    ft = ft.addRight("d");
    assertEquals("<[a], <>, [b, c, d]>", ft.toString());
    ft = ft.addRight("e");
    assertEquals("<[a], <>, [b, c, d, e]>", ft.toString());
    ft = ft.addRight("f");
    assertEquals("<[a], <(b, c, d)>, [e, f]>", ft.toString());
    ft = ft.addRight("g");
    assertEquals("<[a], <(b, c, d)>, [e, f, g]>", ft.toString());
    ft = ft.addRight("h");
    assertEquals("<[a], <(b, c, d)>, [e, f, g, h]>", ft.toString());
    ft = ft.addRight("i");
    assertEquals("<[a], <[(b, c, d)], <>, [(e, f, g)]>, [h, i]>", ft.toString());
    ft = ft.addRight("j");
    assertEquals("<[a], <[(b, c, d)], <>, [(e, f, g)]>, [h, i, j]>", ft.toString());
    ft = ft.addRight("k");
    assertEquals("<[a], <[(b, c, d)], <>, [(e, f, g)]>, [h, i, j, k]>", ft.toString());
    ft = ft.addRight("l");
    assertEquals("<[a], <[(b, c, d)], <>, [(e, f, g), (h, i, j)]>, [k, l]>", ft.toString());
  }

  @Test
  public void testAddLeft()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    ft = ft.addLeft("a");
    assertEquals("<a>", ft.toString());
    ft = ft.addLeft("b");
    assertEquals("<[b], <>, [a]>", ft.toString());
    ft = ft.addLeft("c");
    assertEquals("<[c, b], <>, [a]>", ft.toString());
    ft = ft.addLeft("d");
    assertEquals("<[d, c, b], <>, [a]>", ft.toString());
    ft = ft.addLeft("e");
    assertEquals("<[e, d, c, b], <>, [a]>", ft.toString());
    ft = ft.addLeft("f");
    assertEquals("<[f, e], <(d, c, b)>, [a]>", ft.toString());
    ft = ft.addLeft("g");
    assertEquals("<[g, f, e], <(d, c, b)>, [a]>", ft.toString());
    ft = ft.addLeft("h");
    assertEquals("<[h, g, f, e], <(d, c, b)>, [a]>", ft.toString());
    ft = ft.addLeft("i");
    assertEquals("<[i, h], <[(g, f, e)], <>, [(d, c, b)]>, [a]>", ft.toString());
    ft = ft.addLeft("j");
    assertEquals("<[j, i, h], <[(g, f, e)], <>, [(d, c, b)]>, [a]>", ft.toString());
    ft = ft.addLeft("k");
    assertEquals("<[k, j, i, h], <[(g, f, e)], <>, [(d, c, b)]>, [a]>", ft.toString());
    ft = ft.addLeft("l");
    assertEquals("<[l, k], <[(j, i, h), (g, f, e)], <>, [(d, c, b)]>, [a]>", ft.toString());
  }

  @Test
  public void testLeftView()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    assertNull(ft.leftHead());
    assertNull(ft.leftTail());
    ft = ft.addRight("a");
    assertEquals("a", ft.leftHead());
    assertEquals("<>", ft.leftTail().toString());
    ft = ft.addLeft("b");
    assertEquals("b", ft.leftHead());
    assertEquals("<a>", ft.leftTail().toString());
    ft = ft.addLeft("c");
    assertEquals("c", ft.leftHead());
    assertEquals("<[b], <>, [a]>", ft.leftTail().toString());
    ft = ft.addLeft("d");
    assertEquals("d", ft.leftHead());
    assertEquals("<[c, b], <>, [a]>", ft.leftTail().toString());
    ft = ft.addRight("x");
    assertEquals("d", ft.leftHead());
    assertEquals("<[c, b], <>, [a, x]>", ft.leftTail().toString());
  }
  
  @Test
  public void testTailWithNodesInMiddle()
  {
    Measure<Integer, Object> measure = Measure.<Object>size();
    FTree<Integer, Object> ft = FTree.treeOf(measure);
    for (int i = 0; i < 500; i++)
    {
      ft = ft.addRight(i);
    }
    FTree<Integer, Object> ft2 = ft;
    assertEquals((Integer) 500, ft.cached());
    for (int i = 499; i > -1; i--)
    {
      ft = ft.leftTail();
      ft2 = ft2.rightTail();
      assertEquals((Integer) i, ft.cached()); 
      assertEquals((Integer) i, ft2.cached()); 
    }
  }
  

  @Test
  public void testRightView()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    assertNull(ft.rightHead());
    assertNull(ft.rightTail());
    ft = ft.addLeft("a");
    assertEquals("a", ft.rightHead());
    assertEquals("<>", ft.rightTail().toString());
    ft = ft.addRight("b");
    assertEquals("b", ft.rightHead());
    assertEquals("<a>", ft.rightTail().toString());
    ft = ft.addRight("c");
    assertEquals("c", ft.rightHead());
    assertEquals("<[a], <>, [b]>", ft.rightTail().toString());
    ft = ft.addRight("d");
    assertEquals("d", ft.rightHead());
    assertEquals("<[a], <>, [b, c]>", ft.rightTail().toString());
    ft = ft.addLeft("x");
    assertEquals("d", ft.rightHead());
    assertEquals("<[x, a], <>, [b, c]>", ft.rightTail().toString());
  }

  @Test
  public void testAppend()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    FTree<Void, Object> ft2 = FTree.treeOf();
    assertEquals("<>", ft.append(ft2).toString());
    assertEquals("<>", ft2.append(ft).toString());
    ft = ft.addRight("a1");
    assertEquals("<a1>", ft.append(ft2).toString());
    assertEquals("<a1>", ft2.append(ft).toString());
    ft2 = ft2.addRight("a2");
    assertEquals("<[a1], <>, [a2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <>, [a1]>", ft2.append(ft).toString());
    ft = ft.addRight("b1");
    assertEquals("<[a1], <>, [b1, a2]>", ft.append(ft2).toString());
    assertEquals("<[a2, a1], <>, [b1]>", ft2.append(ft).toString());
    ft2 = ft2.addRight("b2");
    assertEquals("<[a1], <(b1, a2)>, [b2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <(b2, a1)>, [b1]>", ft2.append(ft).toString());
    ft = ft.addRight("c1");
    assertEquals("<[a1], <(b1, c1, a2)>, [b2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <(b2, a1)>, [b1, c1]>", ft2.append(ft).toString());
    ft2 = ft2.addRight("c2");
    assertEquals("<[a1], <(b1, c1, a2)>, [b2, c2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <(b2, c2, a1)>, [b1, c1]>", ft2.append(ft).toString());
    ft = ft.addRight("d1");
    assertEquals("<[a1], <[(b1, c1)], <>, [(d1, a2)]>, [b2, c2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <(b2, c2, a1)>, [b1, c1, d1]>", ft2.append(ft).toString());
    ft2 = ft2.addRight("d2");
    assertEquals("<[a1], <[(b1, c1)], <>, [(d1, a2)]>, [b2, c2, d2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <[(b2, c2)], <>, [(d2, a1)]>, [b1, c1, d1]>", ft2.append(ft).toString());
    ft = ft.addRight("e1");
    assertEquals("<[a1], <[(b1, c1, d1)], <>, [(e1, a2)]>, [b2, c2, d2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <[(b2, c2)], <>, [(d2, a1)]>, [b1, c1, d1, e1]>", ft2.append(ft).toString());
    ft2 = ft2.addRight("e2");
    assertEquals("<[a1], <[(b1, c1, d1)], <>, [(e1, a2)]>, [b2, c2, d2, e2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <[(b2, c2, d2)], <>, [(e2, a1)]>, [b1, c1, d1, e1]>", ft2.append(ft).toString());
    ft = ft.addRight("f1");
    assertEquals("<[a1], <[(b1, c1, d1)], <>, [(e1, f1, a2)]>, [b2, c2, d2, e2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <[(b2, c2, d2), (e2, a1)], <>, [(b1, c1, d1)]>, [e1, f1]>", ft2.append(ft).toString());
    ft2 = ft2.addRight("f2");
    assertEquals("<[a1], <[(b1, c1, d1), (e1, f1, a2)], <>, [(b2, c2, d2)]>, [e2, f2]>", ft.append(ft2).toString());
    assertEquals("<[a2], <[(b2, c2, d2), (e2, f2, a1)], <>, [(b1, c1, d1)]>, [e1, f1]>", ft2.append(ft).toString());
  }

  @Test
  public void testAppend2()
  {
    List<FTree<Void, Integer>> fts = new ArrayList<FTree<Void, Integer>>();
    List<Integer> l = new ArrayList<Integer>();
    Measure<Void, Integer> measure = Measure.nil();
    for (int i = 0; i < 65536; i++)
    {
      fts.add(FTree.treeOf(measure, i));
      l.add(i);
    }
    while (fts.size() != 1)
    {
      List<FTree<Void, Integer>> newFts = new ArrayList<FTree<Void, Integer>>();
      Iterator<FTree<Void, Integer>> iter = fts.iterator();
      while (iter.hasNext())
      {
        FTree<Void, Integer> ft1 = iter.next();
        FTree<Void, Integer> ft2 = iter.next();
        newFts.add(ft1.append(ft2));
      }
      fts = newFts;
    }
    assertEquals(l, fts.get(0).toList());
  }

  @Test
  public void testAppend3() throws Exception
  {
    FTree<Void, Integer> ft = FTree.treeOf();
    for (int j = 0; j < 150; j++)
    {
      ft = ft.addRight(j);
      FTree<Void, Integer> ft2 = FTree.treeOf();
      for (int k = 0; k < j; k++)
      {
        ft2 = ft2.addLeft(j);
        List<Integer> a1 = ft.append(ft2).toList();
        List<Integer> a2 = ft2.append(ft).toList();
        List<Integer> e1 = ft.toList();
        e1.addAll(ft2.toList());
        List<Integer> e2 = ft2.toList();
        e2.addAll(ft.toList());
        assertEquals(e1, a1);
        assertEquals(e2, a2);
      }
    }
  }
  
  @Test
  public void testIter()
  {
    FTree<Void, Object> ft = FTree.treeOf();
    FTree<Void, Object> ft2 = FTree.treeOf();
    final int iters = 10000;
    List<Object> l = new ArrayList<Object>();
    List<Object> l2 = new ArrayList<Object>();
    for (int i = 0; i < iters; i++)
    {
      ft = ft.addLeft(i);
      ft2 = ft2.addRight(i);
      l.add(i);
      l2.add(i);
    }
    Collections.reverse(l);
    List<Object> ftl = new ArrayList<Object>();
    List<Object> ftl2 = new ArrayList<Object>();
    for (Object o : ft)
    {
      ftl.add(o);
    }
    for (Object o : ft2)
    {
      ftl2.add(o);
    }
    assertEquals(l, ftl);
    assertEquals(l2, ftl2);
  }
  
  @Test
  public void testSizeMonoid()
  {
    FTree<Integer, Object> ft = FTree.treeOf(Measure.<Object>size());
    for (int i = 0; i < 1000; i++)
    {
      assertEquals((Integer) (i * 2), ft.cached());
      ft = ft.addLeft(i);
      assertEquals((Integer) (i * 2 + 1), ft.cached());
      ft = ft.addRight(i);
    }    
  }
  
  @Test
  public void testSizeMonoidUnderLeftRightView()
  {
    // TODO (see testTailWithNodesInMiddle)
  }
  
  @Test
  public void testSizeMonoidUnderAppend()
  {
    FTree<Integer, Object> ft = FTree.treeOf(Measure.<Object>size());
    assertEquals((Integer) 0, ft.cached()); 
    for (int j = 0; j < 150; j++)
    {
      ft = ft.addRight(j);
      assertEquals((Integer) (j + 1), ft.cached()); 
      FTree<Integer, Object> ft2 = FTree.treeOf(Measure.<Object>size());
      for (int k = 0; k < j; k++)
      {
        ft2 = ft2.addLeft(j);
        assertEquals((Integer) (k + 1), ft2.cached());
        assertEquals((Integer) (j + k + 2), ft.append(ft2).cached());        
        assertEquals((Integer) (j + k + 2), ft2.append(ft).cached());        
      }
    }
  }
  
  @Test
  public void testSplitWithSizeMonoid()
  {
    Measure<Integer, Object> measure = Measure.<Object>size();
    FTree<Integer, Object> ft = FTree.treeOf(measure);
    assertNull(ft.split(Predicate.index(123), measure.empty()));
    ft = ft.addRight("a");
    assertEquals("{<>, a, <>}", ft.split(Predicate.index(123), measure.empty()).toString());
    assertEquals("{<>, a, <>}", ft.split(Predicate.index(0), measure.empty()).toString());
    ft = ft.addRight("b");
    assertEquals("{<>, a, <b>}", ft.split(Predicate.index(0), measure.empty()).toString());
    assertEquals("{<a>, b, <>}", ft.split(Predicate.index(1), measure.empty()).toString());
    assertNull(ft.split(Predicate.index(2), measure.empty()));
    ft = ft.addRight("c");
    assertEquals("{<>, a, <[b], <>, [c]>}", ft.split(Predicate.index(0), measure.empty()).toString());
    assertEquals("{<a>, b, <c>}", ft.split(Predicate.index(1), measure.empty()).toString());    
    assertEquals("{<[a], <>, [b]>, c, <>}", ft.split(Predicate.index(2), measure.empty()).toString());    
    ft = ft.addRight("d");
    assertEquals("{<>, a, <[b, c], <>, [d]>}", ft.split(Predicate.index(0), measure.empty()).toString());
    assertEquals("{<a>, b, <[d], <>, [c]>}", ft.split(Predicate.index(1), measure.empty()).toString());    
    assertEquals("{<[a], <>, [b]>, c, <d>}", ft.split(Predicate.index(2), measure.empty()).toString());    
    assertEquals("{<[a], <>, [b, c]>, d, <>}", ft.split(Predicate.index(3), measure.empty()).toString());    
  }

  @Test
  public void testSplitAppendWithSizeMonoid()
  {
    Measure<Integer, Object> measure = Measure.<Object>size();
    FTree<Integer, Object> ft = FTree.treeOf(measure);
    for (int i = 0; i < 200; i++)
    {
      ft = ft.addLeft(i);
      List<Object> ftList = ft.toList();
      for (int j = 0; j < i; j++)
      {
        Split<Integer, Object> split = ft.split(Predicate.index(j), 0);
        assertEquals((Integer) j, split.getLeft().cached());
        assertEquals((Integer) (i - j), split.getRight().cached());
        FTree<Integer, Object> appendSplit = split.getLeft().addRight(split.getCenter()).append(split.getRight());
        assertEquals(appendSplit.toList(), ftList);
      }
    }

  }
  
}
