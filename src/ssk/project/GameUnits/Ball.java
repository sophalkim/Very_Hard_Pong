package ssk.project.GameUnits;

import java.util.Random;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class Ball extends GameUnit {

	public float vX;
	public float vY;
	int angle;
	int paddleSfx;
	int wallSfx;
	Random r = new Random();
	
	public Ball(boolean playSound) {
		this.playSound = playSound;
	}
	
	public Ball(View v, Context context, int screenW, int screenH, boolean playSound) {
		this.playSound = playSound;
		this.screenW = screenW;
		this.screenH = screenH;
		paddleSfx = soundPool.load(context, R.raw.bounce_paddle, 1);
		wallSfx = soundPool.load(context, R.raw.bounce_wall, 1);
		bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.metal_ball);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenW / 15, screenH / 20, false);
		w = bitmap.getWidth();
		h = bitmap.getHeight();
		angle = 0;
		x = (int) (screenW / 2) - (w / 2);
		y = 0;
		vY = screenH / 100;
		randomDirection();
	}
	
	public Ball(Context context, Ball b, boolean playSound) {
		this.playSound = playSound;
		screenW = b.screenW;
		screenH = b.screenH;
		bitmap = b.bitmap;
		x = b.x;
		y = b.y - 20;
		w = b.w;
		h = b.h;
		vX = (-1) * b.vX;
		vY = screenH / 150;
		angle = 0;
	}
	
	public void update(Canvas canvas) {
		y += (int) vY;
		x += (int) vX;
		bounceWall();
		rotateBall(canvas);
	}
	
	public void update2(Canvas canvas, Ball b) {
		y += (int) vY;
		x += (int) vX;
		bounceWall2(b);
		rotateBall(canvas);
	}
	
	public void bounceWall() {
		// Bounce off Top Wall
		if (y < 0) {
			vY = (-1) * vY;
			playWallSfx();
		}	
		// Bounce off Left/Right Wall
		if (vX < 0 && x < 0 || vX > 0 && x + w > screenW) {
			vX = (-1) * vX;
			playWallSfx();
		}
	}
	
	public void bounceWall2(Ball b) {
		// Bounce off Top Wall
		if (y < 0) {
			vY = (-1) * vY;
			b.playWallSfx();
		}	
		// Bounce off Left/Right Wall
		if (vX < 0 && x < 0 || vX > 0 && x + w > screenW) {
			vX = (-1) * vX;
			b.playWallSfx();
		}
	}
	
	public boolean bouncePaddle(Paddle p) {
		// Bounce off Paddle Left Edge
		if (vY > 0 && y + h < p.y + p.h && y + h > p.y && x + w > p.x && x < p.x + (p.w * .25)) {
			vY = (-1) * screenH / 100;
			if (vX < 0) {
				vX = -1 * (screenW / 75);
			} else {
				vX = screenW / 75;
			}
			playPaddleSfx();
			return true;
		}
		// Bounce off Paddle Middle
		if (vY >= 0 && y + h < p.y + p.h && y + h >= p.y && x + w >= p.x + (p.w * .25) && x <= p.x + (p.w * .75)) {
			vY = (-1) * screenH / 100;
			if (vX < 0) {
				vX = -1 * (screenW / 100);
			} else {
				vX = screenW / 100;
			}
			playPaddleSfx();
			return true;
		}
		// Bounce off Paddle Right Edge
		if (vY > 0 && y + h < p.y + p.h && y + h > p.y && x + w > p.x + (p.w * .75) && x < p.x + p.w) {
			vY = (-1) * screenH / 100;
			if (vX < 0) {
				vX = -1 * (screenW / 75);
			} else {
				vX = screenW / 75;
			}
			playPaddleSfx();
			return true;
		}
		return false;
	}
	
	public boolean bouncePaddle2(Paddle p, Ball b) {
		if (vY > 0 && y + h < p.y + p.h && y + h > p.y && x + w > p.x && x < p.x + p.w) {
			vY = (-1) * vY;
			b.playPaddleSfx();
			return true;
		}
		return false;
	}
	
	public void randomDirection() {
		if (r.nextInt(2) == 1) {
			vX = (screenW / 100);
		} else {
			vX = -1 * (screenW / 100);
		}
	}
	
	public void rotateBall(Canvas canvas) {
		if (angle++ > 360) {
			angle = 0;
		}
		canvas.save();
		canvas.rotate(angle, x + (w / 2), y + (h / 2));
		canvas.drawBitmap(bitmap, x, y, null);
		canvas.restore();
	}
	
	public void playPaddleSfx() {
		if (playSound) {
			soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
		}
	}
	
	public void playWallSfx() {
		if (playSound) {
			soundPool.play(wallSfx, 1, 1, 1, 0, 1);
		}
	}
}