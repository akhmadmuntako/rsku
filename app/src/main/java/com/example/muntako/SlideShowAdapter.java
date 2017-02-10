package com.example.muntako;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by akhmadmuntako on 09/02/2017.
 */
public class SlideShowAdapter extends PagerAdapter {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    List<Info_List> infos;
    Utama utama;



    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
