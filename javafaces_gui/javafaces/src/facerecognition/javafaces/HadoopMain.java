package facerecognition.javafaces;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class HadoopMain {

	public static class Map extends MapReduceBase implements
	Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);

		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
						throws IOException {

			/*getting the path of the Image*/
			String imagePath = value.toString();
			System.out.println(imagePath);
			/*Call the recognize Method*/
			String result=FaceRec.recognize(imagePath);

			/*resultText contains path of output folder*/
			Text resultText=new Text(result);
			output.collect(resultText, one);

		}


	}
	public static class Reduce extends MapReduceBase implements
	Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
						throws IOException {
			output.collect(key, new IntWritable());
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(HadoopMain.class);
		conf.setJobName("faceReco");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setNumMapTasks(7);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(0);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}


}
