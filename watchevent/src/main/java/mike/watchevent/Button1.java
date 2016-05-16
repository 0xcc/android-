package mike.watchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Administrator on 15-12-21.
 */
public class Button1 extends Button {


    public Button1(Context context) {
        super(context);
    }

    public Button1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.i("dispatch","Button1 TouchEvent "+event.getPointerId(event.getActionIndex())+" x:"+event.getX());
        return true;
        //return super.dispatchTouchEvent(event);
    }
}
