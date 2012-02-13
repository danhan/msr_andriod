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

public class CombineFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		combineMonth();
	}

	private static void combineMonth(){
		FileInputStream fis = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		
		try{
			String files[] = {"/tmp/source-change.stat.m",
							"/tmp/test-change.stat.m",
							"/tmp/build-change.stat.m",
							"/tmp/doc-change.stat.m"
								};
			
			String outFile = "/tmp/combine.m";
			ArrayList<Integer> source = new ArrayList<Integer>();
			ArrayList<Integer> test = new ArrayList<Integer>();
			ArrayList<Integer> build = new ArrayList<Integer>();
			ArrayList<Integer> doc = new ArrayList<Integer>();
				
			for(int i=0;i<files.length;i++){
				 fis = new FileInputStream(files[i]); 
				 InputStreamReader isr = new InputStreamReader(fis);
				 in = new BufferedReader(isr);			 
			
				String line = in.readLine();
				while(line != null){
					StringTokenizer tokenizer = new StringTokenizer(line,"\t");				
					
					String id = tokenizer.nextToken();						
					int num = Integer.parseInt(tokenizer.nextToken());
					
					if(i ==0){
						source.add(num);
					}else if(i == 1){
						test.add(num);
					}else if(i == 2){
						build.add(num);
					}else if(i == 3){
						doc.add(num);
					}
					
					line = in.readLine();
				}				
			}
			
				
			 FileOutputStream fstream = new FileOutputStream(outFile);		
			 out = new OutputStreamWriter(fstream);//					 			
			 	
			out.write("source code"+"\t"+"test code"+"\t"+"build code"+"\t"+"document"+"\n");
			for(int i=0;i<source.size();i++){
				
				out.write(source.get(i)+"\t"+test.get(i)+"\t"+build.get(i)+"\t"+doc.get(i)+"\n");
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
	
	
	private static void combineYear(){
		FileInputStream fis = null;
		BufferedReader in = null;
		OutputStreamWriter out = null;
		
		try{
			String files[] = {"/tmp/source-change.stat.y",
							"/tmp/test-change.stat.y",
							"/tmp/build-change.stat.y",
							"/tmp/doc-change.stat.y"
								};
			
			String outFile = "/tmp/combine.y";
			ArrayList<Integer> source = new ArrayList<Integer>();
			ArrayList<Integer> test = new ArrayList<Integer>();
			ArrayList<Integer> build = new ArrayList<Integer>();
			ArrayList<Integer> doc = new ArrayList<Integer>();
				
			for(int i=0;i<files.length;i++){
				 fis = new FileInputStream(files[i]); 
				 InputStreamReader isr = new InputStreamReader(fis);
				 in = new BufferedReader(isr);			 
			
				String line = in.readLine();
				while(line != null){
					StringTokenizer tokenizer = new StringTokenizer(line,"\t");				
					
					String id = tokenizer.nextToken();						
					int num = Integer.parseInt(tokenizer.nextToken());
					
					if(i ==0){
						source.add(num);
					}else if(i == 1){
						test.add(num);
					}else if(i == 2){
						build.add(num);
					}else if(i == 3){
						doc.add(num);
					}
					
					line = in.readLine();
				}				
			}
			
				
			 FileOutputStream fstream = new FileOutputStream(outFile);		
			 out = new OutputStreamWriter(fstream);//					 			
			 	
			out.write("source code"+"\t"+"test code"+"\t"+"build code"+"\t"+"document"+"\n");
			for(int i=0;i<source.size();i++){
				
				out.write(source.get(i)+"\t"+test.get(i)+"\t"+build.get(i)+"\t"+doc.get(i)+"\n");
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
	

}
