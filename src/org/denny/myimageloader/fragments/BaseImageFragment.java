package org.denny.myimageloader.fragments;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.denny.myimageloader.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseImageFragment extends Fragment{
    protected DisplayImageOptions mDisplayOptions = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayOptions = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.empty_photo)
        .cacheInMemory()
        .cacheOnDisc()
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
    }

}
