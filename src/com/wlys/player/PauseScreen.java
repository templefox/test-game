package com.wlys.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseScreen implements Screen
{

    private GameScreen gameScreen;
    private Stage stage;
    private Image bg;
    private ImageButton back;
    private ImageButton restart;

    private Texture bgTexture;
    private Texture backTexture;
    private Texture restartTexture;

    public PauseScreen(GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;
        stage = new Stage();

        bgTexture = new Texture(Gdx.files.internal("gfx/main/menuboard.png"));
        backTexture = new Texture(Gdx.files.internal("gfx/main/menu_exit.png"));
        restartTexture = new Texture(
                Gdx.files.internal("gfx/main/menu_reset.png"));

        TextureRegion backRegion = new TextureRegion(backTexture);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(
                backRegion);

        TextureRegion restartRegion = new TextureRegion(restartTexture);
        TextureRegionDrawable restartDrawable = new TextureRegionDrawable(
                restartRegion);

        bg = new Image(bgTexture);
        back = new ImageButton(backDrawable);
        restart = new ImageButton(restartDrawable);

        bg.setPosition((Config.WIDTH - bg.getWidth()) / 2f, 50f);
        back.setPosition(50f, 150f);
        restart.setPosition(Config.WIDTH - restart.getWidth() - 50f, 150f);

        stage.addActor(bg);
        stage.addActor(back);
        stage.addActor(restart);
        //舞台响应事件
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        gameScreen.draw(delta);
        stage.act(delta);
        stage.draw();
        if (back.isPressed())
        {
            //销毁 gameScreen
            gameScreen.dispose();
            //销毁 pauseScreen
            gameScreen.getGame().getScreen().dispose();
            //跳转到 menuScreen
            gameScreen.getGame().setScreen(new MenuScreen(gameScreen.getGame()));
        }
        if (restart.isPressed())
        {
            //销毁 pauseScreen
            gameScreen.getGame().getScreen().dispose();
            //跳转到 gameScreen
            gameScreen.getGame().setScreen(gameScreen);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        bgTexture.dispose();
        backTexture.dispose();
        restartTexture.dispose();
        stage.dispose();
    }

}
