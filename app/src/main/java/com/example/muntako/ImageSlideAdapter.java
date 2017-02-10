package com.example.muntako;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    Activity activity;
    List<Info_List> infos;

    public ImageSlideAdapter(Activity activity, List<Info_List> infos) {
        this.activity = activity;
        this.infos = infos;
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.placeholder)
                .showStubImage(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder).cacheInMemory()
                .cacheOnDisc().build();
        imageListener = new ImageDisplayListener();
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);

        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();

                Log.d("position adapter", "" + position);
                /*Product product = (Product) products.get(position);
                arguments.putParcelable("singleProduct", product);

                // Start a new fragment
                fragment = new ProductDetailFragment();
                fragment.setArguments(arguments);

                FragmentTransaction transaction = activity
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment,
                        ProductDetailFragment.ARG_ITEM_ID);
                transaction.addToBackStack(ProductDetailFragment.ARG_ITEM_ID);
                transaction.commit();*/
            }
        });
        imageLoader.displayImage(
                ((Info_List) infos.get(position)).getGambar(),mImageView,options,imageListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}