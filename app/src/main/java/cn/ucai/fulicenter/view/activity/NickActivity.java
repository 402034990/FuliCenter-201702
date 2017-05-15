package cn.ucai.fulicenter.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.UserDao;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class NickActivity extends AppCompatActivity {
    @BindView(R.id.mnick_back)
    ImageView mNickBack;
    @BindView(R.id.etnick)
    EditText etNick;
    @BindView(R.id.save_nick)
    Button saveNick;
    Unbinder bind;
    User user;
    IUserModel model;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.update_user_nick));
        pd.show();
    }

    private void initView() {
        user = FuLiCenterApplication.getInstance().getUser();
        etNick.setText(user.getMuserNick());
        etNick.selectAll();
        model = new UserModel();
    }

    private void dismissDialog() {
        if (pd != null&&pd.isShowing()) {
            pd.dismiss();
        }
    }
    @OnClick({R.id.mnick_back, R.id.save_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mnick_back:
                finish();
                break;
            case R.id.save_nick:
                updateNick();
                break;
        }
    }

    private void updateNick() {
        initDialog();
        if (checkInput()) {
            model.updateNick(this, user.getMuserName(), etNick.getText().toString().trim(), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        updateNick(result);
                    }
                    dismissDialog();
                }

                @Override
                public void onError(String error) {
                    dismissDialog();
                }
            });
        }
        dismissDialog();
    }

    private boolean checkInput() {
        if (etNick.getText().toString().trim().equals(user.getMuserNick())) {
            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
            return false;
        }
        return true;
    }

    private void updateNick(Result result) {
        User user1 = (User) result.getRetData();
        CommonUtils.showLongToast(R.string.update_user_nick_success);
        SharePrefrenceUtils.getInstance().setUserName(user1.getMuserName());
        FuLiCenterApplication.getInstance().setUser(user1);
        UserDao dao = new UserDao(NickActivity.this);
        dao.saveUser(user1);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        dismissDialog();
    }
}
