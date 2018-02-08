package midien.kheldiente.spinangbote.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class EditPlayerTextView extends AppCompatTextView implements EditPlayer {

    public String name = "";

    private int mTextColor = Color.BLACK;

    private int mPaddingLeft = 20;
    private int mPaddingTop = 20;
    private int mPaddingRight = 20;
    private int mPaddingBottom = 20;

    private int mMarginTop = 20;
    private int mMarginBottom = 20;

    private static final float ALPA_VAL = .8f;

    private LinearLayout.LayoutParams mLayoutParams;

    @NonNull
    Paint mLinePaint;

    private int mLineColor = Color.BLACK;
    private int mLineStroke = 40;

    private float mLineStartX = 0.0f;
    private float mLineStartY = 0.0f;
    private float mLineStopX = 0.0f;
    private float mLineStopY = 0.0f;


    // limit text to 8 characters only
    private InputFilter[] mMaxLengthFilter = {new InputFilter.LengthFilter(8)};

    public EditPlayerTextView(Context context) {
        super(context);
        init();
    }

    public EditPlayerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Large);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextColor(mTextColor);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        setFilters(mMaxLengthFilter);
        setAlpha(ALPA_VAL);

        //Set margins (top and bottom)
        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(0, mMarginTop, 0, mMarginBottom);
        setLayoutParams(mLayoutParams);

        // Set up the line paint at the bottom of text
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineStroke);

        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLineStartX = w / 4;
        mLineStartY = h;
        mLineStopX = (w / 4) * 3;
        mLineStopY = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(mLineStartX, mLineStartY, mLineStopX, mLineStopY, mLinePaint);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        setText(name);
    }
}
