
package org.denny.myimageloader;

import org.denny.myimageloader.fragments.DetailFragment;
import org.denny.myimageloader.util.Constants;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        startDetailFragment();
    }

    private void startDetailFragment() {
        String tag = "detailFragment";
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(tag) == null) {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(getIntent().getExtras());
            fm.beginTransaction().add(android.R.id.content, detailFragment, tag).commit();
        }
    }

}
