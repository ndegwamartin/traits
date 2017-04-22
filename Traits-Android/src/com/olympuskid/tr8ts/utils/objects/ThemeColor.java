package com.olympuskid.tr8ts.utils.objects;


public class ThemeColor {
	int fontColor;
	int fontShadowColor;

	public ThemeColor(int _fontColor, int _fontShadowColor) {
		fontColor = _fontColor;
		fontShadowColor = _fontShadowColor;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getFontShadowColor() {
		return fontShadowColor;
	}

	public void setFontShadowColor(int fontShadowColor) {
		this.fontShadowColor = fontShadowColor;
	}
}
