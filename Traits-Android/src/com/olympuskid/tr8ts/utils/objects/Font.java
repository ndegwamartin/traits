package com.olympuskid.tr8ts.utils.objects;

import android.graphics.Typeface;

public class Font {
	private boolean isItalic = false;

	public Font(Typeface typeface, Integer typefaceStyle) {
		this.typeface = typeface;
		this.typefaceStyle = typefaceStyle;

		if (typefaceStyle.equals(2) || typefaceStyle.equals(3)) {

			isItalic = true;
		}

	}

	private Typeface typeface;
	private Integer typefaceStyle;

	public Integer getTypefaceStyle() {
		return typefaceStyle;
	}

	public void setTypefaceStyle(Integer typefaceStyle) {
		this.typefaceStyle = typefaceStyle;

		if (typefaceStyle.equals(2) || typefaceStyle.equals(3)) {

			isItalic = true;
		}
	}

	public Typeface getTypeface() {
		return typeface;
	}

	public void setTypeface(Typeface typeface) {
		this.typeface = typeface;
	}

	public boolean isItalic() {
		return isItalic;
	}

	public void setItalic(boolean isItalic) {
		this.isItalic = isItalic;
	}

}
