package andriod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseBug extends DefaultHandler{
	
	private BugEntity bug = new BugEntity();
	private FileWriter fstream = null; // new FileWriter(file_name);
	private BufferedWriter out = null; //new BufferedWriter(fstream);
	private int num_of_bug = 0;
	

	public ParseBug(String file_log){
		try{
			fstream = new FileWriter(file_log);
			out = new BufferedWriter(fstream);	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {		
		String value = new String(ch,start,length);		
		if(value.trim().length()>0)
			bug.action(value.replace('\n', ' '));
		else
			bug.setState(null);
	}

	@Override
	public void endElement(String uri, String localName,String qName)
			throws SAXException {
		try{
			if(qName.equalsIgnoreCase("bug")){			
				// write into database
				//System.out.println("bug number: "+this.num_of_bug);
				//bug.print();
				// TODO write to database.....
				num_of_bug++;
				//System.out.println(num_of_bug++ +"||"+bug.toString());
				//System.out.println("==================");
			}else if(qName.equalsIgnoreCase("Android_Bugs")){	
				System.out.println(num_of_bug);
				out.close();
			}
		}catch(Exception e){
			try{
				if(null != out) out.close();	
			}catch(Exception ee){
				ee.printStackTrace();
			}			
			e.printStackTrace();
			
		}
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase("Android_Bugs")){
			//add some pre-process code
		}else if(qName.equalsIgnoreCase("bug")){
			bug.setState(Constants.BUG_START);
			bug.action(null);
		}else{			
			bug.setState(qName);
		}
		
	}

    public void execute(String filename) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();        

        saxParser.parse(filename, this);
    }	
	
	
    static public void main(String [] args) {
    	ParseBug bug_parser = new ParseBug("./data/bugs.txt");          
        try {
                String filename = (args.length>0)?args[1]:"./data/android_platform_bugs.xml";                        
                bug_parser.execute( filename );
                                
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
