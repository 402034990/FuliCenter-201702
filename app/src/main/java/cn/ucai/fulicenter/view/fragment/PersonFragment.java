package cn.ucai.fulicenter.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.CollectActivity;
import cn.ucai.fulicenter.view.activity.SettingsActivity;
import cn.ucai.fulicenter.view.custom_view.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    User user;
    @BindView(R.id.owner_photo)
    CircleImageView ownerPhoto;
    @BindView(R.id.owner_name)
    TextView ownerName;
    @BindView(R.id.owner_collect_baby)
    LinearLayout ownerCollectBaby;
    @BindView(R.id.center_user_gridview)
    GridView centerUserGridView;
    Unbinder unbinder;

    int collectCount = 0;
    IUserModel model;
    @BindView(R.id.owner_collect_count)
    TextView ownerCollectCount;

    public PersonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        initOrderList();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ownerCollectCount.setText(String.valueOf(collectCount));
        if (FuLiCenterApplication.getInstance().getUser() != null) {
            user = FuLiCenterApplication.getInstance().getUser();
            ownerName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), ownerPhoto);
            initCollectCount();
        }
    }

    @OnClick(R.id.owner_collect_baby)
    public void CollectBaby() {
        startActivity(new Intent(getContext(),CollectActivity.class));
    }
    private void initCollectCount() {
        model = new UserModel();
        model.findCollectsCount(getContext(), user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    if (result.isSuccess()) {
                        ownerCollectCount.setText(result.getMsg());
                    } else {
                        collectCount = 0;
                        ownerCollectCount.setText(String.valueOf(collectCount));
                    }
                }
            }

            @Override
            public void onError(String error) {
                collectCount = 0;
                ownerCollectCount.setText(String.valueOf(collectCount));
            }
        });
    }

    private void initOrderList() {
        ArrayList<HashMap<String, Object>> imageList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("image", R.drawable.order_list1);
        imageList.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.drawable.order_list2);
        imageList.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("image", R.drawable.order_list3);
        imageList.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("image", R.drawable.order_list4);
        imageList.add(map4);
        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("image", R.drawable.order_list5);
        imageList.add(map5);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), imageList, R.layout.simple_grid_item, new String[]{"image"}
                , new int[]{R.id.image_siplme});
        centerUserGridView.setAdapter(simpleAdapter);
    }

    @OnClick(R.id.owner_set)
    public void onSettings() {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
