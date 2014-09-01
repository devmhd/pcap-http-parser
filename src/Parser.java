import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;




public class Parser {
	
	static String outputFilename, inputFilename;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length == 0){
			System.err.println("Argument missing.");
			return;
		}
		inputFilename = args[0];
		
		if(args.length == 1){
			outputFilename = "output.txt";
			
		} else{
			outputFilename = args[1];
		}
		
		
		
		try {
			Process p = Runtime.getRuntime().exec("python main.py "+ inputFilename + " output.json");
			
			BufferedReader reader = new BufferedReader(new FileReader("output.json"));
			String line = null;
			
			StringBuilder sb = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			
			
			JSONObject whole = new JSONObject(sb.toString());
			
		
			
			JSONArray entries = whole.getJSONObject("log").getJSONArray("entries");
			
			StringBuilder output = new StringBuilder();
			
			for(int i=0; i< entries.length(); ++i){
				
				JSONObject entry = entries.getJSONObject(i);
				JSONObject request = entry.getJSONObject("request");
				JSONArray queries = request.getJSONArray("queryString");
				
				output.append("" + i+ ". " + request.getString("method") +  "\n");
				output.append(request.getString("url") + "\n");
				
				
				if(queries.length() != 0){
					output.append("Queries:\n");
					for(int j=0; j<queries.length(); ++j){
						output.append("\t" + j + ". " + queries.getJSONObject(j).getString("name") + " : " + queries.getJSONObject(j).getString("value") + '\n');
					}
					
				}
				
				output.append("\n");
				
				
			}
			
			System.out.println(output.toString());
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFilename)));
            writer.write(output.toString());
            writer.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

	}

}
