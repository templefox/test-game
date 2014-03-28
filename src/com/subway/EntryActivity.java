package com.subway;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.subway.system.MetroLine;
import com.subway.system.MetroNode;
import com.subway.system.MetroSystem;

public class EntryActivity extends AndroidApplication{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		
		initialize(new GameCenter(), true); 
	}
}
