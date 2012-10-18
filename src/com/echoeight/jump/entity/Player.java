package com.echoeight.jump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends BaseMoveableEntity
{
	int health;
	Sprite sprite;
	SpriteBatch batch;

	public Player(EntityManager em, float x, float y, float width, float height, SpriteBatch batch)
	{
		super(em, x, y, width, height);
		texture = new Texture(Gdx.files.internal("data/player.png"));
		health = 1;
		id = em.assignId(this);
		sprite = new Sprite(texture);
		this.em = em;
		this.batch = batch;
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
		sprite.setRotation(Gdx.input.getAccelerometerX()*6);
		sprite.setBounds(loc.getX(), loc.getY(), width, height);
		sprite.draw(batch);
		rect = sprite.getBoundingRectangle();
	}

}