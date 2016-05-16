package mike.mylauncher.CCTableLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mike.mylauncher.utils.MyLog;

/**
 * Created by Administrator on 15-11-18.
 */
/*
*设计思路：
* 把每个ViewGroup先弄成 配置大小的smallcell,然后把 n行个 m列个 smallcell拼成bigcell
* bigcell就是 定位 view的位置
*
* 当从bigcell中删除 view的时候，这个bigcell的信息也要随着删除
* */
public class CCTableLayout extends ViewGroup  {

    //每个smallcell的宽度和高度
    public int mSmallCellPixelWidth;
    public int mSmallCellPixelHeight;

    //每行 每列拥有的 smallcell 数量
    public int mRows;
    public int mColumns;

    public boolean mRelayout=true;

    public SmallCell[][] mSmallCellArray;

    public static class SmallCellInfoWithIndex{
        public SmallCell mSmallCell;
        public int rowIndex;
        public int colIndex;
        public SmallCellInfoWithIndex(SmallCell mSmallCell,int rowIndex,int colIndex){
            this.mSmallCell=mSmallCell;
            this.rowIndex=rowIndex;
            this.colIndex=colIndex;
        }
    }

    public static enum  Orientation{
       UP,DOWN,LEFT,RIGHT;
    }

    public static enum CCLayoutParamsMode{
        CELL,NORMAL;

    }

    //CCTableLayout中的子控件用
    public   class CCLayoutParams extends ViewGroup.MarginLayoutParams{

        public int mRowStart;
        public int mColStart;
        public int mRowStop;
        public int mColStop;

        public int mOldRowStart;
        public int mOldColStart;
        public int mOldRowStop;
        public int mOldColStop;


        CCLayoutParamsMode mMode;

        public CCLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            init();
        }

        public CCLayoutParams(int width, int height) {
            super(width, height);
            init();
        }
        private void init(){
            this.mMode=CCLayoutParamsMode.CELL;
        }

        public void setMode(CCLayoutParamsMode mode){
            this.mMode=mode;
        }

        public CCLayoutParams(LayoutParams source) {
            super(source);
        }

        private void init(AttributeSet attrs){
            final String namespace="http://schemas.android.com/apk/res-auto";
            this.mRowStart =attrs.getAttributeIntValue(namespace, "rowstart", 0);
            this.mColStart=attrs.getAttributeIntValue(namespace, "colstop", 0);
            this.mRowStop=attrs.getAttributeIntValue(namespace,"rowstop",0);
            this.mColStart=attrs.getAttributeIntValue(namespace,"colstop",0);
        }


        public  void setCells(int row1,int col1,int row2,int col2){
            this.mOldRowStart=this.mRowStart;
            this.mOldColStart=this.mColStart;
            this.mOldRowStop=this.mRowStop;
            this.mOldColStop=this.mColStop;

            this.mRowStart=row1;
            this.mColStart=col1;
            this.mRowStop=row2;
            this.mColStop=col2;
        }

        public void add(int row1add,int col1add,int row2add,int col2add){
            this.mRowStart=mRowStart+row1add;
            this.mColStart=mColStart+col1add;
            this.mRowStop=mRowStop+row2add;
            this.mColStop=mColStop+col2add;
        }


        public boolean isIn(int row,int col){
            if (row>=mRowStart && row<=mRowStop && col >=mColStart&& col<=mColStop){
                return true;
            }
            return false;

            //dispatchTouchEvent()
        }

    }


    public class SmallCell {
        //像素区间 [(left,top),(right,bottom) )

        public int left;
        public int top;
        public int right;
        public int bottom;
        public View belong;


        public SmallCell(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public boolean isInUse(){
            return belong!=null;
        }

        public SmallCell copy(){
            SmallCell result=new SmallCell(this.left,this.top,this.right,this.bottom);
            result.belong=this.belong;
            return  result;
        }

        private void fillColor(Canvas canvas){
            Random random=new Random();
            Paint paint=new Paint();
            paint.setARGB(0xff, random.nextInt(0xff), random.nextInt(0xff), random.nextInt(0xff));
            canvas.drawRect((float) left, (float) top, (float) right - 1, (float) bottom - 1, paint);
            //drawSmallCellStatus(canvas);
        }

        private void fillPosition(Canvas canvas){
            Paint paint=new Paint();
            paint.setTextSize(20);
            String info="l:"+this.left+" t:"+this.top;
            int top2=this.top;
            canvas.drawText(info, (float) left, (float) top2, paint);

            top2=this.top+20;
            info="r:"+this.right+" b:"+this.bottom;
            canvas.drawText(info, (float) left, (float) top2, paint);

            top2+=20;
            info="used:"+String.valueOf(this.belong != null ? true : false);
            canvas.drawText(info, (float) left, (float) top2, paint);
        }

        public void debugInfo(Canvas canvas){
            fillColor(canvas);
            fillPosition(canvas);
        }

        // 判断哪个point到 original 比较近，如果一样的话返回 point1
        public SmallCell whichIsNear(SmallCell cellInfo1,SmallCell cellInfo2){

            double lineToPoint1=Math.sqrt( Math.pow( (cellInfo1.left - this.left), 2)+ Math.pow(cellInfo1.right-this.right,2) );
            double lineToPoint2=Math.sqrt( Math.pow( (cellInfo2.left - this.left), 2)+ Math.pow(cellInfo2.right-this.right,2) );
            return lineToPoint1 <= lineToPoint2 ? cellInfo1:cellInfo2;
        }

        public void setFrame(int left,int top,int right,int bottom){
            if (this.left!=left || this.top!=top || this.right!=right || this.bottom!=bottom){
                this.left=left;
                this.bottom=bottom;
                this.right=right;
                this.top=top;
            }
        }

        public boolean isIn(int x,int y){
            boolean result=false;

            if (x>=this.left && y>= this.top
                    && x< this.right && y<this.bottom){
                result=true;
            }
            return result;
        }

        public Rect getBound(){
            return new Rect(left,top,right,bottom);
        }

        public void detach(){
            this.belong=null;
        }

    }

    public class BigCell {
        // bigcell 中的 smallcell 区间信息 是两头闭区间 []
        public int mRowStart;
        public int mColStart;

        public int mRowStop;
        public int mColStop;

        private BigCell(){

        }

        public BigCell(int rowstart, int colstart, int rowstop, int colstop){
            this.mRowStart =rowstart;
            this.mColStart =colstart;

            this.mRowStop =rowstop;
            this.mColStop =colstop;
        }

        //bigcell中的smallcell是否有被暂用的情况
        public boolean isEmpty(){
            for (int row= mRowStart;row<= mRowStop;row++){
                for (int col= mColStart;col<= mColStop;col++){
                    if (mSmallCellArray[row][col].belong!=null){
                        return false;
                    }
                }
            }
            return true;
        }

        public Rect getBound(){
            Rect result;
            SmallCell cellinfo1=mSmallCellArray[this.mRowStart][this.mColStart];
            SmallCell cellinfo2=mSmallCellArray[this.mRowStop][this.mColStop];
            result=new Rect(cellinfo1.left,cellinfo1.top,cellinfo2.right,cellinfo2.bottom);
            return result;
        }

        public boolean in(int smallrow,int smallcol){
            if (this.mRowStart <= smallrow&& this.mColStart <=smallcol&&
                    this.mRowStop >= smallrow && this.mColStop >=smallcol){
                return true;
            }
            return false;
        }
    }

    public CCTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        //Paint.FontMetrics
    }

    public CCTableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        final String namespace="http://schemas.android.com/apk/res-auto";

        this.mRows =attrs.getAttributeIntValue(namespace, "rows", 1);
        this.mColumns = attrs.getAttributeIntValue(namespace, "columns", 1);

        if (mSmallCellArray==null){
            mSmallCellArray=new SmallCell[mRows][mColumns];

            for (int row=0;row<mRows;row++){
                for (int col=0;col<mColumns;col++){
                    mSmallCellArray[row][col]=new SmallCell(0,0,0,0);
                }
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        MyLog.e("CCTablelayout", "onMeasure width:" + widthSize + " heightSize:" + heightSize + " measuredWidth:" + getMeasuredWidth() + " measuredHeight:" + getMeasuredHeight());

        if (getMeasuredHeight()!=heightSize || getMeasuredWidth() != widthSize){
            mRelayout=true;

            int paddingLeft=this.getPaddingLeft();
            int paddintTop=this.getPaddingTop();
            int paddingRight=this.getPaddingRight();
            int paddingBottom=this.getPaddingBottom();

            int tempWidth=widthSize-paddingLeft-paddingRight;
            int tempHeight=heightSize-paddintTop-paddingBottom;

            this.mSmallCellPixelWidth=tempWidth/mColumns;
            this.mSmallCellPixelHeight=tempHeight/mRows;

            int childcnt=getChildCount();
            for (int i=0;i<childcnt;i++){
                View child=getChildAt(i);
                CCLayoutParams layoutParams=(CCLayoutParams)child.getLayoutParams();
                final int row1= layoutParams.mRowStart;
                final int col1=layoutParams.mColStart;
                final int row2=layoutParams.mRowStop;
                final int col2=layoutParams.mColStop;

                int childWidthMode = MeasureSpec.EXACTLY;
                int childWidthSize = this.mSmallCellPixelWidth*(col2-col1+1);

                int childHeightMode = MeasureSpec.EXACTLY;
                int childHeightSize = this.mSmallCellPixelHeight*(row2-row1+1);

                child.measure(
                        MeasureSpec.makeMeasureSpec(childWidthSize-5, childWidthMode),
                        MeasureSpec.makeMeasureSpec(childHeightSize-5, childHeightMode)
                );
            }
            MyLog.e("CCTablelayout", "mSmallCellPixelWidth:" + mSmallCellPixelWidth + " mSmallCellPixelHeight:" + mSmallCellPixelHeight);
                    setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(heightSize-1 , MeasureSpec.EXACTLY));
        }
        MyLog.i("event", "CCTableLayout::onMeasure");
    }


    //系统已经考虑了 padding了，l,t,r,b 是踢出 padding之后的位置,就是说实在父坐标系中参数
    //布局的时候，坐标系是在父坐标系中，相当于在父坐标系中，布局当前的这个viewgroup本身的位置
    //在onLayout的时候，布局信息已经确定
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed,l,t,r,b);
        if (mRelayout){
            MyLog.e("CCTablelayout", "onLayout l:"+l+" t:"+t+" r:"+r+" b:"+b);
            //需要重新布局，重新确定每个 bigcell的位置，每个child的位置和大小
            mRelayout=false;
            //1重新划分 smallcell空间,传参数的时候相当于重新调整坐标系了
            buildSmallCellArray(getPaddingLeft(), getPaddingTop(), r - l - getPaddingLeft(), b - t - getTop());

            CCLayoutParams params;
            int childcnt=getChildCount();
            for (int i=0;i<childcnt;i++){
                //到bigcell中去比较，如果不在bigcellarray中，那么需要重新布局，否则是不需要的
                View child=getChildAt(i);
                params=(CCLayoutParams)child.getLayoutParams();
                if (params.mMode==CCLayoutParamsMode.CELL) {
                    if (params != null) {
                        placeViewToCells(child, params.mRowStart, params.mColStart, params.mRowStop, params.mColStop);
                    } else {
                        placeToNextFreeCell(child, 1, 1);
                    }
                }
                child.layout(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            }
        }

        MyLog.i("event", "onLayout");
    }

    @Override
    public CCLayoutParams generateLayoutParams(AttributeSet attrs) {
        return  new CCTableLayout.CCLayoutParams(this.getContext(),attrs);
    }

    @Override
    protected CCLayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new CCLayoutParams(p);
    }

    @Override
    protected CCLayoutParams generateDefaultLayoutParams() {
        BigCell areaInfo=getNextEmptyArea(1,1);
        if (areaInfo!=null){
            CCLayoutParams params=new CCLayoutParams(0,0);
            params.setCells(areaInfo.mRowStart,areaInfo.mColStart,areaInfo.mRowStop,areaInfo.mColStop);
            return params;
        }else{
            throw new RuntimeException("No table space for new view");
        }
    }

    //调整或者建立smallcell对象 的位置
    private void buildSmallCellArray(int l, int t, int r, int b){
            int left=0,top=0,right=0,bottom=0;

            for (int row=0;row<mRows;row++){
                left=l;
                top=t+mSmallCellPixelHeight*row;
                bottom=t+mSmallCellPixelHeight*row+mSmallCellPixelHeight;
                right=l+mSmallCellPixelWidth;
                for (int col=0;col<mColumns;col++){
                    if (mSmallCellArray[row][col]==null){
                        //首次创建
                        mSmallCellArray[row][col]=new SmallCell(left,top,right,bottom);
                    }else{
                        //重新布局后的尺寸
                        mSmallCellArray[row][col].setFrame(left,top,right,bottom);
                    }
                    //MyLog.i("CCTablelayout","smallcell left:"+left+" top:"+top+" right:"+right+" bottom:"+bottom);
                    left=left+mSmallCellPixelWidth;
                    right=right+mSmallCellPixelWidth;
                }
            }
    }

//    @Override
    protected void onDraw(Canvas canvas) {
        for (SmallCell[] smallCellInfoArray:mSmallCellArray){
            for (SmallCell smallCellInfo: smallCellInfoArray) {
                if (smallCellInfo!=null){
                    smallCellInfo.debugInfo(canvas);
                }
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public void addView(View child) {
        //在视图没有显示之前不能添加，因为这个时候 还没有经过measure ，不能确定表格大小
        CCLayoutParams params=(CCLayoutParams)child.getLayoutParams();
        if (params.mMode==CCLayoutParamsMode.CELL) {
            //格子模式
            if (params == null) {
                if (placeToNextFreeCell(child, 1, 1) == false) {
                    throw new RuntimeException("no cell for the view");
                }
            } else {
                if (placeViewToCells(child, params.mRowStart, params.mColStart, params.mRowStop, params.mColStop) == false) {
                    if (placeToNextFreeCell(child, params.mRowStop - params.mRowStart + 1,
                            params.mColStop - params.mColStart + 1) == false) {
                        throw new RuntimeException("no cell for the view");
                    }
                }
            }
        }
        super.addView(child);
    }

    @Override
    public void removeView(View view) {
        CCLayoutParams params=(CCLayoutParams)view.getLayoutParams();
        for (int row=params.mRowStart;row<=params.mRowStop;row++){
            for (int col=params.mColStart;col<=params.mColStop;col++){
                mSmallCellArray[row][col].detach();
            }
        }
        super.removeView(view);
    }

    private void freeCells(View view){
        CCLayoutParams layoutParams=(CCLayoutParams)view.getLayoutParams();
        for (int row=layoutParams.mRowStart;row<=layoutParams.mRowStop;row++){
            for (int col=layoutParams.mColStart;col<=layoutParams.mColStop;col++){
                mSmallCellArray[row][col].belong=null;
            }
        }
    }

    private boolean placeToNextFreeCell(View view,int rowscnt,int colcnt){
        CCLayoutParams params=(CCLayoutParams)view.getLayoutParams();
        BigCell bigCell =getNextEmptyArea(rowscnt,colcnt);
        if(bigCell !=null){
            //params=new CCLayoutParams(0,0);
            //view.setLayoutParams(params);
            placeViewToCells(view, bigCell.mRowStart, bigCell.mColStart, bigCell.mRowStop, bigCell.mColStop);
            return true;
        }
        return false;
    }

    private void markBelong(View view,int row1, int col1, int row2, int col2){
        for (int row=row1;row<=row2;row++){
            for (int col=col1;col<=col2;col++){
                mSmallCellArray[row][col].belong=view;
            }
        }
    }

    /*
    *将view定位到指定位置,param必须已经分配
    * */
    private boolean placeViewToCells(View view, int row1, int col1, int row2, int col2){

        CCLayoutParams layoutParams=(CCLayoutParams)view.getLayoutParams();

        if (isBigAreaCanBePlaced(view,row1,col1,row2,col2)){
            if (layoutParams!=null) {
                freeCells(view);
            }else{
                layoutParams=generateDefaultLayoutParams();
                view.setLayoutParams(layoutParams);
            }

            //重新设置格子
            layoutParams.setCells(row1,col1,row2,col2);

            int rowcnt=layoutParams.mRowStop-layoutParams.mRowStart+1;
            int colcnt=layoutParams.mColStop-layoutParams.mColStart+1;

            int left=this.getPaddingLeft()+layoutParams.mColStart*mSmallCellPixelWidth+ layoutParams.leftMargin;
            int top=this.getPaddingTop()+layoutParams.mRowStart*mSmallCellPixelHeight+ layoutParams.topMargin;
            int right=this.getPaddingLeft()+mSmallCellPixelWidth*(layoutParams.mColStop+1)-layoutParams.rightMargin;
            int bottom=this.getPaddingTop()+mSmallCellPixelHeight*(layoutParams.mRowStop + 1) - layoutParams.bottomMargin;

            markBelong(view, layoutParams.mRowStart, layoutParams.mColStart,layoutParams.mRowStop,layoutParams.mColStop);

            layoutParams.setMode(CCLayoutParamsMode.CELL);
            view.setLeft(left);
            view.setRight(right);
            view.setTop(top);
            view.setBottom(bottom);
            view.setX(left);
            view.setY(top);

            return true;
        }

        return false;
    }


    private BigCell getNextEmptyArea(int rowcnt,int colcnt){
        for (int row=0;row+rowcnt-1<mRows;row++){
            for (int col=0;col+colcnt-1<mColumns;col++){
                if (isBigAreaCanBePlaced(null,row,col,row+rowcnt-1,col+colcnt-1)==true){
                    BigCell bigCell =new BigCell();
                    bigCell.mRowStart =row;
                    bigCell.mColStart =col;
                    bigCell.mRowStop =row+rowcnt-1;
                    bigCell.mColStop =col+colcnt-1;
                    return bigCell;
                }
            }
        }
        return null;
    }

/*
*
* -----------
* |  view   |
* |     ----|-------
* ------|----  rect|
*      |__________ |
*
*
*
*
*
 */

    public List<View> kickViews(Rect rect,int eventX,int eventY){
        List<View> result=new ArrayList<View>();
        SmallCellInfoWithIndex smallCellInfoWithIndex= takeSmallCellIgnoreParent(eventX,eventY);
        View targetWillBeKickedView;
        if (smallCellInfoWithIndex==null || smallCellInfoWithIndex.mSmallCell.belong==null){
            return result;
        }

        targetWillBeKickedView=smallCellInfoWithIndex.mSmallCell.belong;

        //确定被拖动的view占用的cell的 row,col数量
        BigCell bigCell =getBigAreaIgnoreBelong(rect);


        if (bigCell!=null){
            CCLayoutParams params=(CCLayoutParams)targetWillBeKickedView.getLayoutParams();
            Rect rect1=getRect(bigCell.mRowStart,bigCell.mColStart,bigCell.mRowStop,bigCell.mColStop);
            Orientation orientation;
            int toLeft=Math.abs(eventX-rect1.left);
            int toRight=Math.abs(eventX-rect1.right);
            int toTop=Math.abs(eventY-rect1.top);
            int toBottom=Math.abs(eventY-rect1.bottom);

            int min=Math.min( toBottom,
                            Math.min(toTop,
                                        Math.min(toLeft,toRight)));
            if (min==toLeft){
                orientation=Orientation.RIGHT;
            }else if (min==toRight){
                orientation=Orientation.LEFT;
            }else if (min==toTop){
                orientation=Orientation.DOWN;
            }else{
                orientation=Orientation.UP;
            }

            
            moveView(targetWillBeKickedView,orientation);

//            Rect targetRect=getRect(params.mRowStart, params.mColStart, params.mRowStop, params.mColStop);
//            //Rect intersectRect=getIntesectRect(rect, targetRect); //targetRect.intersect(rect);
//            Rect intersectRect=new Rect(rect);
//            if(intersectRect.intersect(targetRect)){
//                //有交集才需要排挤targetWillBeKickedView
//                //rect的中心点
//                Point rectCenter=new Point((rect.right+rect.left)/2,(rect.top+rect.bottom)/2);
//
//                Orientation orientation;
//                if (intersectRect.right-intersectRect.left >= intersectRect.bottom-intersectRect.top){
//                    //横向移动
//                    if ( Math.abs( rectCenter.x-targetRect.left) > Math.abs( rectCenter.x-intersectRect.right) ){
//                        orientation=Orientation.LEFT;
//                    }else{
//                        orientation=Orientation.RIGHT;
//                    }
//                }else{
//                    //纵向移动
//                    if ( Math.abs( rectCenter.y-targetRect.top) > Math.abs( rectCenter.y-intersectRect.bottom) ){
//                        orientation=Orientation.UP;
//                    }else{
//                        orientation=Orientation.DOWN;
//                    }
//                }

                //// TODO: 15-11-25 开始移动
                //开始移动
                //orientation=Orientation.UP;

            }

        return result;
    }

    private List<View> moveView(View target,Orientation orientation){
        CCLayoutParams params=(CCLayoutParams)target.getLayoutParams();
        List<View> result=new ArrayList<>();
        switch (orientation){
            case LEFT:
                  moveHorizontal(target,-1,result);
                break;
            case RIGHT:
                  moveHorizontal(target,1,result);
                break;
            case UP:
                moveVertical(target,-1,result);
                break;
            case DOWN:
                moveVertical(target,1,result);
                break;
            default:
                throw new RuntimeException("wrong orientation of the move action");
        }
        return result;

    }


    private boolean moveVertical(View target,int rowStep,List<View> result){
            CCLayoutParams params=(CCLayoutParams)target.getLayoutParams();
            if(verticalMovable(target, rowStep)) {
                int targetRow = -1;
                if (rowStep < 0) {
                    // 向上
                    targetRow = params.mRowStart - 1;
                } else if (rowStep > 0) {
                    //向下
                    targetRow = params.mRowStop + 1;
                }

                List<View> beInfluenced=new ArrayList<>();
                if (targetRow>=0 && targetRow<mRows){
                for (int col=params.mColStart;col<=params.mColStop;col++){
                        View view=mSmallCellArray[targetRow][col].belong;
                        if (view!=null && beInfluenced.contains(view)==false){
                            beInfluenced.add(view);
                        }
                    }
                }

                for (View viewBeInfluenced:beInfluenced){
                    moveVertical(viewBeInfluenced, rowStep, result);
                }
                placeViewToCells(target,params.mRowStart+rowStep,params.mColStart,params.mRowStop+rowStep,params.mColStop);
            }

        return false;
    }

    private boolean verticalMovable(View target,int rowStep){
        CCLayoutParams params=(CCLayoutParams)target.getLayoutParams();
        int targetRow=-1;
        if (rowStep<0){
            // 向上
            targetRow=params.mRowStart-1;
        }else if (rowStep>0){
            //向下
            targetRow=params.mRowStop+1;
        }

        //所有需要跟着移动的views,如果需要跟着移动的 views的数量==0，说明可以移动
        List<View> beInfluenced=new ArrayList<>();
        if (targetRow>=0 && targetRow<mRows){
            for (int col=params.mColStart;col<=params.mColStop;col++){
                View view=mSmallCellArray[targetRow][col].belong;
                if (view!=null && beInfluenced.contains(view)==false){
                    beInfluenced.add(view);
                }
            }
        }

        for (View viewBeInfluenced:beInfluenced){
            if (verticalMovable(viewBeInfluenced, rowStep)==false){
                return false;
            }
        }
        //beInfluenced.size()==0 或则，每一个都是可以移动的
        return true;

    }

    private boolean moveHorizontal(View target,int colStep,List<View> result){
        CCLayoutParams params=(CCLayoutParams)target.getLayoutParams();
        if(horizontalMovable(target,colStep)){
            int targetCol=-1;
            if (colStep<0){
                // 向左
                targetCol=params.mColStart-1;
            }else if (colStep>0){
                //向右
                targetCol=params.mColStop+1;
            }

            List<View> beInfluenced=new ArrayList<>();
            for (int row=params.mRowStart;row<=params.mRowStop;row++){
                if (targetCol>=0 && targetCol<mColumns){
                    View view=mSmallCellArray[row][targetCol].belong;
                    if (view!=null && beInfluenced.contains(view)==false){
                        beInfluenced.add(view);
                    }
                }
            }

            for (View viewBeInfluenced:beInfluenced){
                moveHorizontal(viewBeInfluenced, colStep, result);
            }
             placeViewToCells(target,params.mRowStart,params.mColStart+colStep,params.mRowStop,params.mColStop+colStep);
            return true;
        }
        return false;
    }
    // step<0 向左，step>0向右
    private boolean horizontalMovable(View target,int colStep){
        CCLayoutParams params=(CCLayoutParams)target.getLayoutParams();

        int targetCol=-1;
        if (colStep<0){
            // 向左
            targetCol=params.mColStart-1;
        }else if (colStep>0){
            //向右
            targetCol=params.mColStop+1;
        }

        //所有需要跟着移动的views,如果需要跟着移动的 views的数量==0，说明可以移动
        List<View> beInfluenced=new ArrayList<>();
        if (targetCol>=0 && targetCol<mColumns){
            for (int row=params.mRowStart;row<=params.mRowStop;row++){
                View view=mSmallCellArray[row][targetCol].belong;
                if (view!=null && beInfluenced.contains(view)==false){
                    beInfluenced.add(view);
                }
            }
        }

        for (View viewBeInfluenced:beInfluenced){
            if (horizontalMovable(viewBeInfluenced,colStep)==false){
                return false;
            }
        }
        //beInfluenced.size()==0 或则，每一个都是可以移动的
        return true;
    }



    /*
    *
    * */
    private Rect getRect(int row1,int col1,int row2,int col2){
        int left=getPaddingLeft()+ mSmallCellPixelWidth* col1;
        int top=getPaddingTop()+mSmallCellPixelHeight*row1;
        int right=getPaddingLeft()+ mSmallCellPixelWidth*(col2+1); //getLeft()+(row2+1)*mSmallCellPixelWidth;
        int bottom=getPaddingTop()+mSmallCellPixelHeight*(row2+1);
        return new Rect(left,top,right,bottom );
    }

    private BigCell getBigAreaIgnoreBelong(Rect rect){
        BigCell result=null;
        SmallCellInfoWithIndex smallCellInfoWithIndex=takeSmallCellIgnoreParent(rect.left,rect.top);
        if (smallCellInfoWithIndex!=null){
            int rowcnt=(rect.right-rect.left+mSmallCellPixelWidth)/mSmallCellPixelWidth;
            int colcnt=(rect.bottom-rect.top+mSmallCellPixelHeight)/mSmallCellPixelHeight;

            int rowstart=smallCellInfoWithIndex.rowIndex;
            int colstart=smallCellInfoWithIndex.colIndex;

            int rowstop=rowstart+rowcnt-1;
            int colstop=colstart+colcnt-1;

            if (rowstart>=0&& colstart>=0 && rowstop<mRows&& colstop<mColumns){
                result=new BigCell(rowstart,colstart,rowstop,colstop);
            }
        }

        return result;
    }

    private SmallCellInfoWithIndex takeIntactSmallCell(int x, int y){
        //这个算法需要改进
        if (mSmallCellArray!=null){
            int rows=mSmallCellArray.length;
            for (int row=0;row<rows;row++){
                if (mSmallCellArray[row]!=null) {
                        int cols=mSmallCellArray[row].length;
                        for (int col=0;col<cols;col++){
                            SmallCell smallCellInfo=mSmallCellArray[row][col];
                            if (smallCellInfo!=null && smallCellInfo.isIn(x,y)){
                                if (smallCellInfo.belong==null){
                                    SmallCellInfoWithIndex result=new SmallCellInfoWithIndex(smallCellInfo,row,col);
                                    return result;
                                }
                                return  null;
                            }
                        }
                    }
                }
            }
        return null;
    }

    private SmallCellInfoWithIndex takeSmallCellIgnoreParent(int x,int y){
        if (mSmallCellArray!=null){
            int rows=mSmallCellArray.length;
            for (int row=0;row<rows;row++){
                if (mSmallCellArray[row]!=null) {
                    int cols=mSmallCellArray[row].length;
                    for (int col=0;col<cols;col++){
                        SmallCell smallCellInfo=mSmallCellArray[row][col];
                        if (smallCellInfo!=null && smallCellInfo.isIn(x,y)){
                                SmallCellInfoWithIndex result=new SmallCellInfoWithIndex(smallCellInfo,row,col);
                            return  result;
                        }
                    }
                }
            }
        }
        return null;
    }

    //判断一块区域是否可以使用，或者是否可以被view使用
    public boolean isBigAreaCanBePlaced(View view,int row1,int col1,int row2,int col2){

        if (row1<=row2&& row1>=0&&row1<mRows && row2>=0&&row2<mRows && col1>=0 && col1<mColumns && col2 >=0&& col2<mColumns){
            for (int row=row1;row<=row2;row++){
                for (int col=col1;col<=col2;col++){
                    if (mSmallCellArray[row][col].belong!=null && mSmallCellArray[row][col].belong!=view){
                        return  false;
                    }
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

}
