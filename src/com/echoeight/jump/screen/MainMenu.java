package com.echoeight.jump.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.echoeight.jump.JumpShot;

public class MainMenu implements Screen {

	JumpShot game;
	SpriteBatch batch;	
	Texture texture;
	OrthographicCamera camera;
	
	public MainMenu(JumpShot game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		Gdx.gl.glClear(16384);
		batch.begin();
			batch.draw(texture, camera.viewportWidth/2, camera.viewportHeight/2);
		batch.end();
		if(Gdx.input.isTouched()){
			game.setScreen(new LevelScreen(game));
		}
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		texture = new Texture(Gdx.files.internal("data/menu.png"));
	}
	
	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
	}

}
