package wordnet;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
        
    private final Digraph digraph;
    private final TreeMap<Integer, String> syns;
    private final TreeMap<String, List<Integer>> nounWords;
    private final SAP sap;
    
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        In ssc = new In(synsets);
        In hsc = new In(hypernyms);
        
        syns = new TreeMap<Integer, String>();
        nounWords = new TreeMap<String, List<Integer>>();
        
        // Input the nouns
        while (ssc.hasNextLine()) {
            String[] line = ssc.readLine().split(",");
            String[] words = line[1].trim().split("\\s");
            for (String word : words) {
                if (!nounWords.containsKey(word)) {
                    nounWords.put(word, new ArrayList<Integer>());
                }
                nounWords.get(word).add(Integer.parseInt(line[0].trim()));
            }
            syns.put(Integer.parseInt(line[0].trim()), line[1].trim());
            
        }
        
        digraph = new Digraph(syns.size());
        
        while (hsc.hasNextLine()) {
            String[] numbers = hsc.readLine().split(",");
            int hypo = Integer.parseInt(numbers[0].trim());
            for (int i = 1; i < numbers.length; i++) {
                int hype = Integer.parseInt(numbers[i].trim());
                digraph.addEdge(hypo, hype);
            }
        }
        
        if (hasCycleOrMultipleRoots(digraph))
            throw new IllegalArgumentException();
        
        
        sap = new SAP(digraph);
    }
    
    // Test Whether the Digraph is a one-rooted DAG
    private boolean hasCycleOrMultipleRoots(Digraph dg) {
        TreeSet<Integer> grey = new TreeSet<Integer>();
        TreeSet<Integer> black = new TreeSet<Integer>();
        
        int rootCount = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (dg.outdegree(i) == 0)
                rootCount += 1;
        }
        if (rootCount != 1) {
            return true;
        }

        
        int[] incomingEdges = new int[dg.V()];        
        for (int i = 0; i < dg.V(); i++) {
            if (!black.contains(i)) {
                if (!visit(incomingEdges, grey, black, dg, i)) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean visit(int[] incomingEdges, 
                          TreeSet<Integer> grey, 
                          TreeSet<Integer> black, 
                          Digraph dg, 
                          int i) {
        grey.add(i);
        for (int nb : dg.adj(i)) {
            incomingEdges[nb] += 1;
            if (grey.contains(nb)) {
                return false;
            } else if (black.contains(nb)) {
                continue;
            } else {
                if (!visit(incomingEdges, grey, black, dg, nb))
                    return false;
            }
        }
        grey.remove(i);
        black.add(i);
        return true;
    }

    public Iterable<String> nouns()
    {
        return nounWords.keySet();
    }
    
    public boolean isNoun(String word)
    {
        if (word == null)
            throw new NullPointerException();
        return nounWords.containsKey(word);
    }
    
    public int distance(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        
        return sap.length(nounWords.get(nounA), 
                          nounWords.get(nounB));
    }
    
    public String sap(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        int ancestor = sap.ancestor(nounWords.get(nounA),
                                    nounWords.get(nounB));
        return syns.get(ancestor);
    }
    
    public static void main(String[] args)
    {
        WordNet wn = new WordNet("data\\wordnet\\synsets100-subgraph.txt", 
                "data\\wordnet\\hypernyms100-subgraph.txt");
        System.out.println(wn.distance("thing", "freshener"));
        System.out.println(wn.sap("thing", "freshener"));
    }

        
}
