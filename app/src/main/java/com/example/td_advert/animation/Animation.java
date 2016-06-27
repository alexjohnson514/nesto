package com.example.td_advert.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class Animation implements IPlayable {
	private View target;
	private android.view.animation.Animation animation;

	public Animation(Context ctx, View target, int animationResId) {
		this(ctx, target, animationResId, null);

	}

	public Animation(Context ctx, View target, int animationResId,
			AnimationListener listener) {
		super();
		this.target = target;

		animation = AnimationUtils.loadAnimation(ctx.getApplicationContext(),
				animationResId);

		if (listener != null) {
			animation.setAnimationListener(listener);
		}

	}

	@Override
	public void play() {
		
		target.startAnimation(animation);
		target.setVisibility(View.VISIBLE);
	}

	@Override
	public void end() {
		
	}



}
