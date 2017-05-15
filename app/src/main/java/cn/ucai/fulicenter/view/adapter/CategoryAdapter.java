package cn.ucai.fulicenter.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.CategoryGoodsDetailActivity;

import static cn.ucai.fulicenter.application.FuLiCenterApplication.DATABASE;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;


    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && groupList != null ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList != null && groupList != null ? childList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_group_item, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        CategoryGroupBean bean = groupList.get(groupPosition);
        holder.tvCategoryName.setText(bean.getName());
        ImageLoader.downloadImg(context, holder.mIvCategoryGroup, bean.getImageUrl());
        if (isExpanded) {
            holder.mIvCategoryExpand.setImageResource(R.mipmap.expand_off);
        } else {
            holder.mIvCategoryExpand.setImageResource(R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_child_item, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final CategoryChildBean bean = childList.get(groupPosition).get(childPosition);
        holder.mTvCategoryChildName.setText(bean.getName());
        ImageLoader.downloadImg(context,holder.mIvCategoryChild,bean.getImageUrl());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,CategoryGoodsDetailActivity.class)
                .putExtra("CategoryGoodsDetailId",bean.getId())
                .putExtra("CategoryGroupName",groupList.get(groupPosition).getName())
                .putExtra("CategoryChildList",childList.get(groupPosition)));
                SharedPreferences sp = context.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("CategoryGroupName", groupList.get(groupPosition).getName());
                edit.commit();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addList(ArrayList<CategoryGroupBean> mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.groupList.addAll(mGroupList);
        this.childList.addAll(mChildList);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.mIv_Category_group)
        ImageView mIvCategoryGroup;
        @BindView(R.id.tv_Category_name)
        TextView tvCategoryName;
        @BindView(R.id.mIv_Category_expand)
        ImageView mIvCategoryExpand;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.mIv_Category_child)
        ImageView mIvCategoryChild;
        @BindView(R.id.mTv_Category_Child_Name)
        TextView mTvCategoryChildName;
        @BindView(R.id.child_linearLayout)
        LinearLayout linearLayout;
        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
