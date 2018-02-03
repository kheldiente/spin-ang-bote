package midien.kheldiente.spinangbote.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.utils.ActivityUtils;

public class AddPlayerActivity extends AppCompatActivity {

    private static final String TAG = AddPlayerActivity.class.getSimpleName();

    AddPlayerPresenter mAddPlayerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        AddPlayerFragment addPlayerFragment = (AddPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(addPlayerFragment == null) {
            // Create the fragment
            addPlayerFragment = AddPlayerFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), addPlayerFragment, R.id.content_frame);
        }

        // Create the presenter
        mAddPlayerPresenter = new AddPlayerPresenter(addPlayerFragment);


    }
}
