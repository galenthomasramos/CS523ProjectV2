//package com.cs523proj;

public class Circle {

	public float radius;
	public Point point;

	public Circle() {
		point = new Point(0, 0);
		radius = 1;
	}

	public Circle(float x, float y, float r) {
		point = new Point(x, y);
		radius = r;
	}

	public float getRadius() {
		return radius;
	}

	public double getArea() {
		return Math.PI * (radius * radius);
	}

	public double getPerimeter() {
		return 2 * Math.PI * radius;
	}
}