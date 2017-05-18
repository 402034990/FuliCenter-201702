package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

public class OrderActivity extends AppCompatActivity {
    int sum = 0;
    @BindView(R.id.BuyBack)
    ImageView BuyBack;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCitys)
    Spinner etCitys;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.Buy)
    Button Buy;
    private static String URL = "http://218.244.151.190/demo/charge";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);
        ButterKnife.bind(this);
        sum = getIntent().getIntExtra("Payment", 0);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[] { "wx", "alipay", "upacp", "bfb", "jdpay_wap" });

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    @OnClick(R.id.BuyBack)
    public void Back() {
        finish();
    }


    public void onClick(View view) {
        if (checkInfo()) {
            // 产生个订单号
            String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

            // 构建账单json对象
            JSONObject bill = new JSONObject();

            // 自定义的额外信息 选填
            JSONObject extras = new JSONObject();
            try {
                extras.put("extra1", "extra1");
                extras.put("extra2", "extra2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                bill.put("order_no", orderNo);
                bill.put("amount", sum);
                bill.put("extras", extras);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //壹收款: 创建支付通道的对话框
            PingppOne.showPaymentChannels(this, bill.toString(), URL, new PaymentHandler() {
                @Override public void handlePaymentResult(Intent data) {
                    if (data != null) {
                        /**
                         * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                         * error_msg：支付结果信息
                         */
                        int code = data.getExtras().getInt("code");
                        String result = data.getExtras().getString("result");
                    }
                }
            });
        }
    }

    private boolean checkInfo() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            etName.setError("收货人姓名不能为空");
            etName.requestFocus();
            return false;
        }

        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("手机号码不能为空");
            etPhone.requestFocus();
            return false;
        }

        if (!phone.matches("[1][\\d]{10}")) {
            etPhone.setError("手机格式不正确");
            etPhone.requestFocus();
            return false;
        }

        String city = etCitys.getSelectedItem().toString();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("街道地址不能为空");
            etAddress.requestFocus();
            return false;
        }
        return true;
    }
}
