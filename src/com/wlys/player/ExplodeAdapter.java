package com.wlys.player;


public class ExplodeAdapter implements WorldListener
{

    GameScreen gameScreen;

    public ExplodeAdapter(GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;
    }

    @Override
    public void onPlayerFired(Player player)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerHit(Player player)
    {

    }

    @Override
    public void onEnemyFired(Enemy enemy)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnemyDestroyed(Enemy enemy)
    {
        gameScreen.addExplode(enemy.getX(), enemy.getY());
        gameScreen.addScore(50);
    }

}
