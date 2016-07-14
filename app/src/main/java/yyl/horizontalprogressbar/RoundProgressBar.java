package yyl.horizontalprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 圆形进度条
 * Created by yinyiliang on 2016/7/13 0013.
 */
public class RoundProgressBar extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xffFC00D1;
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp
    private static final int DEFAULT_CIRCLE_RADIUS = 30;//dp

    private int mRadius = dp2px(DEFAULT_CIRCLE_RADIUS);
    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);

    private Paint mPaint = new Paint();
    //最大画笔宽度
    private int mMaxPaintWidth;

    public RoundProgressBar(Context context) {
        this(context,null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(
                attrs,R.styleable.RoundProgressBar);

        mRadius = (int) ta.getDimension(
                R.styleable.RoundProgressBar_radius,mRadius);

        mTextSize = (int) ta.getDimension(
                R.styleable.RoundProgressBar_progress_text_size, mTextSize);
        mTextColor = ta.getColor(
                R.styleable.RoundProgressBar_progress_text_color,mTextColor);

        mUnReachColor = ta.getColor(
                R.styleable.RoundProgressBar_progress_unreach_color,mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(
                R.styleable.RoundProgressBar_progress_unreach_height,mUnReachHeight);

        mReachColor = ta.getColor(
                R.styleable.RoundProgressBar_progress_reach_color,mReachColor);
        mReachHeight = (int) ta.getDimension(
                R.styleable.RoundProgressBar_progress_reach_height,mReachHeight);

        mReachHeight = (int) (mUnReachHeight * 2.5f);

        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //设置接口处为弧形
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxPaintWidth = Math.max(mReachHeight,mUnReachHeight);
        //默认四个padding一致
        int expect = mRadius*2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        int width = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect,heightMeasureSpec);

        int realWidth = Math.min(width,height);

        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth)/2;

        setMeasuredDimension(realWidth,realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent())/2;

        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth/2,getPaddingTop() + mMaxPaintWidth/2);
        mPaint.setStyle(Paint.Style.STROKE);

        //绘制unreach bar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        //绘制reach bar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        RectF reacF = new RectF(0,0,mRadius*2,mRadius*2);
        float sweepAngle = getProgress()*1.0f/getMax()*360;
        canvas.drawArc(reacF,0,sweepAngle,false,mPaint);
        //绘制text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text,mRadius-textWidth/2,mRadius - textHeight,mPaint);

        canvas.restore();
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                getResources().getDisplayMetrics());
    }
    
    private int sp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value,
                getResources().getDisplayMetrics());
    }
}
