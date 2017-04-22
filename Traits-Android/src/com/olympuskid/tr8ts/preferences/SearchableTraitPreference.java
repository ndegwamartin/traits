package com.olympuskid.tr8ts.preferences;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.adapters.SearchableTraitsAdapter;
import com.olympuskid.tr8ts.adapters.SearchableTraitsAdapter.SearchableTraitListActionInterface;
import com.olympuskid.tr8ts.db.models.Trait;
import com.olympuskid.tr8ts.utils.TraitManager;

public class SearchableTraitPreference extends DialogPreference implements SearchableTraitListActionInterface {
	private List<Trait> customTraits;
	int textlength = 0;
	TraitManager traitManager;
	SearchableTraitPreference dialogPrefString;
	protected SearchableTraitsAdapter arrayAdapter;
	ListView listview = null;

	public SearchableTraitPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.share_dialog_layout);
		setPositiveButtonText(android.R.string.ok);

		setDialogIcon(R.drawable.traitsicon);
		traitManager = new TraitManager(getContext(), false);

		// task.execute((Void[]) null);
		customTraits = traitManager.getCustomTraits(null);

		dialogPrefString = this;
	}

	/** Hide the cancel button */
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);
		builder.setNegativeButton(null, null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			// persistBoolean(!getPersistedBoolean(true));
			// persistString(Utils.getString(spinner.getSelectedItem()));
		}
		Log.d("Dialog Pref", "# onDialogClosed: " + positiveResult);
	}

	@Override
	protected void onBindDialogView(View view) {

		// spinner = (Spinner) view.findViewById(R.id.spinner);
		// spinner.setSelection(TypeFaceHelper.getFontSizeIndex(Utils.getInt(Utils.ReadPrefString(this.getContext(), Constants.KEY_PREF_FONT_SIZE,
		// Constants.DEFAULT_FONT_SIZE).replaceFirst("px", ""))));
		super.onBindDialogView(view);
	}

	@Override
	public View onCreateDialogView() {
		final EditText editText = new EditText(getContext());
		editText.setHint(R.string.type_text_to);
		listview = new ListView(getContext());
		// editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clearpurple, 0, 0, 0);

		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(editText);
		layout.addView(listview);

		LinearLayout mainLayout = new LinearLayout(getContext());
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mainLayout.addView(layout);

		arrayAdapter = new SearchableTraitsAdapter(getContext(), customTraits, dialogPrefString);
		listview.setAdapter(arrayAdapter);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				textlength = editText.getText().length();
				hardRefreshList(editText.getText().toString());
			}

		});

		return mainLayout;
	}

	AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

		@Override
		protected void onPreExecute() {
			// showIndeterminateProgressBar(getStringResource(R.string.loading), getStringResource(R.string.please_wait));
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// openCamera();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// dismissProgressDialog();
		}

	};

	@Override
	public void refreshList() {
		if (listview != null && arrayAdapter != null) {
			listview.setAdapter(arrayAdapter);
			arrayAdapter.notifyDataSetChanged();
		}

	}

	public void hardRefreshList(String text) {

		customTraits.clear();
		customTraits = text != null ? traitManager.getCustomTraits(text.toLowerCase(Locale.US).trim()) : traitManager.getCustomTraits(null);
		arrayAdapter = new SearchableTraitsAdapter(getContext(), customTraits, dialogPrefString);
		if (listview != null) {
			listview.setAdapter(arrayAdapter);
		}
	}
}
