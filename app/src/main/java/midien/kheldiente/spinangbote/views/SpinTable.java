package midien.kheldiente.spinangbote.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import midien.kheldiente.spinangbote.R;

import static android.support.v4.util.Preconditions.checkNotNull;

public class SpinTable extends ViewGroup {

    private static final String TAG = SpinTable.class.getSimpleName();

    BottleView mBottleView;
    RoundTableView mRoundTableView;
    private List<Player> mPlayers;

    private int PLAYERS = 1;

    private static final int ALPHA_VAL = 100;

    private static final float BOTTLE_PADDING_TOP_BOTTOM = 1.1f;
    private static final float BOTTLE_PADDING_LEFT_RIGHT = 4.0f;

    private float xPadding = 0.0f;
    private float yPadding = 0.0f;

    private float mOuterCircleLeft = 0.0f;
    private float mOuterCircleTop = 0.0f;
    private float mOuterCircleRight = 0.0f;
    private float mOuterCircleBottom = 0.0f;

    private float mOuterCircleCenterX = 0.0f;
    private float mOuterCircleCenterY = 0.0f;
    private float mOuterCircleRadius = 0.0f;

    private float mBottleLeft = 0;
    private float mBottleTop = 0;
    private float mBottleRight = 0;
    private float mBottleBottom = 0;

    private OnBottleStoppedListener mListener;

    public SpinTable(Context context) {
        super(context);
        init();
    }

    public interface OnBottleStoppedListener {

        void onBottleStopped(String player);

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
            PLAYERS = a.getInteger(R.styleable.SpinTable_players, 2);
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

        // Add round table view
        mRoundTableView = new RoundTableView(getContext());
        addView(mRoundTableView);
        // Add bottle
        mBottleView = new BottleView(getContext());
        addView(mBottleView);

        if(isInEditMode()) {
            // Add all player views
            mPlayers = createPlayerViews(getContext(), PLAYERS);
            for (Player pv : mPlayers) {
                addView(pv);
            }
        }
    }

    public void addPlayer(String name) {
        if(mPlayers == null)
            mPlayers = new ArrayList<>(0);

        Player pv = new Player(getContext());
        pv.name = name;

        // Add to list
        mPlayers.add(pv);
        addView(pv);

        // Update the player count
        PLAYERS = mPlayers.size();

        setWillNotDraw(false);
    }

    private static List<Player> createPlayerViews(Context context, int size) {
        List<Player> pvs = new ArrayList<>(size);
        for(int s = 0;s < size;s++) {
            pvs.add(new Player(context));
        }

        return pvs;
    }


    public void setListener(OnBottleStoppedListener listener) {
        checkNotNull(listener);
        mListener = listener;
    }

    public void spinBottle() {
        mBottleView.spin();
    }

    public void noOfPlayers(int playerSize) {
        PLAYERS = playerSize;
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

        mOuterCircleCenterX = (float) w / 2; //Divide by 2 to center
        mOuterCircleCenterY = (float) h / 2; // Divide by 2 to center
        mOuterCircleRadius = Math.min(mOuterCircleCenterX, mOuterCircleCenterY);

        mOuterCircleLeft = 0 + xPadding;
        mOuterCircleTop = mOuterCircleCenterY + yPadding - (w / 4 * 2) ;
        mOuterCircleRight = w - xPadding;
        mOuterCircleBottom = mOuterCircleCenterY - yPadding + (w / 4 * 2);

        // Lay out the child view that actually draws the round table.
        mRoundTableView.layout((int) mOuterCircleLeft,
                (int) mOuterCircleTop,
                (int) mOuterCircleRight,
                (int) mOuterCircleBottom);

        // Bound coordinates for bottle
        mBottleLeft = ((mOuterCircleCenterX + xPadding) / 2) + (mOuterCircleRadius / BOTTLE_PADDING_LEFT_RIGHT);
        mBottleTop = (mOuterCircleCenterY + yPadding - mOuterCircleRadius / BOTTLE_PADDING_TOP_BOTTOM) + (mOuterCircleRadius / 2);
        mBottleRight = w - ((mOuterCircleCenterX + xPadding) / 2) - (mOuterCircleRadius / BOTTLE_PADDING_LEFT_RIGHT);
        mBottleBottom = (mOuterCircleCenterY - yPadding + mOuterCircleRadius / BOTTLE_PADDING_TOP_BOTTOM) - (mOuterCircleRadius / 2);

        // Lay out the child view that actually draws the bottle.
        mBottleView.layout((int) mBottleLeft,
                (int )mBottleTop,
                (int) mBottleRight,
                (int) mBottleBottom);

        // Get player view coordinates
        CalcUtils.plotPlayer(mOuterCircleCenterX, mOuterCircleCenterY, mOuterCircleRadius, mPlayers);

        // Draw the player's names
        for (Player pv : mPlayers) {
            Log.d(TAG, pv.toString());
            // Lay out the player views that actually draws the player's name.
            pv.layout((int) pv.centerX - 100,
                    (int) pv.centerY - 50,
                    (int) pv.centerX + 100,
                    (int) pv.centerY + 50
            );

            pv.setRotation(pv.median + 270);
        }
    }

    /**
     *  Class for spinning table
     */
    private class RoundTableView extends View {

        private List<float[]> mPoints;
        private List<float[]> mAngles;

        private Paint mInnerCircleStrokePaint;
        private Paint mInnerCircleFillPaint;
        private Paint mOuterCirclePaint;
        private Paint mLinePaint;

        private RectF mOuterCircleBounds;
        private RectF mInnerCircleBounds;

        private float mRoundCenterX = 0.0f;
        private float mRoundCenterY = 0.0f;
        private float mRadius = 0.0f;

        public RoundTableView(Context context) {
            super(context);
            init();
        }

        public RoundTableView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            // Set up the paint for outer circle.
            mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mOuterCirclePaint.setStyle(Paint.Style.FILL);
            mOuterCirclePaint.setColor(Color.BLACK);
            mOuterCirclePaint.setAlpha(ALPHA_VAL);

            // Set up the fill paint for inner circle.
            mInnerCircleFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mInnerCircleFillPaint.setStyle(Paint.Style.FILL);
            mInnerCircleFillPaint.setAlpha(ALPHA_VAL);
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

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            // Set up outer circle bounds
            mOuterCircleBounds = new RectF(0,0, w, h);
            // Set up inner circle bounds
            mInnerCircleBounds = new RectF(w / 4, h / 4, (w / 4) * 3, (h / 4) * 3);

            // Coordinates for center
            mRoundCenterX = (mOuterCircleBounds.left + mOuterCircleBounds.right) / 2;
            mRoundCenterY = (mOuterCircleBounds.top + mOuterCircleBounds.bottom) / 2;
            mRadius = (mOuterCircleBounds.right - mOuterCircleBounds.left) / 2;

            // Get coordinates for line(s)
            mPoints = CalcUtils.getPoints(mRoundCenterX, mRoundCenterY, 1000, PLAYERS);

            // Get angles for arcs
            mAngles = CalcUtils.getStartAndSweepAngles(PLAYERS);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Draw arcs to form a circle
            for(float[] angles: mAngles) {
                canvas.drawArc(mOuterCircleBounds, angles[0], angles[1], true, mOuterCirclePaint);
            }
            // Draw the divider lines
            for(float[] p: mPoints) {
                canvas.drawLine(mRoundCenterX, mRoundCenterY, p[0], p[1], mLinePaint);
            }
            // Draw inner circle stroke
            canvas.drawOval(mInnerCircleBounds, mInnerCircleStrokePaint);
            // Draw inner circle fill
            canvas.drawOval(mInnerCircleBounds, mInnerCircleFillPaint);
        }
    }

    /**
     * Class for the spinning bottle
     */
    private class BottleView extends View implements Animation.AnimationListener {

        private Bitmap  mBottle;
        private RectF mBottleBounds;

        private final Random RANDOM = new Random();

        private int mAngle = 0;
        private float mPivotX = 0.0f;
        private float mPivotY = 0.0f;

        private boolean isSpinning = false;

        private int LAST_ANGLE = -1;
        private final static int DURATION = 2500;

        private Animation mAnimRotate;

        public BottleView(Context context) {
            super(context);
            init();
        }

        public BottleView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            if(mBottle != null)
                mBottle.recycle(); // recycle!

            // Set up bottle image
            mBottle = BitmapFactory.decodeResource(getResources(), R.drawable.bottle);
        }

        public void spin() {
            if(isSpinning)
                return;

            // Flag if bottle is spinning
            isSpinning = true;

            Log.d(TAG, String.format("spin => fromAngle: %s, toAngle: %s", LAST_ANGLE, mAngle));

            mAngle = RANDOM.nextInt(3600 - 360) + 360;
            mPivotX = getWidth() / 2;
            mPivotY = getHeight() / 2;

            mAnimRotate = new RotateAnimation(LAST_ANGLE == -1 ?  0 : LAST_ANGLE, mAngle, mPivotX, mPivotY);

            LAST_ANGLE = mAngle;
            mAnimRotate.setDuration(DURATION);
            mAnimRotate.setFillAfter(true);

            mAnimRotate.setAnimationListener(this);
            startAnimation(mAnimRotate);
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            // Set coordinates of left, top (x, y) and right, bottom (x,y)
            mBottleBounds = new RectF(0.0f, 0.0f, w, h);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw bottle
            canvas.drawBitmap(mBottle, null, mBottleBounds, null);
        }

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            // Flag if bottle stopped spinning
            isSpinning = false;
            // Get angle for bottle
            int pointedAngle = (LAST_ANGLE - 90) % 360;
            int playerIndex = CalcUtils.pointedPlayer(pointedAngle, mPlayers);
            mListener.onBottleStopped(mPlayers.get(playerIndex).name);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

}
