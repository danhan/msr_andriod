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

public class SAXBugComment extends DefaultHandler {
	ArrayList<Integer> ids = null;
	ArrayList<Integer> bugIDs = null;
	ArrayList<Integer> numOfComments = null; // for how many comments in each
												// bug
	ArrayList<String> author = null;
	ArrayList<String> what = null;
	ArrayList<String> when = null;

	MySQLOperation dao = null;
	int numOfComment = 0; // how many comments in total 67730
	String[] columns = { "id", "bugid", "author", "when_", "what" };
	int[] types = { 0, 0, 2, 2, 2 };

	public ArrayList<Integer> parse(String filename, String database,
			String tableName) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		try {
			dao = new MySQLOperation();
			dao.preConnect(database, tableName);
		} catch (Exception e) {
			if (null != dao)
				dao.close();
			e.printStackTrace();
		}

		ids = new ArrayList<Integer>();
		bugIDs = new ArrayList<Integer>();
		numOfComments = new ArrayList<Integer>();
		author = new ArrayList<String>();
		when = new ArrayList<String>();
		what = new ArrayList<String>();

		saxParser.parse(filename, this);

		return numOfComments;
	}

	// state indicator

	private boolean parsingBugid = false;
	private boolean parsingAuthor = false;
	private boolean parsingWhen = false;
	private boolean parsingWhat = false;

	private String str_concat = "";// to get all what

	// Catch the start of an XML element
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("bugid")) {
			if (numOfComment > 0 && bugIDs.size() % 500 == 0) {
				System.out.println("write database..start Element");
				this.writeToDatabase();
			}
			parsingBugid = true;
			numOfComments.add(new Integer(0));
		} else if (qName.equals("comment")) {
			numOfComment++;
			ids.add(new Integer(this.numOfComment));
			int lastIndex = numOfComments.size() - 1;
			numOfComments.set(lastIndex, numOfComments.get(lastIndex)
					.intValue() + 1);
		} else if (qName.equals("author")) {
			parsingAuthor = true;
		} else if (qName.equals("when")) {
			parsingWhen = true;
		} else if (qName.equals("what")) {
			parsingWhat = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("what")) {
			parsingWhat = false;
			this.what.add(str_concat.trim());
			this.str_concat = "";
		} else if (qName.equals("Android_Bugs")) {
			System.out.println("write .......");
			this.writeToDatabase();
		}
	}

	// Catch the content of an XML element <bugid>content</bugid>
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (parsingBugid) {
			bugIDs.add(Integer.parseInt(new String(ch, start, length)));
			parsingBugid = false;
		} else if (parsingAuthor) {
			author.add(new String(ch, start, length));
			parsingAuthor = false;
		} else if (parsingWhen) {
			when.add(new String(ch, start, length));
			parsingWhen = false;
		} else if (parsingWhat) {
			str_concat += new String(ch, start, length);
		}
	}

	private void clearObjects() {
		this.ids.clear();
		this.numOfComments.clear();
		this.bugIDs.clear();
		this.author.clear();
		this.when.clear();
		this.what.clear();
	}

	private ArrayList<Integer> BugIdToRow() {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		try {
			for (int i = 0; i < this.bugIDs.size(); i++) {
				for (int j = 0; j < this.numOfComments.get(i).intValue(); j++)
					rows.add(this.bugIDs.get(i));
			}
			// System.out.println("row size: "+rows.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	/*
	 * "id","bugid","author","when","what"
	 */
	private void writeToDatabase() {
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<Integer> rows = BugIdToRow();
		try {
			System.out.println("bug number is : " + ids.size());

			for (int i = 0; i < ids.size(); i++) {
				values.add(ids.get(i).toString());
				// System.out.println("private key"+
				// ids.get(i)+"; bug number: "+rows.get(i));
				values.add(rows.get(i).toString());
				values.add(author.get(i));
				values.add(when.get(i));
				values.add(what.get(i));
				// System.out.println(values.toString());
				dao.insert(this.columns, values, this.types);
				values.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// clean all objects
		this.clearObjects();

	}

	static public void main(String[] args) {

		double start = System.currentTimeMillis();

		SAXBugComment sbi = new SAXBugComment();
		String database = "msr";
		String tableName = "bug_comment";
		try {
			String filename = (args.length > 0) ? args[1]
					: "./data/android_platform_bugs.xml";
			// : "./data/sub-xml.xml";
			ArrayList<Integer> numOfComments = sbi.parse(filename, database,
					tableName);
			int total = 0;
			for (Integer num : numOfComments) {
				total += num;
			}
			System.out.println("bug number: " + sbi.bugIDs.size());
			System.out.println("number of comments: "
					+ sbi.numOfComments.size());
			System.out.println("author: " + sbi.author.size());
			System.out.println("when: " + sbi.when.size());
			System.out.println("what: " + sbi.what.size());
			System.out.println("total comments: " + sbi.numOfComment);

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