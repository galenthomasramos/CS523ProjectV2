//package com.cs523proj;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Explorer extends Ant {
	List<PVector> trace = new ArrayList<PVector>();
	List<Circle> circlesTrail = new ArrayList<Circle>();

	public Explorer(PApplet _parent,int colony, PVector _position, int _radius,
			int _color, int id) {
		super(_parent, colony, _position, _radius, _color, id);
	}

	/* After the GPS sampling interval has passed,
	 * set the new current position and save the last
	 * one in the trace to be displayed
	 */
	
	public void positionChanged(PVector newPos) {
		trace.add(position);
		position = newPos;
	}
	
	public void render() {
		super.render();

		/* Render the trace, i.e. the list of past positions 
		for (PVector p : trace) {
			parent.fill(color);
			parent.ellipse(p.x, p.y, 2*buffer.radius, 2*buffer.radius);
		} */
	}
}
