package com.cs523proj;
import processing.core.*;
import ketai.sensors.*;

public class Processing_main extends PApplet {
	
	float diameter = 100.0f;
	Colony colony1;
	
	int colony1Color;
	PVector colony1Pos;
	
	PVector mappedVector = new PVector(0,0);
	
	KetaiLocation location;
	float accuracy;
	float longitude, latitude, altitude;
	

	public void setup(){
		//size(640, 480);
		orientation(LANDSCAPE);
		textAlign(CENTER, CENTER);
		textSize(36);
		
		location = new KetaiLocation(this);
	}
	
	public void draw(){
		background(150);		
		
		if (location.getProvider() == "none")
			text("Location data is unavailable. \n" + "Please check your location settings.", width/2, height/2);
		
		else if	(second() % 2 == 0){
			mappedVector = convertCoords(latitude, longitude);
			fill(255);
			stroke(0);
			strokeWeight(2);
			ellipse(mappedVector.x, mappedVector.y, 5,5);
		}
			
			 //text("Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Altitude: " + altitude + "\n" + "Accuracy: " + accuracy + "\n" + "Provider: " + location.getProvider(), width/2, height/2);
	}

	public void onLocationEvent(double _latitude, double _longitude,double _altitude, float _accuracy) { // longitude = _longitude;
			latitude = (float)_latitude;
			altitude = (float)_altitude;
			accuracy = (float)_accuracy;
			System.out.println("lat/lon/alt/acc: " + latitude + "/" + longitude + "/" + altitude + "/" + accuracy);
			}


		
	
	
	public PVector convertCoords(float lat, float lon){
		float latMin, latMax, lonMin, lonMax;
		
		//Heritage Green Park Coords
		latMin = 41.879888f;
		latMax = 41.879283f;
		lonMin = -87.643111f;
		lonMax = -87.644015f;
		
		float mappedLat = map(lat, latMin, latMax, 0, displayWidth);
		float mappedLon = map(lon, lonMin, lonMax, displayHeight, 0);

		return new PVector(mappedLat, mappedLon);
	}
	

}
