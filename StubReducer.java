import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StubReducer extends Reducer<Text, Text, Text, IntWritable> {
	int counter = 0;
	int posscore = 0;
	int negscore = 0;
	int neuscore = 0;
	
	@Override
  public void reduce(Text key, Iterable <Text> values, Context context)
      throws IOException, InterruptedException {
		for( Text Value : values){
		  counter++;
		  
		  if(Value.toString().contains("positive")){
			  posscore = posscore + 1;
		  }
		  else if(Value.toString().contains("negative")){
			  negscore = negscore + 1;
		  }
		  else{
			  neuscore = neuscore + 1;
		  }
		  
	   }
		context.write(new Text("No of Records"), new IntWritable(counter));
		context.write(new Text("No of Positive Reviews"), new IntWritable(posscore));
		context.write(new Text("No of Negative Reviews"), new IntWritable(negscore));
		context.write(new Text("No of Neutral Reviews"), new IntWritable(neuscore));
	}
}