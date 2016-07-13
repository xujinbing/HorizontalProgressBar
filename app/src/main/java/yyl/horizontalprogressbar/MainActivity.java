package yyl.horizontalprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int UP_DATA = 0X110;

    private HorizontalProgressBar mProgressBar;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = mProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
            if (progress >= 100) {
                mHandler.removeMessages(UP_DATA);
            }
            mHandler.sendEmptyMessageDelayed(UP_DATA,100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (HorizontalProgressBar) findViewById(R.id.progress_1);

        mHandler.sendEmptyMessage(UP_DATA);
    }
}
