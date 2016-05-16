package mike.http1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLDOMActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmldom);

        List<City> result= DOMXMLParse();

        StringBuilder str=new StringBuilder();
        for (int i=0;i<result.size();i++){
            str.append(result.get(i).toString()+"\n");
        }

        TextView txtView=(TextView)findViewById(R.id.txtView);
        txtView.setText(str.toString());
    }

    private List<City> DOMXMLParse(){
        List<City> result=new ArrayList<City>();
        DocumentBuilder documentBuilder=null;
        DocumentBuilderFactory documentBuilderFactory=null;
        Document document=null;
        InputStream inputStream=null;
        documentBuilderFactory=DocumentBuilderFactory.newInstance();

        try {
            documentBuilder=documentBuilderFactory.newDocumentBuilder();


            inputStream=getResources().openRawResource(R.raw.city);
            document=documentBuilder.parse(inputStream);

            Element root=document.getDocumentElement();
            NodeList nodeList=root.getElementsByTagName("city");
            City city=null;

            for (int i=0;i<nodeList.getLength();i++){
                Element cityElement=(Element)nodeList.item(i);
                city=new City();
                city.setId(cityElement.getAttribute("id"));
                Element nameElement=(Element)cityElement.getElementsByTagName("name").item(0);
                city.setName(nameElement.getTextContent());
                city.setCode(((Element) cityElement.getElementsByTagName("code").item(0)).getTextContent());
                result.add(city);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  result;
    }

}
