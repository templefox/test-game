package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EnemyPlane extends Actor
{

    private Texture texture;
    private Animation animation;
    private float stateTime;

    public EnemyPlane()
    {
        super();
        texture = new Texture(Gdx.files.internal("gfx/main/enemy2.png"));
        TextureRegion[] regionArray = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight())[0];
        animation = new Animation(0.2f, regionArray);
        animation.setPlayMode(Animation.LOOP);

        setWidth(texture.getWidth() / 4f);
        setHeight(texture.getHeight());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > 3f)
        {
            stateTime = 0;
        }
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        batch.draw(keyFrame, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
    }

    @Override
    public void clear()
    {
        super.clear();
        texture.dispose();
    }

}
