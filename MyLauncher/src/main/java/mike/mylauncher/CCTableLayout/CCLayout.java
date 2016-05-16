package mike.mylauncher.CCTableLayout;

import android.animation.ValueAnimator;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mike.mylauncher.R;
import mike.mylauncher.androidutils.utils;
import mike.mylauncher.components.LaunchableActivity;
import mike.mylauncher.utils.MyLog;

/**
 * Created by Administrator on 16-1-1.
 */
public class CCLayout extends ViewGroup {

    public static class SmallCell{
        int mRowPixel1;
        int mColPixel1;
        int mRowPixel2;
        int mColPixel2;
        BigCell container;

        public SmallCell(int rowPixel1,int colPixel1,int rowPixel2,int colPixel2){
            this.mRowPixel1=rowPixel1;
            this.mColPixel1=colPixel1;

            this.mRowPixel2=rowPixel2;
            this.mColPixel2=colPixel2;
        }

        public SmallCell(Point point1,Point point2){
            this.mRowPixel1=point1.x;
            this.mColPixel1=point1.y;

            this.mRowPixel2=point2.x;
            this.mColPixel2=point2.y;
        }

        public void reset(int x1,int y1,int x2,int y2){
            this.mRowPixel1=x1;
            this.mColPixel1=y1;
            this.mRowPixel2=x2;
            this.mColPixel2=y2;
        }

        //一个点是否在smallcell中
        public boolean in(Point point){
            boolean result=false;
            if (point.x>= mRowPixel1 && point.x<mRowPixel2
                    && point.y>= mColPixel1 && point.y<mColPixel2 ){
                result=true;
            }
            return  result;
        }

        public boolean in(int x,int y){
            return in(new Point(x,y));
        }

        public void setContainer(BigCell bigCell){
            this.container=bigCell;
        }

        public void free(){
            this.container=null;
        }
    }

    public  class BigCell{
        //[ [] ,[])
        int mRowIdx1;
        int mColIdx1;

        int mRowIdx2;
        int mColIdx2;

        //左右padding
        public int mPaddingLR;

        //上下padding
        public int mPaddingBT;

        View mView;
        //Object tag;
        public BigCell(int rowIdx1,int colIdx1,int rowIdx2,int colIdx2){
            this.mRowIdx1=rowIdx1;
            this.mColIdx1=colIdx1;
            this.mRowIdx2=rowIdx2;
            this.mColIdx2=colIdx2;
        }

        public void setView(View view){
            if (view!=null){
                this.mView=view;
                resetViewPosition();
                view.setTag(view.getId(), this);
            }else {
                if (mView.getTag(mView.getId())!=null){
                    mView.setTag(mView.getId(),null);
                }
                this.mView=null;
            }

        }

        public void resetViewPosition(){

            if (mView instanceof LinearLayout && (mView instanceof MyLinerLayout)==false){
                MyLog.i("asdf","asdf");
            }

            int x1=this.mColIdx1*mSmallWidth;
            int y1=this.mRowIdx1*mSmallHeight;
            int x2=this.mColIdx2*mSmallWidth;
            int y2=this.mRowIdx2*mSmallHeight;

            this.mView.setLeft(x1);
            this.mView.setTop(y1);
            this.mView.setRight(x2);
            this.mView.setBottom(y2);
            this.mView.setX(x1);
            this.mView.setY(y1);

            CCLayoutParams layoutParams=(CCLayoutParams)this.mView.getLayoutParams();
            if (layoutParams==null){
                layoutParams=generateDefaultLayoutParams();
                mView.setLayoutParams(layoutParams);
            }
            layoutParams.width=x2-x1;
            layoutParams.height=y2-y1;

            this.mView.layout(x1,y1,x2,y2);
        }


        public void free(){
            if (mSmallCells!=null){
                for (int i=mRowIdx1;i<mRowIdx2;i++){
                    for (int j=mColIdx1;j<mColIdx2;j++){
                        SmallCell cell=mSmallCells[i][j];
                        cell.free();
                        BigCell bigCell= new BigCell(i,j,i+1,j+1);
                        if (bigCellList.contains(bigCell)) {
                            bigCellList.remove(bigCell);
                        }
                    }
                }
            }
            //this.tag=null;
            this.mRowIdx1=mRowIdx2=mColIdx1=mColIdx2=0;
//            this.mView.setTag(mView.getId(),null);
            this.setView(null);
            bigCellList.remove(this);
        }


        @Override
        public boolean equals(Object object){
            if (object instanceof  BigCell){
                BigCell bigCell=(BigCell)object;
                if (bigCell.mColIdx1==this.mColIdx1 && bigCell.mRowIdx1==this.mRowIdx1 &&
                        bigCell.mRowIdx2==this.mRowIdx2 &&  bigCell.mColIdx2==this.mColIdx2){
                    return true;
                }
            }
            return  false;
        }

        public BigCell copy(){
            BigCell result= new BigCell(this.mRowIdx1,this.mColIdx1,this.mRowIdx2,this.mColIdx2);
            return result;
        }

    }

    public class CCLayoutParams extends ViewGroup.MarginLayoutParams {

        public BigCell mBigCell;

        public CCLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CCLayoutParams(LayoutParams source){
            super(source);
        }

        public CCLayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public BigCell findBigCell(BigCell bigCell){
        for (BigCell cell :bigCellList){
            if (cell.equals(bigCell)){
                return  cell;
            }
        }
        return null;
    }

    public BigCell createBigCell(int rowIdx1,int colIdx1,int rowIdx2,int colIdx2){
        if (rowIdx1<0 || rowIdx2<0  || colIdx1<0 || colIdx2<0){
            throw  new IllegalArgumentException("any argument can't be smaller than 0");
        }

        if (mSmallCells==null){
            throw new IllegalStateException("cant't create bigcell before grids info inited");
        }

        if (mSmallCells.length< rowIdx1 || mSmallCells.length<rowIdx2 ){
            throw  new IllegalArgumentException("row index out of bounds");
        }

        if (mSmallCells[rowIdx1].length < colIdx1 || mSmallCells[rowIdx1].length<colIdx2 ){
            throw new IllegalArgumentException("colum index out bounds");
        }

        if (canCreatBigCell(rowIdx1,colIdx1,rowIdx2,colIdx2)==false){
            throw new IllegalStateException("can't create bigcell at illegal position");
        }

        BigCell bigCell=new BigCell(rowIdx1,colIdx1,rowIdx2,colIdx2);
        if (bigCellList.contains(bigCell)==false){
            bigCellList.add(bigCell);
            for (int i=rowIdx1;i<rowIdx2;i++){
                for (int j=colIdx1;j<colIdx2;j++){
                    mSmallCells[i][j].container=bigCell;
                }
            }
        }
        return bigCell;
    }

    public boolean canCreatBigCell(int rowIdx1,int colIdx1,int rowIdx2,int colIdx2){
        for (int i=rowIdx1;i<rowIdx2;i++){
            for (int j=colIdx1;j<colIdx2;j++){
                if( mSmallCells[i][j].container!=null){
                    return false;
                }
            }
        }
        return true;
    }

    List<BigCell> bigCellList=new ArrayList<BigCell>();
    SmallCell[][] mSmallCells;

    private int mSmallWidth;
    private int mSmallHeight;

    private int mRows;
    private int mColumns;

    private int initialX;
    private int initialY;
    private float lastTouchX=0;
    private float lastTouchY=0;

    public View mDragingView;

    private final static int CONTAINER_KEY=0;

    public int getRows(){
        return mRows;
    }

    public int getColumns(){
        return mColumns;
    }

    public CCLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray a=context.getTheme().obtainStyledAttributes(attrs, R.styleable.cclayout, 0, 0);

        try {
            mRows=a.getInt(R.styleable.cclayout_rows,4);
            mColumns=a.getInt(R.styleable.cclayout_columns, 4);

            mSmallWidth=  a.getDimensionPixelSize(R.styleable.cclayout_cellwidth, 100);
            mSmallHeight=a.getDimensionPixelSize(R.styleable.cclayout_cellheight, 100);

            initSmallCellArray();
        }catch (Exception err){
            MyLog.i("err",err.getMessage());
        }
        finally {
            a.recycle();
        }
    }


    private void initSmallCellArray(){
        if (mSmallCells==null){
            mSmallCells=new SmallCell[mRows][mColumns];
        }

        for (int row=0;row<mRows;row++){
            if (mSmallCells[row]==null){
                mSmallCells[row]=new SmallCell[mColumns];
            }
            for (int column=0;column<mColumns;column++){
                int x1=column*mSmallWidth;
                int y1=row*mSmallHeight;
                int x2=(column+1)*mSmallWidth;
                int y2=(row+1)*mSmallHeight;

                SmallCell cell=mSmallCells[row][column];
                if (cell==null){
                    mSmallCells[row][column]=new SmallCell(x1,y1,x2,y2);
                }else{
                    if (cell.mRowPixel1!=x1 || cell.mColPixel1!= y1 ||
                            cell.mRowPixel2!=x2 || cell.mColPixel2!=y2) {
                        cell.reset(x1,y1,x2,y2);
                    }
                }
            }
        }
    }


    @Override
    protected CCLayoutParams generateDefaultLayoutParams(){

        CCLayoutParams layoutParams=new CCLayoutParams(0,0);
        return  layoutParams;

    }

    protected LayoutParams generateLayoutParams(int width,int height) {
        CCLayoutParams layoutParams=new CCLayoutParams(width,height);
        return  layoutParams;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        if (getMeasuredWidth()!=widthSize || getMeasuredHeight() !=heightSize){

            widthSize=mColumns*mSmallWidth;
            heightSize=mRows* mSmallHeight;
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize-1 , MeasureSpec.EXACTLY));
            initSmallCellArray();
            int childcnt=getChildCount();

            int childWidth=MeasureSpec.makeMeasureSpec(mSmallWidth, MeasureSpec.EXACTLY);
            int childHeight=MeasureSpec.makeMeasureSpec(mSmallHeight,MeasureSpec.EXACTLY);
            for (int i=0;i<childcnt;i++){
                View child=getChildAt(i);

                child.measure(childWidth,childHeight);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (BigCell bigCell:bigCellList){
            //if (bigCell.mView!=null && bigCell.mView instanceof )
            bigCell.resetViewPosition();
        }
    }

    public void addView(LaunchableActivity activityInfo,int row1, int col1, int row2, int col2) {
        Context context= getContext();

        Drawable drawable = activityInfo.getActivityIcon(context.getPackageManager(), context, 100);
        String appName = activityInfo.getActivityLabel().toString();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MyLinerLayout linearLayout=(MyLinerLayout)layoutInflater.inflate(R.layout.app_item, null);

        ImageView imageView=(ImageView) linearLayout.findViewById(R.id.appImg);
        imageView.setImageDrawable(drawable);

        TextView textView = (TextView) linearLayout.findViewById(R.id.txtAppName);
        textView.setText(appName);

        addView(linearLayout, row1, col1, row2, col2);
        linearLayout.setTag(activityInfo);

        linearLayout.setOnClickListener(mAppClickListener);
        linearLayout.setOnLongClickListener(mAppLongClickListener);
    }


    public void addView(AppWidgetManager appWidgetManager,AppWidgetHost appWidgetHost,int appWidgetId){

        AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;
        AppWidgetHostView hostView=appWidgetHost.createView(getContext(), appWidgetId, appWidgetProviderInfo);
        int widget_minWidth = appWidgetProviderInfo.minWidth ;
        int widget_minHeight = appWidgetProviderInfo.minHeight ;

        MyLinerLayout layout=new MyLinerLayout(this.getContext());

        int columncnt=0;
        if (widget_minWidth%mSmallWidth==0){
            columncnt=widget_minWidth/mSmallWidth;
        }else{
            columncnt=(widget_minWidth+mSmallWidth)/mSmallWidth;
        }

        int rowcnt=0;

        if (widget_minHeight%mSmallHeight==0){
            rowcnt=widget_minHeight/mSmallHeight;
        }else{
            rowcnt=(widget_minHeight+mSmallHeight)/mSmallHeight;
        }

        int row1=-1,row2=-1 ,col1=-1,col2=-1;
        for (int row=0;row<=mSmallCells.length-rowcnt;row++){
            //限定row区域
            int startrow=row;
            int endrow=row+rowcnt;
            if (endrow>mSmallCells.length){
                break;
            }

            int okcol=0;
            for (int startcol=0;startcol<mSmallCells[0].length;startcol++){
                boolean restart=false;
                if (col1==-1){
                    col1=startcol;
                    okcol=0;
                }
                for (int rowidx=startrow;rowidx<endrow;rowidx++){
                    if (mSmallCells[rowidx][startcol].container!=null) {
                        col1=-1;
                        break;
                    }
                }
                okcol++;
                if (okcol==columncnt&&col1!=-1){
                    col2=startcol+1; //col1+columncnt;
                    col1=col2-columncnt;
                    break;
                }
            }

            if (okcol==columncnt){
                row1=startrow;
                row2=endrow;
                break;
            }
        }

        //添加widght
        if (row1!=-1 && row2!=-1 && col1!=-1 && col2!=-1){
            layout.setBackgroundColor(Color.YELLOW);
            addView(layout, row1, col1, row2, col2);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(widget_minWidth,LayoutParams.MATCH_PARENT);
            hostView.setLayoutParams(params);

            layout.addView(hostView);
            layout.setGravity(Gravity.CENTER);
            layout.setOnLongClickListener(mAppLongClickListener);
            layout.setInterceptEvent(mWidgetInterceptEvent);
        }
    }

    private View.OnClickListener mAppClickListener=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final LaunchableActivity activityInfo=(LaunchableActivity)v.getTag();
            //activityInfo.getComponent()

            Animation animation=createScaleAnimation(v);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent();
                    ComponentName component = activityInfo.getComponent();
                    intent.setComponent(component);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            v.startAnimation(animation);
        }
    };

    private BigCell oldBigCell;

    private OnLongClickListener mAppLongClickListener=new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag(v.getId())!=null) {
                getParent().requestDisallowInterceptTouchEvent(true);
                mDragingView = v;
                v.bringToFront();
                BigCell bigCell = (BigCell) v.getTag(v.getId());
                oldBigCell=bigCell.copy();
                bigCell.free();

                animateDragging();
            }
            return true;
        }
    };

    private MyLinerLayout.InterceptEvent mWidgetInterceptEvent=new MyLinerLayout.InterceptEvent() {
        @Override
        public boolean onInterceptEvent(MotionEvent event) {
            boolean result=false;
            int action=event.getAction();
            switch (event.getAction()&MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_MOVE:
                    if (mDragingView!=null)
                        return true;
                    break;
            }

            return false;
        }
    };


    private void animateDragging(){
        if (mDragingView!=null){
            Animation scaleAnimation=createDragAnimation(1.0f, 1.2f);
            mDragingView.clearAnimation();
            mDragingView.startAnimation(scaleAnimation);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //MyLog.i("asdf", "onInterceptTouchEvent");
        if (mDragingView!=null) {
            int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    MyLog.i("asdf", "dwon");
                    touchDown(ev);
                    break;
                case MotionEvent.ACTION_MOVE:
                    MyLog.i("asdf", "move");
                    touchMove(ev);
                    break;
                case MotionEvent.ACTION_UP:
                    MyLog.i("asdf", "up");
                    touchUp(ev);
                    break;
            }
        }

        return false;
    }

    private void touchDown(MotionEvent event) {
        initialX = (int) event.getRawX();
        initialY = (int) event.getRawY();
        lastTouchX=event.getRawX();
        lastTouchY=event.getRawY();
    }

    private void touchMove(MotionEvent event){
        if (mDragingView!=null){
            MyLog.i("x,y","x:"+event.getX()+" y:"+event.getY());
            moveDraggedView(event);
        }
    }

    private void touchUp(MotionEvent event){
        if (mDragingView!=null){
            boolean dragresult=false;
            dragresult= replaceDragingView((int)event.getX(),(int)event.getY());
            if (dragresult==false){
                oldBigCell.setView(mDragingView);
            }

            Animation scaleAnimation=createDragAnimation(1.2f,1.0f);
            mDragingView.clearAnimation();
            mDragingView.startAnimation(scaleAnimation);

            mDragingView=null;
            getParent().requestDisallowInterceptTouchEvent(false);

        }
        lastTouchX=0;
        lastTouchY=0;
        oldBigCell=null;
    }

    private boolean replaceDragingView(int x,int y){
        int width=mDragingView.getWidth();
        int height=mDragingView.getHeight();

        int columncnt=1;
        int rowcnt=1;
        if (width%mSmallWidth==0){
            columncnt=width/mSmallWidth;
        }else {
            columncnt=(width+mSmallWidth)/mSmallWidth;
        }

        if (height%mSmallHeight==0){
            rowcnt=height/mSmallHeight;
        }else {
            rowcnt=(height+mSmallHeight)/mSmallHeight;
        }

        int startColumn=x/mSmallWidth;
        int startRow=y/mSmallHeight;

        boolean result =resetDraggingView(startRow,startColumn,startRow+rowcnt,startColumn+columncnt);

        if (result==false){
            MyLog.i("errr","reset false");
        }
        return result;
    }

    private void moveDraggedView(MotionEvent event) {


        if (mDragingView!=null) {
            View childAt = mDragingView;

            int width = childAt.getWidth();
            int height = childAt.getHeight();

            float l= mDragingView.getX();
            if (lastTouchX!=0){
                l=l+(event.getRawX()-lastTouchX);
            }

            float t=mDragingView.getY();
            if (lastTouchY!=0){
                t=t+(event.getRawY()-lastTouchY);
            }


            MyLog.i("dragginpos", "x:" +l + " y:" + t + " width:" + width + " height:" + height);

            childAt.layout((int)l, (int)t, (int)l + width, (int)t + height);
            lastTouchX=event.getRawX();
            lastTouchY=event.getRawY();
        }
    }

    private Animation createRotateAnimation(View view){
        Animation rotate = new RotateAnimation(-2.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setDuration(500);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
        return rotate;
    }

    private Animation createScaleAnimation(View view){
        Animation scale=new ScaleAnimation(1.0f,1.2f,1.0f,1.20f,Animation.RELATIVE_TO_SELF,0.5F,Animation.RELATIVE_TO_SELF,0.5F);
        scale.setRepeatMode(Animation.REVERSE);
        scale.setDuration(300);
        scale.setInterpolator(new AccelerateDecelerateInterpolator());
        return scale;
    }

    private Animation createDragAnimation(float xrate,float yrate){
        Animation scale=new ScaleAnimation(xrate,yrate,xrate,yrate,Animation.RELATIVE_TO_SELF,0.5F, Animation.RELATIVE_TO_SELF,0.5F);
        scale.setRepeatMode(Animation.REVERSE);
        scale.setDuration(300);
        scale.setInterpolator(new AccelerateDecelerateInterpolator());
        scale.setFillAfter(true);
        return scale;
    }

    public void addView(View view,int row1,int col1,int row2,int col2){
        BigCell bigCell=null;
        int childcnt=getChildCount();
        try {
            bigCell=createBigCell(row1,col1,row2, col2);
            bigCell.setView(view);
            super.addView(view);
            childcnt=getChildCount();
        }catch (Exception err){
            MyLog.i("err","addView: "+err.getMessage());
        }

    }

    private boolean resetDraggingView(int row1,int col1,int row2,int col2 ){
        BigCell bigCell=null;
        try {
            bigCell=createBigCell(row1,col1,row2,col2);
            if (bigCell!=null){
                bigCell.setView(mDragingView);
            }else {
                return false;
            }
        }catch (Exception err){
            MyLog.i("err","resetDragingView: "+err.getMessage()+"row1="+row1+" col1="+col1+" row2="+row2+" col2="+col2);
            return false;
        }
        return true;
    }

    public void removeView(View view){
        for (BigCell bigCell:bigCellList){
            if (bigCell.mView==view){
                bigCell.free();
                super.removeView(view);
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        MyLog.i("cclayout", "draw");
    }


    private void drawSmallCells(Canvas canvas){
        Paint paint=new Paint();
        paint.setTextSize(15);

        int x1,y1,x2,y2;

        for (SmallCell[] smallCells:mSmallCells){
            for (SmallCell cell:smallCells){
                paint.setColor(getRandomColor());
                canvas.drawRect(cell.mRowPixel1,cell.mColPixel1 ,cell.mRowPixel2,cell.mColPixel2,paint);
            }
        }
    }

    private void drawBigCells(Canvas canvas){

        Paint paint=new Paint();
        paint.setTextSize(30);

        int x1,y1,x2,y2;
        for(BigCell cell:bigCellList){
            x1=cell.mColIdx1*mSmallWidth;
            y1=cell.mRowIdx1*mSmallHeight;

            x2=cell.mColIdx2*mSmallWidth;
            y2=cell.mRowIdx2*mSmallHeight;

            Rect rect=new Rect(x1,y1,x2,y2);

            paint.setColor(0x00000000);
            canvas.drawRect(rect, paint);

            x1=cell.mColIdx1*mSmallWidth+20;
            y1=cell.mRowIdx1*mSmallHeight+20;
            paint.setColor(getRandomColor());
            canvas.drawText("big cell", x1, y1, paint);
        }
    }

    private int getRandomColor(){
       Random random=new Random();
       return random.nextInt()|0xff000000;
    }

}
