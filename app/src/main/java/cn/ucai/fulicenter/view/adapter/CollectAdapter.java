package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.CollectActivity;
import cn.ucai.fulicenter.view.activity.GoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CollectBean> list;
    String FootText;
    boolean isMore;
    boolean isDragging = true;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
        notifyDataSetChanged();
    }


    public void setFootText(String footText) {
        FootText = footText;
        notifyDataSetChanged();
    }
    public CollectAdapter(Context context, ArrayList<CollectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View layout;
        switch (viewType) {
            case I.TYPE_ITEM:
                layout = View.inflate(context, R.layout.collect_item, null);
                holder = new CollectViewHolder(layout);
                break;
            case I.TYPE_FOOTER:
                layout = View.inflate(context, R.layout.newgoods_footer, null);
                holder = new FooterViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            footer.mtv_footer.setText(FootText);
            return;
        }
        CollectViewHolder collect = (CollectViewHolder) holder;
        final CollectBean bean = list.get(position);
        collect.collectName.setText(bean.getGoodsName());
        ImageLoader.downloadImg(context,collect.mCollectIv,bean.getGoodsThumb(),isDragging);
        collect.collectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCollect(bean, position);
            }
        });
        collect.mCollectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CollectActivity)context).startActivityForResult(new Intent(context, GoodsDetailActivity.class)
                .putExtra("GoodsId",bean.getGoodsId()),I.REQUEST_CODE_COLLECT);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }
    private void deleteCollect(CollectBean bean, final int position) {
        IUserModel model = new UserModel();
        model.deleteCollect(context, bean.getGoodsId(), bean.getUserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    if (result.isSuccess()) {
                        list.remove(position);
                        CommonUtils.showLongToast(R.string.delete_collect_success);
                        notifyDataSetChanged();
                    } else {
                        CommonUtils.showLongToast(R.string.delete_collect_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size()+1;
    }

    public void initArrayList(ArrayList<CollectBean> mArrayList) {
        list.clear();
        list.addAll(mArrayList);
        notifyDataSetChanged();
    }

    public void addArrayList(ArrayList<CollectBean> mArrayList) {
        list.addAll(mArrayList);
        notifyDataSetChanged();
    }

    static class CollectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mCollectIv)
        ImageView mCollectIv;
        @BindView(R.id.collect_delete)
        ImageView collectDelete;
        @BindView(R.id.collect_name)
        TextView collectName;

        CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView mtv_footer;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mtv_footer = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
}
