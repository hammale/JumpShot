package com.echoeight.jump;

import com.badlogic.gdx.Game;
import com.echoeight.jump.entity.EntityManager;
import com.echoeight.jump.screen.LevelScreen;

public class JumpShot extends Game
{	
	public static EntityManager em;
	
	@Override
	public void create()
	{
		JumpShot.em = new EntityManager();
		setScreen(new LevelScreen(this));
	}
}