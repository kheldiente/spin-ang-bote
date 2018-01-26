package midien.kheldiente.spinangbote.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import midien.kheldiente.spinangbote.R;

public class SpinTable extends View {

    List<Paint> mPlayerPaints;

    Paint mInnerCircleStrokePaint;
    Paint mInnerCircleFillPaint;
    Paint mOuterCirclePaint;
    Paint mLinePaint;

    Bitmap  mBottle;

    private int mPlayerSize = 0;s

    private static final int ALPHA = 100;

    private float xPadding = 0.0f;
    private float yPadding = 0.0f;

    private float mInnerCircleCenterX = 0.0f;
    private float mInnerCircleCenterY = 0.0f;
    private float mInnerCircleRadius = 0.0f;

    private float mOuterCircleCenterX = 0.0f;
    private float mOuterCircleCenterY = 0.0f;
    private float mOuterCircleRadius = 0.0f;

    private float mStartX = 0.0f;
    private float mStartY = 0.0f;
    private float mStopX = 0.0f;
    private float mStopY = 0.0f;

    private float mBottleLeft = 0;
    private float mBottleTop = 0;
    private float mBottleRight = 0;
    private float mBottleBottom = 0;

    public SpinTable(Context context) {
        super(context);
        init();
    }

    public SpinTable(Context context, AttributeSet attrs) {
        super(context, attrs);

        // attrs contains the raw values for the XML attributes
        // that were specified in the layouto, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyleAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.SpinTable, which is an array of
        // the custom attributes that were declared in attrs.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SpinTable,
                0,
                0);

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of the class.
            //
            // The R.styleable.SpinTable_* constants represent the index for
            // each custom attribute in the R.styleable.SpinTable array.
            mPlayerSize = a.getInteger(R.styleable.SpinTable_players, 0);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

        init();
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // Do nothing. Do not call the superclass method--that would start a layout pass
        // on this view's children. PieChart lays out its children in onSizeChanged().
    }

    /**
     * Iniitalize the control. This code is in a separate method so that
     * it can be called from both constructors
     */
    private void init() {
        // Force the background to software rendering because otherwise the Clear
        // filter won't work
        setLayerToSW(this);

        // Set up the paint for outer circle.
        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setStyle(Paint.Style.FILL);
        mOuterCirclePaint.setAlpha(ALPHA);

        // Set up the fill paint for inner circle.
        mInnerCircleFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCircleFillPaint.setStyle(Paint.Style.FILL);
        mInnerCircleFillPaint.setAlpha(ALPHA);
        mInnerCircleFillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        // Set up the stroke paint for inner circle.
        mInnerCircleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mInnerCircleStrokePaint.setStrokeWidth(40);
        // Masks outer circle using PorterDuff.
        mInnerCircleStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // Set up the line paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(20);
        mLinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mBottle = BitmapFactory.decodeResource(getResources(), R.drawable.bottle);
        invalidate();
    }

    /**
     * Called in order for PorterDuff to work
     * @param v View to apply
     */
    private void setLayerToSW(View v) {
        if(!v.isInEditMode() && Build.VERSION.SDK_INT >= 11)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Set dimensions for text, pie chart, etc.
        // Account for padding
        xPadding = (float) (getPaddingLeft() + getPaddingRight());
        yPadding = (float) (getPaddingTop() + getPaddingBottom());

        mInnerCircleCenterX = (float) w / 2; // Divide by 2 to center
        mInnerCircleCenterY = (float) h / 2;
        mInnerCircleRadius = Math.min(mInnerCircleCenterX, mInnerCircleCenterY) / 1.5f - xPadding;

        mOuterCircleCenterX = (float) w / 2; //Divide by 2 to center
        mOuterCircleCenterY = (float) h / 2; // Divide by 2 to center
        mOuterCircleRadius = Math.min(mInnerCircleCenterX, mInnerCircleCenterY) - xPadding;

        // Coordinates for line
        mStartX = xPadding;
        mStartY = mInnerCircleCenterY;
        mStopX = w - xPadding;
        mStopY = mInnerCircleCenterY;

        // Bound coordinates for bottle
        mBottleLeft = (mInnerCircleCenterX / 2) + (mInnerCircleRadius / 2.3f);
        mBottleTop = (mInnerCircleCenterY - mInnerCircleRadius) + (mInnerCircleRadius / 4);
        mBottleRight = w - (mInnerCircleCenterX / 2) - (mInnerCircleRadius / 2.3f);
        mBottleBottom = mInnerCircleCenterY + mInnerCircleRadius - (mInnerCircleRadius / 4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw outer circle
        canvas.drawCircle(mOuterCircleCenterX, mOuterCircleCenterY, mOuterCircleRadius, mOuterCirclePaint);
        // Draw line
        canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mLinePaint);
        // Draw inner circle stroke
        canvas.drawCircle(mInnerCircleCenterX, mInnerCircleCenterY, mInnerCircleRadius, mInnerCircleStrokePaint);
        // Draw inner circle fill
        canvas.drawCircle(mInnerCircleCenterX, mInnerCircleCenterY, mInnerCircleRadius, mInnerCircleFillPaint);
        // Draw bottle
        // canvas.drawBitmap(mBottle, mBottleLeft, mBottleTop, null);
        canvas.drawBitmap(mBottle, null, new RectF(mBottleLeft, mBottleTop, mBottleRight, mBottleBottom), null);
        // then recycle after use
        mBottle.recycle();
    }

}
