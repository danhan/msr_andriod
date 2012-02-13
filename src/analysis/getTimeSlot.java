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
import java.util.StringTokenizer;


public class getTimeSlot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		String inFile = args[0];
		//String outFile = args[1];
		FileInputStream fis = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ"); 
		SimpleDateFormat rDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat rDay = new SimpleDateFormat("yyyy-MM");
		
		try{
			 fis = new FileInputStream(inFile); 
			 InputStreamReader isr = new InputStreamReader(fis);
			 in = new BufferedReader(isr);			 			
			String line = in.readLine();
			long max = Long.MIN_VALUE;
			long min = Long.MAX_VALUE;
			String min_date = "";
			String max_date = "";
			String date_str = "";
			
			while(line != null){
				StringTokenizer tokenizer = new StringTokenizer(line,"\t");				
				
				String date = tokenizer.nextToken();
				Date date_formated = df.parse(date);	
				
				String to_day = rDay.format(date_formated);
				long time = date_formated.getTime();
				if(time<min) {
					min = time;
					if(to_day.contains("1970"))
						continue;
					min_date = to_day;					
					date_str = date;
				}
				if(time>max){
					max = time;
					max_date = to_day;
					
				}
		
				line = in.readLine();
			}		
			System.out.println("min: "+min_date+";max: "+max_date+";date string: "+date_str);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != in) in.close();				
			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}

}
