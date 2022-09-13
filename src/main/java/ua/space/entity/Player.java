package ua.space.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

public class Player {
	public static final double PLAYER_SIZE = 64;
	public int hp;
	private double x;
	private double y;
	private float angle;

	private float speed;
	private float maxSpeed;
	private boolean speedUP;

	private Image image;
	private Image imageSpeed;
	private Area shap;

	public Player(int hp, double x, double y, float speed, float maxSpeed, Image image, Image imageSpeed) {
		this.hp = hp;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.maxSpeed = maxSpeed;
		this.image = image;
		this.imageSpeed = imageSpeed;

		Path2D p = new Path2D.Double();
		int value = (int) PLAYER_SIZE;
		p.append(new Rectangle(value / 2 - value / 2, value / 2 - value / 2, value, value), true);
		shap = new Area(p);
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
	}

	public Area getShap() {
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		at.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
		return new Area(at.createTransformedShape(shap));
	}

	public void draw(Graphics2D graphics2d) {
		AffineTransform oldTran = graphics2d.getTransform();
		graphics2d.translate(x, y);
		AffineTransform tran = new AffineTransform();
		tran.rotate(Math.toRadians(angle + 90), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
		graphics2d.drawImage(speedUP ? imageSpeed : image, tran, null);
		graphics2d.setTransform(oldTran);
	}

	public void setAngle(float angle) {
		if (angle < 0) {
			angle = 359;
		} else if (angle > 359) {
			angle = 0;
		}
		this.angle = angle;
	}

	public void speedUP() {
		speedUP = true;
		if (speed > maxSpeed) {
			speed = maxSpeed;
		} else {
			speed += 0.01f;
		}
	}

	public void speedDown() {
		speedUP = false;
		if (speed <= 0) {
			speed = 0;
		} else {
			speed -= 0.005f;
		}
	}

	public double getCentralX() {
		return x + PLAYER_SIZE / 2;
	}

	public double getCentralY() {
		return y + PLAYER_SIZE / 2;
	}

	public int lossHp(int demage) {
		return hp -= demage;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public float getAngle() {
		return angle;
	}

	public int getHp() {
		return hp;
	}
}
