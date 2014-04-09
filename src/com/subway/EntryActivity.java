package com.subway;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class EntryActivity extends AndroidApplication{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		
		initialize(new GameCenter(), true); 
	}
}
