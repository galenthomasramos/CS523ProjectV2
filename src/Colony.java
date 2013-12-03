//package com.cs523proj;

import java.util.ArrayList;
import java.util.List;

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
	static int STEPS = 5;
	List<ColonyEvent> eventlog = new ArrayList<ColonyEvent>();
	
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
	
	public void newFoodSpot(float multiplier, int id) {
		eventlog.add(new ColonyEvent("Ant "+id+" found a food spot, adding "+ants*(multiplier-1)+" ants to colony",
					(int)(ants*(multiplier-1)), true));
//		System.out.println(eventlog.get(eventlog.size()-1).description);
		ants *= multiplier;
	}
	
	public void lostAntOrCheating(int players, int id) {
		eventlog.add(new ColonyEvent("Ant "+id+" is cheating or lost, removing "+ants/(players*STEPS)+" ants from colony",
				(int)(ants/(players*STEPS)), false));
//		System.out.println(eventlog.get(eventlog.size()-1).description);
		ants -= ants/(players*STEPS);
	}
	
	public void colonyDutyFulfilled(int n, int id) {
		eventlog.add(new ColonyEvent("Ant "+id+" fulfilled a colony duty, adding "+n+" ants to colony",
				n, true));
//		System.out.println(eventlog.get(eventlog.size()-1).description);
		ants += n;
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
