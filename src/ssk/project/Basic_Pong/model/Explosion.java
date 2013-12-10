package ssk.project.Basic_Pong.model;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Explosion {
	
	public static final int STATE_ALIVE = 0;
	public static final int STATE_DEAD = 1;
	
	private Particle[] particles;
	private int x, y;
	private int size;
	private int state;
	
	public Explosion(int particleNr, int x, int y) {
		setState(STATE_ALIVE);
		particles = new Particle[particleNr];
		for (int i=0; i<particles.length; i++) {
			Particle p = new Particle(x, y);
			particles[i] = p;
		}
		size = particleNr;
	}
	
	public Particle[] getParticles() {
		return particles;
	}	
	public void setParticles(Particle[] particles) {
		this.particles = particles;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public boolean isAlive() {
		return state == STATE_ALIVE;
	}	
	
	public boolean isDead() {
		return state == STATE_DEAD;
	}
	
	public void update() {
		if (state != STATE_DEAD) {
			boolean isDead = true;
			for (int i=0; i<particles.length; i++) {
				if (particles[i].isAlive()) {
					particles[i].update();
					isDead = false;
				}
			}
			if (isDead)
				state = STATE_DEAD;
		}
	}
	
	public void update(Rect container) {
		if (state != STATE_DEAD) {
			boolean isDead = true;
			for (int i=0; i<particles.length; i++) {
				if (particles[i].isAlive()) {
					particles[i].update(container);
					isDead = false;
				}
			}
			if (isDead)
				state = STATE_DEAD;
		}
	}
	
	public void draw(Canvas canvas) {
		for (int i=0; i<particles.length; i++) {
			if (particles[i].isAlive()) {
				particles[i].draw(canvas);
			}
		}
	}
}
