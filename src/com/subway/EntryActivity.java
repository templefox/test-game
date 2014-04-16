package com.subway;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class EntryActivity extends AndroidApplication{
	GameCenter gameCenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameCenter = new GameCenter();
		initialize(gameCenter, true); 
	}
}
