package com.example.td_advert.animation;

import java.util.ArrayList;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MultiAnimationExecutor implements IPlayable, AnimationListener {
	private ArrayList<IPlayable> playableList = new ArrayList<IPlayable>();

	public void addPlayable(IPlayable iPlayable) {
		playableList.add(iPlayable);
	}

	@Override
	public void play() {
		for (IPlayable iPlayable: playableList){
			iPlayable.play();			
		}

	}

	@Override
	public void onAnimationEnd(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}
}
