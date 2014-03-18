package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player extends GameObject
{

    private GameScreen screen;
    private Texture texture;
    private Animation animation;
    private Texture shipATexture;
    private Texture shipBTexture;
    private Texture shipCTexture;
    private float deadTime;

    public Player(GameScreen screen)
    {
        super();
        this.screen = screen;
        texture = new Texture(Gdx.files.internal("gfx/main/ship2.png"));
        shipATexture = new Texture(Gdx.files.internal("gfx/main/ship_a.png"));
        shipBTexture = new Texture(Gdx.files.internal("gfx/main/ship_b.png"));
        shipCTexture = new Texture(Gdx.files.internal("gfx/main/ship_c.png"));

        TextureRegion[] regionArray = TextureRegion.split(texture,
                texture.getWidth() / 4, texture.getHeight())[0];
        animation = new Animation(0.2f, regionArray);
        animation.setPlayMode(Animation.LOOP);

        setWidth(texture.getWidth() / 4f);
        setHeight(texture.getHeight());

        Array<Rectangle> runs = new Array<Rectangle>();
        runs.add(new Rectangle(getWidth() / 3f, 0, getWidth() / 3f, getHeight()));
        geometry = new CollisionGeometry(runs);
    }

    public void update()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += deltaTime;
        if (stateTime > 1f)
        {
            stateTime = 0;
            fire();
        }
        if (state == DEAD_STATE)
        {
            deadTime -= deltaTime;
            if (deadTime < 0)
            {
                screen.getGame().setScreen(new PauseScreen(screen));
                deadTime = 1f;
            }
            else
            {
                aStartX += aOffsetX * deltaTime;
                aStartY += aOffsetY * deltaTime;
                bStartX += bOffsetX * deltaTime;
                bStartY += bOffsetY * deltaTime;
                cStartX += cOffsetX * deltaTime;
                cStartY += cOffsetY * deltaTime;
                aScale -= 0.9f * deltaTime;
                bScale -= 0.9f * deltaTime;
                cScale -= 0.9f * deltaTime;
                aRotation += 360 * deltaTime;
                bRotation += 360 * deltaTime;
                cRotation += 360 * deltaTime;
            }
        }
    }

    @Override
    public void setInCollision(boolean inCollision)
    {
        // 首次碰撞
        if (state == NORMAL_STATE && inCollision)
        {
            state = DEAD_STATE;
            deadTime = 1f;
            aStartX = bStartX = cStartX = getX();
            aStartY = bStartY = cStartY = getY();
            aScale = 1f;
            bScale = 1f;
            cScale = 1f;
            aRotation = 0f;
            bRotation = 0f;
            cRotation = 0f;
        }
        super.setInCollision(inCollision);
    }

    // 左上角
    private float aOffsetX = -50;
    private float aOffsetY = 50;
    private float aStartX;
    private float aStartY;
    private float aScale = 1;
    private float aRotation;
    // 右上角
    private float bOffsetX = 50;
    private float bOffsetY = 50;
    private float bStartX;
    private float bStartY;
    private float bScale = 1;
    private float bRotation;
    // 下方
    private float cOffsetX = 0;
    private float cOffsetY = -50;
    private float cStartX;
    private float cStartY;
    private float cScale = 1;
    private float cRotation;

    public void draw(SpriteBatch batch)
    {

        if (state == NORMAL_STATE)
        {
            TextureRegion keyFrame = animation.getKeyFrame(stateTime);
            batch.draw(keyFrame, getX(), getY(), 0, 0, getWidth(), getHeight(),
                    1, 1, 0);
        }
        else
        {
            batch.draw(shipATexture, aStartX, aStartY,
                    shipATexture.getWidth() / 2f,
                    shipATexture.getHeight() / 2f, shipATexture.getWidth(),
                    shipATexture.getHeight(), aScale, aScale, aRotation, 0, 0,
                    shipATexture.getWidth(), shipATexture.getHeight(), false,
                    false);
            batch.draw(shipBTexture, bStartX, bStartY,
                    shipBTexture.getWidth() / 2f, shipBTexture.getWidth() / 2f,
                    shipBTexture.getWidth(), shipBTexture.getHeight(), bScale,
                    bScale, bRotation, 0, 0, shipBTexture.getWidth(),
                    shipBTexture.getHeight(), false, false);
            batch.draw(shipCTexture, cStartX, cStartY,
                    shipCTexture.getWidth() / 2f, shipCTexture.getWidth() / 2f,
                    shipCTexture.getWidth(), shipCTexture.getHeight(), cScale,
                    cScale, cRotation, 0, 0, shipCTexture.getWidth(),
                    shipCTexture.getHeight(), false, false);
        }

    }

    private void fire()
    {
        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;
        screen.addPlayerBullet(centerX - getWidth() / 6f * 2, centerY);
        screen.addPlayerBullet(centerX + getWidth() / 6f * 2, centerY);
    }

    public void clear()
    {
        texture.dispose();
        shipATexture.dispose();
        shipBTexture.dispose();
        shipCTexture.dispose();
    }

}
