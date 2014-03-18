package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explode extends GameObject
{
    private Texture texture;
    private Animation animation;

    public Explode()
    {
        super();
        texture = new Texture(Gdx.files.internal("gfx/main/explosion2.png"));
        TextureRegion[][] explodeRegion2Array = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight() / 2);
        TextureRegion[] explodeRegionArray = new TextureRegion[8];
        for (int i = 0, k = 0; i < 2; i++)
        {
            for (int j = 0; j < 4; j++, k++)
            {
                explodeRegionArray[k] = explodeRegion2Array[i][j];
            }
        }
        animation = new Animation(0.1f, explodeRegionArray);

        setWidth(texture.getWidth() / 4);
        setHeight(texture.getHeight() / 2);
    }

    public void update()
    {
        stateTime += Gdx.graphics.getDeltaTime();
        if (animation.isAnimationFinished(stateTime))
        {
            stateTime = 0;
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
