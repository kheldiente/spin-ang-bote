package midien.kheldiente.spinangbote.spintable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import midien.kheldiente.spinangbote.BaseActivity;
import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.utils.ActivityUtils;

public class SpinTableActivity extends BaseActivity {

    private static final String TAG = SpinTableActivity.class.getSimpleName();

    public static final String EXTRA_PLAYERS = "players";

    private SpinTablePresenter mSpinTableRepresenter;

    private List<String> mPlayers = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_table);

        Intent mIntent = getIntent();
        if(mIntent != null) {
            if (mIntent.hasExtra(EXTRA_PLAYERS)) {
                mPlayers = mIntent.getStringArrayListExtra(EXTRA_PLAYERS);
            }
        }

        SpinTableFragment spinTableFragment = (SpinTableFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(spinTableFragment == null) {
            // Create the fragment
            spinTableFragment = SpinTableFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), spinTableFragment, R.id.content_frame);
        }

        // Create the presenter
        mSpinTableRepresenter = new SpinTablePresenter(spinTableFragment, mPlayers);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
