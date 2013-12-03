//package com.cs523proj;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

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
	int players = 4;
	int last_known_pos = 3;
	Date last_collision;
	Date started_cheating;
	int THRESHOLD = 2000;

	int wait = -1;

	Date last_colony_duty;
	
	PImage antImage;
	
	public void setup(){
		colorMode(HSB);
		size(400, 400);
		tempTrail = new Trail(this, 10);
		orientation(LANDSCAPE);
		textAlign(CENTER, CENTER);
		textSize(12);
		
		collisionDetected = false;
		
		PVector buttonSize = new PVector(80,20);
		
		//location = new KetaiLocation(this);
		cp5 = new ControlP5(this);

		colony1Pos = new PVector(this.width/5, this.height/5);
		colony1 = new Colony(this,0, colony1Pos, 35, 220);
		colony1.setAnts(400);
		
		cp5.addButton("createTrail")
		    .setValue(1)
		    .setPosition(10,this.height - (int)buttonSize.y)
		    .setSize((int)buttonSize.x,(int)buttonSize.y)
		    ;
		

		cp5.addButton("createFoodSource")
			.setValue(2)
			.setPosition(20 + buttonSize.x, height - buttonSize.y)
		    .setSize((int)buttonSize.x,(int)buttonSize.y)
		    ;
		
		antImage = loadImage("files/pics/ant.png");
		antImage.resize(0, (height/15));

		frameRate(60);

		colony1Pos = new PVector(this.width/5, this.height/5);
		colony1 = new Colony(this,0, colony1Pos, 35, 40);
		colony1.setAnts(400);
		
		explorersList = new ArrayList<Explorer>();
		explorersList.add(new Explorer(this,0, new PVector(0,0), 10, 150,1));

		
		last_colony_duty = new Date();
		wait = randomWait();
		redistributeAnts();		

	}

	public void draw(){
		background(150);	
		
		tempTrail.render();
		colony1.render();
		
		int old = tempTrail.current_collision;
//		System.out.println("last "+last_known_pos);
		collisionDetected = isCollidingWithTrail();

		Date now = new Date();
		if (collisionDetected) {
			last_collision = GregorianCalendar.getInstance().getTime();
//			System.out.println("Collision detected");
//			System.out.println("New collision position is circle "+tempTrail.current_collision + " old "+old);
			if (old<tempTrail.current_collision-30 && old>0) {
				last_known_pos = old;
				cheating = true;
				started_cheating = new Date();
			}
			else if (tempTrail.current_collision<=last_known_pos) {
				cheating = false;
				started_cheating = null;
				last_known_pos = tempTrail.current_collision;
			}
		} else {
			if (last_collision != null && !cheating)
				if (now.getTime()-last_collision.getTime() > THRESHOLD){
//					System.out.println("Registering as cheating");
					cheating = true;
					started_cheating = new Date();
				}
				
//			System.out.println("Last known position is circle "+tempTrail.current_collision);
		}
		
		drawExplorers();
		
		if(cheating && now.getTime()-started_cheating.getTime()>THRESHOLD) {
			started_cheating = now;
			if (explorersList.get(0).ants >0)
				colony1.lostAntOrCheating(players,explorersList.get(0).antID);
		}
		
		if(now.getTime()-last_colony_duty.getTime()>wait) {
			last_colony_duty = now;
			wait = randomWait();
			colony1.colonyDutyFulfilled(explorersList.get(0).ants/10,explorersList.get(0).antID);
		}
			

		fill(255);
		textSize(14);
		text("Cheating: "+Boolean.toString(cheating), width*0.92f, height*0.9f);
		text("Colony: "+Integer.toString(colony1.ants), width*0.92f, height*0.15f);
		
		textSize(22);
		text("Score:", width * 0.92f, height * 0.1f);
		this.image(antImage, width * 0.87f, height * 0.07f);
	}
	
	// between 3 and 10 seconds
	private int randomWait() {
		Random rn = new Random();
		return 1000*(rn.nextInt(7));
	}
	
	public void redistributeAnts () {
		for (Explorer e : explorersList) {
			e.setAntsNumber(colony1.ants/players);
		}

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
//		tempTrail.randomPopulate(colony1Pos.x,colony1Pos.y,width-1, height-1);
		tempTrail.populate(new PVector(width-20,height-20), colony1Pos);
		ellipse(width-20, height-20, 50, 50);
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
