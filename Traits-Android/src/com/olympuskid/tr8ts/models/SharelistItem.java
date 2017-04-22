package com.olympuskid.tr8ts.models;

public class SharelistItem {
	private int imageResourceId;
	private String shareTitle;

	public SharelistItem(int imageResourceId, String shareTitle) {
		super();// no need as super is Object and java compiler automatically calls this
		this.imageResourceId = imageResourceId;
		this.shareTitle = shareTitle;
	}

	public int getImageResourceId() {
		return imageResourceId;
	}

	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
}
