
package org.denny.myimageloader.manager;

import org.denny.myimageloader.R;
import org.denny.myimageloader.util.AsyncTask;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

public class ThumbnailLoader extends AbsImageLoader {

    public ThumbnailLoader(Context context, Handler uiHandler) {
        super(context, uiHandler);
    }

    @Override
    public void loadLocalImage(final ImageEntry imageEntry, final ImageView imv) {
        if (BitmapWorkerTask.cancelPotentialWork(imageEntry, imv)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imv, imageEntry);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(),
                    getPlaceHolderBitmap(R.drawable.empty_photo), task);
            imv.setImageDrawable(asyncDrawable);
            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, imageEntry);
        }
    }
}
