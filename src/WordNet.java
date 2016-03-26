import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;

final class WordNet {
    
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
    private final TreeSet<String> nounWords;
    
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        Scanner ssc = new Scanner(synsets);
        Scanner hsc = new Scanner(hypernyms);
        
        syns = new TreeMap<Integer, Synset>();
        nounWords = new TreeSet<String>();
        
        // Input the nouns
        while (ssc.hasNextLine()) {
            String[] line = ssc.nextLine().split(",");
            assert(line.length == 3);
            String[] words = line[1].trim().split("\\s");
            List<String> wordList = new ArrayList<String>();
            for (String word : words) {
                wordList.add(word);
                nounWords.add(word);
            }
            syns.put(Integer.parseInt(line[0].trim()), 
                     new Synset(line[1].trim(), wordList, line[2].trim()));
            
        }
        
        digraph = new Digraph(syns.size());
        while (hsc.hasNextLine()) {
            String[] numbers = hsc.nextLine().split(",");
            int hypo = Integer.parseInt(numbers[0].trim());
            for (int i = 1; i < numbers.length; i++) {
                int hype = Integer.parseInt(numbers[i].trim());
                digraph.addEdge(hypo, hype);
            }
        }
    }
    
    public Iterable<String> nouns()
    {
        return nounWords;
    }
    
    public boolean isNoun(String word)
    {
        if (word == null)
            throw new NullPointerException();
        return nounWords.contains(word);
    }
    
    public int distance(String nounA, String nounB)
    {
        
    }
    
    public String sap(String nounA, String nounB)
    {
        
    }
    
    public static void main(String[] args)
    {
        
    }

        
}
