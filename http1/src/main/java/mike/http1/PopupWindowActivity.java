package mike.http1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupWindowActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
        textView=(TextView)findViewById(R.id.textview1);
        button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
                initMenu();
            }
        });

    }
    PopupWindow mMenu;

    private void initPopupWindow(){
        View contentView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.popwindow1,null);
        contentView.setBackgroundColor(Color.BLUE);

        final PopupWindow popupWindow=new PopupWindow(findViewById(R.id.main_layout),300,200);

        popupWindow.setContentView(contentView);
        final EditText editText=(EditText)contentView.findViewById(R.id.edit_text1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(button);



        Button btn_sure=(Button)contentView.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                mMenu.dismiss();
            }
        });



    }

    private void initMenu(){
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        LinearLayout layout= (LinearLayout)layoutInflater.inflate(R.layout.popupwindow_menu, null);

         mMenu=new PopupWindow(layout,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        mMenu.setAnimationStyle(R.style.popupAnimation);

        mMenu.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        mMenu.update();
    }


    private void initAnimationPopupWindow(int resid){

        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        View view= layoutInflater.inflate(resid, null);

        PopupWindow popupWindow=new PopupWindow(view,400, ViewGroup.LayoutParams.WRAP_CONTENT);

        //popupWindow.setBackgroundDrawable();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.update();


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

         
        //popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

}
