/**
 * 
 */
package com.olympuskid.tr8ts.dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.olympuskid.tr8ts.MainActivity;
import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.db.models.Trait;
import com.olympuskid.tr8ts.preferences.SearchableTraitPreference;
import com.olympuskid.tr8ts.utils.Constants;
import com.olympuskid.tr8ts.utils.TraitManager;
import com.olympuskid.tr8ts.utils.TypeFaceHelper;
import com.olympuskid.tr8ts.utils.Utils;
import com.olympuskid.tr8ts.utils.objects.Tr8;

/**
 * @author Olympus Kid
 * 
 */
public class AlertDialogActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	SharedPreferences prefs;
	private TraitManager traitManager;

	@SuppressWarnings("deprecation")
	// called before onStart()
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		this.setTitle(getStringResource(R.string.settings));
		this.setTheme(R.style.traitsDialogTheme);
		// this.setTitleColor(getResources().getColor(R.color.white));

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Preference fontSizePref = findPreference(Constants.KEY_PREF_FONT_SIZE);
		fontSizePref.setSummary(Utils.getString(sharedPreferences.getString(Constants.KEY_PREF_FONT_SIZE, Constants.DEFAULT_FONT_SIZE)));

		Preference fontStylePref = findPreference(Constants.KEY_PREF_FONT_STYLE);
		fontStylePref.setSummary(TypeFaceHelper.getTypeFaceStyleValue(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_TYPEFACE_STYLE,
				Constants.TYPEFACE_STYLE_NORMAL))));

		Preference typeFacePref = findPreference(Constants.KEY_PREF_FONT_TYPEFACE);
		typeFacePref.setSummary(TypeFaceHelper.getTypeFaceValue(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_TYPEFACE,
				Constants.TYPEFACE_DEFAULT))));

		Preference typeFaceStylePref = findPreference(Constants.KEY_PREF_FONT_TYPEFACE_STYLE);
		typeFaceStylePref.setSummary(TypeFaceHelper.getTypeFaceStyleValue(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_TYPEFACE_STYLE,
				Constants.TYPEFACE_STYLE_NORMAL))));

		Preference typeFaceThemePref = findPreference(Constants.KEY_PREF_FONT_THEME);
		typeFaceThemePref.setSummary(TypeFaceHelper.getThemeValue(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_THEME,
				Constants.THEME_DEFAULT))));

		traitManager = new TraitManager(getApplicationContext(), false);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		// prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		Intent intent = new Intent(this, MainActivity.class);

		if (key.equals(Constants.KEY_PREF_FONT_SIZE)) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(sharedPreferences.getString(key, Constants.DEFAULT_FONT_SIZE));
			intent.putExtra(Constants.KEY_PREF_FONT_SIZE, sharedPreferences.getString(key, Constants.DEFAULT_FONT_SIZE));
		} else if (key.equals(Constants.KEY_PREF_NEW_TRAIT_POSITIVE) || key.equals(Constants.KEY_PREF_NEW_TRAIT_NEGATIVE)) {

			createCustomTrait(sharedPreferences, key);

		} else if (key.equals(Constants.KEY_PREF_FONT_THEME)) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(TypeFaceHelper.getThemeValue(Utils.getInt(sharedPreferences.getString(key, Constants.THEME_DEFAULT))));

			intent.putExtra(Constants.KEY_PREF_FONT_THEME, sharedPreferences.getString(key, Constants.THEME_DEFAULT));

		} else if (key.equals(Constants.KEY_PREF_FONT_TYPEFACE_STYLE)) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(TypeFaceHelper.getTypeFaceStyleValue(Utils.getInt(sharedPreferences.getString(key, Constants.TYPEFACE_STYLE_NORMAL))));

			// refresh parent header

			findPreference(Constants.KEY_PREF_FONT_STYLE).setSummary(
					TypeFaceHelper.getTypeFaceStyleValue(Utils.getInt(sharedPreferences.getString(key, Constants.TYPEFACE_STYLE_NORMAL))));

			intent.putExtra(Constants.KEY_PREF_FONT_TYPEFACE_STYLE, sharedPreferences.getString(key, Constants.TYPEFACE_STYLE_NORMAL));

		} else if (key.equals(Constants.KEY_PREF_FONT_TYPEFACE)) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(sharedPreferences.getString(key, Constants.TYPEFACE_DEFAULT));

			intent.putExtra(Constants.KEY_PREF_FONT_TYPEFACE, sharedPreferences.getString(key, Constants.TYPEFACE_DEFAULT));
		} else if (key.equals(Constants.KEY_PREF_MANAGE_CUSTOM_TRAITS)) {

		} else {
		}

		if (!key.equals(Constants.KEY_PREF_NEW_TRAIT_POSITIVE) && !key.equals(Constants.KEY_PREF_NEW_TRAIT_NEGATIVE)) {

			intent.putExtra("sharedPref", key);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			this.startActivity(intent);
		}
	}

	protected void createCustomTrait(SharedPreferences sharedPreferences, String key) {
		String trait = sharedPreferences.getString(key, "").trim();
		if (!trait.isEmpty()) {

			if (!Utils.isNumeric(trait.substring(0, 1))) {

				Tr8 t = new Tr8();
				t.setName(trait);
				t.setPolarity(key.equals(Constants.KEY_PREF_NEW_TRAIT_POSITIVE) ? 1 : -1);

				if (!traitManager.traitAlreadyExists(t)) {
					traitManager.addTrait(t, true);
					@SuppressWarnings("deprecation")
					SearchableTraitPreference pref = (SearchableTraitPreference) findPreference(Constants.KEY_PREF_MANAGE_CUSTOM_TRAITS);
					pref.hardRefreshList(null);
					Utils.renderFlashMessage(this, trait + " " + getStringResource(R.string.added));
				} else {

					Utils.renderFlashMessage(this, trait + " " + getStringResource(R.string.already_exists));
				}
				Utils.ClearPrefString(this, key);

			} else {

				Utils.ClearPrefString(this, key);
				Utils.showAlertMessage(this, getStringResource(R.string.add_new_trait), getStringResource(R.string.the_first_letter_shouldnt_be_number));

			}

		} else {
			Utils.ClearPrefString(this, key);
			Utils.showAlertMessage(this, getStringResource(R.string.add_new_trait), getStringResource(R.string.please_fill_in_a_value));
		}
	}

	/*
	 * SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
	 * 
	 * @Override public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { // listener implementation } };
	 */
	private String getStringResource(int id) {
		return getResources().getString((id));
	}

	public boolean deleteCustomTrait(Long id) {

		return traitManager.deleteTrait(id);

	}

	public boolean editCustomTrait(Long id, String name) {
		if (name == null || name.trim().isEmpty()) {
			return false;
		}

		Trait trait = traitManager.getTrait(id);
		trait.setName(name);
		return traitManager.updateTrait(trait);
	}

	@Override
	public void onDestroy() {

		traitManager.closeDB();
		super.onDestroy();
	}

}
