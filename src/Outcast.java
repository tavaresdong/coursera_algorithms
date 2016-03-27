import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    
    private final WordNet wn;

    public Outcast(WordNet wordnet)
    {
        wn = wordnet;
    }
    
    public String outcast(String[] nouns)
    {
        String ret = null;
        int dist = Integer.MIN_VALUE;
        for (String noun : nouns) {
            int cur = lengthSum(noun, nouns);
            System.out.println("Word: " + noun + " Length: " + cur);
            if (cur > dist) {
                dist = cur;
                ret = noun;
            }
        }
        return ret;
    }
    
    private int lengthSum(String noun, String[] group)
    {
        int len = 0;
        for (String word : group) {
            if (word.equals(noun))
                continue;
            len += wn.distance(noun, word);
        }
        return len;
    }
    
    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }
}
