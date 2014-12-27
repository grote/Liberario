/*    Liberario
 *    Copyright (C) 2013 Torsten Grote
 *
 *    This program is Free Software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.grobox.liberario;

import de.grobox.liberario.data.FavDB;
import de.schildbach.pte.dto.Location;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SetHomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.activity_set_home);

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_action_home);
		setTitle(getString(R.string.home_dialog_title));

		Intent intent = getIntent();

		// show new home text
		if(!intent.getBooleanExtra("new", true)) {
			findViewById(R.id.homeMsgView).setVisibility(View.GONE);
		}

		// home location TextView
		final AutoCompleteTextView homeView = (AutoCompleteTextView) findViewById(R.id.homeView);
		LocationAdapter locAdapter = new LocationAdapter(this, FavLocation.LOC_TYPE.FROM, true);
		locAdapter.setFavs(true);
		homeView.setAdapter(locAdapter);
		homeView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
				Location loc = (Location) parent.getItemAtPosition(position);
				homeView.setText(loc.uniqueShortName());
				homeView.setTag(loc);
				homeView.requestFocus();

				// hide soft-keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(homeView.getWindowToken(), 0);
			}
		});

		// clear from text button
		final ImageButton homeClearButton = (ImageButton) findViewById(R.id.homeClearButton);
		homeClearButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				homeView.setText("");
				homeView.requestFocus();
				homeView.setTag(null);
				homeClearButton.setVisibility(View.GONE);
			}
		});

		// When text changed
		homeView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// clear saved station
				homeView.setTag(null);

				// show clear button
				homeClearButton.setVisibility(View.VISIBLE);
			}
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});

		// station name favorites button
		findViewById(R.id.homeFavButton).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int size = ((LocationAdapter) homeView.getAdapter()).addFavs();

				if(size > 0) {
					homeView.showDropDown();
				}
				else {
					Toast.makeText(v.getContext(), getResources().getString(R.string.error_no_favs), Toast.LENGTH_SHORT).show();
				}
			}
		});

		// OK Button
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (homeView.getTag() != null && homeView.getTag() instanceof Location) {
					// save home location in file
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							FavDB.setHome(v.getContext(), (Location) homeView.getTag());
							return null;
						}
						
						@Override
						protected void onPostExecute(Void result) {
							Intent returnIntent = new Intent();
							setResult(RESULT_OK, returnIntent);

							close(v);
						}
					}.execute();
				} else {
					Toast.makeText(v.getContext(), getResources().getString(R.string.error_only_autocomplete_station), Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Cancel Button
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);

				close(v);
			}
		});
	}

	public void close(View v) {
		finish();
	}

}
