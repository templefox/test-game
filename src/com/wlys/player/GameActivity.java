package com.wlys.player;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class GameActivity extends AndroidApplication
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //����ʹ��opengl 2.0���ƣ� ��ΪͼƬ����2���ݴη�
        initialize(new GameCenter(), true);
    }

}
