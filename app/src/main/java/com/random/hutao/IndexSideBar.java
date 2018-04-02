package com.random.hutao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hutao on 2018/3/31.
 */

public class IndexSideBar extends View {

    private static final boolean DEBUG = false;

    private static String[] mLetterIndexArray = {
//            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };

    private List<String> mLetterIndexList;
    private int curLetterIndex = -1;    // current letter index
    private Paint mPaint;
    private Rect mTextBounds;

    private int mViewWidth;     // IndexSideBar width
    private int mViewHeight;    // IndexSideBar height


    public IndexSideBar(Context context) {
        this(context, null);
    }

    public IndexSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLetterIndexList = Arrays.asList(mLetterIndexArray);    // String[] -> List
        mTextBounds = new Rect();
        mPaint = new Paint();       // Paint.ANTI_ALIAS_FLAG
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.SANS_SERIF);
        mPaint.setTextSize(dp2px(getContext(), 12));  // 12sp // 12dp

        setBackgroundColor(Color.alpha(0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        if (DEBUG) {    // 72, 1581
            Toast.makeText(getContext(), "IndexSideBar: onSizeChanged()\n" + w + ", " + h, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (DEBUG) {    // 72, 1581
            Toast.makeText(getContext(), "IndexSideBar: onDraw()\n" + getWidth() + ", " + getHeight(), Toast.LENGTH_SHORT).show();
        }

        int size = mLetterIndexList.size();
        float cellHeight = mViewHeight * 1.0f / size;

        for (int index = 0; index < size; index++) {
            mPaint.setColor(Color.BLACK);       // black
            if (index == curLetterIndex) {
                //这里设置选中字母的颜色
                mPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));   // white
            }
            String letter = mLetterIndexList.get(index);

            float xPos = (mViewWidth - mPaint.measureText(letter)) / 2;

            mPaint.getTextBounds(letter, 0, letter.length(), mTextBounds);
            int textHeight = mTextBounds.height();
            // baseline - left bottom, no left top
            float yPos = cellHeight / 2 + textHeight / 2 + cellHeight * index;

            // xPos - The x-coordinate of the origin of the text being drawn
            // yPos - The y-coordinate of the baseline of the text being drawn
            canvas.drawText(letter, xPos, yPos, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int size = mLetterIndexList.size();
        int oldLetterIndex = curLetterIndex;
        int tmpLetterIndex = (int) (y / mViewHeight * size);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            setBackgroundColor(Color.alpha(0));         // 设置背景为透明
            curLetterIndex = -1;
            invalidate();
            if (listener != null) {
                listener.onTouchedLetterListener();
            }
        } else {
            setBackgroundResource(R.drawable.select_index_bar);    // 设置背景为指定样式
            if (tmpLetterIndex != oldLetterIndex) {
                if (tmpLetterIndex >= 0 && tmpLetterIndex < size) {
                    if (listener != null) {
                        listener.onTouchingLetterListener(mLetterIndexList.get(tmpLetterIndex));
                    }
                    curLetterIndex = tmpLetterIndex;
                    invalidate();
                }
            }
        }

        return true;
    }

    private OnTouchLetterListener listener;

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    // 触摸事件的回调接口定义
    public interface OnTouchLetterListener {
        void onTouchingLetterListener(String letter);

        void onTouchedLetterListener();
    }

    public void setLetterIndexList(List<String> list) {
        setLetterIndexList(list, true);
    }

    // 设置侧边栏的字母
    // perform为true时，表示接收外部传进的字母列表
    // perform为false时，表示放弃外部传进的字母列表，使用默认的字母列表
    public void setLetterIndexList(List<String> list, boolean perform) {
        mLetterIndexList = perform ? list : Arrays.asList(mLetterIndexArray);
        invalidate();
    }

    /**
     * 单位换算
     *
     * @param context
     * @param dip
     * @return
     */
    public float dp2px(Context context, int dip) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm);
    }
}
