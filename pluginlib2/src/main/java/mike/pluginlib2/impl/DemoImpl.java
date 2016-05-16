package mike.pluginlib2.impl;

import android.content.Context;
import android.widget.Toast;

import mike.pluginlib2.interfaces.DemoInterface;

/**
 * Created by Administrator on 16-5-16.
 */
public class DemoImpl implements DemoInterface {

    private Context m_context;

    @Override
    public void init(Context context) {
        m_context = context;
    }

    @Override
    public void sayHello() {
        Toast.makeText(m_context, "hello world", Toast.LENGTH_SHORT).show();
    }

}
