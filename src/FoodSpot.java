//package com.cs523proj;

import processing.core.PApplet;
import processing.core.PVector;

public class FoodSpot {

	PApplet parent;
	PVector position;
	Circle buffer;
	int color;
	int radius;
	Trail trail;
	boolean displayTrail;

	public FoodSpot(PApplet _parent, PVector _position, int _radius, int _color) {
		parent = _parent;
		position = _position;
		radius = _radius;
		buffer = new Circle (position.x, position.y, radius);
		color = _color;
		displayTrail = false;
	}

	public void render() {
		//System.out.println("IN FOODSPOT.RENDER()");
		parent.fill(this.parent.color(color, 255, 255));
		parent.ellipse(buffer.point.getX(),buffer.point.getY(), 2*buffer.radius, 2*buffer.radius);
		
		if(displayTrail){
			trail.render();
		}
	}

	public void setTrail(Trail trail){
		this.trail = trail;
	}
	
	boolean isColliding(Circle c)
	{
		boolean colliding = false;
		
		System.out.println("FoodSpot Collision Detected");
		
		float circleDistanceX = PApplet.abs(buffer.point.getX() - c.point.getX());
		float circleDistanceY = PApplet.abs(buffer.point.getY() - c.point.getY());
	 
		float distance = parent.pow(circleDistanceX, 2) +
							 parent.pow(circleDistanceY, 2);
	 
		colliding = (distance <= parent.pow(buffer.radius+c.radius, 2));
		
		if(colliding){
			if(!displayTrail)
				displayTrail = true;
			
			if(trail != null){
				
			}
		}
		
		return (colliding);
		
	}

}
