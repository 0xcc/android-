package mike.http1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-27.
 */
public class MySAXParserHelper extends DefaultHandler{

    List<City> list=new ArrayList<>();
    City city;

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        state=-1;
        if (city!=null && localName.equals("city")){
            list.add(city);
        }

    }

    int state=0;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("city")){
            String id=attributes.getValue("id");
            city=new City();
            city.setId(id);
            state=0;
        }else if (localName.equals("name")){
            state=1;

        }else if (localName.equals("code")){
            state=2;
        }
    }



    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        String theString=String.valueOf(ch,start,length);
        if (state==1){
            city.setName(theString);
        }else if (state==2){
            city.setCode(theString);
        }

    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }
}
