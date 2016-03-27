import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    
    private final class Synset {
        private final String synset;
        private final List<String> words;
        private final String desc;
        
        Synset(String ss, List<String> ws, String ds) {
            synset = ss;
            words = ws;
            desc = ds;
        }
    }
    
    private final Digraph digraph;
    private final TreeMap<Integer, Synset> syns;
    private final TreeMap<String, Integer> nounWords;
    private final SAP sap;
    
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        In ssc = new In(synsets);
        In hsc = new In(hypernyms);
        
        syns = new TreeMap<Integer, Synset>();
        nounWords = new TreeMap<String, Integer>();
        
        // Input the nouns
        while (ssc.hasNextLine()) {
            String[] line = ssc.readLine().split(",");
            assert(line.length == 3);
            String[] words = line[1].trim().split("\\s");
            List<String> wordList = new ArrayList<String>();
            for (String word : words) {
                wordList.add(word);
                nounWords.put(word, Integer.parseInt(line[0].trim()));
            }
            syns.put(Integer.parseInt(line[0].trim()), 
                     new Synset(line[1].trim(), wordList, line[2].trim()));
            
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
        
        sap = new SAP(digraph);
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
        return sap.length(nounWords.get(nounA), 
                          nounWords.get(nounB));
    }
    
    public String sap(String nounA, String nounB)
    {
        int ancestor = sap.ancestor(nounWords.get(nounA),
                                    nounWords.get(nounB));
        return syns.get(ancestor).synset;
    }
    
    public static void main(String[] args)
    {
        In in = new In("data\\wordnet\\digraph1.txt");
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
