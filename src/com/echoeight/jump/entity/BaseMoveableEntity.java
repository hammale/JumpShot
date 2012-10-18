package com.echoeight.jump.entity;


public abstract class BaseMoveableEntity extends BaseEntity
  implements MoveableEntity
{
  protected float dx;
  protected float dy;

  public BaseMoveableEntity(EntityManager em, float x, float y, float width, float height)
  {
    super(em, x, y, width, height);
    dx = 0.0F;
    dy = 0.0F;
  }

  public void update(float delta)
  {
    loc.setX(loc.getX() + delta * dx);
    loc.setY(loc.getY() + delta * dy);
    rect.setX(loc.getX());
    rect.setY(loc.getY());
  }

  public double getDX()
  {
    return dx;
  }

  public double getDY()
  {
    return dy;
  }

  public void setDX(float dx)
  {
    this.dx = dx;
  }

  public void setDY(float dy)
  {
    this.dy = dy;
  }
}