package cn.ucai.fulicenter.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_back)
    ImageView registerBack;
    @BindView(R.id.registeruserName)
    EditText userName;
    @BindView(R.id.registeruserNick)
    EditText userNick;
    @BindView(R.id.registpassword)
    EditText rpassword;
    @BindView(R.id.registRepassword)
    EditText Repassword;
    Unbinder bind;
    IUserModel model;
    String mUserName,mPassWord,mNick, mEtPassWord;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind = ButterKnife.bind(this);
    }

    public void Register(View v) {
        initDialog();
        if (checkInput()) {
            model = new UserModel();
            model.register(this, mUserName, mNick, MD5.getMessageDigest(mPassWord), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (s != null) {
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                            userName.requestFocus();
                            userName.setError(getString(R.string.register_fail_exists));
                        } else if (result.getRetCode() == I.MSG_REGISTER_FAIL) {
                            userName.requestFocus();
                            userName.setError(getString(R.string.register_fail));
                        } else {
                            Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, mUserName));
                            finish();
                        }
                    }
                    dismissDialog();
                }

                @Override
                public void onError(String error) {
                    dismissDialog();
                }
            });
        } else {
            dismissDialog();
        }
    }

    private void initDialog() {
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage(getString(R.string.registering));
        pd.show();
    }

    private void dismissDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }
    private boolean checkInput() {
        mUserName = userName.getText().toString().trim();
        mPassWord = rpassword.getText().toString().trim();
        mEtPassWord = Repassword.getText().toString().trim();
        mNick = userNick.getText().toString().trim();

        if (TextUtils.isEmpty(mUserName)) {
            userName.requestFocus();
            userName.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if (!mUserName.matches("[a-zA-Z]\\w{5,15}")) {
            userName.requestFocus();
            userName.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if (TextUtils.isEmpty(mNick)) {
            userNick.requestFocus();
            userNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }

        if (TextUtils.isEmpty(mPassWord)) {
            rpassword.requestFocus();
            rpassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }

        if (TextUtils.isEmpty(mEtPassWord)) {
            Repassword.requestFocus();
            Repassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }

        if (!mPassWord.equals(mEtPassWord)) {
            Repassword.requestFocus();
            Repassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
