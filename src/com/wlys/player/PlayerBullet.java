package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PlayerBullet extends GameObject
{
    private Texture texture;

    private Animation animation;

    private Vector2 speed;

    public PlayerBullet()
    {
        super();
        texture = new Texture(Gdx.files.internal("gfx/main/meci.png"));
        TextureRegion[] normalRegionArray = TextureRegion.split(texture,
                texture.getWidth() / 2, texture.getHeight())[0];

        animation = new Animation(0.4f, normalRegionArray);
        animation.setPlayMode(Animation.LOOP);

        setWidth(35f);
        setHeight(55f);

        speed = new Vector2(0, Config.HEIGHT / 2f);

        Array<Rectangle> runs = new Array<Rectangle>();
        runs.add(new Rectangle(0, 0, getWidth(), getHeight()));
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
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        batch.draw(keyFrame, getX(), getY(), 0, 0, getWidth(), getHeight(), 1,
                1, 0);
    }

    public void clear()
    {
        texture.dispose();
    }

}
