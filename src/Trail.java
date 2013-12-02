//package com.cs523proj;

import java.util.ArrayList;
import java.util.List;

import processing.core.*;

public class Trail {
	PApplet parentApplet;
	ArrayList<Circle> circleList;
	float circleRadius;
	int current_collision = -1;
	int color = 255;
	
	public Trail(PApplet _parent, float circleRadius){
		this.circleRadius = circleRadius;
		parentApplet = _parent;
		circleList = new ArrayList<Circle>();
	}
	
	public Trail(PApplet _parent, float _circleRadius, List<PVector> passedList){
		parentApplet = _parent;
		this.circleRadius = _circleRadius;
		
		for(PVector pvec: passedList){
			circleList.add(new Circle(pvec.x, pvec.y, circleRadius));
		}
	}
	
	public boolean isColliding (Ant ant) {
		int i=0;
		for(Circle circ: circleList){
			
			boolean isColliding = ant.isColliding(circ);
			
			//change color attribute of ant that is colliding with trail:
			if(isColliding){
				ant.color = 0;
				current_collision = i;
				System.out.println("isColliding = " + isColliding);
				return true;
			}
			else
				ant.color = 255;
			i++;
		}
		return false;
	}
	
	//Add circles in regions of absent circles of trail
	public void interpolateTrail(){
		int i = 1;
		int noToAdd;
		//float angleBetween;
		float interpolateDistance;
		
		float tempDist;
		
		float trailDivisor = 0.5f;
		
		PVector tempVec1;
		PVector tempVec2;
		
		PVector interpolateVec;
		
		Circle tempCircle;
		ArrayList<Circle> tempCircles = new ArrayList();
		
		int originalSize = circleList.size();
		
		for (i = i; i < originalSize; i++){
			tempDist = PApplet.dist(circleList.get(i).point.getX(),circleList.get(i).point.getY(), circleList.get(i-1).point.getX(), circleList.get(i-1).point.getY());
			tempCircles.add(circleList.get(i-1));
			if(tempDist > (trailDivisor * circleRadius)){
				//System.out.println("trailDivisor * circleRadius= " + (trailDivisor * circleRadius));
				noToAdd = (int)(tempDist / (trailDivisor * circleRadius));
				//System.out.println("noToAdd= " + noToAdd);
				
				interpolateDistance = tempDist / noToAdd;
				//System.out.println("interpolateDistance= " + interpolateDistance);
				
				tempVec1 = new PVector(circleList.get(i-1).point.getX(), circleList.get(i-1).point.getY());
				tempVec2 = new PVector(circleList.get(i).point.getX(), circleList.get(i).point.getY());
				
				//angleBetween = PVector.angleBetween(tempVec1, tempVec2);
						
				for(float j = 1; j < noToAdd; j++){
					//System.out.println(("percentage of vectors distance drawn: " + ((j * 100) / (float)noToAdd)* 0.01f));
					interpolateVec = PVector.lerp(tempVec1, tempVec2, ((j * 100) / (float)noToAdd) * 0.01f);
					tempCircle = new Circle(interpolateVec.x, interpolateVec.y, circleRadius);
					
					//tempCircles.add((int)j, tempCircle);
					tempCircles.add(tempCircle);
				}
			}
		}
		tempCircles.add(circleList.get(originalSize - 1));
		
		this.circleList = tempCircles;
	}
	
	public void render(){
		
		if(circleList.size() > 0){
			//System.out.println("Trail.circleList.size() = " + circleList.size());
			for(Circle circ: circleList){
				parentApplet.fill(this.parentApplet.color(color));
				parentApplet.stroke(0);
				//parentApplet.strokeWeight(5);
				parentApplet.noStroke();
				parentApplet.ellipse(circ.point.getX(), circ.point.getY(), circ.getRadius(), circleRadius);
			}
		}
	}
	
	public void randomPopulate(){
		
		float divX = 5;
		float divY = 10;
		
		int countX;
		int countY;
		
		float mappedX;
		float mappedY;
		
		int randInt;
		int randX;
		
		ArrayList<PVector> pvecs= new ArrayList<PVector>();
		
		circleList = new ArrayList<Circle>();
		
		for(int i = 0; i < divY; i++){
			mappedY = parentApplet.map(i, 0, divY, 0, parentApplet.height);
			randX = (int) (Math.random() * parentApplet.width);
			circleList.add(new Circle(randX, mappedY, circleRadius));
		}
		
	}
	
}
