import java.io.IOException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Postagger {
	public static String taggedString(String sample) throws IOException,
    ClassNotFoundException{
		MaxentTagger tagger = new MaxentTagger(
		        "tagger/english-left3words-distsim.tagger");
		String tagged = tagger.tagString(sample);
		return tagged;
	}
}
