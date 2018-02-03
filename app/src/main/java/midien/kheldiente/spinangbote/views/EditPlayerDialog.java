package midien.kheldiente.spinangbote.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import midien.kheldiente.spinangbote.R;

import static android.support.v4.util.Preconditions.checkNotNull;

public class EditPlayerDialog extends Dialog implements View.OnClickListener {

    private OnUpdateListener mOnUpdateListener;

    // Views
    EditText mNameEditTxt;
    ImageView mCancelIv;
    ImageView mOkIv;

    String mName = "";
    int mIndex = -1;

    public interface OnUpdateListener {

        void onUpdate(int index, String name);

    }

    public EditPlayerDialog(@NonNull Context context) {
        super(context);
    }

    public EditPlayerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        mOnUpdateListener = listener;
    }

    public void setValues(int index, String name) {
        mIndex = index;
        mName = name;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_player);
        setCancelable(false);

        // Set dialog window width to match parent
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setFormWidgets();
    }

    private void setFormWidgets() {
        mNameEditTxt = findViewById(R.id.add_edittxt);
        mCancelIv = findViewById(R.id.cancel_iv);
        mOkIv = findViewById(R.id.ok_iv);

        // Set player name to edit
        mNameEditTxt.setText(mName);

        mCancelIv.setOnClickListener(this);
        mOkIv.setOnClickListener(this);
    }

    private void updateName() {
        dismiss();
        checkNotNull(mOnUpdateListener);
        mName = mNameEditTxt.getText().toString();
        mOnUpdateListener.onUpdate(mIndex, mName);
    }

    private void doNothing() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cancel_iv:
                doNothing();
                break;
            case R.id.ok_iv:
                updateName();
                break;
        }
    }
}
