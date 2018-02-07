package midien.kheldiente.spinangbote.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import midien.kheldiente.spinangbote.R;

public class BannerView extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    private Paint mTopLinePaint; // From left to right
    private Paint mBottomLinePaint; // From right to left
    private TextView mTextView;

    private float mTopStartX, mTopStartY, mTopStopX, mTopStopY;
    private float mTextLeft, mTextTop, mTextRight, mTextBottom;
    private float mBottomStartX, mBottomStartY, mBottomStopX, mBottomStopY;

    private static final int LINE_PADDING = 40;

    // Animator
    private ValueAnimator mAnimator;
    private float mAnimatingFraction;

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        // Setup top line paint
        mTopLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTopLinePaint.setColor(Color.BLACK);
        mTopLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTopLinePaint.setStrokeWidth(30);

        // Setup bottom line paint
        mBottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomLinePaint.setColor(Color.BLACK);
        mBottomLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBottomLinePaint.setStrokeWidth(30);


        // Set up text view
        mTextView = new TextView(getContext());
        mTextView.setTextColor(Color.BLACK);
        mTextView.setText(getResources().getString(R.string.app_name));
        mTextView.setTextSize(50);
        mTextView.setGravity(Gravity.CENTER);
        // mTextView.setBackgroundColor(Color.BLACK);

        // Add text layout
        addView(mTextView);

        // Setup value animator
        mAnimator = new ValueAnimator();
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.setDuration(400);
        mAnimator.addUpdateListener(this);
        mAnimatingFraction = 0f;

        // MUST call this to init call onDraw method
        setWillNotDraw(false);
    }

    public void startAnimation() {
        mAnimator.setFloatValues(0f, 1f);
        mAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Text
        mTextTop =  (h / 4);
        mTextRight = w;
        mTextBottom = (h / 4) * 3;
        mTextView.layout((int) mTextLeft,
                (int) mTextTop,
                (int) mTextRight,
                (int) mTextBottom);

        // Center the text view using getHeight AFTER layout-ed
        mTextTop = (h / 2) - (mTextView.getHeight() / 4);
        mTextBottom = (h / 2) + (mTextView.getHeight() / 4);
        // Layout it again
        mTextView.layout((int) mTextLeft,
                (int) mTextTop,
                (int) mTextRight,
                (int) mTextBottom);

        // Top line will be drawn from left to right
        mTopStartY = mTextTop - LINE_PADDING;
        mTopStopX = w;
        mTopStopY = mTextTop - LINE_PADDING;

        // Bottom line will be drawn from right to left
        mBottomStartY = mTextBottom + LINE_PADDING;
        mBottomStopX = w;
        mBottomStopY = mTextBottom + LINE_PADDING;

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // Do nothing. Do not call the superclass method--that would start a layout pass
        // on this view's children. BannerView lays out its children in onSizeChanged().
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw top line paint from left to right
        float topStopX = mTopStopX * mAnimatingFraction;
        canvas.drawLine(mTopStartX, mTopStartY, topStopX, mTopStopY, mTopLinePaint);

        //Draw bottom line paint from right to left
        float bottomStartX = mBottomStopX - (mBottomStopX * mAnimatingFraction);
        canvas.drawLine(bottomStartX, mBottomStartY, mBottomStopX, mBottomStopY, mBottomLinePaint);

        // Scale and pivot text view
        // mTextView.setScaleY(mAnimatingFraction);
        mTextView.setScaleY(mAnimatingFraction);

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        // Get our float from the animation. This method returns the Interpolated float.
        mAnimatingFraction = animation.getAnimatedFraction();

        // Must call this to ensure view re-draws
        invalidate();
    }
}
