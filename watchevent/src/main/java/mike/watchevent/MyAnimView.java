package mike.watchevent;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Administrator on 16-1-1.
 */
public class MyAnimView  extends View {

    public static final int RADIUS=50;
    private Point currentPoint;
    private Paint mPaint;

    String color;


    public MyAnimView(Context context,AttributeSet attrs){
        super(context,attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if (currentPoint==null){
            currentPoint=new Point(RADIUS,RADIUS);
            drawCircle(canvas);
            startAnimation();
        }else{
            drawCircle(canvas);
        }

    }

    private void drawCircle(Canvas canvas){

        float x=currentPoint.x;
        float y=currentPoint.y;
        canvas.drawCircle(x,y,RADIUS,mPaint);
    }

    private void startAnimation(){
        Point startPoint=new Point(RADIUS,RADIUS);
        Point endPoint=new Point(getWidth()-RADIUS,getHeight()-RADIUS);
        ValueAnimator anim=ValueAnimator.ofObject(new PointEvaluator(),startPoint,endPoint);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                currentPoint=(Point)animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(5000);
        anim.setInterpolator(new AccelerateInterpolator(2f));
        anim.start();
    }

    public String getColor(){
        return this.color;
    }

    public void setColor(String color){
        this.color=color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }

}















