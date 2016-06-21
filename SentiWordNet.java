import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SentiWordNet {
	private static Map<String, Double> dictionary;
	static String pathToSWN = "/home/training/workspace/OpinionAnalysis/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
	public SentiWordNet(String pathToSWN) {
		
		dictionary = new HashMap<String, Double>();
		
		
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;
				if (!line.trim().startsWith("#")) {
				
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					
					String[] synTermsSplit = data[4].split(" ");

					
					for (String synTermSplit : synTermsSplit) {
					
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "_"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
											if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

					
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				try {
					csv.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public  Double getsentencescore(String sentence){
		Double sentencescore = 0.0;
		Double wordscore = 0.0;
		String [] words = sentence.split(" ");
		for(String word : words){
			String [] SplitWord = word.split("_");
			String pos = wordnet_pos_code(SplitWord[1]);
			if(pos==""){
				 wordscore = 0.0;
			}
			else{
				wordscore = extract(SplitWord[0],pos);
			}
			sentencescore += wordscore;
			
		}
		return sentencescore;
		
}
	public static String wordnet_pos_code(String pos){
		
		if(pos.startsWith("J")){
			pos = "a"; 
			return pos; 
		}
		 if(pos.startsWith("V")){
			pos = "v"; 
			return pos;
		}
		 if(pos.startsWith("R")){
			pos = "r"; 
			return pos;
		}
		 if(pos.startsWith("N")){
			pos = "n";
			return pos;
		}
		return "";
	}
	public static double extract(String word, String pos) {
		try{
			return dictionary.get(word + "_" + pos);
		}
		catch(NullPointerException e){
			return 0.0;
		}
	}
	/*public static void main (String [] args) throws IOException {
		SentiWordNet sn = new SentiWordNet(pathToSWN);
		Double score = extract("good","a");
		System.out.println(score);
	}*/
}
	