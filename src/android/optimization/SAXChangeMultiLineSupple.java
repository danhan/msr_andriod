package android.optimization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import javax.xml.parsers.SAXParserFactory;

import mysql.MySQLOperation;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXChangeMultiLineSupple extends DefaultHandler {
	ArrayList<Integer> changeIDs = null; 	
	ArrayList<Integer> numOfMsgLine = null;
	ArrayList<Integer> numOfTargetLine = null;
	ArrayList<String> msgLine = null;
	ArrayList<String> targetLine = null;	
	int numOfChange = 0;

	MySQLOperation dao = null;	
	String[] columns = {"changeid","line_of_msg","line_of_target","message","target"};
	int[] types = {0,0,0,2,2};
	
	String delimiter = "|*|";
	String database = "";
	String tableName = "";
	
	public ArrayList<Integer> parse(String filename,String database, String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		this.database = database;
		this.tableName = tableName;
		changeIDs = new ArrayList<Integer>();
		numOfMsgLine = new ArrayList<Integer>();
		numOfTargetLine = new ArrayList<Integer>();
		msgLine = new ArrayList<String>();
		targetLine = new ArrayList<String>();		

		saxParser.parse(filename, this);

		return numOfTargetLine;
	} 

	private boolean isMsgLine = false;
	private String msg_str = "";
	private String target_str = "";
	
	// Catch the start of an XML element
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {		
		if (qName.equals("change")) {	
			if(numOfChange>0 && numOfChange % 1000 ==0){
				System.out.println("write . endElement......");
				this.writeToDatabase();
			}							
			numOfChange++;			 
			changeIDs.add(new Integer(numOfChange));
			numOfMsgLine.add(new Integer(0));
			numOfTargetLine.add(new Integer(0));
		}else if (qName.equals("message")) {			
			isMsgLine = true;
		}else if (qName.equals("line")) {
			if(isMsgLine){
				numOfMsgLine.set(numOfMsgLine.size()-1, numOfMsgLine.get(numOfMsgLine.size()-1).intValue()+1);			
			}else{
				numOfTargetLine.set(numOfTargetLine.size()-1, numOfTargetLine.get(numOfTargetLine.size()-1).intValue()+1);
			}
		}else if (qName.equals("target")) {			
			isMsgLine = false;
		}
	}
	

	// Catch the content of an XML element <bugid>content</bugid>
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isMsgLine) {
			msg_str += new String(ch, start, length);
			msg_str += delimiter;					
		}else if (!isMsgLine) {
			target_str += new String(ch, start, length);
			target_str += delimiter;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("changes")){
			System.out.println("write .......");
			this.writeToDatabase();
		}else if (qName.equals("message")){
			msgLine.add(msg_str);
			msg_str = "";
		}else if (qName.equals("target")){
			targetLine.add(target_str);
			target_str = "";
		}
	}	

	private void clearObjects(){
		this.changeIDs.clear();
		this.numOfMsgLine.clear();
		this.numOfTargetLine.clear();
		this.msgLine.clear();
		this.targetLine.clear();
	}
	/*
	"changeid","line_of_msg","line_of_target","message","target"
	 */	
		private void writeToDatabase(){	
			
			ArrayList<String> values = new ArrayList<String>();	
			
			try{
				System.out.println("change number is : "+changeIDs.size());
				if(changeIDs.get(0).intValue() == 1767001){
					try{
						 dao = new MySQLOperation();
						 dao.preConnect(this.database,this.tableName);	
					}catch(Exception e){
						if(null != dao) dao.close();
						e.printStackTrace();		
					}
				}
				if(changeIDs.get(0).intValue() > 1767000){
					System.out.println("start to write into database: change id: "+changeIDs.get(0));
					for(int i=0;i<changeIDs.size();i++){
						values.add(changeIDs.get(i).toString());						
						values.add(numOfMsgLine.get(i).toString());
						values.add(numOfTargetLine.get(i).toString());				
						values.add(msgLine.get(i));
						values.add(targetLine.get(i));
						dao.insert(this.columns, values,this.types);
						values.clear();
					}					
				}else{
					System.out.println("change id: "+changeIDs.get(0));
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
			//clean all objects
			this.clearObjects();					
		}	
	

	static public void main(String[] args) {
		
		double start = System.currentTimeMillis();
		
		SAXChangeMultiLineSupple sbi = new SAXChangeMultiLineSupple();
		String database = "msr";
		String tableName = "change_message";		
		try {
			String filename = (args.length > 0) ? args[1]
					: "/home/dan/Downloads/git.log.xml";
			ArrayList<Integer> numOfTargetLine = sbi.parse(filename,database,tableName);			
			int total = 0;
			for (Integer num : numOfTargetLine) {
				//System.out.println(num);
				total += num;
			}
			System.out.println("change number: "+sbi.numOfChange);	
			System.out.println("change number: "+sbi.changeIDs.size());			
			System.out.println("numOfMsgLine: "+sbi.numOfMsgLine.size());
			System.out.println("numOfTargetLine: "+sbi.numOfTargetLine.size());
			System.out.println("msgLine: "+sbi.msgLine.size());
			System.out.println("targetLine: "+sbi.targetLine.size());	
			System.out.println("target line total: "+total);
			
			System.out.println("execution time: "
					+ (System.currentTimeMillis() - start) / 1000);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

	}
}