package com.echoeight.jump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SimplePlatform extends BaseMoveableEntity
{
	int health;
	Sprite sprite;
	PlatColor color;
	SpriteBatch batch;
	
	public enum PlatColor { BLUE, GREEN, RED, YELLOW };
	
	public SimplePlatform(EntityManager em, float x, float y, float width, float height, PlatColor color, SpriteBatch batch)
	{
		super(em, x, y, width, height);
		this.color = color;
		this.batch = batch;
		if(color == PlatColor.BLUE){
			texture = new Texture(Gdx.files.internal("data/plat1.png"));
		}else if(color == PlatColor.GREEN){
			texture = new Texture(Gdx.files.internal("data/plat2.png"));
		}else if(color == PlatColor.RED){
			texture = new Texture(Gdx.files.internal("data/plat3.png"));
		}else if(color == PlatColor.YELLOW){
			texture = new Texture(Gdx.files.internal("data/plat4.png"));
		}
		health = 1;
		id = em.assignId(this);
		sprite = new Sprite(texture);
		rect = sprite.getBoundingRectangle();
	}

	public void damage(int damage) {
		health -= damage;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	public void draw()
	{
		sprite.setBounds(loc.getX(), loc.getY(), width, height);
		sprite.draw(batch);
	}

}