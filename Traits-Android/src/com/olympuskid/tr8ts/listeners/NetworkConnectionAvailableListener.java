package com.olympuskid.tr8ts.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.olympuskid.tr8ts.MainActivity;

public class NetworkConnectionAvailableListener extends BroadcastReceiver {
	private MainActivity mainActivity;

	@Override
	public void onReceive(Context _context, Intent intent) {

		mainActivity = (MainActivity) _context;

		final NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		final boolean isOnline = (networkInfo != null && networkInfo.isConnected());

		mainActivity.renderAdView(isOnline);

	};
}
