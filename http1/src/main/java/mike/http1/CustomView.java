package mike.http1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 16-5-2.
 */
public class CustomView extends View {

    private Bitmap bitmap;
    private Matrix matrix;
    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void  init(){
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.youtube);
        matrix=new Matrix();
        float cosValue=(float)Math.cos(-Math.PI/6);
        float sinValue=(float)Math.sin(-Math.PI/6);

        try {
            matrix.setValues(new float[]{
                    cosValue,-sinValue,100,
                    sinValue,cosValue,100,
                    0,0,2
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
         //canvas.drawBitmap(bitmap,0.0f,0.0f,null);

         canvas.drawBitmap(bitmap,matrix,null);
        //bitmap.recycle();
    }
}
