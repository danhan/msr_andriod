package sample;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class ReadXMLFile {
 
   public static void main(String argv[]) {
 
    try {
 
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
 
	DefaultHandler handler = new DefaultHandler() {
 
	boolean bfname = false;
	boolean blname = false;
	boolean bnname = false;
	boolean bsalary = false;
	
 
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
	
		
		System.out.println("Start Element :" + qName);			
		
		if (qName.equalsIgnoreCase("bugid")) {
			bfname = true;
		}
 
		if (qName.equalsIgnoreCase("title")) {
			blname = true;
		}
 
		if (qName.equalsIgnoreCase("status")) {
			bnname = true;
		}
 
		if (qName.equalsIgnoreCase("owner")) {
			bsalary = true;
		}
 
	}
 
	public void endElement(String uri, String localName,
		String qName) throws SAXException {
		
		if (qName.equalsIgnoreCase("bug")) {
			System.out.println("End Element :" + qName);			
		}
		
	
	}
 
	public void characters(char ch[], int start, int length) throws SAXException {
 
		if (bfname) {
			System.out.println("First Name : " + new String(ch, start, length));
			bfname = false;
		}
 
		if (blname) {
			System.out.println("Last Name : " + new String(ch, start, length));
			blname = false;
		}
 
		if (bnname) {
			System.out.println("Nick Name : " + new String(ch, start, length));
			bnname = false;
		}
 
		if (bsalary) {
			System.out.println("Salary : " + new String(ch, start, length));
			bsalary = false;
		}
 
	}
 
     };
 
       saxParser.parse("D:\\alberta\\courses\\664_mining_software_repositories\\project\\android_platform_changes\\git.log.xml", handler);
 
     } catch (Exception e) {
       e.printStackTrace();
     }
 
   }
 
}
