package midien.kheldiente.spinangbote.splash;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import midien.kheldiente.spinangbote.BaseActivity;
import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.player.AddPlayerActivity;
import midien.kheldiente.spinangbote.spintable.SpinTableActivity;
import midien.kheldiente.spinangbote.views.BannerView;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private BannerView mBannerView;
    private Button mPlayBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Start animation on banner
        mBannerView = findViewById(R.id.banner);
        mBannerView.startAnimation();

        mPlayBtn = findViewById(R.id.btn_play);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddPlayer();
            }
        });

    }

    private void goToAddPlayer() {
        Intent spinTableAct = new Intent(this, AddPlayerActivity.class);
        startActivity(spinTableAct, ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in,
                android.R.anim.fade_out).toBundle());
    }

    private void getAllAppsInstalled() {
        final PackageManager pm = getPackageManager();
        // Get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "App names :" + packageInfo.loadLabel(getPackageManager()).toString());
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }

}
