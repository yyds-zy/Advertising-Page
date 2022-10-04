package com.free.banner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.free.banner.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class ADBannerAdapter extends BannerAdapter<ADBannerAdapter.BannerListBean, RecyclerView.ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<BannerListBean> mDataList;
    private StandardGSYVideoPlayer mVideo = null;

    //视频
    public static final int VEDIO = 1;
    //图片
    public static final int IMAGE = 2;

    public ADBannerAdapter(Context context, List<BannerListBean> dataList) {
        super(dataList);
        mContext = context;
        mDataList = dataList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //通过isVedio获取返回不同的布局
    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).isVedio()) {
            return VEDIO;
        } else {
            return IMAGE;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        LayoutInflater from = LayoutInflater.from(mContext);
        if (viewType == VEDIO) {
            view = from.inflate(R.layout.item_gsyvideopay, parent, false);
            holder = new VideoHolder(view);
        } else {
            view = from.inflate(R.layout.item_banner_image, parent, false);
            holder = new ImageHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, BannerListBean data, int position, int size) {
        if (holder instanceof VideoHolder) {
            setVideo(holder, data, position, size);
        } else {
            setImage(holder, data, position, size);
        }
    }


    private void setImage(RecyclerView.ViewHolder holder, BannerListBean data, int position, int size) {
        ImageView image = ((ImageHolder) holder).image;
        Glide.with(mContext)
                .load(data.getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(image);
    }

    private void setVideo(RecyclerView.ViewHolder holder, BannerListBean data, int position, int size) {
        mVideo = ((VideoHolder) holder).video;
        mVideo.setUpLazy(data.getUrl(), true, mContext.getCacheDir(), null, "");
        mVideo.getTitleTextView().setVisibility(View.GONE);
        mVideo.getBackButton().setVisibility(View.GONE);
        mVideo.getFullscreenButton().setVisibility(View.GONE);
        //音频焦点冲突时是否释放
        mVideo.setReleaseWhenLossAudio(true);
        //禁止全屏
        mVideo.setAutoFullWithSize(false);
        //禁止滑动
        mVideo.setIsTouchWiget(false);

        mVideo.startPlayLogic();
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public static class BannerListBean {


        public boolean isVedio() {
            return Vedio;
        }

        public void setVedio(boolean vedio) {
            Vedio = vedio;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private boolean Vedio = false;
        private String url;

    }

    //释放视频播放器
    public void setVideoStop() {
        if (mVideo != null) {
            mVideo.release();
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private StandardGSYVideoPlayer video;

        public VideoHolder(View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.banner_vp);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.banner_iv);
        }
    }

    public StandardGSYVideoPlayer getMyvideo() {
        return this.mVideo;
    }
}
