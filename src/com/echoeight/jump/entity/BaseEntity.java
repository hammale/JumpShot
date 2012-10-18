package com.echoeight.jump.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.echoeight.jump.util.Location;

public abstract class BaseEntity
  implements Entity
{
  protected float width;
  protected float height;
  protected Rectangle hitbox = new Rectangle();
  protected int id;
  protected EntityManager em;
  protected Location loc;
  protected Rectangle rect;
  protected Texture texture;

  public BaseEntity(EntityManager em, float x, float y, float width, float height)
  {
    this.width = width;
    this.height = height;
    this.em = em;
    em.assignId(this);
    loc = new Location(x, y);
    rect = new Rectangle();
    rect.setHeight(height);
    rect.setWidth(width);
    rect.setX(x);
    rect.setY(y);
  }

  public Texture getTexture()
  {
    return texture;
  }

  public void setLocation(Location loc)
  {
    this.loc = loc;
    rect.x = loc.getX();
    rect.y = loc.getY();
  }

  public Location getLocation()
  {
    return loc;
  }

  public void update(float delta)
  {
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void destroy()
  {
    em.remove(this);
  }

  public void setLocation(float x, float y)
  {
    loc.addX(x);
    loc.addX(y);
  }

  public void setX(float x)
  {
    loc.setX(x);
    rect.setX(x);
  }

  public void setY(float y)
  {
    loc.setY(y);
    rect.setY(y);
  }

  public void setWidth(float width)
  {
    this.width = width;
    rect.width = width;
  }

  public void setHeight(float height)
  {
    this.height = height;
    rect.height = height;
  }

  public double getX()
  {
    return loc.getX();
  }

  public double getY()
  {
    return loc.getY();
  }

  public double getHeight()
  {
    return height;
  }

  public double getWidth()
  {
    return width;
  }

  public Rectangle getRectangle()
  {
    return rect;
  }

  public void setRectangle(Rectangle rect)
  {
    this.rect = rect;
  }

  public boolean intersects(Entity other)
  {
    return rect.overlaps(other.getRectangle());
  }
}