package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.view.custom_view.CircleImageView;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.msetting_back)
    ImageView msettingBack;
    @BindView(R.id.msetting_avatar)
    CircleImageView msettingAvatar;
    @BindView(R.id.msetting_name)
    TextView msettingName;
    @BindView(R.id.msetting_nick)
    TextView msettingNick;
    @BindView(R.id.exist_login)
    Button existLogin;
    Unbinder bind;
    User user;
    @BindView(R.id.modify_nick)
    LinearLayout modifyNick;
    @BindView(R.id.LinearLayout_Avatar)
    LinearLayout LinearLayoutAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        user = FuLiCenterApplication.getInstance().getUser();
        Log.i("main", "SettingsActivity.user:" + user);
        if (user != null) {
            msettingName.setText(user.getMuserName());
            msettingNick.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, msettingAvatar);
        }
    }

    @OnClick({R.id.msetting_back, R.id.exist_login, R.id.modify_nick,R.id.LinearLayout_Avatar})
    public void onBack(View view) {
        switch (view.getId()) {
            case R.id.msetting_back:
                finish();
                break;
            case R.id.exist_login:
                logout();
                break;
            case R.id.modify_nick:
                startActivity(new Intent(this, NickActivity.class));
                break;
            case R.id.LinearLayout_Avatar:
                startActivity(new Intent(this,UpdateAvatarActivity.class));
                break;
        }
    }

    private void logout() {
        FuLiCenterApplication.getInstance().setUser(null);
        SharePrefrenceUtils.getInstance().removeUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
