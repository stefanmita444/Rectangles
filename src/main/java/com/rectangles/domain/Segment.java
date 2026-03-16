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
      return this.start.y() == this.end.y();
  }

  public double getConstantY() {
      return this.start.y();
  }

  public double getConstantX() {
      return this.start.x();
  }

  public double getMinX() { return Math.min(this.start.x(), this.end.x()); }
  public double getMaxX() { return Math.max(this.start.x(), this.end.x()); }
  public double getMinY() { return Math.min(this.start.y(), this.end.y()); }
  public double getMaxY() { return Math.max(this.start.y(), this.end.y()); }
}
