//package com.cs523proj;

public class Point {
	float x;
	float y;
	
	   public Point(float x, float y) {
	      // .. set x and Y coord
		   this.x = x;
		   this.y = y;
	   }

	   // Getters and Setters
	   public float getX() {
	      return x;
	   }

	  public float getY() {
	      return y;
	  }

	  public String toPrint() {
	      return "[" + x + "," + y + "]";
	  }
	  // Your other Pofloat methods...
	}