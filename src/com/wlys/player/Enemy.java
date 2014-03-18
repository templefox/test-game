package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy extends GameObject
{

    private GameScreen screen;
    private Texture texture;
    private Animation animation;
    private float stateTime;
    private Vector2 speed;
    private int fireNum;

    public Enemy(GameScreen screen)
    {
        super();
        this.screen = screen;
        texture = new Texture(Gdx.files.internal("gfx/main/enemy2.png"));
        TextureRegion[] regionArray = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight())[0];
        animation = new Animation(0.2f, regionArray);
        animation.setPlayMode(Animation.LOOP);

        setWidth(texture.getWidth() / 4f);
        setHeight(texture.getHeight());

        Array<Rectangle> runs = new Array<Rectangle>();
        runs.add(new Rectangle(getWidth() / 3f, 0, getWidth() / 3f, getHeight()));
        runs.add(new Rectangle(getWidth() / 3f, 0, getWidth() / 3f, getHeight()));
        runs.add(new Rectangle(getWidth() / 3f, 0, getWidth() / 3f, getHeight()));
        geometry = new CollisionGeometry(runs);
        speed = new Vector2();
    }

    public void update()
    {
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime < 3f)
        {
            speed.x = 0;
            speed.y = -50;
            if (fireNum == 0)
            {
                fire();
                fireNum++;
            }
        }
        else if (stateTime >= 3f && stateTime < 6f)
        {
            speed.x = 50;
            speed.y = 50;
            if (fireNum == 1)
            {
                fire();
                fireNum++;
            }
        }
        else if (stateTime >= 6f && stateTime < 9f)
        {
            speed.x = 0;
            speed.y = 50;
            if (fireNum == 2)
            {
                fire();
                fireNum++;
            }
        }
        else if (stateTime >= 9f && stateTime < 12f)
        {
            speed.x = -60;
            speed.y = -60;
            if (fireNum == 3)
            {
                fire();
                fireNum++;
            }
        }
        else
        {
            stateTime = 0;
            fireNum = 0;
        }
        translate(speed.x * Gdx.graphics.getDeltaTime(),
                speed.y * Gdx.graphics.getDeltaTime());
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
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        batch.draw(keyFrame, getX(), getY(), 0, 0, getWidth(), getHeight(), 1,
                1, 0);
    }

    private void fire()
    {
        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;
        screen.addEnemyBullet(centerX - getWidth() / 6f * 2, centerY);
        screen.addEnemyBullet(centerX + getWidth() / 6f * 2, centerY);
    }

    public void clear()
    {
        texture.dispose();
    }

}
