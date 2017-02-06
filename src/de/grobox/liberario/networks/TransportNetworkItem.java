/*    Transportr
 *    Copyright (C) 2013 - 2017 Torsten Grote
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

package de.grobox.liberario.networks;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.mikepenz.fastadapter.commons.items.AbstractExpandableItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

import de.grobox.liberario.R;

class TransportNetworkItem extends AbstractExpandableItem<RegionItem, TransportNetworkViewHolder, TransportNetworkItem> {

	private final TransportNetwork network;
	private static final ViewHolderFactory<TransportNetworkViewHolder> FACTORY = new ItemFactory();

	TransportNetworkItem(TransportNetwork network) {
		super();
		this.network = network;
	}

	@IdRes
	@Override
	public int getType() {
		return R.id.list_item_transport_network;
	}

	@Override
	@LayoutRes
	public int getLayoutRes() {
		return R.layout.list_item_transport_network;
	}

	@Override
	public void bindView(TransportNetworkViewHolder ui, List<Object> payloads) {
		super.bindView(ui, payloads);
		ui.bind(network);
	}

	@Override
	public long getIdentifier() {
		return network.getId().ordinal();
	}

	TransportNetwork getTransportNetwork() {
		return network;
	}

	@Override
	public ViewHolderFactory<TransportNetworkViewHolder> getFactory() {
		return FACTORY;
	}

	private static class ItemFactory implements ViewHolderFactory<TransportNetworkViewHolder> {
		public TransportNetworkViewHolder create(View v) {
			return new TransportNetworkViewHolder(v);
		}
	}

}
