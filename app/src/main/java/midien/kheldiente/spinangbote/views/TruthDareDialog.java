package midien.kheldiente.spinangbote.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import midien.kheldiente.spinangbote.R;

public class TruthDareDialog extends DialogFragment {

    private String mPlayer = "";

    private TextView mConsequenceMsg;

    public static TruthDareDialog newInstance() {
        TruthDareDialog f = new TruthDareDialog();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_truthdare, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mConsequenceMsg = view.findViewById(R.id.consequence_msg);
        mConsequenceMsg.setText(mPlayer);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
    }

    public void setPlayer(String player) {
        mPlayer = player;
    }
}
