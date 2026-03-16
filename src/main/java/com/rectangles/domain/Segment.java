package com.rectangles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Segment {
  private Point start;
  private Point end;

  public boolean isHorizontal() {
      return this.start.getY() == this.end.getY();
  }

  public double getConstantY() {
      return this.start.getY();
  }

  public double getConstantX() {
      return this.start.getX();
  }

  public double getMinX() { return Math.min(this.start.getX(), this.end.getX()); }
  public double getMaxX() { return Math.max(this.start.getX(), this.end.getX()); }
  public double getMinY() { return Math.min(this.start.getY(), this.end.getY()); }
  public double getMaxY() { return Math.max(this.start.getY(), this.end.getY()); }
}
