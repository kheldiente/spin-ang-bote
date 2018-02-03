package midien.kheldiente.spinangbote.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PlayerView extends View {

    Paint mNamePaint;

    private final int TEXT_SIZE = 50;

    public String name = "Player";
    private int color = Color.WHITE;

    public float centerX = 0.0f;
    public float centerY = 0.0f;

    public float median = 0.0f;

    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setTextColor(int colorId) {
        color = colorId;
    }

    private void init() {
        mNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNamePaint.setColor(color);
        mNamePaint.setAlpha(200);
        mNamePaint.setTextAlign(Paint.Align.CENTER);
        mNamePaint.setTextSize(TEXT_SIZE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the player's name
        canvas.drawText(name, centerX, centerY, mNamePaint);
    }

    @Override
    public String toString() {
        return String.format("name: %s, centerX: %s, centerY: %s, median: %s", name, centerX, centerY, median);
    }
}
