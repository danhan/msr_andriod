package sample;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXBugID extends DefaultHandler{
        ArrayList<Integer> bugIDs = null;     
        
        public ArrayList<Integer> parse(String filename) throws ParserConfigurationException, SAXException, IOException {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                bugIDs = new ArrayList<Integer>();

                saxParser.parse(filename, this);

                return bugIDs;
        }

        //state indicator

        private boolean parsingBugid = false;

        // Catch the start of an XML element
        public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
                        if (qName.equals("bugid")) {
                                // state change

                                parsingBugid = true;
                        }
        }
        // Catch the content of an XML element <bugid>content</bugid>
        public void characters(char[] ch, int start, int length) throws SAXException {
                if (parsingBugid) {
                        bugIDs.add(Integer.parseInt(new String(ch, start, length)));
                        // state change

                        parsingBugid = false;
                }
        }

        static public void main(String [] args) {
                SAXBugID sbi = new SAXBugID();          
                try {
                        String filename = (args.length>0)?args[1]:"./data/android_platform_bugs.xml";                        
                        ArrayList<Integer> bugIDs = sbi.parse( filename );
                        Collections.sort(bugIDs);
                        for (Integer bugid : bugIDs) {
                                System.out.println(bugid);
                        }
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