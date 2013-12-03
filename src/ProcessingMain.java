//package com.cs523proj;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import processing.core.*;

//import ketai.sensors.*;
//import ketai.ui.*;

import controlP5.*;

public class ProcessingMain extends PApplet {
	ControlP5 cp5;
	
	float diameter = 100.0f;
	Colony colony1;
	
	int colony1Color;
	PVector colony1Pos;
	
	ArrayList<FoodSpot> foodSpots;
	//ArrayList<Trail> trails;
	
	PVector mappedVector = new PVector(0,0);
	
	//KetaiLocation location;
	float accuracy;
	float longitude, latitude, altitude;
	
	Trail tempTrail;
	//Trail tempTrail = new Trail(this, 10);
	boolean cheating = false;
	
	ArrayList<Explorer> explorersList;

	boolean trailCollisionDetected;
	boolean foodSpotCollisionDetected;
	
	int players = 4;
	int last_known_pos = 3;
	Date last_collision;
	Date started_cheating;
	int THRESHOLD = 2000;

	int wait = -1;

	Date last_colony_duty;
	
	PImage antImage;
	
	//KetaiVibrate motor;
	
	public void setup(){
		colorMode(HSB);
		size(1100, 550);
		tempTrail = new Trail(this, 10);
		orientation(LANDSCAPE);
		textAlign(CENTER, CENTER);
		textSize(12);
		
		//motor = new KetaiVibrate(this);
		
		trailCollisionDetected = false;
		foodSpotCollisionDetected = false;
		
		PVector buttonSize = new PVector(80,20);
		
		//location = new KetaiLocation(this);
		cp5 = new ControlP5(this);

		colony1Pos = new PVector(this.width/5, this.height/5);
		colony1 = new Colony(this,0, colony1Pos, 35, 220);
		colony1.setAnts(400);
		
		/*
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
		*/
		
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
		
		foodSpots = new ArrayList<FoodSpot>();
		//trails = new ArrayList<Trail>();
		popFoodSpots();
	}

	public void draw(){
		background(150);	
		
		tempTrail.render();
		colony1.render();
		
		int old = tempTrail.current_collision;
//		System.out.println("last "+last_known_pos);
		trailCollisionDetected = isCollidingWithTrail();
		Date now = new Date();
		
		
		if (trailCollisionDetected) {
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
		foodSpotCollisionDetected = isCollidingWithFoodSpot();
		if(foodSpotCollisionDetected){
			
			
		}
		
		renderFoodSpots();
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
			
		//foodSpotCollisionEvent();

		
		fill(255);
		textSize(14);
		text("Cheating: "+Boolean.toString(cheating), width*0.92f, height*0.9f);
		text("Colony: "+Integer.toString(colony1.ants), width*0.92f, height*0.15f);
		
		textSize(22);
		text("Score:", width * 0.92f, height * 0.1f);
		this.image(antImage, width * 0.87f, height * 0.07f);
	}
	
	//public void foodSpotCollisionEvent(){
	public boolean isCollidingWithFoodSpot(){
		
		boolean isColliding = false;
		
		for(Explorer exp: explorersList){
			for(FoodSpot fs: foodSpots){
				if(fs.isColliding(exp.buffer)){
					isColliding = true;
					System.out.println("Is colliding with food spot");
				}
			}
		}
		
		return isColliding;
	}
	
	public void popFoodSpots(){
		
		int foodSpotRadius = 25;
		
		PVector[] pVecs = {new PVector(width * 0.8f, height * 0.1f),
				new PVector(width * 0.78f, height * 0.4f),
				new PVector(width * 0.7f, height * 0.7f),
				new PVector(width * 0.5f, height * 0.85f),
				new PVector(width * 0.1f, height * 0.9f)};
		
		for(int i = 0; i < 5; i++)
			foodSpots.add(new FoodSpot(this, pVecs[i], foodSpotRadius, 105, colony1));
	}
	
	public void renderFoodSpots(){
		for (FoodSpot fs: foodSpots)
			fs.render();
	}
	
	/*
	public void popTrail(FoodSpot fs){
		Trail newTrail = new Trail(this, 10);
		newTrail.populate(fs.position, colony1.position);//   *New random populate function
		trails.add(newTrail);
	}
	*/
	
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
	
	/*
	public void createTrail(int theValue){
		System.out.println("within Create Trail");
//		tempTrail.randomPopulate(colony1Pos.x,colony1Pos.y,width-1, height-1);
		tempTrail.populate(new PVector(width-20,height-20), colony1Pos);
		ellipse(width-20, height-20, 50, 50);
		tempTrail.interpolateTrail();
	}
	*/
	

	public void drawExplorers(){
		for(Explorer exp: explorersList){
			exp.render();
		}
	}
	
	//Checks every explorer to see if it is colliding with circles that constitute the trail:
	public boolean isCollidingWithTrail(){
		
		for(Explorer exp: explorersList){
			for(FoodSpot fs: foodSpots){
				if(fs.trail != null)
					if(fs.trail.isColliding(exp))
						return true;
			}
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
	
	/*
	void vibrate(long[] pattern){
		if(motor.hasVibrator())
			motor.vibrate();
		else {
			println("No Vibration Service Available");
		}
		
	}
	*/
}
