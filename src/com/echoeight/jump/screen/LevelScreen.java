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
import com.echoeight.jump.entity.BasePlatform;
import com.echoeight.jump.entity.BasePlatform.PlatColor;
import com.echoeight.jump.entity.Bullet;
import com.echoeight.jump.entity.Entity;
import com.echoeight.jump.entity.EntityManager;
import com.echoeight.jump.entity.MoveablePlatform;
import com.echoeight.jump.entity.Player;
import com.echoeight.jump.entity.SimplePlatform;

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
		jump = true;
		generateLevel();
		p = new Player(em, camera.viewportWidth/2, camera.viewportHeight/2, 50, 58, batch);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		Gdx.gl.glClear(16384);
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			reset();
			return;
		}
		if(!paused){
			if(Gdx.input.isKeyPressed(Keys.MENU)){
				paused = true;
			}
			em.flush();
			delta = Gdx.graphics.getDeltaTime();

			camera.update();

			camera.translate(0, speed, 0);

			speed += 0.0005F;
			if(speed >= 4.9F){
				speed = 4.9F;
			}
			p.setDX(0);

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
				batch.draw(bg, 0, camera.position.y-((camera.viewportHeight/2))-5);
				for(BasePlatform plat : em.platforms){
					plat.draw();
				}
				for(Entity ent : em.getMovingEntities()) {
					ent.draw();
					if(ent.getY() < camera.position.y
							&& !(ent instanceof Player)){
						em.markForDelete(ent);
					}
				}
			batch.end();
			em.flush();

			handlePlayerInput();
			gravity(p);
			jumpPlayer();

			if(p.getY() >= (camera.position.y)-20){
				camera.translate(0, 5, 0);
				camera.update();
			}
			
			for(BasePlatform ent : em.platforms){
				if(ent instanceof MoveablePlatform){
					if(ent.getX() > Gdx.graphics.getWidth()){
						((MoveablePlatform) ent).setDX((float) -((MoveablePlatform) ent).getDX());
					}else if(ent.getX() < 0-ent.getWidth()
							&& ((BaseMoveableEntity) ent).getDX() < 0){
						((MoveablePlatform) ent).setDX((float) -((MoveablePlatform) ent).getDX());
					}
					ent.update(delta);
				}
			}
			for(Entity ent : em.getMovingEntities()){
				if(ent instanceof Bullet){
					if(ent.getX() > Gdx.graphics.getWidth()){
						em.markForDelete(ent);
					}else if(ent.getX() < 0-ent.getWidth()
							&& ((BaseMoveableEntity) ent).getDX() < 0){
						em.markForDelete(ent);
					}else{
						ent.update(delta);
					}
				}else{
					if(ent.getX() > Gdx.graphics.getWidth()){
						if(ent instanceof MoveablePlatform){
							((MoveablePlatform) ent).setDX((float) -((MoveablePlatform) ent).getDX());
						}else{
							ent.setX((float) (0.0-ent.getWidth()));
						}
					}else if(ent.getX() < 0-ent.getWidth()
							&& ((BaseMoveableEntity) ent).getDX() < 0){
						if(ent instanceof MoveablePlatform){
							((MoveablePlatform) ent).setDX((float) -((MoveablePlatform) ent).getDX());
						}else{
							ent.setX(Gdx.graphics.getWidth());
						}
					}
					ent.update(delta);
					if(((BaseMoveableEntity) ent).getY() <= 0){
						ent.setY(0);
						jump = false;
					}
				}
				if(ent.getY() < camera.position.y
						&& !(ent instanceof Player)){
					em.markForDelete(ent);
				}
			}	

			for(BasePlatform plat : em.platforms){
				plat.update(delta);
			}

			if(p.getY() < camera.position.y-(camera.viewportHeight/2)){
				reset();
				return;
			}

		}else{
			batch.begin();
			batch.draw(pausedScreen, 0, camera.position.y-((camera.viewportHeight/2))-10);
			batch.draw(pausedText, (camera.viewportWidth/2)-(145), camera.position.y);
			batch.end();
			if(Gdx.input.isTouched()){
				paused = false;
			}
		}
	}

	private void reset(){
		dispose();
		game.setScreen(new MainMenu(game));
	}

	private void gravity(Player ent) {
		if(p.getY() > 0){
			for(BasePlatform plat : em.platforms){
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
		float angle = (float) Math.toDegrees(bul.sprite.getRotation());
		if(angle == 0){
			bul.setDY(0.1F);
		}else if(angle == -90){
			bul.setDX(-0.1F);
		}else if(angle == 90){
			bul.setDX(0.1F);
		}else if(angle > 0){
			angle = angle/1000;
			if(angle > 0.045F){
				angle = 0.045F;
			}
			bul.setDY((float) ((0.1-angle)));
			bul.setDX(-angle);
		}else if(angle < 0){
			angle = angle/1000;
			if(angle > 0.045F){
				angle = 0.045F;
			}
			bul.setDY((float)  ((0.1-angle)));
			bul.setDX(-angle);
		}
		bul.setDX((float) (bul.getDX()*2000));
		bul.setDY((float) (bul.getDY()*2000));
	}

	private void generateLevel() {
		for(int i=0;i<100;i++){
			if(difficulty > 80){
				if(ran.nextInt(5) == 0){
					MoveablePlatform mp = new MoveablePlatform(em, genX, genY, 65, 23, Arrays.asList(PlatColor.values()).get(ran.nextInt(4)), batch);
					mp.setDX(100);
				}else{
					new SimplePlatform(em, genX, genY, 65, 23, Arrays.asList(PlatColor.values()).get(ran.nextInt(4)), batch);
				}
			}else{
				new SimplePlatform(em, genX, genY, 65, 23, Arrays.asList(PlatColor.values()).get(ran.nextInt(4)), batch);
			}
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
