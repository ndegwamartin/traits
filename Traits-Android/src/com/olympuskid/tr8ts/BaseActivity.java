package com.olympuskid.tr8ts;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class BaseActivity extends FragmentActivity {

	public FragmentManager getMyFragmentManager() {
		FragmentManager mgr = getSupportFragmentManager();

		return mgr;

	}
}
