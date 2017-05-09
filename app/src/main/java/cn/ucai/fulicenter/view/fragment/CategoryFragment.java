package cn.ucai.fulicenter.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.adapter.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;
    Unbinder unbinder;

    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    CategoryAdapter mAdapter;
    IModelGoods goods;
    //大类的长度
    int groupSize = 0;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(), mGroupList, mChildList);
        expandablelistview.setAdapter(mAdapter);
    }

    private void initData() {
        goods = new ModelGoods();
        loadCategoryGroupData();
    }

    private void loadCategoryGroupData() {
        goods.loadCategoryGroup(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    mGroupList = ResultUtils.array2List(result);
                    int i = 0;
                    for (final CategoryGroupBean group : result) {
                        int id = group.getId();
                        mChildList.add(i,new ArrayList<CategoryChildBean>());
                        loadCategoryChildData(i, id);
                        i++;
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void loadCategoryChildData(final int i, int id) {
        goods.loadCategoryChild(getContext(), id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] childBean) {
                groupSize++;
                if (childBean != null) {
                    ArrayList<CategoryChildBean> childList = ResultUtils.array2List(childBean);
                    if (childList != null) {
                        mChildList.set(i,childList);
                    }
                }

                if (mGroupList.size() == groupSize) {
                    mAdapter.addList(mGroupList, mChildList);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
