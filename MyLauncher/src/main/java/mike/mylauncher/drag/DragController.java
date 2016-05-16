package mike.mylauncher.drag;

import android.gesture.Gesture;
import android.view.View;

/**
 * Created by Administrator on 15-11-27.
 */
public class DragController {
    //正在被拖动的view
    public View mViewDraging;

    public void startDrag(View view){
        endDrag();
        mViewDraging=view;
    }

    public void move(float eventX,float eventY){

    }

    public void endDrag(){
        mViewDraging=null;
    }

}
