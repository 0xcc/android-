package mike.mylauncher.CCTableLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import mike.mylauncher.utils.MyLog;

/**
 * Created by Administrator on 15-11-20.
 */
public class MyTextView extends TextView {


    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.getText().equals("ABCDEFGHIJK")){
            MyLog.i("MyTextView", "Xleft:" + this.getLeft() + " top:" + this.getTop() + " right:" + this.getRight() + " bottom:" + this.getBottom());
        }

       // MyLog.i("MyTextView","left:"+this.getLeft()+" top:"+this.getTop()+" right:"+this.getRight()+" bottom:"+this.getBottom());

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//
//        MyLog.i("MyTextView.measure","widthMeasureSpec:"+MeasureSpec.getSize(widthMeasureSpec)+" heightMeasureSpec:"+MeasureSpec.getSize(heightMeasureSpec));
//    }


}
