package com.example.td_advert.animation;

import java.util.ArrayList;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimationSequence implements IPlayable, AnimationListener {
	private ArrayList<IPlayable> playableList = new ArrayList<IPlayable>();
	private int index = 0;

	public void addPlayable(IPlayable iPlayable) {
		playableList.add(iPlayable);
	}

	@Override
	public void play() {
		if (playableList != null && playableList.size() > 0
				&& index < playableList.size()) {
			IPlayable iPlayable = playableList.get(index);
			iPlayable.play();
			index++;
		}else{
			end();
		}

	}

	@Override
	public void onAnimationEnd(Animation animation) {
        play();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void end() {
		
	}
}
