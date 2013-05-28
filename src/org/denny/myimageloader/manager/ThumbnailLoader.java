
package org.denny.myimageloader.manager;


import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

public class ThumbnailLoader extends AbsImageLoader {

    public ThumbnailLoader(Context context, Handler uiHandler) {
        super(context, uiHandler);
    }

    @Override
    public void loadLocalImage(final ImageEntry imageEntry, final ImageView imv) {
        ImageLoaderWorker worker = new ImageLoaderWorker(imv);
        worker.execute(imageEntry);
    }
}
