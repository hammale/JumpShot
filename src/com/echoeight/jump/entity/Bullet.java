package com.echoeight.jump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends BaseMoveableEntity
{
	public Sprite sprite;
	SpriteBatch batch;
	float degrees;

	public Bullet(EntityManager em, float x, float y, float width, float height, SpriteBatch batch, float degrees)
	{
		super(em, x, y, width, height);
		texture = new Texture(Gdx.files.internal("data/bullet.png"));
		id = em.assignId(this);
		sprite = new Sprite(texture);
		this.em = em;
		this.batch = batch;
		this.degrees = degrees*6;
		sprite.setRotation(degrees);
	}
	
	public void draw()
	{
		sprite.setBounds(loc.getX(), loc.getY(), width, height);
		sprite.draw(batch);
		rect = sprite.getBoundingRectangle();
	}

}