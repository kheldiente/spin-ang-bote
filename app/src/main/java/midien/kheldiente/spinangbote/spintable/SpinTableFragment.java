package midien.kheldiente.spinangbote.spintable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.views.SpinTableView;
import midien.kheldiente.spinangbote.views.TruthDareDialog;


public class SpinTableFragment extends Fragment implements SpinTableContract.View, SpinTableView.OnBottleStoppedListener{

    private static final String TAG = SpinTableFragment.class.getSimpleName();

    private SpinTableContract.Presenter mPresenter;

    private SpinTableView mSpinTableView;
    private Button mSpinBtn;
    private TruthDareDialog mTruthDareDialog;

    public static SpinTableFragment newInstance() {
        return new SpinTableFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_spin_table, container, false);

        mSpinTableView = root.findViewById(R.id.spin_table);
        mSpinBtn = root.findViewById(R.id.btn_spin);

        mSpinTableView.setListener(this);
        mSpinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinBottle();
            }
        });
        mPresenter.loadPlayers();
        return root;
    }

    @Override
    public void setPresenter(SpinTableContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPointedPlayer(String player) {
        Log.d(TAG, String.format("Bottle stopped to player: %s", player));
        mTruthDareDialog = TruthDareDialog.newInstance();
        mTruthDareDialog.setPlayer(player);
        mTruthDareDialog.show(getFragmentManager(), TAG);
    }

    @Override
    public void spinBottle() {
        mSpinTableView.spinBottle();
    }

    @Override
    public void addPlayerView(String player) {
        mSpinTableView.addPlayer(player);
    }

    @Override
    public void onBottleStopped(String player) {
        showPointedPlayer(player);
    }
}
