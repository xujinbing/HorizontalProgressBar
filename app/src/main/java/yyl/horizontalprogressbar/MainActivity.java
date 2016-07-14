package yyl.horizontalprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int UP_DATA = 0X110;

    //private HorizontalProgressBar mProgressBar;
    private CustomHorizontalProgressBar mProgressBar;
    private RoundProgressBar mRoundProgressBar;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = mProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
            mRoundProgressBar.setProgress(++progress);
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

        mProgressBar = (CustomHorizontalProgressBar) findViewById(R.id.progress_2);
        mRoundProgressBar = (RoundProgressBar) findViewById(R.id.progress_3);

        mHandler.sendEmptyMessage(UP_DATA);
    }
}
