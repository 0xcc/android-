package mike.watchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Administrator on 15-12-21.
 */
public class Button2 extends Button {

    public Button2(Context context) {
        super(context);
    }

    public Button2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.i("dispatch", "Button2 TouchEvent " + event.getPointerId(event.getActionIndex())+" x:"+event.getX());
        return true;
        //return super.dispatchTouchEvent(event);
    }
}
