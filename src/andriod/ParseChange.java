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

public class ParseChange extends DefaultHandler{
	
	private ChangeEntity change = new ChangeEntity();
	private FileWriter fstream = null; // new FileWriter(file_name);
	private BufferedWriter out = null; //new BufferedWriter(fstream);
	private int num_of_change = 0;
	

	public ParseChange(String file_log){
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
			change.action(value.replace("&apos", ""));
		else
			change.setState(null);
	}

	@Override
	public void endElement(String uri, String localName,String qName)
			throws SAXException {
		try{
			if(qName.equalsIgnoreCase("change")){			
				// write into database
				//System.out.println("change number: "+this.num_of_change);
				//change.print();
				// TODO write to database.....
				System.out.println(num_of_change++ +"||"+change.toString());
				//System.out.println("==================");
			}else if(qName.equalsIgnoreCase("changes")){				
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
		if(qName.equalsIgnoreCase("changes")){
			//add some pre-process code
		}else if(qName.equalsIgnoreCase("change")){			
			change.setState(Constants.CHANGE_START);
			change.action(null);
		}else{						
			change.setState(qName);
		}
		
	}

    public void execute(String filename) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();        

        saxParser.parse(filename, this);
}	
	
	
    static public void main(String [] args) {
    	ParseChange change_parser = new ParseChange("./data/changes.txt");          
        try {
                String filename = (args.length>0)?args[1]:"./data/sub-change.xml";                        
                change_parser.execute( filename );
                                
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


