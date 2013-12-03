//package com.cs523proj;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Ant {
	PApplet parent;
	PVector position;
	Circle buffer;
	int radius;
	int color;
	int colonyID;
	int antID = 0;
	int ants = 1;

	public Ant(PApplet _parent,int colony, PVector _position, int _radius, int _color, int id) {
		colonyID = colony;
		parent = _parent;
		position = _position;
		radius = _radius;
		buffer = new Circle (this.position.x, this.position.y, radius);
		color = _color;
		antID = id;
	}

	public void render(){
		buffer.point.x = parent.mouseX;
		buffer.point.y = parent.mouseY;
		
		parent.fill(this.parent.color(color,255,255));
		parent.ellipse(buffer.point.getX(),buffer.point.getY(), 2*buffer.radius, 2*buffer.radius);
		
		//System.out.println("IN ANT RENDER:  buffer.point.getX(): " + buffer.point.getX() + " buffer.point.getY(): " + buffer.point.getY());
	}
	
	public void setAntsNumber(int n) {
		ants = n;
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
