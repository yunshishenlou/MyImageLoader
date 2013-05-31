
package org.denny.myimageloader.manager;

import org.denny.myimageloader.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

public abstract class AbsImageLoader {
    protected final Context mContext;
    protected final Handler mUiHandler;
    protected static Bitmap PLACE_HOLDER_BITMAP = null;

    protected AbsImageLoader(Context context, Handler uiHandler) {
        this.mContext = context;
        this.mUiHandler = uiHandler;
    }

    protected Bitmap getPlaceHolderBitmap(int resId) {
        synchronized (AbsImageLoader.class) {
            if (PLACE_HOLDER_BITMAP == null) {
                PLACE_HOLDER_BITMAP = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.empty_photo);
            }
            return PLACE_HOLDER_BITMAP;
        }
    }
    public abstract void loadLocalImage(ImageEntry imageEntry, ImageView imv);

}
