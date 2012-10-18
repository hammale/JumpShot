package com.echoeight.jump.screen;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.echoeight.jump.JumpShot;
import com.echoeight.jump.entity.BaseMoveableEntity;
import com.echoeight.jump.entity.Entity;
import com.echoeight.jump.entity.EntityManager;
import com.echoeight.jump.entity.Player;
import com.echoeight.jump.entity.SimplePlatform;
import com.echoeight.jump.entity.SimplePlatform.PlatColor;

public class LevelScreen implements Screen {

	private OrthographicCamera camera;
	public SpriteBatch batch;
	public EntityManager em;
	float delta;

	Random ran = new Random();

	int genX = 0;
	int genY = 0;
	float speed = 0.5F;
	int difficulty;

	boolean jump, paused;
	Player p;
	JumpShot game;

	public LevelScreen(JumpShot game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		if(!paused){
			if (Gdx.input.isKeyPressed(Keys.MENU)){
				reset();
				return;
			}
			delta = Gdx.graphics.getDeltaTime();
			Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
			Gdx.gl.glClear(16384);

			camera.update();

			camera.translate(0, speed, 0);

			speed += 0.001F;

			p.setDX(0);

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			for(SimplePlatform plat : em.platforms){
				plat.draw();
			}
			for(Entity ent : em.getMovingEntities()) {
				ent.draw();
			}
			batch.end();

			handlePlayerInput();
			jumpPlayer();		
			gravity(p);

			if(Gdx.input.isTouched()){
			}

			if(p.getY() >= (camera.position.y)-20){
				camera.translate(0, 5, 0);
				camera.update();
			}

			for(Entity ent : em.getMovingEntities()){
				if(ent.getX() > Gdx.graphics.getWidth()){
					ent.setX((float) (0.0-ent.getWidth()));
				}else if(ent.getX() < 0-ent.getWidth()
						&& ((BaseMoveableEntity) ent).getDX() < 0){
					ent.setX(Gdx.graphics.getWidth());
				}
				ent.update(delta);
				if(((BaseMoveableEntity) ent).getY() <= 0){
					ent.setY(0);
					jump = false;
				}
			}	
			for(SimplePlatform plat : em.platforms){
				plat.update(delta);
			}
		}
	}

	private void reset(){
//		paused = true;
//		dispose();
		game.setScreen(new MainMenu(game));
	}

	private void gravity(Player ent) {
		if(p.getY() > 0){
			for(SimplePlatform plat : em.platforms){
				if(p.intersects(plat)
						&& p.getDY() <= 0){
					jump = false;
					jumpPlayer();					
					return;
				}
			}
			p.setDY((float) (p.getDY()-10));
		}

	}

	private void jumpPlayer() {
		if(!jump){
			jump = true;
			p.setDY(300);
		}
	}

	private void handlePlayerInput() {
		p.setDX(Gdx.input.getAccelerometerX()*-80);
	}

	private void generateLevel() {
		for(int i=0;i<100;i++){
			new SimplePlatform(em, genX, genY, 65, 23, Arrays.asList(PlatColor.values()).get(ran.nextInt(4)), batch);
			genX = ran.nextInt((int) camera.viewportWidth-70);
			genY += ran.nextInt(100) + difficulty;
			difficulty++;
			if(difficulty >= 125){
				difficulty = 125;
			}
		}
		paused = false;
	}

	@Override
	public void show() {
		em = JumpShot.em;
		difficulty = 75;
		if(!Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			Gdx.app.exit();
		}
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		jump = false;
		p = new Player(em, Gdx.graphics.getWidth()/2, 0, 50, 58, batch);
		Gdx.app.postRunnable(new Runnable(){
			@Override
			public void run(){
				generateLevel();
			}
		});
	}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
		paused = false;
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		em.dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void resize(int width, int height) {
	}

}
