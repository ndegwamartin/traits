package com.olympuskid.tr8ts.utils.objects;

import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class SpicOptions {
	private int color;
	private float textSize;
	private Style style;
	private int ShadowColor;
	private Typeface typeFace;
	private boolean isFontItalic;
	private float strokeWidth;
	private float textSkewX;
	private float textHorizontalScaleFactor;
	private boolean aPickedFont;
	private float XOffset;
	private float YOffset;

	public SpicOptions() {
		style = Style.FILL_AND_STROKE;
		textSize = 45;
		typeFace = Typeface.DEFAULT;
		strokeWidth = 4.5f;
		textSkewX = -0.25f;
		setTextHorizontalScaleFactor(1.0f);
		aPickedFont = false;
		XOffset = 0.0f;
		YOffset = 0.0f;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public int getShadowColor() {
		return ShadowColor;
	}

	public void setShadowColor(int shadowColor) {
		ShadowColor = shadowColor;
	}

	public Typeface getTypeFace() {
		return typeFace;
	}

	public void setTypeFace(Typeface typeFace) {
		this.typeFace = typeFace;
	}

	public boolean isFontItalic() {
		return isFontItalic;
	}

	public void setFontItalic(boolean isFontItalic) {
		this.isFontItalic = isFontItalic;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public float getTextSkewX() {
		return textSkewX;
	}

	public void setTextSkewX(float textSkewX) {
		this.textSkewX = textSkewX;
	}

	public float getTextHorizontalScaleFactor() {
		return textHorizontalScaleFactor;
	}

	public void setTextHorizontalScaleFactor(float textHorizontalScaleFactor) {
		this.textHorizontalScaleFactor = textHorizontalScaleFactor;
	}

	public boolean isAPickedFont() {
		return aPickedFont;
	}

	public void setIsAPickedFont(boolean aPickedFont) {
		this.aPickedFont = aPickedFont;
	}

	public float getYOffset() {
		return YOffset;
	}

	public void setYOffset(float yOffset) {
		YOffset = yOffset;
	}

	public float getXOffset() {
		return XOffset;
	}

	public void setXOffset(float xOffset) {
		XOffset = xOffset;
	}

}
