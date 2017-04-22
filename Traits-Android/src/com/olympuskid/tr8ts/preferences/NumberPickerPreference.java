package com.olympuskid.tr8ts.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.utils.Constants;
import com.olympuskid.tr8ts.utils.TypeFaceHelper;
import com.olympuskid.tr8ts.utils.Utils;

public class NumberPickerPreference extends DialogPreference {
	Spinner spinner = null;

	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.spinner);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			// persistBoolean(!getPersistedBoolean(true));
			persistString(Utils.getString(spinner.getSelectedItem()));
		}
		Log.d("Dialog Pref", "# onDialogClosed: " + positiveResult);
	}

	@Override
	protected void onBindDialogView(View view) {

		spinner = (Spinner) view.findViewById(R.id.spinner);
		spinner.setSelection(TypeFaceHelper.getFontSizeIndex(Utils.getInt(Utils.ReadPrefString(this.getContext(), Constants.KEY_PREF_FONT_SIZE,
				Constants.DEFAULT_FONT_SIZE).replaceFirst("px", ""))));
		super.onBindDialogView(view);
	}
}
