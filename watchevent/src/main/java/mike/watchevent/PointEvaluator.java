package mike.watchevent;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Administrator on 16-1-1.
 */
public class PointEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction,Object startValue,Object endValue){
        Log.i("fraction:",fraction+"");
        Point startPoint=(Point)startValue;
        Point endPoint=(Point)endValue;
        float x=startPoint.x+fraction*(endPoint.x-startPoint.x);
        float y=startPoint.y+fraction*(endPoint.y-startPoint.y);
        Point point=new Point((int)x,(int)y);
        return point;
    }
}














