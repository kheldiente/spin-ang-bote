package midien.kheldiente.spinangbote.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import midien.kheldiente.spinangbote.R;

public class TruthDareDialog extends DialogFragment {

    private String mPlayer = "";

    private TruthDareCard mTruthCard;
    private TextView mTruthDareTxt;
    private Button mDoneBtn;

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

        setFormWidgets();
        return mTruthCard;
    }

    private void setFormWidgets() {
        // Inflate your own custom layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_truthdare, null);
        mTruthDareTxt = view.findViewById(R.id.truthdare_msg);
        mDoneBtn = view.findViewById(R.id.btn_done);

        mTruthDareTxt.setText(getString(R.string.truth_or_dare_msg, mPlayer));
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Add view to TruthCard view
        addContentView(view);
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

    public void addContentView(View view) {
        mTruthCard.addView(view);
    }


    public class TruthDareCard extends ViewGroup implements ValueAnimator.AnimatorUpdateListener, ValueAnimator.AnimatorListener {

        private static final int FLIPS = 2;

        int deviceWidth;

        // Animator
        private ValueAnimator mAnimator;
        private float mAnimatingFraction;


        public TruthDareCard(Context context) {
            super(context);
            init();
        }

        public TruthDareCard(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            final Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point deviceDisplay = new Point();
            display.getSize(deviceDisplay);
            deviceWidth = deviceDisplay.x;

            setBackgroundResource(R.drawable.floor_bg);

            // Setup value animator
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.setDuration(200);
            mAnimator.addUpdateListener(this);
            mAnimator.addListener(this);
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
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            // Notice:
            // This logic only works on LinearLayout. Layouts that are relative and use constraints
            // are not supported
            //
            // Only get the first index. ONLY 1 child is allowed to add
            final int count = getChildCount();
            if(count == 0)
                return;

            // Get the available size of child view
            final int childLeft = this.getPaddingLeft();
            final int childTop = this.getPaddingTop();
            final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
            final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();

            View child = getChildAt(0);

            if (child.getVisibility() == GONE)
                return;

            // Do the layout
            child.layout(childLeft, childTop, childRight, childBottom);

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int count = getChildCount();
            if(count == 0)
                return;

            // Measurement will ultimately be computing these values.
            int maxWidth = 0;
            int childState = 0;

            final View child = getChildAt(0);

            if (child.getVisibility() == GONE)
                return;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());

            setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), child.getMeasuredHeight());
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // canvas.drawText(getResources().getString(R.string.truth_or_dare_msg), mTextX, mTextY, mConsTxtPaint);
            // Scale y to gradually scale width
            setScaleX(mAnimatingFraction);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimatingFraction = animation.getAnimatedFraction();

            // Must call this to redraw view
            invalidate();
        }

        @Override
        public void onAnimationStart(Animator animator) {}

        @Override
        public void onAnimationEnd(Animator animator) {}

        @Override
        public void onAnimationCancel(Animator animator) {}

        @Override
        public void onAnimationRepeat(Animator animator) {}
    }
}
