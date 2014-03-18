package com.wlys.player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen
{

    private Game game;
    private Stage stage;
    private Image bg;
    private Image logo;
    private ImageButton start;
    private Image player;
    private EnemyPlane enemy1;
    private EnemyPlane enemy2;
    private Texture bgTexture;
    private Texture logoTexture;
    private Texture startTexture;
    private Texture playerTexture;
    

    public MenuScreen(Game game)
    {
        super();
        this.game = game;
        
        bgTexture = new Texture(
                Gdx.files.internal("gfx/menu/menu_background.png"));
        logoTexture = new Texture(Gdx.files.internal("gfx/menu/logo.png"));
        startTexture = new Texture(Gdx.files.internal("gfx/menu/play.png"));
        playerTexture = new Texture(Gdx.files.internal("gfx/main/BigPlane.png"));
        
        TextureRegion region = new TextureRegion(startTexture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        stage = new Stage();
        bg = new Image(bgTexture);
        logo = new Image(logoTexture);
        start = new ImageButton(drawable);
        player = new Image(playerTexture);
        enemy1 = new EnemyPlane();
        enemy2 = new EnemyPlane();

        bg.setWidth(Gdx.graphics.getWidth());
        bg.setHeight(Gdx.graphics.getHeight());
        bg.setPosition(0, 0);
        logo.setWidth(Gdx.graphics.getWidth() / 10f * 7);// 7/10
        logo.setHeight(Gdx.graphics.getHeight() / 3f * 1);// 1/3
        logo.setPosition((Gdx.graphics.getWidth() - logo.getWidth()) / 2f,
                Gdx.graphics.getHeight() / 2f);
        start.setPosition((Gdx.graphics.getWidth() - start.getWidth()) / 2f,
                Gdx.graphics.getHeight() / 3f * 1);// 1/3

        player.setPosition(Gdx.graphics.getWidth() / 3f * 2,
                Gdx.graphics.getHeight() / 5f * 3);// 2/3 3/5
        MoveByAction playerMoveByAction = Actions.moveBy(
                -Gdx.graphics.getWidth() / 10f,
                Gdx.graphics.getHeight() / 10f, 2);// 向左上走动
        MoveToAction playerMoveToAction = Actions.moveTo(
                Gdx.graphics.getWidth() / 3f * 2,
                Gdx.graphics.getHeight() / 5f * 3, 2);// 返回
        SequenceAction playerSequenceAction = Actions.sequence(
                playerMoveByAction, playerMoveToAction);//序列化这两个动作
        RepeatAction playerRepeatAction = Actions.forever(playerSequenceAction);// 循环动作
        player.addAction(playerRepeatAction);

        enemy1.setPosition(-enemy1.getWidth(),
                Gdx.graphics.getHeight() / 4f * 3);// 3/4
        MoveByAction enemy1MoveByAction = Actions.moveBy(
                Gdx.graphics.getWidth() + enemy1.getWidth(), 0, 4);// 向右走动
        MoveToAction enemy1MoveToAction = Actions.moveTo(
                -enemy1.getWidth(),
                Gdx.graphics.getHeight() / 4f * 3, 0);// 返回
        SequenceAction enemy1SequenceAction = Actions.sequence(
                enemy1MoveByAction, enemy1MoveToAction);//序列化这两个动作
        enemy1.addAction(Actions.forever(enemy1SequenceAction));// 循环动作
        enemy1.setOrigin(enemy1.getWidth()/2f, enemy1.getHeight()/2f);
        enemy1.setRotation(90);

        enemy2.setPosition(-enemy2.getWidth(),
                Gdx.graphics.getHeight() / 4f * 2);// 2/4
        MoveByAction enemy2MoveByAction = Actions.moveBy(
                Gdx.graphics.getWidth() + enemy2.getWidth(), 0, 4);// 向右走动
        MoveToAction enemy2MoveToAction = Actions.moveTo(
                -enemy1.getWidth(),
                Gdx.graphics.getHeight() / 4f * 2, 0);// 返回
        SequenceAction enemy2SequenceAction = Actions.sequence(
                enemy2MoveByAction, enemy2MoveToAction);//序列化这两个动作
        DelayAction enemy2DelayAction = Actions.delay(2, enemy2SequenceAction);//延迟2s
        enemy2.addAction(Actions.forever(enemy2DelayAction));// 循环动作
        enemy2.setOrigin(enemy2.getWidth()/2f, enemy2.getHeight()/2f);//设置中心点
        enemy2.setRotation(90);//逆时针旋转90°

        stage.addActor(bg);
        stage.addActor(enemy1);
        stage.addActor(enemy2);
        stage.addActor(logo);
        stage.addActor(start);
        stage.addActor(player);
        
        Gdx.input.setInputProcessor(stage);//舞台可以接受输入事件
    }

    @Override
    public void render(float delta)
    {
        if (start.isPressed())
        {
            game.setScreen(new GameScreen(game));
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void show()
    {


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
        logoTexture.dispose();
        startTexture.dispose();
        stage.dispose();
    }

}
