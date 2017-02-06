package de.grobox.liberario.favorites;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import de.grobox.liberario.R;

abstract class AbstractFavoritesViewHolder extends RecyclerView.ViewHolder {

	protected final View layout;
	protected final ImageView icon;
	protected final ImageButton overflow;

	AbstractFavoritesViewHolder(View v) {
		super(v);
		layout = v;
		icon = (ImageView) v.findViewById(R.id.logo);
		overflow = (ImageButton) v.findViewById(R.id.overflowButton);
	}

	@CallSuper
	void onBind(final FavoritesItem item, final FavoriteListener listener) {
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onFavoriteClicked(item);
			}
		});
	}

}
