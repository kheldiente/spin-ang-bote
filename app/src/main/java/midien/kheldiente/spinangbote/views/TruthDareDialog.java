package midien.kheldiente.spinangbote.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import midien.kheldiente.spinangbote.R;

public class TruthDareDialog extends DialogFragment {

    private String mPlayer = "";

    private TruthDareCard mTruthCard;

    public static TruthDareDialog newInstance() {
        TruthDareDialog f = new TruthDareDialog();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mTruthCard = new TruthDareCard(getContext());
        return mTruthCard;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        mTruthCard.startAnimation();
    }

    public void setPlayer(String player) {
        mPlayer = player;
    }

    private class TruthDareCard extends View implements ValueAnimator.AnimatorUpdateListener {

        private Paint mConsTxtPaint;

        private float mTextX, mTextY;

        private static final int FLIPS = 2;

        // Animator
        private ValueAnimator mAnimator;
        private float mAnimatingFraction;

        private static final int DESIRED_WIDTH = 200;
        private static final int DESIRED_HEIGHT = 1000;

        public TruthDareCard(Context context) {
            super(context);
            init();
        }

        public TruthDareCard(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setBackgroundResource(R.drawable.floor_bg);

            mConsTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mConsTxtPaint.setColor(Color.BLACK);
            mConsTxtPaint.setTextAlign(Paint.Align.CENTER);
            mConsTxtPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mConsTxtPaint.setFakeBoldText(true);
            mConsTxtPaint.setTextSize(50);

            // Setup value animator
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.setDuration(200);
            mAnimator.addUpdateListener(this);
            mAnimatingFraction = 0f;

            setScaleX(mAnimatingFraction);

            invalidate();
        }

        public void startAnimation(float start, float stop) {
            mAnimator.setFloatValues(start, stop);
            mAnimator.setRepeatCount(FLIPS);
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mAnimator.start();
        }

        public void startAnimation() {
            startAnimation(0f, 1f);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            int width, height;

            switch(widthMode) {
                case MeasureSpec.EXACTLY:
                    // Must be this size
                    width = widthSize;
                    break;
                case MeasureSpec.AT_MOST:
                    // Can't be bigger than..
                    width = Math.min(DESIRED_WIDTH, widthSize);
                    break;
                default:
                    // Be desired width
                    width = DESIRED_WIDTH;
                    break;
            }

            switch(heightMode) {
                case MeasureSpec.EXACTLY:
                    // Must the this size
                    height = heightSize;
                    break;
                case MeasureSpec.AT_MOST:
                    // Can't be bigger than..
                    height = Math.min(DESIRED_HEIGHT, heightSize);
                    break;
                default:
                    // Be desired width
                    height = DESIRED_HEIGHT;
                    break;
            }

            // Must call to set height and width
            setMeasuredDimension(width, height);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mTextX = w / 2;
            mTextY = h / 4;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawText(mPlayer, mTextX, mTextY, mConsTxtPaint);
            // Scale y to gradually scale width
            setScaleX(mAnimatingFraction);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimatingFraction = animation.getAnimatedFraction();

            // Must call this to redraw view
            invalidate();
        }
    }
}
