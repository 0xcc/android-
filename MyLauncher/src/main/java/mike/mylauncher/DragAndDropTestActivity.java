package mike.mylauncher;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DragAndDropTestActivity extends AppCompatActivity {

    TextView textViewdragable;
    FrameLayout targetFrameLayout;
    FrameLayout targetFrameLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        textViewdragable=(TextView)findViewById(R.id.txtViewDragable);

        targetFrameLayout=(FrameLayout)findViewById(R.id.targetFrameLayout);
        targetFrameLayout.setOnDragListener(frameLayoutDragListener);

        targetFrameLayout2=(FrameLayout)findViewById(R.id.targetFrameLayout2);
        targetFrameLayout2.setOnDragListener(frameLayoutDragListener2);

        textViewdragable.setOnLongClickListener(textViewLongClickListener);
        //textViewdragable.setOnDragListener(textViewDragListener);

    }

    private View.OnDragListener frameLayoutDragListener=new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            String action="";
            boolean result=false;
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_ENDED:
                    action="1::ACTION_DRAG_ENDED";
                    result= true;
                    break;
                case  DragEvent.ACTION_DRAG_ENTERED:
                    action="1::ACTION_DRAG_ENTERED";
                    result= true;
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    action="1::ACTION_DRAG_EXITED";
                    result= false;
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    action="1::ACTION_DRAG_LOCATION";
                    result= true;
                    return true;
                    //break;
                case DragEvent.ACTION_DRAG_STARTED:
                    action="1::ACTION_DRAG_STARTED";
                    result= true;
                    break;
                case DragEvent.ACTION_DROP:
                    action="1::ACTION_DROP";
                    result= true;
                    break;
            }
            Log.i("onDrag","class1: "+v.getClass().getName());
            Log.i("onDrag","Drag frame 1 : "+action);
            return result;
        }
    };

    private View.OnDragListener frameLayoutDragListener2=new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            String action="";
            boolean result=false;
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_ENDED:
                    action="2::ACTION_DRAG_ENDED";
                    result= true;
                    break;
                case  DragEvent.ACTION_DRAG_ENTERED:
                    action="2::ACTION_DRAG_ENTERED";
                    result= true;
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    action="2::ACTION_DRAG_EXITED";
                    result= true;
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    action="2::ACTION_DRAG_LOCATION";
                    result= true;
                    return true;
                    //break;
                case DragEvent.ACTION_DRAG_STARTED:
                    action="2::ACTION_DRAG_STARTED";
                    result= true;
                    break;
                case DragEvent.ACTION_DROP:
                    action="2::ACTION_DROP";
                    result= true;
                    break;
            }
            Log.i("onDrag","class2: "+v.getClass().getName());
            Log.i("onDrag","Drag frame 2 : "+action);
            return result;
        }
    };

    private View.OnLongClickListener textViewLongClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Log.i("onLongClick","onLongClick");
            ClipData.Item item = new ClipData.Item("ClipData.Item");
            String[] mimeTypes=new String[1];
            mimeTypes[0]="text/plain";

            ClipData dragData = new ClipData("datalabel",mimeTypes,item);

            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(textViewdragable);
            v.startDrag(dragData,myShadow,null,0);
            return true;
        }
    };

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics (Point size, Point touch){
        // Defines local variables
          int width, height;

        // Sets the width of the shadow to half the width of the original View
        width = getView().getWidth() ;

        // Sets the height of the shadow to half the height of the original View
        height = getView().getHeight() ;

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.

        shadow.setBounds(0, 0, width, height);

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height);
        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width/2 , height/2);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    @Override
    public void onDrawShadow(Canvas canvas) {

        // Draws the ColorDrawable in the Canvas passed in from the system.
        shadow.draw(canvas);
    }
}



}
