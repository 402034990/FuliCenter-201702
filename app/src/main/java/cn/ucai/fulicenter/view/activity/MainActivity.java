package cn.ucai.fulicenter.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    NewGoodsFragment mGoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoodsFragment = new NewGoodsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.framlayout,mGoodsFragment).commit();
    }

    public void onChangedCheck(View view) {

    }
}
