package com.olympuskid.tr8ts.listeners;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.olympuskid.tr8ts.MainActivity;

public class AdmobEventListener extends AdListener {
	private MainActivity mainActivity;
	public static boolean isAdLoaded = false;

	public AdmobEventListener(Context _context) {
		mainActivity = (MainActivity) _context;
	}

	// Called when an ad is received.
	@Override
	public void onAdLoaded() {
		mainActivity.renderAdView(true);
		isAdLoaded = true;
		super.onAdLoaded();
	}

	// Called when an ad request failed.
	@Override
	public void onAdFailedToLoad(int errorCode) {
		@SuppressWarnings("unused")
		String errorReason = "";
		switch (errorCode) {
		case AdRequest.ERROR_CODE_INTERNAL_ERROR:
			errorReason = "Internal error";
			break;
		case AdRequest.ERROR_CODE_INVALID_REQUEST:
			errorReason = "Invalid request";
			break;
		case AdRequest.ERROR_CODE_NETWORK_ERROR:
			errorReason = "Network Error";
			break;
		case AdRequest.ERROR_CODE_NO_FILL:
			errorReason = "No fill";
			break;
		}
		// hide view here
		mainActivity.renderAdView(false);
		super.onAdFailedToLoad(errorCode);
	}

	// Called when an ad opens an overlay that covers the screen.
	@Override
	public void onAdOpened() {
		super.onAdOpened();
	}

	// Called when the user is about to return to the application after clicking on an ad.
	@Override
	public void onAdClosed() {
		super.onAdClosed();
	}

	// Called when an ad leaves the application (e.g., to go to the browser).
	@Override
	public void onAdLeftApplication() {
		super.onAdLeftApplication();
	}
}
