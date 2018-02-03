package midien.kheldiente.spinangbote.spintable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.utils.ActivityUtils;

public class SpinTableActivity extends AppCompatActivity {

    private static final String TAG = SpinTableActivity.class.getSimpleName();

    public static final String EXTRA_PLAYERS = "players";

    private SpinTablePresenter mSpinTableRepresenter;

    private String[] mPlayers = {"Michael", "Joem", "Imelda", "Joelito"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_table);


        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_PLAYERS)) {
                mPlayers = savedInstanceState.getStringArray(EXTRA_PLAYERS);
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
}
