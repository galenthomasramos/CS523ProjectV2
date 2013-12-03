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
	boolean cheating = false;
	
	ArrayList<Explorer> explorersList;

	boolean collisionDetected;
	int last_known_pos = -1;
	
	public void setup(){
		colorMode(HSB);
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
		
		cp5.addButton("createFoodSource")
		.setValue(2)
		.setPosition(thePVector)
		
		tempTrail.randomPopulate();
		tempTrail.interpolateTrail();
		
		frameRate(60);
		colony1Pos = new PVector(this.width/5, this.height/5);
		colony1 = new Colony(this, colony1Pos, 35, 40);
		
		explorersList = new ArrayList<Explorer>();
		explorersList.add(new Explorer(this, new PVector(0,0), 10, 150));
	}
	
	public void draw(){
		background(150);	
		
		tempTrail.render();
		colony1.render();
		
		int old = tempTrail.current_collision;
//		System.out.println("last "+last_known_pos);
		collisionDetected = isCollidingWithTrail();
		
		if (collisionDetected) {
			System.out.println("Collision detected");
			System.out.println("New collision position is circle "+tempTrail.current_collision + " old "+old);
			if (old<tempTrail.current_collision-30 && old>0) {
				last_known_pos = old;
				cheating = true;
			}
			else if (tempTrail.current_collision<=last_known_pos) {
				cheating = false;
				last_known_pos = tempTrail.current_collision;
			}
		} else {
			System.out.println("Last known position is circle "+tempTrail.current_collision);
		}
		
		drawExplorers();

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
		text("Cheating: "+Boolean.toString(cheating), width*0.8f, height*0.9f);
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
		
		for(Explorer exp: explorersList){
			if(tempTrail.isColliding(exp))
				return true;
		}
		
		return false;
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
