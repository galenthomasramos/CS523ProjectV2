//package com.cs523proj;

import processing.core.PApplet;
import processing.core.PVector;

public class Colony {
	PVector position;
	PApplet parent;
	Circle buffer;
	int color;
	int radius;
	int ID = 0;
	int ants = 0;
	
	public Colony(PApplet _parent, int id, PVector _position, int _radius, int _color) {
		ID = ID;
		parent = _parent;
		position = _position;
		radius = _radius;
		buffer = new Circle (position.x, position.y, radius);
		color = _color;

	}
	
	public void setAnts (int n) {
		ants = n;
	}
	
	public void newFoodSpot(float multiplier) {
		ants *= multiplier;
	}
	
	public void lostAntOrCheating(int players) {
		System.out.println("Removing "+ants/(players*60)+" ants");
		ants -= ants/(players*60);
	}

	public void render() {
		
		parent.stroke(0);
		parent.strokeWeight(2);
		//parent.fill(color);
		parent.fill(this.parent.color(color, 100, 255));
		parent.ellipse(buffer.point.getX(),buffer.point.getY(), 2*buffer.radius, 2*buffer.radius);
//		System.out.println("IN COLONY RENDER: " + "buffer.x: " + buffer.point.getX() + "  buffer.y: " + buffer.point.getY() + "2*buffer.radius: " + 2*buffer.radius );
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
