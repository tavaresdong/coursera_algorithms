import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public final class WordNet {
    
    private final class Synset {
        private final List<String> words;
        private final String desc;
        
        Synset(List<String> ws, String ds) {
            words = ws;
            desc = ds;
        }
    }
    
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
                     new Synset(wordList, line[2].trim()));
            
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
