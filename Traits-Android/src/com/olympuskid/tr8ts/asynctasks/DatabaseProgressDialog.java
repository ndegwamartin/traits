package com.olympuskid.tr8ts.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.utils.TokenManager;
import com.olympuskid.tr8ts.utils.TraitManager;
import com.olympuskid.tr8ts.utils.objects.Tr8;

public class DatabaseProgressDialog extends DialogFragment {
	private static ProgressDialog progressBar = null;
	private int progressBarStatus = 0;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// prepare for a progress bar dialog
		if (progressBar != null && progressBar.isShowing()) {
			progressBar.dismiss();
		}
		progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(false);
		progressBar.setMessage(getString(R.string.loading));
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(progressBarStatus);
		progressBar.setMax(100);

		return progressBar;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance()) {

			getDialog().setDismissMessage(null);// bug in support frags
		}

		super.onDestroyView();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	// called once n only once
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		// set to not cancellable
		setCancelable(false);

		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(false);
		progressBar.setMessage(getString(R.string.loading));
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);

		// Create and execute the background task.

		DatabaseContentAsyncTask mTask = new DatabaseContentAsyncTask();
		mTask.execute();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private class DatabaseContentAsyncTask extends AsyncTask<Void, Integer, Void> {

		private TraitManager traitManager;
		private List<Tr8> tokens;

		@Override
		protected void onPreExecute() {

			try {
				traitManager = new TraitManager(getActivity(), true);
				progressBar.show();
				tokens = getTokens();
				traitManager.setTokenCount(tokens.size());
			} catch (Exception e) {
				Log.d("444", "Dbconetntasync");
			}

		}

		@Override
		protected Void doInBackground(Void... ignore) {
			try {

				for (int i = 0; i < tokens.size(); i++) {

					// loop to check if complete
					progressBarStatus = traitManager.setAsyncTrait(tokens.get(i));

					// Update the progress bar
					publishProgress(progressBarStatus);

				}

			} catch (Exception e) {

				Log.d("DatabaseContentAsyncTask Backgrund status error", e.getMessage());
			}
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... percent) {
			progressBar.setProgress(percent[0]);
		}

		@Override
		protected void onPostExecute(Void ignore) {
			if (progressBar != null && progressBar.isShowing()) {
				// close the progress bar dialog
				progressBar.dismiss();
			}

			try {
				traitManager.closeDB();
			} catch (Exception e) {
				Log.d("444", "On Post Execute, closing db");
			}
		}
	}

	private List<Tr8> getTokens() {
		List<Tr8> tr8s = new ArrayList<Tr8>();

		for (String tokName : TokenManager.getPositiveTokens()) {
			Tr8 t = new Tr8();
			t.setName(tokName);
			t.setPolarity(1);
			tr8s.add(t);
		}

		for (String tokName : TokenManager.getNegativeTokens()) {
			Tr8 t = new Tr8();
			t.setName(tokName);
			t.setPolarity(-1);
			tr8s.add(t);
		}

		for (String tokName : TokenManager.getNonPolarTokens()) {
			Tr8 t = new Tr8();
			t.setName(tokName);
			t.setPolarity(0);
			tr8s.add(t);
		}

		return tr8s;
	}
}
