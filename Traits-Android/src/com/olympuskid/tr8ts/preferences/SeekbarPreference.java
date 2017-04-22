package com.olympuskid.tr8ts.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import com.olympuskid.tr8ts.R;

public class SeekbarPreference extends DialogPreference {

	public SeekbarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.seekbar_preference);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);
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
