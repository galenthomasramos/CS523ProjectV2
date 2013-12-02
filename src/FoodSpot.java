//package com.cs523proj;

import processing.core.PApplet;
import processing.core.PVector;

public class FoodSpot {

	PApplet parent;
	PVector position;
	Circle buffer;
	int color;
	int radius;

	public FoodSpot(PApplet _parent, PVector _position, int _radius, int _color) {
		parent = _parent;
		position = _position;
		radius = _radius;
		buffer = new Circle (position.x, position.y, radius);
		color = _color;
	}

	public void render() {
		parent.fill(this.parent.color(color));
		parent.ellipse(buffer.point.getX(),buffer.point.getY(), 2*buffer.radius, 2*buffer.radius);
	}

	boolean isColliding(Circle c)
	{
		float circleDistanceX = PApplet.abs(buffer.point.getX() - c.point.getX());
		float circleDistanceY = PApplet.abs(buffer.point.getY() - c.point.getY());
	 
		float distance = parent.pow(circleDistanceX, 2) +
							 parent.pow(circleDistanceY, 2);
	 
		return (distance <= parent.pow(buffer.radius+c.radius, 2));
	}

}
