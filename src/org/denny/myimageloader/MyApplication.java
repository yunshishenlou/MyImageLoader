
package org.denny.myimageloader;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }

    private void initImageLoader(Context context) {
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.empty_photo)
        .cacheInMemory()
        .cacheOnDisc()
        .displayer(new RoundedBitmapDisplayer(5))
        .build();
        
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .enableLogging() 
                .defaultDisplayImageOptions(displayOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

}
