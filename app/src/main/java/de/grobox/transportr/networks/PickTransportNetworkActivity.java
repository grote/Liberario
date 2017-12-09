/*
 *    Transportr
 *
 *    Copyright (c) 2013 - 2017 Torsten Grote
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

package de.grobox.transportr.networks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.common.collect.RowSortedTable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Inject;

import de.grobox.transportr.R;
import de.grobox.transportr.TransportrActivity;
import de.grobox.transportr.map.MapActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ParametersAreNonnullByDefault
public class PickTransportNetworkActivity extends TransportrActivity implements ISelectionListener<IItem> {

	public final static String FORCE_NETWORK_SELECTION = "ForceNetworkSelection";

	@Inject
	TransportNetworkManager manager;
	private FastItemAdapter<IItem> adapter;
	private ExpandableExtension<IItem> expandableExtension;
	private RecyclerView list;
	private boolean firstStart, selectAllowed = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_transport_network);
		getComponent().inject(this);
		setUpCustomToolbar(false);

		Intent intent = getIntent();
		ActionBar actionBar = getSupportActionBar();
		if (intent.getBooleanExtra(FORCE_NETWORK_SELECTION, false)) {
			firstStart = true;
			if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
			findViewById(R.id.firstRunTextView).setVisibility(VISIBLE);
		} else {
			firstStart = false;
			if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
			findViewById(R.id.firstRunTextView).setVisibility(GONE);
		}
		setResult(RESULT_CANCELED);


		adapter = new FastItemAdapter<>();
		adapter.withSelectable(true);
		adapter.getItemAdapter().withComparator(new RegionItem.RegionComparator(this));
		adapter.withSelectionListener(this);
		expandableExtension = new ExpandableExtension<>();
		adapter.addExtension(expandableExtension);
		list = findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(adapter);

		Map<Region, List<Region>> regions = getRegionsByRegion();
		for (Continent continent : Continent.values()) {
			if (!regions.containsKey(continent)) {
				continue;
			}
			ContinentItem continentItem = new ContinentItem(continent);
			List<Region> subRegions = regions.get(continent);
			List<RegionItem> subRegionItems = new ArrayList<>(subRegions.size());
			for (Region subRegion : subRegions) {
				if (subRegion instanceof Country) {
					Country country = (Country)subRegion;
					if (!regions.containsKey(country)) {
						continue;
					}
					CountryItem countryItem = new CountryItem(country);
					List<Region> networks = regions.get(country);
					List<RegionItem> networkItems = new ArrayList<>(networks.size());
					for (Region network : networks) {
						if (!(network instanceof TransportNetwork))
							continue;
						networkItems.add(new TransportNetworkItem((TransportNetwork)network));
					}
					countryItem.withSubItems(networkItems);
					subRegionItems.add(countryItem);
				} else if (subRegion instanceof TransportNetwork) {
					TransportNetwork network = (TransportNetwork)subRegion;
					subRegionItems.add(new TransportNetworkItem(network));
				}
			}

			Collections.sort(subRegionItems, new RegionItem.RegionComparator(this));
			continentItem.withSubItems(subRegionItems);
			adapter.add(continentItem);

		}
		if (savedInstanceState != null) adapter.withSavedInstanceState(savedInstanceState);

		selectItem();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onSelectionChanged(@Nullable IItem item, boolean selected) {
		if (item == null || !selectAllowed || !selected) return;
		selectAllowed = false;
		TransportNetworkItem networkItem = (TransportNetworkItem) item;
		manager.setTransportNetwork(networkItem.getTransportNetwork());
		setResult(RESULT_OK);
		if (firstStart) { // MapActivity was closed
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		}
		supportFinishAfterTransition();
	}

	private void selectItem() {
		TransportNetwork network = manager.getTransportNetwork().getValue();
		if (network == null) {
			selectAllowed = true;
			return;
		}
		int pos = -1;
		Region region = network.getRegion();
		Continent continent = null;
		Country country = null;
		if (region instanceof Country) {
			country = (Country)region;
			continent = country.getContinent();
		} else if (region instanceof Continent) {
			continent = (Continent)region;
		}
		pos = adapter.getPosition(new ContinentItem(continent));
		if (pos != -1) {
			expandableExtension.expand(pos);
			if (country != null) {
				pos = adapter.getPosition(new CountryItem(country));
				if (pos != -1) {
					expandableExtension.expand(pos);
				}
			}
			pos = adapter.getPosition(new TransportNetworkItem(network));
			if (pos != -1) {
				adapter.select(pos, false);
				list.scrollToPosition(pos);
				list.smoothScrollBy(0, -75);
				selectAllowed = true;
			}
		}
	}
	
	private HashMap<Region, List<Region>> getRegionsByRegion() {

		HashMap<Region, List<Region>> regions = new HashMap<>();
		for (TransportNetwork n : TransportNetworks.networks) {
			Region region = n.getRegion();
			Country country;
			Continent continent;
			if (region instanceof Country) {
				country = (Country)region;
				continent = country.getContinent();

				List<Region> countries = new ArrayList<>();
				List<Region> networks = new ArrayList<>();

				if (regions.containsKey(continent))
					countries = regions.get(continent);
				if (!countries.contains(country))
					countries.add(country);
				if (!regions.containsKey(continent))
					regions.put(continent, countries);

				if (regions.containsKey(country)) {
					networks = regions.get(country);
				}
				networks.add(n);
				if (!regions.containsKey(country)) {
					regions.put(country, networks);
				}

			} else if (region instanceof Continent) {
				continent = (Continent)region;

				List<Region> networks = new ArrayList<>();

				if (regions.containsKey(continent)) {
					networks = regions.get(continent);
				}
				networks.add(n);
				if (!regions.containsKey(continent)) {
					regions.put(continent, networks);
				}
			} else {
				continue;
			}
		}
		return regions;
	}

}
