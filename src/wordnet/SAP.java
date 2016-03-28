package wordnet;
import java.util.Map;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private final Digraph digraph;
    
    public SAP(Digraph G)
    {
        if (G == null) 
            throw new NullPointerException();
        
        // Attention!, Need to make a deep copy, or we can modify 
        // This object by taking actions on G from outside
        digraph = new Digraph(G);
    }
    
    
    private Map<Integer, Integer> bfsForAncestor(int node)
    {
        Queue<Integer> queue = new Queue<Integer>();
        SET<Integer> marked = new SET<Integer>();
        Map<Integer, Integer> ancestor = new TreeMap<Integer, Integer>();
        
        queue.enqueue(node);
        marked.add(node);
        ancestor.put(node, 0);
        
        while (!queue.isEmpty()) {
            int cur = queue.dequeue();
            int dist = ancestor.get(cur);
            for (int nb : digraph.adj(cur)) {
                if (!marked.contains(nb)) {
                    marked.add(nb);
                    queue.enqueue(nb);
                    ancestor.put(nb, dist + 1);
                }
            }
        }
        
        return ancestor;
    }
    
    private int[] shortestAncestralPath(int v, int w) {
        Map<Integer, Integer> ancestorV = bfsForAncestor(v);
        Map<Integer, Integer> ancestorW = bfsForAncestor(w);
        
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int ancV : ancestorV.keySet()) {
            if (ancestorW.containsKey(ancV)) {
                int cur = ancestorV.get(ancV) + ancestorW.get(ancV);
                if (cur < shortestLength) {
                    shortestLength = cur;
                    ancestor = ancV;
                }
            }
        }
//        
//        for (Integer ancW : ancestorW.keySet()) {
//            if (ancestorV.containsKey(ancW)) {
//                int cur = ancestorW.get(ancW) + ancestorV.get(ancW);
//                if (cur < shortestLength) {
//                    shortestLength = cur;
//                    ancestor = ancW;
//                }
//            }
//        }
//        
        if (shortestLength == Integer.MAX_VALUE)
            shortestLength = -1;
        return new int[]{shortestLength, ancestor};
    }
    
    // Length of shortest ancestral path between v and w, if 
    // no path, return -1
    public int length(int v, int w)
    {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V())
            throw new IndexOutOfBoundsException();
        return shortestAncestralPath(v, w)[0];
    }

    
    public int ancestor(int v, int w)
    {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V())
            throw new IndexOutOfBoundsException();
        

        return shortestAncestralPath(v, w)[1];
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new NullPointerException();
        
        int shortestLength = Integer.MAX_VALUE;
        for (int vnode : v) {
            for (int wnode : w) {
                shortestLength = Math.min(shortestLength,
                        shortestAncestralPath(vnode, wnode)[0]);
            }
        }
        
        if (shortestLength == Integer.MAX_VALUE)
            return -1;
        return shortestLength;
            
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new NullPointerException();
        
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vnode : v) {
            for (int wnode : w) {
                int[] res = shortestAncestralPath(vnode, wnode);
                if (shortestLength > res[0]) {
                    shortestLength = res[0];
                    ancestor = res[1];
                }
            }
        }
        
        return ancestor;
    }


    public static void main(String[] args)
    {
        In in = new In("data\\wordnet\\digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }
}

