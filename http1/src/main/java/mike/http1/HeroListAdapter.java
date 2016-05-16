package mike.http1;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 16-5-1.
 */
public class HeroListAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public HeroListAdapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtView;
        if (convertView!=null){
            txtView=(TextView)convertView;
        }else{
            txtView= new TextView(context);
        }

        txtView.setTextSize(20);
        cursor.moveToNext();
        txtView.setText("英雄名字:"+cursor.getString(1)+" 武器:"+cursor.getString(2)+" 攻击:"+cursor.getString(3)+" 防御:"+cursor.getString(4));
        return  txtView;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
}
