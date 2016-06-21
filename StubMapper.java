import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StubMapper extends Mapper<LongWritable, Text, Text, Text> {
	String ProductID = "";
	String ReviewText = "";
	String pos_ReviewText="";
	boolean firstline = true;
	static Double reviewscore = 0.0;
	static String pathToSWN = "/home/training/workspace/OpinionAnalysis/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
	SentiWordNet sn = new SentiWordNet(pathToSWN);
	String analysis = "";
  @Override
  
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
	  try {
		  	if(!value.toString().equals("")){
		  		String [] tokens = value.toString().split(",");
				ProductID =  tokens[1].trim();
				ReviewText =  tokens[5].trim().toLowerCase();
				if(firstline){
					firstline = false;
				}
		  		else{
		  		pos_ReviewText = Postagger.taggedString(ReviewText);
		  		System.out.println(pos_ReviewText);
		  		Double score = sn.getsentencescore(pos_ReviewText.toString());
		  		System.out.println(score);
				reviewscore =  score;
				  	if(reviewscore >= 0.75){
				  		analysis = "very positive";
				  	}
				  	else if (reviewscore > 0.25 && reviewscore<0.5){
						analysis = "positive";
					}
				  	else if (reviewscore >= 0.5){
						analysis =  "positive";
					}
				  	else if (reviewscore < 0 && reviewscore >= -0.25){
						analysis = "negative";
					}
				  	else if (reviewscore < -0.25 && reviewscore >= -0.5){
						analysis = "negative";
					}
				  	else if (reviewscore <= -0.75){
						analysis = "very negative";
					}
				  	else{
				  		analysis = "neutral";
				  	}
				System.out.println(ProductID + "has " + analysis + "opinion");
		  		context.write(new Text(ProductID), new Text(analysis));
		  		}
		  	}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	  	  catch (ArrayIndexOutOfBoundsException e) {
	  		e.printStackTrace();
	  	} 
  	}
}
