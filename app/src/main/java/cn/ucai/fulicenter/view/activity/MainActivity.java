package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.view.fragment.CategoryFragment;
import cn.ucai.fulicenter.view.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.fragment.PersonFragment;

public class MainActivity extends AppCompatActivity {
    NewGoodsFragment mGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonFragment mPersonFragment;
    Fragment[] fragments;
    //当前fragment的下标
    int currentIndex = 0;
    //设置fragment点击的下标
    int Index;
    RadioButton[] button;
    @BindView(R.id.newgoods)
    RadioButton newgoods;
    @BindView(R.id.boutique)
    RadioButton boutique;
    @BindView(R.id.category)
    RadioButton category;
    @BindView(R.id.cart)
    RadioButton cart;
    @BindView(R.id.contact)
    RadioButton contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initRadioButton();
        showFragment();
    }

    private void initRadioButton() {
        button = new RadioButton[5];
        button[0] = newgoods;
        button[1] = boutique;
        button[2] = category;
        button[3] = cart;
        button[4] = contact;
    }

    private void showFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.framlayout, mGoodsFragment)
                .add(R.id.framlayout, mBoutiqueFragment)
                .show(mGoodsFragment)
                .hide(mBoutiqueFragment)
                .commit();
    }

    private void initFragment() {
        mGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonFragment = new PersonFragment();
        fragments = new Fragment[5];
        fragments[0] = mGoodsFragment;
        fragments[1] = mBoutiqueFragment;
        fragments[2] = mCategoryFragment;
        fragments[4] = mPersonFragment;
    }

    public void onChangedCheck(View view) {
        switch (view.getId()) {
            case R.id.newgoods:
                Index = 0;
                break;
            case R.id.boutique:
                Index = 1;
                break;
            case R.id.category:
                Index = 2;
                break;
            case R.id.contact:
                if (FuLiCenterApplication.getInstance().getUser() == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), I.REQUEST_CODE_LOGIN);
                } else {
                    Index = 4;
                }
                break;
        }
        showFragmentIndex();
    }

    public void setRadioButton() {
        for (int i = 0; i < button.length; i++) {
            button[i].setChecked(i == Index ? true : false);
        }
    }
    private void showFragmentIndex() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!fragments[Index].isAdded()) {
            ft.add(R.id.framlayout, fragments[Index]);
        }
        if (Index != currentIndex) {
            ft.show(fragments[Index])
                    .hide(fragments[currentIndex])
                    .commit();
        }
        currentIndex = Index;
        setRadioButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_LOGIN) {
            Index = 4;
            showFragmentIndex();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Index == 4 && FuLiCenterApplication.getInstance().getUser() == null) {
            Index = 0;
            showFragmentIndex();
            setRadioButton();
        }
    }
}
