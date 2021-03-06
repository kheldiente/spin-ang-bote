package midien.kheldiente.spinangbote.spintable;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SpinTablePresenter implements SpinTableContract.Presenter {

    private static final String TAG = SpinTablePresenter.class.getSimpleName();

    @NonNull
    private final SpinTableContract.View mSpinTableView;

    private List<String> mPlayers = new ArrayList<>(0);

    public SpinTablePresenter(@NonNull SpinTableContract.View spinTable, List<String> players) {
        mSpinTableView = spinTable;
        mPlayers = players;
        mSpinTableView.setPresenter(this);
    }


    @Override
    public void loadPlayers() {
        for(String p: mPlayers) {
            Log.d(TAG, String.format("adding player %s", p));
            mSpinTableView.addPlayerView(p);
        }
    }

    @Override
    public void onCreate() {}
}
