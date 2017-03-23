package com.androiddemo.wuziqi.simplegobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GoBangPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;//行高，用float防止某些数据时最后一行太明显的误差
    private int MAX_LINE = 10;//设置最大行数为10
    private Paint mPaint = new Paint();//The Paint class holds the style and color information about how to 画几何图形，文本和bitmaps

    private Bitmap mWhitePiece;//白棋子
    private  Bitmap mBlackPiece;//黑棋子
    private float radioPieceOfLineHeight = 3 * 1.0f /4;//用来规范棋子的大小，为行高的3/4

    //白棋先手，或当前轮到白棋
    private boolean mIsWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();//用户点击的白棋的坐标集合
    private ArrayList<Point> mBlackArray = new ArrayList<>();//用户点击的黑棋的坐标集合

    private boolean mIsGameOver;//标志游戏结束
    private  boolean mIsWhiteWinner;//标志白子为赢家，还是黑子为赢家
    private static int MAX_COUNT_IN_LINE = 5;//因为是五子棋，检查相邻棋子的最大个数

    private  static final String INSTANCE = "instance";
    private  static final String INSTANCE_GAME_OVER = "instance_game_over";
    private  static final String INSTANCE_WHITE_ARRAY = "instance_while_array";
    private  static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    private OnGameOverListener mOnGameOverListener;


    public GoBangPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(0x44ff0000);
        init();
    }

    /**
     * 初始化画笔Paint的属性
     */
    private void init() {
        mPaint.setColor(0x88000000);//灰色半透明
        mPaint.setAntiAlias(true);//设置没有抗锯齿的效果
        mPaint.setDither(true);//设置防抖动效果
        mPaint.setStyle(Paint.Style.STROKE);
        //初始化黑白棋子
        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_black);
    }

    /**
     * 测量 决定View的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize,heightSize);//取宽高最小值
        //防止宽和高出现0等特殊情况
        if(widthMode ==MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if (heightMode ==MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }
        setMeasuredDimension(width,width);//设置自定义视图的大小
    }

    /**
     * View大小改变时调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;//计算棋盘行高
        //棋子按照一定比例后新的宽度
        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);
        //从当前存在的位图mWhitePiece，按一定的比例创建一个新的位图,新位图期望的宽度,新位图期望的高度
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);//按照比例后的白棋
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    /**
     * 绘制所有
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制棋盘
        drawBoard(canvas);
        //绘制棋子
        drawPieces(canvas);
        //判断是否游戏结束
        checkGameOver();
    }

    /**
     * 判断是否游戏结束
     */
    private void checkGameOver() {
        //判断是否有五子连成一条线
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if (whiteWin||blackWin){//若白棋或黑棋有一方赢了
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否有五子连成一条线
     */
    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points){
            int x = p.x;
            int y = p.y;
            boolean win = GoBangCheck.checkHorizontal(x,y,points);
            if (win) return true;
            win = GoBangCheck.checkVertical(x,y,points);
            if (win) return true;
            win = GoBangCheck.checkLeftDiagonal(x,y,points);
            if (win) return true;
            win = GoBangCheck.checkRightDiagonal(x,y,points);
            if (win) return true;
        }
        return false;
    }

    /**
     * 判断是否和局
     */
    private void checkHe()
    {
        if(mBlackArray.size() + mWhiteArray.size() >= MAX_LINE * MAX_LINE)
        {
            if(mOnGameOverListener != null)
            {
                mOnGameOverListener.onGameHe();
            }
        }
    }

    /**
     * 绘制棋子
     */
    private void drawPieces(Canvas canvas) {
        //绘制白棋子
        for (int i=0,n=mWhiteArray.size();i<n;i++){
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,(whitePoint.x + (1-radioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1-radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }
        //绘制黑棋子
        for (int i=0,n=mBlackArray.size();i<n;i++){
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,(blackPoint.x + (1-radioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1-radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }
    }


    /**
     * 绘制棋盘具体的横线和纵线
     */
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        /** 0.5个行高+中间9个行高+0.5个底端行高 **/
        for (int i=0; i<MAX_LINE; i++){
            int startX = (int) (lineHeight / 2);//棋盘x轴起点坐标距离左边边界0.5个行高远
            int endX = (int) (w-lineHeight / 2);//棋盘x轴终点坐标距离右边边界0.5个行高远
            int y = (int) ((0.5 + i) * lineHeight);//棋盘纵坐标，例如第一行距离上面1.5个行高
            /** 绘制横线 **/
            canvas.drawLine(startX,y,endX,y,mPaint);
            /** 绘制纵线 **/
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }

    /**
     * 监听手指上下左右滑动
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) return false;//若游戏结束，则不允许再下棋
        int action = event.getAction();
        //ACTION_DOWN: 表示用户开始触摸,不能用是为了防止向下挪动屏幕时落了棋
        //ACTION_UP: 当手指离开的时候
        if(action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();
            //Point p = new Point(x,y);//封装坐标
            Point p = getValidPoint(x,y);
            //设置这个坐标不能点击第二次
            if (mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }

            //分辨是白棋还是黑棋，决定放到哪个棋子的坐标集合
            if (mIsWhite){
                mWhiteArray.add(p);
            }else {
                mBlackArray.add(p);
            }
            invalidate();//刷新View,看到重新绘制的界面
            mIsWhite = !mIsWhite;//将白棋改为相反的值，即该轮到另一方下棋
        }
        return true;
    }

    /**
     * 点击一小区域时，存储相同的坐标
     * 例如，点击左上角一小片区域时，只显示坐标(0,0)
     */
    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight),(int)(y / mLineHeight));
    }

    /**
     * 存储数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    /**
     * 恢复数据
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 再来一局
     */
    public void gobangrestart(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWinner = false;
        invalidate();
    }

    /**
     * 获取MAX_COUNT_IN_LINE的值
     */
    public static int getMaxCountInLine(){
        return MAX_COUNT_IN_LINE;
    }
    public void setOnGameOverListener(OnGameOverListener mOnGameOverListener)
    {
        this.mOnGameOverListener = mOnGameOverListener;
    }
}
