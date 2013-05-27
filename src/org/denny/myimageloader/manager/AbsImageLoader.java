
package org.denny.myimageloader.manager;



import android.content.Context;
import android.widget.ImageView;

public abstract class AbsImageLoader {
    protected final Context mContext;
    private static AbsImageLoader mImageLoader = null;

    protected AbsImageLoader(Context context) {
        this.mContext = context;
    }

    public abstract void loadLocalImage(ImageEntry imageEntry, ImageView imv);

}
