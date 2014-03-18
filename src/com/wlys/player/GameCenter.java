package com.wlys.player;

import com.badlogic.gdx.Game;

public class GameCenter extends Game
{

    @Override
    public void create()
    {
        setScreen(new MenuScreen(this));
    }

}
