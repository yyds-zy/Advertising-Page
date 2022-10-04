package com.free.banner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.free.banner.adapter.ADBannerAdapter;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ADBannerAdapter adapter;
    private Banner banner;
    private List<ADBannerAdapter.BannerListBean> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = findViewById(R.id.banner);
        initData();
        initBanner();
    }

    private void initData() {
        urlList.clear();
        ADBannerAdapter.BannerListBean bannerListBean = new ADBannerAdapter.BannerListBean();
        bannerListBean.setUrl("https://media.w3.org/2010/05/sintel/trailer.mp4");
        bannerListBean.setVedio(true);
        urlList.add(bannerListBean);

        ADBannerAdapter.BannerListBean bannerListBean2 = new ADBannerAdapter.BannerListBean();
        bannerListBean2.setUrl("https://scpic.chinaz.net/files/pic/pic9/201311/apic2098.jpg");
        bannerListBean2.setVedio(false);
        urlList.add(bannerListBean2);
    }

    private void initBanner() {
        adapter = new ADBannerAdapter(this, urlList);
        banner.addBannerLifecycleObserver(this)
                .setStartPosition(0)
                .setAdapter(adapter, false)
                .isAutoLoop(true)
                .setLoopTime(5000)
                .setIndicator(new CircleIndicator(this));
        banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TGA", "" + position);
                //这里是只又一个视频的情况，如果滑动后不是第一个，释放掉视频
                if (position != 0) {
                    adapter.setVideoStop();
                }
                //如果滑动还有页数角标的需求，还可以在这边更新角标
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.setVideoStop();
            adapter = null;
        }
    }
}
