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

package de.grobox.liberario.favorites;

import android.view.View;

import de.grobox.liberario.R;

class HomeFavoriteViewHolder extends SpecialFavoritesViewHolder {

	HomeFavoriteViewHolder(View v) {
		super(v);
	}

	@Override
	void onBind(FavoritesItem item, FavoriteListener listener) {
		super.onBind(item, listener);

		icon.setImageResource(R.drawable.ic_action_home);
		title.setText(R.string.home);
	}

}
