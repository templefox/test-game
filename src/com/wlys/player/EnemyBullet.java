package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class EnemyBullet extends GameObject
{
    private Texture texture;
    private float stateTime;
    private Vector2 speed;

    public EnemyBullet()
    {
        super();
        texture = new Texture(
                Gdx.files.internal("gfx/main/meci_neprijatelji.png"));

        setWidth(texture.getWidth() / 2f);
        setHeight(texture.getHeight());

        speed = new Vector2(0, -Gdx.graphics.getHeight() / 3f);

        Array<Rectangle> runs = new Array<Rectangle>();
        runs.add(new Rectangle(getWidth() / 4f, getHeight() / 4f,
                getWidth() / 2f, getHeight() / 2f));
        geometry = new CollisionGeometry(runs);
    }

    public void update()
    {
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > 10f)
        {
            stateTime = 0;
        }
        translate(0, speed.y * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void setInCollision(boolean inCollision)
    {
        super.setInCollision(inCollision);
        if (inCollision)
        {
            setDemandRemove(true);
        }
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, getX(), getY());
    }

    public void clear()
    {
        texture.dispose();
    }
}
