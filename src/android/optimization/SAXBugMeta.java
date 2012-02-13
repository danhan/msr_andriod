package android.optimization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import javax.xml.parsers.SAXParserFactory;

import mysql.MySQLOperation;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXBugMeta extends DefaultHandler {
	ArrayList<Integer> ids = null;
	ArrayList<Integer> bugIDs = null;
	ArrayList<String> titles = null;
	ArrayList<String> status = null;
	ArrayList<String> closedOn = null;
	ArrayList<String> owner = null;
	ArrayList<String> type = null;
	ArrayList<String> priority = null;
	ArrayList<String> component = null;
	ArrayList<String> stars = null;
	ArrayList<String> reportBy = null;
	ArrayList<String> openDate = null;
	ArrayList<String> desc = null;
	ArrayList<Integer> numOfComments = null; // 61195
	
	MySQLOperation dao = null;	
	String[] columns = {"id","bugid","title","status","closedOn","owner",
						"type","priority","component","stars","reportedBy","openedDate","description","num_of_comments"
						};
	int[] types = {0,0,2,2,2,2,2,2,2,2,2,2,2,0};
	
	
	int numOfBug = 0;
	
	public ArrayList<Integer> parse(String filename,String database,String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();		
		try{
			 dao = new MySQLOperation();
			 dao.preConnect(database,tableName);	
		}catch(Exception e){
			if(null != dao) dao.close();
			e.printStackTrace();		
		}		
		 
		ids = new ArrayList<Integer>();
		bugIDs = new ArrayList<Integer>();
		titles = new ArrayList<String>();
		status = new ArrayList<String>();
		closedOn = new ArrayList<String>();
		owner = new ArrayList<String>();
		type = new ArrayList<String>();
		priority = new ArrayList<String>();
		component = new ArrayList<String>();
		stars = new ArrayList<String>();
		reportBy = new ArrayList<String>();
		openDate = new ArrayList<String>();
		desc = new ArrayList<String>();
		numOfComments = new ArrayList<Integer>();
		saxParser.parse(filename, this);

		return bugIDs;
	}

	// state indicator

	private boolean parsingBugid = false; // 20169
	private boolean parsingTtitle = false; // 20169
	private boolean parsingStatus = false; // 20169
	private boolean parsingclosedOn = false; // 20169
	private boolean parsingOwner = false; // 20169
	private boolean parsingType = false; // 20169
	private boolean parsingPriority = false; // 20169
	private boolean parsingComponent = false; // 20169
	private boolean parsingStars = false; // 20169
	private boolean parsingReportBy = false; // 20169
	private boolean parsingOpenDate = false; // 20169
	private boolean parsingDesc = false; // 20169
	
	private String str_concat = "";

	// Catch the start of an XML element
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("bugid")) {
			if(numOfBug>0 && numOfBug % 1000 ==0){
				System.out.println("write . endElement......");
				this.writeToDatabase();
			}				
			numOfBug++;
			ids.add(new Integer(this.numOfBug));
			numOfComments.add(new Integer(0));
			parsingBugid = true;
		}else if(qName.equals("comment")){
			int lastIndex = numOfComments.size()-1;
			numOfComments.set(lastIndex, numOfComments.get(lastIndex).intValue()+1);
		}else if (qName.equals("title")) {
			parsingTtitle = true;
		} else if (qName.equals("status")) {
			parsingStatus = true;
		}else if (qName.equals("owner")) {
			parsingOwner = true;
		}else if (qName.equals("closedOn")) {
			parsingclosedOn = true;
		}else if (qName.equals("type")) {
			parsingType = true;
		}else if (qName.equals("priority")) {
			parsingPriority = true;
		}else if (qName.equals("component")) {
			parsingComponent = true;
		}else if (qName.equals("stars")) {
			parsingStars = true;
		}else if (qName.equals("reportedBy")) {
			parsingReportBy = true;
		}else if (qName.equals("openedDate")) {
			parsingOpenDate = true;
		}else if(qName.equals("description")){
			parsingDesc = true;
		}
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("Android_Bugs")){
			System.out.println("write .......");
			this.writeToDatabase();
		}else if(qName.equals("description")){
			parsingDesc = false;
			this.desc.add(str_concat.trim());
			this.str_concat = "";
		}
	}



	// Catch the content of an XML element <bugid>content</bugid>
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (parsingBugid) {		
			bugIDs.add(Integer.parseInt(new String(ch, start, length)));
			parsingBugid = false;
		} else if (parsingTtitle) {
			this.titles.add(new String(ch, start, length));
			parsingTtitle = false;
		} else if (parsingStatus) {
			this.status.add(new String(ch, start, length));
			parsingStatus = false;
		}else if (parsingOwner) {
			this.owner.add(new String(ch, start, length));
			parsingOwner = false;
		}else if (parsingclosedOn) {
			this.closedOn.add(new String(ch, start, length));
			parsingclosedOn = false;
		}else if (parsingType) {
			this.type.add(new String(ch, start, length));
			parsingType = false;
		}else if (parsingPriority) {
			this.priority.add(new String(ch, start, length));
			parsingPriority = false;
		}else if (parsingComponent) {
			this.component.add(new String(ch, start, length));
			parsingComponent = false;
		}else if (parsingStars) {
			this.stars.add(new String(ch, start, length));
			parsingStars = false;
		}else if (parsingReportBy) {
			this.reportBy.add(new String(ch, start, length));
			parsingReportBy = false;
		}else if (parsingOpenDate) {
			this.openDate.add(new String(ch, start, length));
			parsingOpenDate = false;
		}else if (parsingDesc) {
			str_concat += new String(ch,start,length); 			
		}
	}
	
	private void clearObjects(){
		this.ids.clear();
		this.numOfComments.clear();
		this.bugIDs.clear();
		this.titles.clear();
		this.status.clear();
		this.closedOn.clear();
		this.owner.clear();
		this.type.clear();
		this.priority.clear();
		this.component.clear();
		this.stars.clear();
		this.reportBy.clear();
		this.openDate.clear();
		this.desc.clear();
	}

/*
 * "id","bugid","title","status","closedOn","owner","type","priority","component","stars","reportedBy","openedDate","description"
 */
	private void writeToDatabase(){	
		ArrayList<String> values = new ArrayList<String>();	
		try{
			System.out.println("bug number is : "+ids.size());
			
			for(int i=0;i<bugIDs.size();i++){
				values.add(ids.get(i).toString());
				System.out.println("private key"+ ids.get(i)+"; bug number: "+bugIDs.get(i));
				values.add(bugIDs.get(i).toString());
				values.add(titles.get(i));
				values.add(status.get(i));				
				values.add(closedOn.get(i));
				values.add(owner.get(i));
				values.add(type.get(i));
				values.add(priority.get(i));
				values.add(component.get(i));
				values.add(stars.get(i));
				values.add(reportBy.get(i));
				values.add(openDate.get(i));
				values.add(desc.get(i));
				values.add(numOfComments.get(i).toString());
				//System.out.println(values.toString());
				dao.insert(this.columns, values,this.types);
				values.clear();
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		//clean all objects
		this.clearObjects();
		
	}	

	static public void main(String[] args) {
		double start = System.currentTimeMillis();
		SAXBugMeta sbi = new SAXBugMeta();
		try {
			String filename = (args.length > 0) ? args[1]
					: "./data/android_platform_bugs.xml";
					//: "./data/sub-xml.xml";
			String database = "msr";
			String tableName = "bug_report";
			ArrayList<Integer> bugIDs = sbi.parse(filename,database,tableName);
			Collections.sort(bugIDs);
			for (Integer bugid : bugIDs) {
				// System.out.println(bugid);
			}
			System.out.println("bug number: " + sbi.ids.size());
			System.out.println("bug number: " + bugIDs.size());
			System.out.println("title number: " + sbi.titles.size());
			System.out.println("status number: " + sbi.status.size());
			System.out.println("closeOn number: " + sbi.closedOn.size());
			System.out.println("owner number: " + sbi.owner.size());
			System.out.println("type number: " + sbi.type.size());
			System.out.println("priority number: " + sbi.priority.size());
			System.out.println("component number: " + sbi.component.size());
			System.out.println("stars number: " + sbi.stars.size());
			System.out.println("reportBy number: " + sbi.reportBy.size());
			System.out.println("openDate number: " + sbi.openDate.size());
			System.out.println("desc number: " + sbi.desc.size());
			
			System.out.println("execution time: "
					+ (System.currentTimeMillis() - start));

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