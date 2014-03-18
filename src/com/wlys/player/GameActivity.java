package com.wlys.player;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameActivity extends AndroidApplication
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //这里使用opengl 2.0绘制， 因为图片不是2的幂次方
        initialize(new GameCenter(), true);
    }

}
