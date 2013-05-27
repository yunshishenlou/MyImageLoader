
package org.denny.myimageloader.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;

public class ImageLoaderFactory {
    private static AbsImageLoader mImageLoader = null;

    public static AbsImageLoader getImageLoader(Class clss, Context context) {
        synchronized (ImageLoaderFactory.class) {
            try {
                if (mImageLoader == null || !(clss.isInstance(mImageLoader))) {
                    Constructor constructor = clss.getConstructor(Context.class);
                    mImageLoader = (AbsImageLoader) constructor.newInstance(context);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return mImageLoader;
    }

}
