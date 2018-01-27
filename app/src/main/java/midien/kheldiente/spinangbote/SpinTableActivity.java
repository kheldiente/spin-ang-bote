package midien.kheldiente.spinangbote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import midien.kheldiente.spinangbote.customviews.SpinTable;

public class SpinTableActivity extends AppCompatActivity implements SpinTable.OnBottleStoppedListener {

    private static final String TAG = SpinTableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_table);

        final SpinTable spinTable = findViewById(R.id.spin_table);
        spinTable.setListener(this);
        findViewById(R.id.btn_spin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinTable.spinBottle();
            }
        });
    }

    @Override
    public void onBottleStopped() {
        Log.d(TAG, "Bottle stopped");
    }
}
