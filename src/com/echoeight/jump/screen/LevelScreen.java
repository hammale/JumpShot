package com.echoeight.jump.screen;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.echoeight.jump.JumpShot;
import com.echoeight.jump.entity.BaseMoveableEntity;
import com.echoeight.jump.entity.Bullet;
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

	Texture bg, pausedScreen, pausedText;

	Random ran = new Random();

	int genX = 0;
	int genY = 0;
	float speed = 0.5F;
	int difficulty;

	int shotDelay = 10;

	boolean jump, paused;
	Player p;
	JumpShot game;

	public LevelScreen(JumpShot game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.MENU)){
			reset();
			return;
		}
		if(!paused){
			em.flush();
			if (Gdx.input.isKeyPressed(Keys.BACK)){
				reset();
				return;
			}
			delta = Gdx.graphics.getDeltaTime();
			Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
			Gdx.gl.glClear(16384);

			camera.update();

			camera.translate(0, speed, 0);

			speed += 0.0005F;
			if(speed >= 5){
				speed = 4.9F;
			}
			p.setDX(0);

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.draw(bg, 0, camera.position.y-((camera.viewportHeight/2))-5);
			for(SimplePlatform plat : em.platforms){
				plat.draw();
			}
			for(Entity ent : em.getMovingEntities()) {
				if(ent instanceof Bullet){
					if(ent.getX() > Gdx.graphics.getWidth()){
						em.markForDelete(ent);
					}else if(ent.getX() < 0){
						em.markForDelete(ent);
					}else{
						ent.draw();
					}
				}else{
					ent.draw();
				}
			}
			batch.end();
			em.flush();

			handlePlayerInput();
			jumpPlayer();		
			gravity(p);

			if(p.getY() >= (camera.position.y)-20){
				camera.translate(0, 5, 0);
				camera.update();
			}

			for(Entity ent : em.getMovingEntities()){
				if(ent instanceof Bullet){
					if(ent.getX() > Gdx.graphics.getWidth()){
						em.markForDelete(ent);
					}else if(ent.getX() < 0){
						em.markForDelete(ent);
					}else{
						ent.update(delta);
					}
				}else{
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
			}	

			for(SimplePlatform plat : em.platforms){
				plat.update(delta);
			}

			if(p.getY() < camera.position.y-(camera.viewportHeight/2)){
				reset();
				return;
			}

		}else{
			batch.begin();
			batch.draw(pausedScreen, 0, camera.position.y-((camera.viewportHeight/2))-5);
			batch.draw(pausedText, (camera.viewportWidth/2)-(145), camera.viewportHeight/2);
			batch.end();
		}
	}

	private void reset(){
		dispose();
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
		if(Gdx.input.isTouched()){
			if(shotDelay == 10){
				Bullet bul = new Bullet(em, (float) (p.getX()+(p.getWidth()/2)), (float) (p.getY()+p.getHeight()), 4, 10, batch, Gdx.input.getAccelerometerX());
				fireBullet(bul);
				shotDelay = 0;
			}
			shotDelay++;
		}
	}

	private void fireBullet(Bullet bul) {
		float angle = bul.sprite.getRotation();
		if(angle>360){
			angle = angle-360;
		}
		if(angle == 90){
			bul.setDY(0.1F);
		}else if(angle == 180){
			angle -= 2;
			bul.setDX(-0.1F);
		}else if(angle == 0 || angle == 360){
			bul.setDX(0.1F);
		}else if(angle == 270){
			bul.setDY(-0.1F);
		}else if(angle < 90 && angle > 0){
			angle = (angle/1000);
			bul.setDX((float) (0.1-angle));
			bul.setDY(angle);
		}else if(angle > 90 && angle < 180){
			angle = ((angle - 88)/1000);
			bul.setDY((float) (0.1-angle));
			bul.setDX(-1*angle);
		}else if(angle > 180 && angle < 270){
			angle = ((angle - 178)/1000);
			bul.setDX((float) (-1*(0.1-angle)));
			bul.setDY(-1*angle);
		}else if(angle > 270 && angle < 360){
			angle = ((angle - 270)/1000);
			bul.setDY((float) (-1*(0.1-angle)));
			bul.setDX(angle);
		}
		bul.setDX((float) (bul.getDX()*5));
		bul.setDY((float) (bul.getDY()*5));
		bul.setDY(5);
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
		bg =  new Texture(Gdx.files.internal("data/levelbg.png"));
		pausedScreen =  new Texture(Gdx.files.internal("data/paused.png"));
		pausedText =  new Texture(Gdx.files.internal("data/pausedtext.png"));
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
		generateLevel();

	}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
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
