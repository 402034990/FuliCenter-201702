package cn.ucai.fulicenter.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.UserDao;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_back)
    ImageView loginBack;
    @BindView(R.id.etuserName)
    EditText etuserName;
    @BindView(R.id.etpassword)
    EditText etpassword;
    Unbinder bind;
    IUserModel model;

    String userName;
    String password;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        model = new UserModel();
    }

    private void initDialog() {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.logining));
        pd.show();
    }

    private void dismissDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }
    public void onLogin(View view) {
        initDialog();
        if (checkInfo()) {
            setLogin(userName, MD5.getMessageDigest(password));
        } else {
            dismissDialog();
        }

    }

    private boolean checkInfo() {
        userName = etuserName.getText().toString().trim();
        password = etpassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            etuserName.requestFocus();
            etuserName.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etpassword.requestFocus();
            etpassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        return true;
    }

    public void onRegister(View view) {
        startActivityForResult(new Intent(this,RegisterActivity.class),0);
    }
    private void setLogin(final String userName, String password) {
        model.login(this, userName, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_fail_unknow_user), Toast.LENGTH_SHORT).show();
                        } else if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_fail_error_password), Toast.LENGTH_SHORT).show();
                        } else {
                            login(result);
                        }
                    }

                }
                dismissDialog();
            }

            @Override
            public void onError(String error) {
                dismissDialog();
            }
        });
    }

    private void login(Result<User> result) {
        User user = result.getRetData();
        Log.i("main", "LoginActivity.setLogin.user:" + user);
        FuLiCenterApplication.getInstance().setUser(user);
        SharePrefrenceUtils.getInstance().setUserName(user.getMuserName());
        UserDao dao = new UserDao(LoginActivity.this);
        dao.saveUser(user);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String username = data.getStringExtra(I.User.USER_NAME);
            etuserName.setText(username);
        }
    }

    @OnClick(R.id.login_back)
    public void onBack(View view) {
//        startActivity(new Intent(this,MainActivity.class));
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
