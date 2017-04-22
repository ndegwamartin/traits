package com.olympuskid.tr8ts.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.R.drawable;
import com.olympuskid.tr8ts.utils.objects.Font;

public class FontPicker extends DialogFragment {
	public static final int ROW_HEIGHT = 80;
	public static final int DEFAULT_PADDING = 12;

	List<Integer> typeFaceStyle = new ArrayList<Integer>();

	List<Typeface> typeFaces = new ArrayList<Typeface>();

	private Context mContext; // The calling activities context.
	private Font mSelectedFont; // The font that was selected

	final List<Font> fonts = new ArrayList<Font>();

	// create callback method to bass back the selected font
	public interface FontPickerDialogListener {
		/**
		 * This method is called when a font is selected in the FontPickerDialog
		 * 
		 * @param dialog
		 *            The dialog used to pick the font. Use dialog.getSelectedFont() to access the pathname of the chosen font
		 */
		public void onFontSelected(FontPicker dialog);
	}

	// Use this instance of the interface to deliver action events
	FontPickerDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// FontPickerDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (FontPickerDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement FontPickerDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get context
		mContext = getActivity();

		if (fonts == null || fonts.size() < 1) {
			typeFaceStyle.add(Typeface.NORMAL);
			typeFaceStyle.add(Typeface.ITALIC);
			typeFaceStyle.add(Typeface.BOLD);
			typeFaceStyle.add(Typeface.BOLD_ITALIC);

			typeFaces.add(Typeface.DEFAULT_BOLD);
			typeFaces.add(Typeface.DEFAULT);
			typeFaces.add(Typeface.MONOSPACE);
			typeFaces.add(Typeface.SANS_SERIF);
			typeFaces.add(Typeface.SERIF);

			// add fonts to List
			Font font;
			for (int tfStyle : typeFaceStyle) {
				for (Typeface tFace : typeFaces) {

					font = new Font(tFace, tfStyle);

					fonts.add(font);

				}
			}
		}
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.font_picker_dialog, null));

		// set adapter to show fonts
		FontAdapter adapter = new FontAdapter(mContext);
		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {

				mSelectedFont = fonts.get(position);
				mListener.onFontSelected(FontPicker.this);
			}
		});

		builder.setTitle(mContext.getResources().getString(R.string.select_font_style));
		builder.setIcon(drawable.traitsicon);

		// Add the buttons
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// don't have to do anything on cancel
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		// TextView customTitleView = new TextView(this.mContext);
		// customTitleView.setText("Select Font");
		// customTitleView.setBackgroundResource(R.drawable.purple_btn);
		// dialog.setCustomTitle(customTitleView);

		return dialog;
	}

	public Font getSelectedFont() {
		return mSelectedFont;
	}

	/*
	 * Adapter to show the fonts in the dialog
	 */
	private class FontAdapter extends BaseAdapter {
		private Context mContext;

		public FontAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return fonts.size();
		}

		@Override
		public Object getItem(int position) {
			return fonts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// We use the position as ID
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) convertView;

			// This function may be called in two cases: a new view needs to be
			// created,
			// or an existing view needs to be reused
			if (view == null) {
				view = new TextView(mContext);
				view.setHeight(ROW_HEIGHT);
				view.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
				view.setTextSize(14);
				// view.setTextSize(view.getTextSize());
				view.setTextColor(getResources().getColor(R.color.purple));
				// view.setBackgroundResource(R.drawable.list_selector);

			} else {
				view = (TextView) convertView;
			}

			// Set text to be font name and written in font style

			Font mSelectedFont = fonts.get(position);
			view.setTypeface(mSelectedFont.getTypeface(), mSelectedFont.getTypefaceStyle());
			view.setText("Traits");

			return view;

		}
	}

}
