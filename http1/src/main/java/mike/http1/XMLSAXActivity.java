package mike.http1;

import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLSAXActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlsax);

        XMLParse();
    }

    private List<City> XMLParse(){
        List<City> result=new ArrayList<>();

        try{

            SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
            SAXParser parser=saxParserFactory.newSAXParser();
            XMLReader xmlReader=parser.getXMLReader();

            MySAXParserHelper helperHandler=new MySAXParserHelper();
            xmlReader.setContentHandler(helperHandler);
            InputStream stream=getResources().openRawResource(R.raw.city);
            InputSource is=new InputSource(stream);
            xmlReader.parse(is);

            result=helperHandler.list;

            StringBuilder str=new StringBuilder();

            for (int i=0;i<result.size();i++){
                City city=result.get(i);
                str.append(city.getId()).append(",").append(city.getName()).append(",").append(city.getCode()).append("\n");
            }

            TextView txtView=(TextView)findViewById(R.id.txtView);
            txtView.setText(str.toString());

        }catch (Exception e){

        }




        return result;
    }

}
