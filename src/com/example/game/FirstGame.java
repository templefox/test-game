package com.example.game;
 
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 
public class FirstGame implements ApplicationListener {
    //��ͼ�õ�SpriteBatch
    private SpriteBatch batch;
    @Override
    public void create() {
        batch = new SpriteBatch(); //ʵ����
    }
 
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void pause() {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); //����
        batch.begin();
        batch.end();
    }
 
    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void resume() {
        // TODO Auto-generated method stub
 
    }
 
}