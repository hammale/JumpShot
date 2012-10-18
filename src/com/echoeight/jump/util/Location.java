package com.echoeight.jump.util;

public class Location
{
  float x;
  float y;

  public Location(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public void addX(float amnt) {
    x += amnt;
  }

  public void addY(float amnt) {
    y += amnt;
  }

  public Location getAddX(float amnt) {
    return new Location(x + amnt, y);
  }

  public Location getAddY(float amnt) {
    return new Location(x, y + amnt);
  }

  public Location getAddBoth(float xamnt, float yamnt) {
    return new Location(x + xamnt, y + yamnt);
  }

  public float getY() {
    return y;
  }

  public void setX(float d) {
    x = d;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setLocation(Location loc) {
    x = loc.getX();
    y = loc.getY();
  }
}