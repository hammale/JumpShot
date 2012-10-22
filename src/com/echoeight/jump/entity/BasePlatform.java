package com.echoeight.jump.entity;

public abstract class BasePlatform extends BaseMoveableEntity {

	public enum PlatColor { BLUE, GREEN, RED, YELLOW };
	
	public BasePlatform(EntityManager em, float x, float y, float width,
			float height) {
		super(em, x, y, width, height);
		
	}

}
