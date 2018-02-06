package midien.kheldiente.spinangbote.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import midien.kheldiente.spinangbote.R;

import static android.support.v4.util.Preconditions.checkNotNull;

public class EditPlayerDialog extends Dialog implements View.OnClickListener, DialogInterface.OnDismissListener {

    private OnUpdateListener mOnUpdateListener;

    private static final int HIDE_MSG_MILLIS = 3000;

    // Views
    private EditText mNameEditTxt;
    private ImageView mCancelIv;
    private ImageView mOkIv;
    private ImageView mDeleteIv;
    private TextView mMsg;

    private String mName = "";
    private String mKey = "";

    // If true, player can be deleted from the list
    private boolean mDeleteable = true;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mMsg.setVisibility(View.INVISIBLE);
        }

    };


    public interface OnUpdateListener {

        void onUpdate(String key, String name);

        void onDelete(String key);

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

    public void setValues(String key, String name) {
        mKey = key;
        mName = name;
    }

    public void cannotBeDeleted(boolean deleteable) {
        mDeleteable = deleteable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_player);
        setCancelable(false);
        setOnDismissListener(this);

        // Set dialog window width to match parent
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setFormWidgets();
    }

    private void setFormWidgets() {
        mNameEditTxt = findViewById(R.id.add_edittxt);
        mCancelIv = findViewById(R.id.cancel_iv);
        mOkIv = findViewById(R.id.ok_iv);
        mDeleteIv = findViewById(R.id.delete_iv);
        mMsg = findViewById(R.id.msg_txt);

        // Set player name to edit
        mNameEditTxt.setText(mName);
        mNameEditTxt.setSelection(mName.length());
        // If {@value mName} is empty, do not show delete icon. If {@value mDeleteable} is false, player cannot be deleted
        if(TextUtils.isEmpty(mName) || !mDeleteable)
            mDeleteIv.setVisibility(View.GONE);

        // Message text view is invisible by default
        mMsg.setVisibility(View.INVISIBLE);

        mCancelIv.setOnClickListener(this);
        mOkIv.setOnClickListener(this);
        mDeleteIv.setOnClickListener(this);
        // Force focus on player edit text
        forceFocus();
    }

    public void setMessageInfo(String msg) {
        mMsg.setText(msg);
        showMessageInfo();
    }

    public void showMessageInfo() {
        mMsg.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(0, HIDE_MSG_MILLIS);
    }

    public void hideMessageInfo() {
        mMsg.setVisibility(View.INVISIBLE);
    }

    private void forceFocus() {
        mNameEditTxt.requestFocus();
        // Open keyboard
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void reset() {
        // Reset all values
        mName = "";
        mNameEditTxt.setText(mName);
    }

    private void updateName() {
        checkNotNull(mOnUpdateListener);
        mName = mNameEditTxt.getText().toString();
        mOnUpdateListener.onUpdate(mKey, mName);
    }

    private void doNothing() {
        dismiss();
    }

    private void deletePlayer() {
        dismiss();
        checkNotNull(mOnUpdateListener);
        mOnUpdateListener.onDelete(mKey);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mHandler = null;
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
            case R.id.delete_iv:
                deletePlayer();
        }
    }

}
