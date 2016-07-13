package yyl.horizontalprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 带有百分数的水平进度条
 * Created by yinyiliang on 2016/7/13 0013.
 */
public class CustomHorizontalProgressBar extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xffFC00D1;
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp

    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);

    private Paint mPaint = new Paint();
    private int mRealWidth;

    public CustomHorizontalProgressBar(Context context) {
        this(context,null);
    }

    public CustomHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.CustomHorizontalProgressBar);

        mTextSize = (int) ta.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_text_size, mTextSize);
        mTextColor = ta.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_text_color,mTextColor);
        mTextOffset = (int) ta.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_text_offset,mTextOffset);

        mUnReachColor = ta.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_unreach_color,mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_unreach_height,mUnReachHeight);

        mReachColor = ta.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_reach_color,mReachColor);
        mReachHeight = (int) ta.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_reach_height,mReachHeight);

        ta.recycle();

        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal,height);

        mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            //给定的是精确值
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mReachHeight,mUnReachHeight),
                            Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {
                //不能超过给定的值
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        boolean noNeedUnReach = false;
        //绘制reach Bar
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        float radio = getProgress()*1.0f / getMax();
        float progressX = radio*mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }

        float endX = progressX - mTextOffset/2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        //绘制text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);

        //绘制unreach bar
        if (!noNeedUnReach) {
            float start = progressX + mTextOffset/2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                getResources().getDisplayMetrics());
    }

    private int sp2px(int value) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value,
                getResources().getDisplayMetrics());
    }
}
