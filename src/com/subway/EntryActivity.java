package com.subway;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class EntryActivity extends AndroidApplication{
	GameCenter gameCenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		gameCenter = new GameCenter(this);
		initialize(gameCenter, true); 
	}
}
