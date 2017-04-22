/**
 * 
 */
package com.olympuskid.tr8ts.adapters;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.db.models.Trait;
import com.olympuskid.tr8ts.dialog.AlertDialogActivity;
import com.olympuskid.tr8ts.preferences.SearchableTraitPreference;
import com.olympuskid.tr8ts.utils.Utils;

/**
 * @author Olympus Kid
 * 
 */
public class SearchableTraitsAdapter extends BaseAdapter {
	Context ctx = null;
	List<Trait> traitsArray = null;
	protected SearchableTraitPreference searchableTraitsPreferenceScreen;

	public SearchableTraitsAdapter(Context activty, List<Trait> list, SearchableTraitPreference dialogPrefScreen) {
		this.ctx = activty;
		this.traitsArray = list;
		searchableTraitsPreferenceScreen = dialogPrefScreen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return traitsArray.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Trait getItem(int position) {
		return traitsArray.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return traitsArray.get(position).getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.searchable_trait_row, null);

			holder.titlename = (TextView) convertView.findViewById(R.id.tView_name);
			holder.editButton = (ImageButton) convertView.findViewById(R.id.btn_edit);
			holder.deleteButton = (ImageButton) convertView.findViewById(R.id.btn_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String polarity = "";

		if (getItem(position).getPolarity() != 0) {
			polarity = getItem(position).getPolarity() == 1 ? "+" : "-";
		}
		holder.titlename.setText(polarity + " " + Utils.captializeFirstLetter(getItem(position).getName()));
		holder.deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ctx instanceof AlertDialogActivity) {
					if (((AlertDialogActivity) ctx).deleteCustomTrait(getItemId(position))) {
						traitsArray.remove(position);
						refreshList();
					}
				}
			}
		});
		holder.editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ctx instanceof AlertDialogActivity) {
					final EditText input = new EditText(ctx);
					input.setText(getItem(position).getName());

					new AlertDialog.Builder(ctx).setTitle(ctx.getResources().getString(R.string.edit_trait)).setView(input)
							.setPositiveButton(ctx.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int whichButton) {
									Editable value = input.getText();
									if (((AlertDialogActivity) ctx).editCustomTrait(getItemId(position), value.toString())) {
										getItem(position).setName(value.toString());
										refreshList();
									}
								}
							}).setNegativeButton(ctx.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int whichButton) {
									// Do nothing.
								}
							}).show();

				}
			}
		});

		return convertView;
	}

	protected static class ViewHolder {
		TextView titlename;
		ImageButton deleteButton;
		ImageButton editButton;
	}

	public interface SearchableTraitListActionInterface {

		public void refreshList();

	}

	private void refreshList() {

		searchableTraitsPreferenceScreen.refreshList();

	}

}
