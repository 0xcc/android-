package mike.http1;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

import javax.net.ssl.SSLEngineResult;

public class XMLSerializerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlserializer);
        SaveXML();
    }

    private void SaveXML(){
        File newxmlfile=new File(Environment.getExternalStorageDirectory()+"/poem.xml");

        try {
            newxmlfile.createNewFile();
            FileOutputStream fileos=new FileOutputStream(newxmlfile);
            XmlSerializer serializer= Xml.newSerializer();
            serializer.setOutput(fileos,"UTF-8");

            serializer.startDocument(null,Boolean.TRUE);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);
            serializer.startTag(null,"poem");
            serializer.attribute(null,"lang","chinese");
                serializer.startTag(null,"title");
                    serializer.text("akjdkajsd");
                serializer.endTag(null,"title");
            serializer.endTag(null,"poem");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
