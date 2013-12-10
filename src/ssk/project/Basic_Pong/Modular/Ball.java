package ssk.project.Basic_Pong.Modular;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Ball extends GameUnit {

	float vX;
	float vY;
	int angle;
	int paddleSfx;
	int wallSfx;
	
	public Ball() {}
	public Ball(Context context) {
		paddleSfx = soundPool.load(context, R.raw.bounce_paddle, 1);
		wallSfx = soundPool.load(context, R.raw.bounce_wall, 1);
	}
	public Ball(int x, int y, int w, int h, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rect = new Rect(x, y, x + w, y + h);
		this.bitmap = bitmap;
	}
	
	public void playPaddleSfx() {
		soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
	}
	
	public void playWallSfx() {
		soundPool.play(wallSfx, 1, 1, 1, 0, 1);
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
	
	public void bounceWall(int screenH, int screenW) {
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
	
	public boolean bouncePaddle(Paddle p, int screenH, int screenW) {
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
}