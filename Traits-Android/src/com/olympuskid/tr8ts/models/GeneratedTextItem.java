package com.olympuskid.tr8ts.models;

public class GeneratedTextItem {
	private boolean isValid;
	private String generatedText;

	public GeneratedTextItem(boolean isValid, String generatedText) {
		this.isValid = isValid;
		this.generatedText = generatedText;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getGeneratedText() {
		return generatedText;
	}

	public void setGeneratedText(String generatedText) {
		this.generatedText = generatedText;
	}
}
