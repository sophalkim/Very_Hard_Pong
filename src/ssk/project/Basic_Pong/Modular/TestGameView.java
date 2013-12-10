package ssk.project.Basic_Pong.Modular;
import java.util.Random;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class TestGameView extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenW;
	int screenH;
	Bitmap bgBitmap;
	TestGameThread thread;
	Paddle p;
	Ball b;
	Ball b2;
	// Ball 2
	boolean secondBallStarting = false;
	boolean secondBallDrawing = false;
	// Sound Effects
	SoundPool soundPool;
	int ballHitWall;
	int ballHitPaddle;
	int powerUpSound;
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Random r = new Random();
	// Power Up
	PowerUp pu;
	boolean isPowerUpAvailable = true;
	boolean isPowerUpVisible = false;
	boolean isPowerUpReversing = false;
	boolean isPowerUpClicked = false;
	boolean isPowerUpScaling = false;
	
	public TestGameView(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		powerUpSound = soundPool.load(context, R.raw.power_up_sound_effect, 1);
		
		paint.setColor(Color.GREEN);
		paint.setTextSize(40);
		goalPaint.setColor(Color.BLUE);
		goalPaint.setTextSize(40);
		
		getHolder().addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, false);
		initBall();
		initPaddle();
		initPowerUp();
	}
	
	public void initPaddle() {
		p = new Paddle();
		p.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava_paddle);
		p.bitmap = Bitmap.createScaledBitmap(p.bitmap, screenW / 4, screenH / 40, false);
		p.x = screenW / 2 - (p.bitmap.getWidth() / 2);
		p.y = screenH - screenH / 10;
		p.w = p.bitmap.getWidth();
		p.h = p.bitmap.getHeight();
		p.rect = new Rect(p.x, p.y, p.x + p.w, p.y + p.h);
	}
	
	public void initBall() {
		b = new Ball();
		b.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);
		b.bitmap = Bitmap.createScaledBitmap(b.bitmap, screenW / 15, screenH / 20, false);
		b.w = b.bitmap.getWidth();
		b.h = b.bitmap.getHeight();
		b.angle = 0;
		b.x = (int) (screenW / 2) - (b.w / 2);
		b.y = 0;
		b.vY = screenH / 100;
		if (r.nextInt(2) == 1) {
			b.vX = (screenW / 100);
		} else {
			b.vX = -1 * (screenW / 100);
		}
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
	
	public void initPowerUp() {
		pu = new PowerUp();
		pu.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.power_up);
		pu.bitmap = Bitmap.createScaledBitmap(pu.bitmap, screenW / 5, screenH / 8, false);
		pu.w = pu.bitmap.getWidth();
		pu.h = pu.bitmap.getHeight();
		pu.x = (int) (screenW - pu.w * 1.2);
		pu.y = screenH / 2;
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if (isPowerUpVisible) {
					if (event.getX() >= pu.x && event.getX() <= pu.x + pu.w && event.getY() >= pu.y && event.getY() <= pu.y + pu.h) {
	 					isPowerUpClicked = true;
	 					break;
	 				}
				}
			}
			case MotionEvent.ACTION_MOVE: {
				if (Math.abs(p.x - (int) event.getX()) <= p.w) {
					p.x = (int) event.getX();
				}
				p.isMoving = true;
				break;
			}		
			case MotionEvent.ACTION_UP: {
				p.isMoving = false;
				break;
			}
		}
		return true;
	}
	
	public void updatePowerUp(Canvas canvas) {
		if (isPowerUpClicked) {
			p.bitmap = Bitmap.createScaledBitmap(p.bitmap, screenW / 3, screenH / 40, false);
			p.w = p.bitmap.getWidth();
			p.h = p.bitmap.getHeight();
			soundPool.play(powerUpSound, 1, 1, 1, 0, 1);
			isPowerUpClicked = false;
			isPowerUpAvailable = false;
			isPowerUpVisible = false;
		}
		if (ballHits == 3 && !isPowerUpVisible && isPowerUpAvailable) {
			soundPool.play(powerUpSound, 1, 1, 1, 0, 1);
			isPowerUpVisible = true;			
		}
		if (isPowerUpVisible) {
			// Rotating PowerUP
			if (!isPowerUpReversing) {
				pu.angle++;
			}
			if (isPowerUpReversing) {
				pu.angle--;
			}
			if (Math.abs(pu.angle) == 10) {
				isPowerUpReversing = !isPowerUpReversing;
			}
			// Scale PowerUP
			if (!isPowerUpScaling) {
				pu.scale = (float) (pu.scale + .1);
			}
			if (isPowerUpScaling) {
				pu.scale = (float) (pu.scale - .1);
			}
			if (Math.abs(pu.scale) == 1.5) {
				isPowerUpScaling = !isPowerUpScaling;
			}
			canvas.save();
			canvas.rotate(pu.angle, pu.x + (pu.w / 2), pu.y + (pu.h / 2));
			canvas.drawBitmap(pu.bitmap, pu.x, pu.y, null);
			canvas.restore();
		}
	}
	
	public void updateBall(Canvas canvas) {
		b.y += (int) b.vY;
		b.x += (int) b.vX;
		collideWithPaddle();
		collideWithWall();
		// Ball Rotation
		if (b.angle++ > 360) {
			b.angle = 0;
		}
		canvas.save();
		canvas.rotate(b.angle, b.x + (b.w / 2), b.y + (b.h / 2));
		canvas.drawBitmap(b.bitmap, b.x, b.y, null);
		canvas.restore();
	}
	
	public void updatePaddle(Canvas canvas) {
		if (p.x > screenW - p.w) {
			p.x = screenW - p.w;
		}
		p.render(canvas);
	}
	
	public void collideWithPaddle() {
		// Bounce off Paddle Left Edge
		if (b.vY > 0 && b.y + b.h < p.y + p.h && b.y + b.h > p.y && b.x + b.w > p.x && b.x < p.x + (p.w * .25)) {
			b.vY = (-1) * screenH / 100;
			if (b.vX < 0) {
				b.vX = -1 * (screenW / 75);
			} else {
				b.vX = screenW / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle Middle
		if (b.vY >= 0 && b.y + b.h < p.y + p.h && b.y + b.h >= p.y && b.x + b.w >= p.x + (p.w * .25) && b.x <= p.x + (p.w * .75)) {
			b.vY = (-1) * screenH / 100;
			if (b.vX < 0) {
				b.vX = -1 * (screenW / 100);
			} else {
				b.vX = screenW / 100;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle Right Edge
		if (b.vY > 0 && b.y + b.h < p.y + p.h && b.y + b.h > p.y && b.x + b.w > p.x + (p.w * .75) && b.x < p.x + p.w) {
			b.vY = (-1) * screenH / 100;
			if (b.vX < 0) {
				b.vX = -1 * (screenW / 75);
			} else {
				b.vX = screenW / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
	}
	
	public void collideWithWall() {
		// Bounce off Top Wall
		if  (b.y < 0) {
			b.vY = (-1) * b.vY;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}	
		// Bounce off Left/Right Wall
		if (b.vX < 0 && b.x < 0 || b.vX > 0 && b.x + b.w > screenW) {
			b.vX = (-1) * b.vX;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}
	}
	
	public void winCondition() {
		if (ballHits == 15) {
			thread.setRunning(false);
			pause = true;
			((TestGameActivity)getContext()).winScreen();
		}
	}
	
	public void loseCondition() {
		// Ball 1
		if (b.y > (screenH - b.h)) {
			thread.setRunning(false);
			pause = true;
			((TestGameActivity)getContext()).loseScreen();
		}
		// Ball 2
		if (secondBallDrawing) {
			if (b2.y > (screenH - b.h)) {
				thread.setRunning(false);
				pause = true;
				((TestGameActivity)getContext()).loseScreen();
			}
		}
	}
	
	public void updateStatusText(Canvas canvas) {
		canvas.drawText("Hits : " + ballHits, 50, 50, paint);
		canvas.drawText("Goal : 15", screenW / 2 + screenW / 4, screenH / 20, goalPaint);
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
				soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
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
				soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
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
				soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
			}
		}
	}
	
	public void collideWithWall2() {
		if (secondBallDrawing) {
			// Bounce off Top wall
			if  (b2.y < 0) {
				b2.vY = (-1) * b2.vY;
				soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
			}
			// Bounce off Left/Right Wall
			if (b2.vX < 0 && b2.x < 0 || b2.vX > 0 && b2.x + b2.w > screenW) {
				b2.vX = (-1) * b2.vX;
				soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		updatePaddle(canvas);
		updateBall(canvas);
		setBall2();
		updateBall2(canvas);
		updatePowerUp(canvas);
		updateStatusText(canvas);
		winCondition();
		loseCondition();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new TestGameThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}