package sample;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class LineParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file_name = "D:\\alberta\\courses\\664_mining_software_repositories\\project\\android_platform_changes\\git.log.xml";
		String output_name = "./data/code.xml";
		BufferedReader in = null;
		OutputStreamWriter out = null;
		        
		getEncoding(file_name);
		try {			
			
	        FileInputStream fis = new FileInputStream(file_name); 
	        InputStreamReader isr = new InputStreamReader(fis);
	        in = new BufferedReader(isr); 
			
			FileOutputStream fstream = new FileOutputStream(output_name);		
			out = new OutputStreamWriter(fstream);//
			
			String line = in.readLine();
			int counter = 100;
			do {
				out.write(line);
				System.out.println(line);
				line = in.readLine();
				counter--;
			} while (line != null && counter >0);
			
			System.out.println("finish");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				in.close();	
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		

	}
	
	public static void getEncoding(String name){
		InputStream inputStream = null; 
		try{
			inputStream = new FileInputStream(name);
	        byte[] head = new byte[3];  
	        inputStream.read(head);    
	        String code = "";  	   
	            code = "gb2312";  
	        if (head[0] == -1 && head[1] == -2 )  
	            code = "UTF-16";  
	        if (head[0] == -2 && head[1] == -1 )  
	            code = "Unicode";  
	        if(head[0]==-17 && head[1]==-69 && head[2] ==-65)  
	            code = "UTF-8";  
	          
	        System.out.println(code); 			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	

}