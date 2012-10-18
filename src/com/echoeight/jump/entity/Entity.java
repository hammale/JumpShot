package com.echoeight.jump.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.echoeight.jump.util.Location;

public abstract interface Entity
{
  public abstract void update(float paramFloat);

  public abstract Texture getTexture();

  public abstract void destroy();

  public abstract int getId();

  public abstract void setId(int paramInt);

  public abstract void draw();

  public abstract void setLocation(float paramFloat1, float paramFloat2);

  public abstract void setX(float paramFloat);

  public abstract void setY(float paramFloat);

  public abstract void setWidth(float paramFloat);

  public abstract void setHeight(float paramFloat);

  public abstract double getX();

  public abstract double getY();

  public abstract double getHeight();

  public abstract double getWidth();

  public abstract boolean intersects(Entity paramEntity);

  public abstract void setLocation(Location paramLocation);

  public abstract Location getLocation();

  public abstract Rectangle getRectangle();

  public abstract void setRectangle(Rectangle paramRectangle);
}