package com.free.banner;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.free.banner.base.BaseActivity;
import com.free.banner.hbanner.HBanner;
import com.free.banner.hbanner.ImageSubView;
import com.free.banner.hbanner.SubView;
import com.free.banner.hbanner.VideoSubView;
import com.free.banner.hbanner.VideoViewType;
import com.free.banner.transformer.DefaultTransformer;
import com.free.banner.transformer.VerticalPageTransformer;
import com.free.banner.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * hbanner 调用示例
 * @author lake
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    private Button add, remove, play, release;
    private BannerViewPager viewPager, viewPager2;
    private HBanner hBanner, hBanner2;
    private View rootView;

    public String[] NEEDED_PERMISSIONS = {
            Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this)
                .inflate(R.layout.activity_main, null);
        setContentView(rootView);
        viewPager = findViewById(R.id.viewpager);
        viewPager2 = findViewById(R.id.viewpager2);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        play = findViewById(R.id.play);
        release = findViewById(R.id.release);

        add.setOnClickListener(this);
        remove.setOnClickListener(this);
        play.setOnClickListener(this);
        release.setOnClickListener(this);
    }

    @Override
    public void onGlobalLayout() {
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        verifyStoragePermissions(this);
    }

    @Override
    protected void afterPermissions() {
        init();
    }

    @Override
    protected String[] needPermissions() {
        return NEEDED_PERMISSIONS;
    }


    private void init() {
        /**
         * 在create banner前需要确保viewpager控件已经被创建
         * 这里是双viewpager，为了方便所以直接对根布局进行视图创建
         * 进行回调
         */
        hBanner = HBanner.create(viewPager);

        List<SubView> data = new ArrayList<>();
        data.add(new ImageSubView.Builder(getBaseContext())
                .url("https://scpic.chinaz.net/files/pic/pic9/201311/apic2098.jpg")
                //.resId(R.mipmap.b1)
                .gravity(ImageView.ScaleType.FIT_XY)
                .duration(6000)
                .build());
        data.add(new ImageSubView.Builder(getBaseContext())
                .resId(R.mipmap.b2)
                .duration(5000)
                .build());
        data.add(new ImageSubView.Builder(getBaseContext())
                .resId(R.mipmap.b3)
                .duration(5000)
                .build());
        data.add(new VideoSubView.Builder(getBaseContext())
                .url("https://media.w3.org/2010/05/sintel/trailer.mp4")
                .gravity(VideoViewType.FULL)
                //让缓存图片显示600ms后再播放，可解决videoview启动时候闪烁问题
                .playOffset(600)
                .isSub(false)
                .build());
        hBanner.sources(data);
        //设置viewpager切换方式
        viewPager.setPageTransformer(true, new VerticalPageTransformer());

        //开始显示或者自动播放
        hBanner.play(true);
    }

    /**
     * 初始化第二个banner
     */
    private void init2() {
        hBanner2 = HBanner.create(viewPager2);
        List<SubView> data2 = new ArrayList<>();
        //被同步banner无需设置时间
        data2.add(new ImageSubView.Builder(getBaseContext())
                .url("https://image.playgame.wiki/2020/04/01/7ff9c9c64c/8a37fa61dc208e51c558c087173b80a8.jpg")
                .build());
        data2.add(new ImageSubView.Builder(getBaseContext())
                .resId(R.mipmap.b1)
                .build());
        data2.add(new ImageSubView.Builder(getBaseContext())
                .resId(R.mipmap.b3)
                .build());
        data2.add(new VideoSubView.Builder(getBaseContext())
                .url("http://vjs.zencdn.net/v/oceans.mp4")
                .isSub(true)
                .build());

        hBanner2.sources(data2);
    }

    /**
     * 同步测试
     */
    private void syncTest() {
        hBanner.addSyncHBanner(hBanner2);
    }

    @Override
    protected void onResume() {
        if (hBanner != null) {
            hBanner.play(true);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (hBanner != null) {
            hBanner.pause(0);
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                hBanner.addSubView(0, new ImageSubView.Builder(getBaseContext())
                        .resId(R.mipmap.defalteimage)
                        .duration(5000)
                        .build());
                hBanner.play(true);
                break;
            case R.id.remove:
//                hBanner.removeSyncHBanner(hBanner2);
//                hBanner.play(true);
                break;
            case R.id.play:
                init();
                break;
            case R.id.release:
                hBanner.release();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        if (hBanner != null) {
            hBanner = null;
        }
        super.onStop();
    }
}
