/**
 * 
 */
package com.olympuskid.tr8ts.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.olympuskid.tr8ts.R;

/**
 * @author Olympus Kid
 * 
 */
public class Utils {

	public static int getRandomNumbersInRange(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static boolean characterIsVowel(String xter) {
		if (xter == null)
			return false;

		xter = xter.trim().toLowerCase();
		if (xter.equals("a") || xter.equals("e") || xter.equals("i") || xter.equals("o") || xter.equals("u")) {

			return true;
		} else
			return false;

	}

	public static String formatedDate(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
		return dateFormat.format(new Date());

	}

	// Integer
	public static Integer ReadPrefInteger(Context context, final String key, final int defaultValue) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref.getInt(key, defaultValue);
	}

	public static void WritePrefInteger(Context context, final String key, final Integer value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();

	} // String

	public static String ReadPrefString(Context context, final String key, String defaultValue) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref.getString(key, defaultValue);
	}

	public static void WritePrefString(Context context, final String key, final String value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void ClearPrefString(Context context, final String key) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear().commit();
	}

	// Boolean
	public static boolean ReadPrefBoolean(Context context, final String key, final boolean defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static void WritePrefBoolean(Context context, final String key, final boolean value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public enum ORDER {
		ASC, DESC
	}

	// Number Helpers

	public static String ordinalNumber(Integer number) {
		if (number >= 11 && number <= 13) {
			return number + "th";
		}
		switch (number % 10) {
		case 1:
			return number + "st";
		case 2:
			return number + "nd";
		case 3:
			return number + "rd";
		default:
			return number + "th";
		}
	}

	public static Short getShort(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				Short Id = Short.parseShort(obj.toString());
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getShort " + e.getMessage());
			return null;
		}

	}

	public static Integer getInt(Object obj) {
		try {
			if (obj == null) {
				return 0;
			} else {
				Integer Id = Integer.parseInt(obj.toString());
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getInt " + e.getMessage());
			return null;
		}

	}

	public static Double getDouble(Object obj) {
		try {
			if (obj == null) {
				return new Double(0);
			} else {
				Double Id = Double.parseDouble(obj.toString());
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getInt " + e.getMessage());
			return null;
		}

	}

	/**
	 * Get Integer value. If the value is null or has an error, the default value is returned.
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return value
	 */
	public static Integer getInt(Object obj, Integer defaultValue) {
		Integer value = getIntOrNull(obj);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static Integer getIntOrNull(Object obj) {
		try {
			Integer id = null;
			if (obj != null) {
				id = Integer.parseInt(obj.toString());
			}

			return id;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Integer getIntOrError(Object obj) {
		Integer id = null;
		if (obj != null) {
			id = Integer.parseInt(obj.toString());
		}

		return id;
	}

	public static Float getFloat(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				Float Id = Float.parseFloat(obj.toString());
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getInt " + e.getMessage());
			return null;
		}

	}

	public static Long getLong(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				Double temp = Double.parseDouble(obj.toString());
				Long Id = temp.longValue();
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getLong " + e.getMessage());
			return null;
		}
	}

	public static Long getLong(Object obj, Long defaultReturn) {
		try {
			if (obj == null) {
				return defaultReturn;
			} else {
				Double temp = Double.parseDouble(obj.toString());
				Long Id = temp.longValue();
				return Id;
			}
		} catch (Exception e) {

			// logger.debug("Error:getLong " + e.getMessage());
			return defaultReturn;
		}
	}

	public static Long getLongOrError(Object obj) throws Exception {
		if (obj == null) {
			throw new Exception("The value is null");
		} else {
			Double temp = Double.parseDouble(obj.toString());
			Long Id = temp.longValue();
			return Id;
		}
	}

	public static String getStrings(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				String temp = String.valueOf(obj.toString());
				return temp;
			}
		} catch (Exception e) {

			// logger.debug("Error:getLong " + e.getMessage());
			return null;
		}
	}

	public static String getString(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				String id = obj.toString();
				return id;

			}
		} catch (Exception e) {

			return null;
		}
	}

	public static BigDecimal getBigDecimal(Object obj) {
		try {
			if (obj == null) {
				return null;
			} else {
				BigDecimal Id = BigDecimal.valueOf(Double.parseDouble(obj.toString()));
				return Id;
			}
		} catch (Exception e) {

			return null;
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}

	public static void showAlertMessage(Context context, String title, String message) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false).setIcon(R.drawable.traitsicon)
				.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public static void renderFlashMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}

	public static void renderFlashMessageLong(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();

	}

	public static String captializeFirstLetter(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
