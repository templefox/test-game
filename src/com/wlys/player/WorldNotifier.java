/*
 * Copyright 2011 Rod Hyde (rod@badlydrawngames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.wlys.player;

import com.badlogic.gdx.utils.Array;

public class WorldNotifier implements WorldListener
{

    private final Array<WorldListener> listeners;

    public WorldNotifier()
    {
        listeners = new Array<WorldListener>();
    }

    public void addListener(WorldListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void onPlayerFired(Player player)
    {
        for (WorldListener listener : listeners)
        {
            listener.onPlayerFired(player);
        }
    }

    @Override
    public void onPlayerHit(Player player)
    {
        for (WorldListener listener : listeners)
        {
            listener.onPlayerHit(player);
        }
    }

    @Override
    public void onEnemyFired(Enemy enemy)
    {
        for (WorldListener listener : listeners)
        {
            listener.onEnemyFired(enemy);
        }
    }

    @Override
    public void onEnemyDestroyed(Enemy enemy)
    {
        for (WorldListener listener : listeners)
        {
            listener.onEnemyDestroyed(enemy);
        }
    }

}
