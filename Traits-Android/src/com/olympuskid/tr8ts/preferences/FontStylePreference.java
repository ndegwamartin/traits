package com.olympuskid.tr8ts.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

public class FontStylePreference extends DialogPreference {

	public FontStylePreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		/*
		 * BaseActivity ba = new BaseActivity(); FragmentManager manager = ba.getMyFragmentManager();
		 * 
		 * FontPicker picker = new FontPicker(); picker.show(manager, "fontPicker");
		 */
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			// persistBoolean(!getPersistedBoolean(true));
			persistString("Frack");
		}
		Log.d("Dialog Pref", "# onDialogClosed: " + positiveResult);
	}
}
