package midien.kheldiente.spinangbote.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import midien.kheldiente.spinangbote.R;

public class PlayerEditText extends AppCompatEditText implements View.OnTouchListener, TextWatcher {

    private final int DRAWABLE_RIGHT = 2;

    private Runnable mUpdateDrawableRunnable;

    public PlayerEditText(Context context) {
        super(context);
        init();
    }

    public PlayerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        updateClearDrawable(isFocused());
        addTextChangedListener(this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            final Drawable clearDrawable = getCompoundDrawables()[DRAWABLE_RIGHT];
            if(clearDrawable != null && motionEvent.getRawX() >= (getRight() - clearDrawable.getBounds().width())) {
                setText("");
                requestFocus();
                return true;
            }
        }

        return false;
    }

    private void updateClearDrawable(boolean focused) {
        updateClearDrawable(null, focused);
    }

    private void updateClearDrawable(final String text, final boolean focused) {
        final String currentText = text != null ? text : getText().toString();
        mUpdateDrawableRunnable = new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(currentText) || !focused)
                    setCompoundDrawables(null, null, null, null);
                else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel,0);
                    setCompoundDrawablePadding(10);
                }
            }
        };
        post(mUpdateDrawableRunnable);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateClearDrawable(charSequence.toString(), isFocused());
    }

    @Override
    public void afterTextChanged(Editable editable) {}
}
