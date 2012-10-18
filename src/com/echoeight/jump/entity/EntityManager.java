package com.echoeight.jump.entity;

import java.util.ArrayList;
import java.util.HashSet;

public class EntityManager
{
	private HashSet<Integer> ids = new HashSet<Integer>();

	ArrayList<Entity> movingEntities = new ArrayList<Entity>();
	public ArrayList<SimplePlatform> platforms = new ArrayList<SimplePlatform>();
	ArrayList<Entity> entitiesrem = new ArrayList<Entity>();

	public EntityManager()
	{
	}

	public int assignId(Entity entity)
	{
		if(entity instanceof SimplePlatform){
			platforms.add((SimplePlatform) entity);
		}else if((entity instanceof MoveableEntity)) {
			movingEntities.add(entity);
		}
		int i = ids.size();
		ids.add(i);
		entity.setId(i);
		return i;
	}

	public HashSet<Integer> getIds() {
		return ids;
	}

	public void markForDelete(Entity ent) {
		entitiesrem.add(ent);
	}

	public void remove(Entity entity) {
		ids.remove(entity.getId());
		entitiesrem.add(entity);
		if ((entity instanceof MoveableEntity)) {
			for (Entity ent : movingEntities) {
				if (ent.getId() == entity.getId())
					movingEntities.remove(entity);
			}
		}
		for (Entity ent : entitiesrem) {
			ids.remove(ent.getId());
		}
		entitiesrem.clear();
	}

	public boolean isMarked(Entity ent) {
		return entitiesrem.contains(ent);
	}

	public void flush() {
		for (Entity ent : entitiesrem) {
			ent.getTexture().dispose();
			ids.remove(ent.getId());
		}
		entitiesrem.clear();
	}

	public void dispose() {
		for (Entity ent : movingEntities) {
			ent.getTexture().dispose();
			ids.remove(ent.getId());
		}
		for (Entity ent : entitiesrem) {
			ent.getTexture().dispose();
			ids.remove(ent.getId());
		}
		ids.clear();
		movingEntities.clear();
		entitiesrem.clear();
	}

	public ArrayList<Entity> getMovingEntities() {
		return movingEntities;
	}

	public void removeAll() {
		ids.clear();
		entitiesrem.clear();
		movingEntities.clear();
	}
}