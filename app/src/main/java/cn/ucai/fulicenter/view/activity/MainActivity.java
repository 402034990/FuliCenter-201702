package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.view.fragment.CategoryFragment;
import cn.ucai.fulicenter.view.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    NewGoodsFragment mGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    Fragment[] fragments;
    //当前fragment的下标
    int currentIndex=0;
    //设置fragment点击的下标
    int Index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        showFragment();
    }

    private void showFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.framlayout,mGoodsFragment)
                .add(R.id.framlayout,mBoutiqueFragment)
                .show(mGoodsFragment)
                .hide(mBoutiqueFragment)
                .commit();
    }

    private void initFragment() {
        mGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        fragments = new Fragment[5];
        fragments[0] = mGoodsFragment;
        fragments[1] = mBoutiqueFragment;
        fragments[2] = mCategoryFragment;
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
        }
        showFragmentIndex();
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
    }
}
