package ua.space.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Enemy {
	private String name;
	private int hp;
	private double size;
	private double x;
	private double y;
	private float angle;
	private float speed;
	private boolean fire;

	private Image image;
	private Area shap;

	public Enemy(String name, int hp, double size, double x, double y, float angle, float speed, Image image, boolean fire) {
		this.name = name;
		this.hp = hp;
		this.size = size;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
		this.image = image;
		this.fire = fire;

		Path2D p = new Path2D.Double();
		int value = (int) size;
		p.append(new Rectangle(value / 2 - value / 2, value / 2 - value / 2, value, value), true);
		shap = new Area(p);
	}
	
	
	@Override
	public String toString() {
		return "Enemy [name=" + name + ", hp=" + hp + ", size=" + size + ", x=" + x + ", y=" + y + ", angle=" + angle
				+ ", speed=" + speed + ", fire=" + fire + ", image=" + image + ", shap=" + shap + "]";
	}


	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
	}

	public void setAngle(float angle) {
		if (angle < 0) {
			angle = 359;
		} else if (angle > 359) {
			angle = 0;
		}
		this.angle = angle;
	}

	public void draw(Graphics2D graphics2d) {
		AffineTransform oldTran = graphics2d.getTransform();
		graphics2d.translate(x, y);
		AffineTransform tran = new AffineTransform();
		tran.rotate(Math.toRadians(angle + 90), size / 2, size / 2);
		graphics2d.drawImage(image, tran, null);
		graphics2d.setTransform(oldTran);
	}

	public Area getShap() {
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		at.rotate(Math.toRadians(angle), size / 2, size / 2);
		return new Area(at.createTransformedShape(shap));
	}

	public boolean check(int width, int heigth) {
		Rectangle2D size = getShap().getBounds2D();
		return (x < -size.getWidth() || y < -size.getHeight() || x > width || y > heigth) ? false : true;
	}

	public int getHp() {
		return hp;
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

	public double getCentralX() {
		return x + size / 2;
	}

	public double getCentralY() {
		return y + size / 2;
	}

	public Image getImage() {
		return image;
	}

	public boolean isFire() {
		return fire;
	}

	public String getName() {
		return name;
	}
}
