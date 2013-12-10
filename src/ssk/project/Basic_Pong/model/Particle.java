package ssk.project.Basic_Pong.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Particle {
	
	public static final int STATE_ALIVE = 0;
	public static final int STATE_DEAD = 1;
	
	public static final int DEFAULT_LIFETIME = 200;
	public static final int MAX_DIMENSION = 5;
	public static final int MAX_SPEED = 10;
	
	private int state;
	private float widht;
	private float height;
	private float x, y;
	private double xv, yv;
	private int age;
	private int lifetime;
	private int color;
	private Paint paint;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public float getWidth() {
		return widht;
	}
	public void setWidth(float width) {
		this.widht = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public double getXv() {
		return xv;
	}
	public void setXv(double xv) {
		this.xv = xv;
	}
	public double getYv() {
		return yv;
	}
	public void setYv(double yv) {
		this.yv = yv;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getLifetime() {
		return lifetime;
	}
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public boolean isAlive() {
		return this.state == STATE_ALIVE;
	}
	
	public boolean isDead() {
		return this.state == STATE_DEAD;
	}
	
	public Particle(int x, int y) {
		this.x = x;
		this.y = y;
		state = Particle.STATE_ALIVE;
		widht = rndInt(1, MAX_DIMENSION);
		height = widht;
		lifetime = DEFAULT_LIFETIME;
		age = 0;
		xv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
		yv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
		if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
			xv *= 0.7;
			yv *= 0.7;
		}
		color = Color.argb(255, rndInt(0,255), rndInt(0, 255), rndInt(0, 255));
		paint = new Paint(color);
	}
	
	public void reset(float x, float y) {
		this.x = x;
		this.y = y;
		state = Particle.STATE_ALIVE;
		age = 0;
	}
	
	static int rndInt(int min, int max) {
		return (int) (min + Math.random() * (max - min +1));
	}
	
	static double rndDbl(double min, double max) {
		return min + (max-min)*Math.random();
	}
	
	public void update() {
		if (state != STATE_DEAD) {
			x += xv;
			y += yv;
			
			int a = color >>> 24;
			a -= 2;
			if (a <= 0) {
				state = STATE_DEAD;
			} else {
				color = (color & 0x00ffffff) + (a << 24);
				paint.setAlpha(a);
				age++;
			}
			if (age >= lifetime) {
				state = STATE_DEAD;
			}
		}
	}
	
	public void update(Rect container) {
		if (isAlive()) {
			if (x <= container.left || x >= container.right - widht) {
				xv *= -1;
			}
			if (y <= container.top || y >= container.bottom - height) {
				yv *= -1;
			}
		}
		update();
	}
	
	public void draw(Canvas canvas) {
		paint.setColor(color);
		canvas.drawRect(x, y, x + widht, y + height, paint);
	}
}
