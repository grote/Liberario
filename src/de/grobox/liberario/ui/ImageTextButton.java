package de.grobox.liberario.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.AttributeSet;

import de.grobox.liberario.R;

public class ImageTextButton extends AppCompatButton {

	public ImageTextButton(Context context) {
		this(context, null);
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.buttonStyle);
	}

	public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		Drawable drawable = getCompoundDrawables()[1];
		drawable.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
		setCompoundDrawables(null, drawable, null, null);
	}

}
