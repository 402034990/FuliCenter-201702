package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.UserDao;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class SplashActivity extends AppCompatActivity {
    MyCountDownTimer mTimer;
    @BindView(R.id.skip)
    TextView tvSkip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mTimer = new MyCountDownTimer(8000, 1000);
        mTimer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FuLiCenterApplication.getInstance().getUser() == null) {
                    String username = SharePrefrenceUtils.getInstance().getUserName();
                    if (username != null) {
                        UserDao dao = new UserDao(SplashActivity.this);
                        User user = dao.getUser(username);
                        Log.i("main", "SplashActivity.user:" + user);
                        if (user != null) {
                            FuLiCenterApplication.getInstance().setUser(user);
                        }
                    }
                }
            }
        }).start();
    }

    @OnClick(R.id.skip)
    void skip() {
        mTimer.cancel();
        mTimer.onFinish();
    }


    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvSkip.setText(getString(R.string.skip) + " " + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        }
    }
}

