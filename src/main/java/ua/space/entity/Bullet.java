package ua.space.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Bullet {
	private double x;
	private double y;
	private float angle;
	private double size;
	private float speed;
	private int damage;

	private Shape shape;
	private Color color;

	public Bullet(double x, double y, float angle, double size, float speed, int damage, Color color) {
		this.x = x - (size / 2);
		this.y = y - (size / 2);
		this.angle = angle;
		this.size = size;
		this.speed = speed;
		this.damage = damage;
		this.shape = new Ellipse2D.Double(0, 0, size, size);
		this.color = color;
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
	}

	public boolean check(int width, int heigth) {
		return (x <= -size || y < -size || x > width || y > heigth) ? false : true;

	}

	public void draw(Graphics2D graphics2d) {
		AffineTransform oldTran = graphics2d.getTransform();
		graphics2d.setColor(color);
		graphics2d.translate(x, y);
		graphics2d.fill(shape);
		graphics2d.setTransform(oldTran);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSize() {
		return size;
	}

	public Area getShape() {
		return new Area(new Ellipse2D.Double(x, y, size, size));
	}

	public int getDamage() {
		return damage;
	}

	public Color getColor() {
		return color;
	}
}
