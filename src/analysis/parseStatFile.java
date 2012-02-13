package analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class parseStatFile {

	
	
	private static void analyzeByYear(String inFile,String outFile){

		FileInputStream fis = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ"); 
		SimpleDateFormat rDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat rDay = new SimpleDateFormat("yyyy");
		
		try{
			 fis = new FileInputStream(inFile); 
			 InputStreamReader isr = new InputStreamReader(fis);
			 in = new BufferedReader(isr);			 
			 FileOutputStream fstream = new FileOutputStream(outFile);		
			out = new OutputStreamWriter(fstream);//			
			String line = in.readLine();
			ArrayList<String> ids = new ArrayList<String>();
			HashMap<Integer,Integer> dayHash = generateTimeSeriesByYear();		
			while(line != null){
				StringTokenizer tokenizer = new StringTokenizer(line,"\t");				
				
				String id = tokenizer.nextToken();	
				ids.add(id);
				String date = tokenizer.nextToken();
				Date date_formated = df.parse(date);				
				String to_day = rDay.format(date_formated);
				
				int int_day = Integer.parseInt(to_day);
				System.out.println(int_day);
				dayHash.put(int_day,dayHash.get(int_day).intValue()+1);												
				
				line = in.readLine();
			}					
			
			
			Map<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(dayHash);
						
			for(int key: sortedMap.keySet()){
				
				out.write(key+"\t"+dayHash.get(key)+"\n");
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != in) in.close();
				if(null != in) out.close();
			}catch(Exception e){
				e.printStackTrace();
			}

		}		
	}
	
	private static void analyzeByMonth(String inFile,String outFile){

		FileInputStream fis = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ"); 
		SimpleDateFormat rDate = new SimpleDateFormat("yyyy-MM");		
		
		try{
			 fis = new FileInputStream(inFile); 
			 InputStreamReader isr = new InputStreamReader(fis);
			 in = new BufferedReader(isr);			 
			 FileOutputStream fstream = new FileOutputStream(outFile);		
			out = new OutputStreamWriter(fstream);//			
			String line = in.readLine();
			ArrayList<String> ids = new ArrayList<String>();
			HashMap<String,Integer> dayHash = generateTimeSeriesByMonth();	
			System.out.println(dayHash.keySet().toString());
			while(line != null){
				StringTokenizer tokenizer = new StringTokenizer(line,"\t");				
				
				String id = tokenizer.nextToken();	
				ids.add(id);
				String date = tokenizer.nextToken();
				
				Date date_formated = df.parse(date);				
				String to_day = rDate.format(date_formated);
				if(dayHash.containsKey(to_day))
					dayHash.put(to_day,dayHash.get(to_day).intValue()+1);												
				
				line = in.readLine();
			}					
			
			
			Map<String, Integer> sortedMap = new TreeMap<String, Integer>(dayHash);
						
			for(String key: sortedMap.keySet()){
				
				out.write(key+"\t"+dayHash.get(key)+"\n");
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != in) in.close();
				if(null != in) out.close();
			}catch(Exception e){
				e.printStackTrace();
			}

		}		
	}	
	
	
	
	private static HashMap<Integer,Integer> generateTimeSeriesByYear(){
		HashMap<Integer,Integer> dayHash = new HashMap<Integer,Integer>();
		int min = 1970;
		int max = 2011;
		try{
			for(int i=min;i<=max;i++){
				dayHash.put(i, 0);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return dayHash;
	}
	
	private static HashMap<String,Integer> generateTimeSeriesByMonth(){
		HashMap<String,Integer> dayHash = new HashMap<String,Integer>();
		int min = 2003;
		int max = 2011;
		try{
			for(int i=min;i<=max;i++){
				String year = new Integer(i).toString();
				for(int j=1;j<=12;j++){
					if((i == max)&&(j>6)){
						break;
					}
					if(j<10)
						dayHash.put(year+"-"+"0"+j, 0);
					else
						dayHash.put(year+"-"+j, 0);
						
				}
					
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return dayHash;
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		if(args[0].equals("y")){
			analyzeByYear(args[1],args[2]);
		}else if(args[0].equals("m")){
			analyzeByMonth(args[1],args[2]);
		}else{
			System.out.println("y/m infile outfile");
		}
		

	}

}
