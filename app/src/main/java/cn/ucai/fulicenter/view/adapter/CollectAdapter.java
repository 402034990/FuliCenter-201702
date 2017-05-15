package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
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
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectViewHolder> {
    Context context;
    ArrayList<CollectBean> list;

    public CollectAdapter(Context context, ArrayList<CollectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectViewHolder(View.inflate(context, R.layout.collect_item, null));
    }

    @Override
    public void onBindViewHolder(CollectViewHolder holder, final int position) {
        final CollectBean bean = list.get(position);
        Log.i("main", "CollectAdapter.CollectBean:" + bean.getUserName());
        holder.collectName.setText(bean.getGoodsName());
        ImageLoader.downloadImg(context,holder.mCollectIv,bean.getGoodsThumb());
        holder.collectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
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
}
