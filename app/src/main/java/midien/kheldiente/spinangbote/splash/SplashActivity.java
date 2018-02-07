package midien.kheldiente.spinangbote.splash;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.List;

import midien.kheldiente.spinangbote.BaseActivity;
import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.views.BannerView;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private BannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBannerView = findViewById(R.id.banner);
        mBannerView.startAnimation();
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBannerView.startAnimation();
            }
        });

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
