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

public class SAXChangeMeta extends DefaultHandler {
	ArrayList<Integer> ids = null;
	ArrayList<String> project = null;
	ArrayList<String> commit_hash = null;
	ArrayList<String> tree_hash = null;
	ArrayList<String> parent_hashes = null;
	ArrayList<String> author_name = null;
	ArrayList<String> author_email = null;
	ArrayList<String> subject = null;
	ArrayList<String> commiter_name = null;
	ArrayList<String> commiter_email = null;
	ArrayList<String> committer_date = null;
	ArrayList<String> author_date = null;
	ArrayList<Integer> line_of_msg = null;
	ArrayList<Integer> line_of_target = null;
	
	int numOfChange = 0;
	
	MySQLOperation dao = null;	

	String[] columns = {"id","project","commit_hash","tree_hash","parent_hashes","author_name",
						"author_email","author_date","commiter_name","commiter_email","committer_date","subject","line_of_msg","line_of_target"
						};
	int[] types = {0,2,2,2,2,2,2,2,2,2,2,2,0,0};

	public ArrayList<String> parse(String filename,String database,String tableName)
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
		project = new ArrayList<String>();
		commit_hash = new ArrayList<String>();		
		tree_hash = new ArrayList<String>();
		parent_hashes = new ArrayList<String>();
		author_name = new ArrayList<String>();
		author_email = new ArrayList<String>();
		subject = new ArrayList<String>();
		commiter_name = new ArrayList<String>();
		commiter_email = new ArrayList<String>();
		committer_date = new ArrayList<String>();
		author_date = new ArrayList<String>();
		line_of_msg = new ArrayList<Integer>();
		line_of_target = new ArrayList<Integer>();

		saxParser.parse(filename, this);

		return project;
	}
	
	

	// state indicator
	private boolean isProject = false;
	private boolean isAuthorEmail = false;
	private boolean isCommitHash = false;
	private boolean isTreeHash = false;
	private boolean isParentHashes = false;
	private boolean isAuthorName = false;
	private boolean isSubject = false;
	private boolean isCommiterName = false;
	private boolean isCommiterEmail = false;
	private boolean isCommitterDate = false;
	private boolean isAuthorDate = false;
	private boolean isMsg = false;

	// Catch the start of an XML element
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("project")) {
			if(numOfChange>0 && numOfChange % 10000 ==0){
				System.out.println("write . endElement......");
				this.writeToDatabase();
			}			
			numOfChange++;
			ids.add(new Integer(this.numOfChange));
			line_of_msg.add(new Integer(0));
			line_of_target.add(new Integer(0));
			isProject = true;
		} else if (qName.equals("author_e-mail")) {
			isAuthorEmail = true;
		} else if (qName.equals("commit_hash")) {
			isCommitHash = true;
		} else if (qName.equals("tree_hash")) {
			isTreeHash = true;
		} else if (qName.equals("parent_hashes")) {
			isParentHashes = true;
		} else if (qName.equals("author_name")) {
			isAuthorName = true;
		} else if (qName.equals("subject")) {
			isSubject = true;
		} else if (qName.equals("commiter_name")) {
			isCommiterName = true;
		} else if (qName.equals("commiter_email")) {
			isCommiterEmail = true;
		} else if (qName.equals("committer_date")) {
			isCommitterDate = true;
		}else if (qName.equals("author_date")) {
			isAuthorDate = true;
		}else if (qName.equals("message")) {
			isMsg = true;
		}else if (qName.equals("target")) {
			isMsg = false;
		}else if(qName.equals("line")){
			if(isMsg){
				line_of_msg.set(line_of_msg.size()-1, line_of_msg.get(line_of_msg.size()-1).intValue()+1);			
			}else{
				line_of_target.set(line_of_target.size()-1, line_of_target.get(line_of_target.size()-1).intValue()+1);
			}			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("changes")){
			System.out.println("write .......");
			this.writeToDatabase();
		}
	}

	// Catch the content of an XML element <bugid>content</bugid>
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String value = new String(ch,start,length);		
		if(value.trim().length()<=0)
			value = "N/A";
		
		if (isProject) {
			project.add(value);
			isProject = false;
		}else if(isAuthorEmail){
			author_email.add(value);
			isAuthorEmail = false;			
		}else if(isCommitHash){
			commit_hash.add(value);
			isCommitHash = false;			
		}
		if(isTreeHash){
			tree_hash.add(value);
			isTreeHash = false;			
		}else if(isParentHashes){
			parent_hashes.add(value);
			isParentHashes = false;			
		}else if(isAuthorName){
			author_name.add(value);
			isAuthorName = false;			
		}else if(isSubject){
			subject.add(value);
			isSubject = false;			
		}else if(isCommiterName){
			commiter_name.add(value);
			isCommiterName = false;			
		}else if(isCommiterEmail){
			commiter_email.add(value);
			isCommiterEmail = false;			
		}else if(isCommitterDate){
			committer_date.add(value);
			isCommitterDate = false;			
		}else if(isAuthorDate){
			author_date.add(value);
			isAuthorDate = false;			
		}
	}
	private void clearObjects(){
		this.ids.clear();
		this.project.clear();
		this.commit_hash.clear();
		this.tree_hash.clear();
		this.parent_hashes.clear();
		this.author_date.clear();
		this.author_email.clear();
		this.author_name.clear();
		this.subject.clear();
		this.commiter_email.clear();
		this.commiter_name.clear();
		this.committer_date.clear();
		this.line_of_msg.clear();
		this.line_of_target.clear();		
	}
/*
* "id","project","commit_hash","tree_hash","parent_hashes","author_name",
* "author_email","author_date","commiter_name","commiter_email","committer_date","subject","line_of_msg","line_of_target"	
 */	
	private void writeToDatabase(){	
		
		ArrayList<String> values = new ArrayList<String>();	
		try{
			System.out.println("change number is : "+ids.size());
			
			for(int i=0;i<ids.size();i++){
				values.add(ids.get(i).toString());						
				values.add(project.get(i));
				values.add(commit_hash.get(i));				
				values.add(tree_hash.get(i));
				values.add(parent_hashes.get(i));
				values.add(author_name.get(i));
				values.add(author_email.get(i));						
				values.add(author_date.get(i));					
				values.add(commiter_name.get(i));
				values.add(commiter_email.get(i));				
				values.add(committer_date.get(i));				
				values.add(subject.get(i));
				values.add(line_of_msg.get(i).toString());
				values.add(line_of_target.get(i).toString());
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
		SAXChangeMeta sbi = new SAXChangeMeta();
		String database = "msr";
		String tableName = "change_report";
		try {
			String filename = (args.length > 0) ? args[1]
					//: "/media/Research/alberta/courses/664_mining_software_repositories/project/android_platform_changes/git.log.xml";
					: "/home/dan/Downloads/git.log.xml";
			ArrayList<String> project = sbi.parse(filename,database,tableName);
			Collections.sort(project);
			for (String bugid : project) {
				//System.out.println(bugid);
			}
			System.out.println("project number : " + sbi.project.size());
			System.out.println("author_date number : " + sbi.author_date.size());
			System.out.println("author_email number : " + sbi.author_email.size());
			System.out.println("author_name number : " + sbi.author_name.size());
			System.out.println("commit_hash number : " + sbi.commit_hash.size());
			System.out.println("commiter_email number : " + sbi.commiter_email.size());
			System.out.println("commiter_name number : " + sbi.commiter_name.size());
			System.out.println("committer_date number : " + sbi.committer_date.size());
			System.out.println("parent_hashes number : " + sbi.parent_hashes.size());			
			System.out.println("tree_hash number : " + sbi.tree_hash.size());
			System.out.println("subject number : " + sbi.subject.size());		
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