package com.example.game;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new FirstGame(), false); 
	}
}
