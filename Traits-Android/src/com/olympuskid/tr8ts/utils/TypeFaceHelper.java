package com.olympuskid.tr8ts.utils;

import android.graphics.Color;
import android.graphics.Typeface;

import com.olympuskid.tr8ts.utils.objects.ThemeColor;

public final class TypeFaceHelper {

	public static String getTypeFaceStyleValue(Integer index) {
		String typeFace = "";
		switch (index) {
		case 0:
			typeFace = "Normal";
			break;
		case 1:
			typeFace = "Italic";
			break;
		case 2:
			typeFace = "Bold";
			break;
		case 3:
			typeFace = "Bold Italic";
			break;
		default:
			break;
		}

		return typeFace;
	}

	public static String getTypeFaceValue(Integer index) {
		String typeFace = "";
		switch (index) {
		case 0:
			typeFace = "Bold";
			break;
		case 1:
			typeFace = "Default";
			break;
		case 2:
			typeFace = "Monospace";
			break;
		case 3:
			typeFace = "San Serif";
			break;
		case 4:
			typeFace = "Serif";
			break;
		case 5:
			typeFace = "Default";
			break;
		default:
			break;
		}

		return typeFace;
	}

	public static Integer getTypeFaceStyle(Integer index) {
		Integer typeFaceStyle = 1;
		switch (index) {
		case 0:
			typeFaceStyle = Typeface.NORMAL;
			break;
		case 1:
			typeFaceStyle = Typeface.ITALIC;
			break;
		case 2:
			typeFaceStyle = Typeface.BOLD;
			break;
		case 3:
			typeFaceStyle = Typeface.BOLD_ITALIC;
			break;
		default:
			break;
		}

		return typeFaceStyle;
	}

	public static Typeface getTypeFace(Integer index) {
		Typeface typeFace = Typeface.DEFAULT;

		switch (index) {

		case 0:
			typeFace = Typeface.DEFAULT_BOLD;
			break;
		case 1:
			typeFace = Typeface.DEFAULT;
			break;
		case 2:
			typeFace = Typeface.MONOSPACE;
			break;

		case 3:
			typeFace = Typeface.SANS_SERIF;
			break;
		case 4:
			typeFace = Typeface.SERIF;
			break;
		default:
			typeFace = Typeface.DEFAULT;
			break;

		}

		return typeFace;
	}

	public static int getFontSizeIndex(Integer fontSize) {
		Integer fontSizeIndex = 1;
		switch (fontSize) {
		case 8:
			fontSizeIndex = 0;
			break;
		case 10:
			fontSizeIndex = 1;
			break;
		case 12:
			fontSizeIndex = 2;
			break;
		case 14:
			fontSizeIndex = 3;
			break;
		case 16:
			fontSizeIndex = 4;
			break;
		case 18:
			fontSizeIndex = 5;
			break;
		case 20:
			fontSizeIndex = 6;
			break;
		case 22:
			fontSizeIndex = 7;
			break;
		case 24:
			fontSizeIndex = 8;
			break;
		case 26:
			fontSizeIndex = 9;
			break;
		case 28:
			fontSizeIndex = 10;
			break;
		case 30:
			fontSizeIndex = 11;
			break;
		case 32:
			fontSizeIndex = 12;
			break;
		case 34:
			fontSizeIndex = 13;
			break;
		case 36:
			fontSizeIndex = 14;
			break;
		default:
			break;
		}

		return fontSizeIndex;

	}

	public static int getTypeFace(Typeface typeface) {

		if (typeface == Typeface.DEFAULT_BOLD) {

			return 0;
		} else if (typeface == Typeface.DEFAULT) {
			return 1;

		} else if (typeface == Typeface.MONOSPACE) {
			return 2;
		} else if (typeface == Typeface.SANS_SERIF) {
			return 3;
		} else if (typeface == Typeface.SERIF) {
			return 4;
		} else {
			return 0;
		}

	}

	public static String getThemeValue(Integer theme) {
		String value = "White Black";

		switch (theme) {
		case 0:
			value = "White Black";
			break;
		case 1:
			value = "Black White";
			break;
		case 2:
			value = "White Red";
			break;
		case 3:
			value = "Red White";
			break;
		case 4:
			value = "White Green";
			break;
		case 5:
			value = "Green White";
			break;
		case 6:
			value = "White Blue";
			break;
		case 7:
			value = "Blue White";
			break;
		case 8:
			value = "White Purple";
			break;
		case 9:
			value = "Purple White";
			break;
		case 10:
			value = "White Orange";
			break;
		case 11:
			value = "Orange White";
			break;
		case 100:
			value = "Random Art";
			break;
		default:
			break;
		}

		return value;
	}

	public static ThemeColor getThemeColors(Integer theme) {
		int font = Color.WHITE;
		int shadow = Color.BLACK;

		switch (theme) {
		case 0:
			font = Color.WHITE;
			shadow = Color.BLACK;
			break;
		case 1:
			font = Color.BLACK;
			shadow = Color.WHITE;
			break;
		case 2:
			font = Color.WHITE;
			shadow = Color.RED;
			break;
		case 3:
			font = Color.RED;
			shadow = Color.WHITE;
			break;
		case 4:
			font = Color.WHITE;
			shadow = Color.rgb(0, 202, 0);
			break;
		case 5:
			font = Color.rgb(0, 202, 0);
			shadow = Color.WHITE;
			break;
		case 6:
			font = Color.WHITE;
			shadow = Color.BLUE;
			break;
		case 7:
			font = Color.BLUE;
			shadow = Color.WHITE;
			break;
		case 8:
			font = Color.WHITE;
			shadow = Color.rgb(205, 0, 205);
			break;
		case 9:
			font = Color.rgb(205, 0, 205);
			shadow = Color.WHITE;
			break;
		case 10:
			font = Color.WHITE;
			shadow = Color.rgb(255, 128, 0);
			break;
		case 11:
			font = Color.rgb(255, 128, 0);
			shadow = Color.WHITE;
			break;
		case 100:
			font = Color.rgb(Utils.getRandomNumbersInRange(0, 255), Utils.getRandomNumbersInRange(0, 255), Utils.getRandomNumbersInRange(0, 255));
			shadow = Color.rgb(Utils.getRandomNumbersInRange(0, 255), Utils.getRandomNumbersInRange(0, 255), Utils.getRandomNumbersInRange(0, 255));
			break;
		default:
			break;
		}

		return new ThemeColor(font, shadow);
	}

}
