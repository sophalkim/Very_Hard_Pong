package ssk.project.Basic_Pong.Modular;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class SubLevel extends BaseLevel {

	Ball b2;
	boolean secondBallStarting = false;
	boolean secondBallDrawing = false;
	PowerUp pu;
	
	public SubLevel(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		pu = new PowerUp(this, context, screenW, screenH);
	}
	
	public synchronized boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		pu.onClick(event);
		return true;
	}
	
	public void updatePowerUp(Canvas canvas) {
		if (ballHits == 3 && !pu.visible && pu.available) {
			pu.show();			
		}
		if (pu.clicked) {
			p.bitmap = Bitmap.createScaledBitmap(p.bitmap, screenW / 3, screenH / 40, false);
			p.w = p.bitmap.getWidth();
			p.h = p.bitmap.getHeight();
			pu.playPowerUpSfx();
			pu.clicked = false;
			pu.available = false;
			pu.visible = false;
		}
		pu.update(canvas);
	}
	
	public void initBall2() {
		b2 = new Ball();
		b2.bitmap = b.bitmap;
		b2.x = b.x;
		b2.y = b.y - 20;
		b2.w = b.w;
		b2.h = b.h;
		b2.vX = (-1) * b.vX;
		b2.vY = screenH / 150;
		b2.angle = 0;
		b2.isVisible = true;
	}
	
	public void loseCondition() {
		super.loseCondition();
		// Ball 2
		if (secondBallDrawing) {
			if (b2.y > (screenH - b.h)) {
				thread.setRunning(false);
				pause = true;
				((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	public void setBall2() {
		if (ballHits == 4) {
			secondBallStarting = true;
		}	
		if (secondBallStarting) {
			initBall2();
			secondBallStarting = false;
			secondBallDrawing = true;
		}
	}
	
	public void updateBall2(Canvas canvas) {
		if (secondBallDrawing) {
			b2.y += (int) b2.vY;
			b2.x += (int) b2.vX;
			collideWithPaddle2();
			collideWithWall2();
			// Ball Rotation
			if (b2.angle++ > 360) {
				b2.angle = 0;
			}
			canvas.save();
			canvas.rotate(b2.angle, b2.x + (b2.w / 2), b2.y + (b2.h / 2));
			canvas.drawBitmap(b2.bitmap, b2.x, b2.y, null);
			canvas.restore();
		}
	}
	
	public void collideWithPaddle2() {
		if (secondBallDrawing) {
			// Ball 2 Bounce off Paddle  Left Edge
			if (b2.vY > 0 && b2.y + b2.h < p.y + p.h && b2.y + b2.h > p.y && b2.x + b2.w > p.x && b2.x < p.x + (p.w * .25)) {
				b2.vY = -1 * b2.vY;
				if (b.vX < 0) {
					b.vX = -1 * (screenW / 75);
				} else {
					b.vX = screenW / 75;
				}
				ballHits++;
				b.playPaddleSfx();
			}
			// Ball 2 Bounce off Paddle  Middle
			if (b2.vY >= 0 && b2.y + b2.h < p.y + p.h && b2.y + b2.h >= p.y && b2.x + b2.w >= p.x + (p.w * .25) && b2.x <= p.x + (p.w * .75)) {
				b2.vY = -1 * b2.vY;
				if (b.vX < 0) {
					b.vX = -1 * (screenW / 100);
				} else {
					b.vX = screenW / 100;
				}
				ballHits++;
				b.playPaddleSfx();
			}
			// Ball 2 Bounce off Paddle Right Edge
			if (b2.vY > 0 && b2.y + b2.h < p.y + p.h && b2.y + b2.h > p.y && b2.x + b2.w > p.x + (p.w * .75) && b2.x < p.x + p.w) {
				b2.vY = -1 * b2.vY;;
				if (b.vX < 0) {
					b.vX = -1 * (screenW / 75);
				} else {
					b.vX = screenW / 75;
				}
				ballHits++;
				b.playPaddleSfx();
			}
		}
	}
	
	public void collideWithWall2() {
		if (secondBallDrawing) {
			// Bounce off Top wall
			if  (b2.y < 0) {
				b2.vY = (-1) * b2.vY;
				b.playWallSfx();
			}
			// Bounce off Left/Right Wall
			if (b2.vX < 0 && b2.x < 0 || b2.vX > 0 && b2.x + b2.w > screenW) {
				b2.vX = (-1) * b2.vX;
				b.playWallSfx();
			}
		}
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		setBall2();
		updateBall2(canvas);
		updatePowerUp(canvas);
		loseCondition();
	}
}