//package com.cs523proj;

import java.util.ArrayList;

import processing.core.*;
//import ketai.sensors.*;
import controlP5.*;

public class ProcessingMain extends PApplet {
	ControlP5 cp5;
	
	float diameter = 100.0f;
	Colony colony1;
	
	int colony1Color;
	PVector colony1Pos;
	
	PVector mappedVector = new PVector(0,0);
	
	//KetaiLocation location;
	float accuracy;
	float longitude, latitude, altitude;
	
	Trail tempTrail;
	//Trail tempTrail = new Trail(this, 10);
	
	ArrayList<Explorer> explorersList;

	boolean collisionDetected;
	
	public void setup(){
		colorMode(RGB);
		size(800, 480);
		tempTrail = new Trail(this, 10);
		orientation(LANDSCAPE);
		textAlign(CENTER, CENTER);
		textSize(12);
		
		collisionDetected = false;
		
		PVector buttonSize = new PVector(80,20);
		
		//location = new KetaiLocation(this);
		cp5 = new ControlP5(this);
		
		cp5.addButton("createTrail")
		   .setValue(1)
	       .setPosition(10,this.height - (int)buttonSize.y)
		   .setSize((int)buttonSize.x,(int)buttonSize.y)
		   ;
		
		tempTrail.randomPopulate();
		tempTrail.interpolateTrail();
		
		frameRate(60);
		colony1Pos = new PVector(this.width/5, this.height/5);
		colony1 = new Colony(this, colony1Pos, 30, 0x22FFAA);
		
		explorersList = new ArrayList<Explorer>();
		explorersList.add(new Explorer(this, new PVector(0,0), 10, 0xFF0000));
	}
	
	public void draw(){
		background(150);	
		
		tempTrail.render();
		
		collisionDetected = isCollidingWithTrail();
		
		if (collisionDetected)
			System.out.println("Collision detected");
		
		drawExplorers();
		
		colony1.render();
		//if (location.getProvider() == "none")
			//text("Location data is unavailable. \n" + "Please check your location settings.", width/2, height/2);
		/*
		else if	(second() % 2 == 0){
			mappedVector = convertCoords(latitude, longitude);
			fill(255);
			stroke(0);
			strokeWeight(2);
			ellipse(mappedVector.x, mappedVector.y, 5,5);
		}
		*/
		
		//else{

		//}
			 //text("Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Altitude: " + altitude + "\n" + "Accuracy: " + accuracy + "\n" + "Provider: " + location.getProvider(), width/2, height/2);
	}
/*
	public void onLocationEvent(double _latitude, double _longitude,double _altitude, float _accuracy) { // longitude = _longitude;
			latitude = (float)_latitude;
			altitude = (float)_altitude;
			accuracy = (float)_accuracy;
			System.out.println("lat/lon/alt/acc: " + latitude + "/" + longitude + "/" + altitude + "/" + accuracy);
			}
*/
	public void createTrail(int theValue){
		System.out.println("within Create Trail");
		tempTrail.randomPopulate();
		tempTrail.interpolateTrail();
	}
	
	public void drawExplorers(){
		for(Explorer exp: explorersList){
			exp.render();
		}
	}
	
	//Checks every explorer to see if it is colliding with circles that constitute the trail:
	public boolean isCollidingWithTrail(){
		boolean isColliding = false;
		
		for(Explorer exp: explorersList){
			for(Circle circ: tempTrail.circleList){
				
				isColliding = exp.isColliding(circ);
				
				//change color attribute of ant that is colliding with trail:
				if(isColliding){
					exp.color = 0;
					System.out.println("isColliding = " + isColliding);
					return isColliding;
				}
				else
					exp.color = 255;
			}
		}
		
		return isColliding;
	}
	
	public PVector convertCoords(float lat, float lon){
		float latMin, latMax, lonMin, lonMax;
		
		latMin = 41.879888f;
		latMax = 41.879283f;
		lonMin = -87.643111f;
		lonMax = -87.644015f;
		
		float mappedLat = map(lat, latMin, latMax, 0, displayWidth);
		float mappedLon = map(lon, lonMin, lonMax, displayHeight, 0);

		return new PVector(mappedLat, mappedLon);
	}
	

	
}
