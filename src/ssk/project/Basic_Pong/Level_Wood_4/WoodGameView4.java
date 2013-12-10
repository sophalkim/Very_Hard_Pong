package ssk.project.Basic_Pong.Level_Wood_4;
import java.util.Random;

import ssk.project.Basic_Pong.model.Explosion;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class WoodGameView4 extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenWidth;
	int screenHeight;
	Bitmap backgroundBitmap;
	WoodGameThread4 thread;
	// Paddle 1
	int paddleX;
	int paddleY;
	int paddleWidth;
	int paddleHeight;
	boolean paddleMoving = false;
	Bitmap paddleBitmap;
	// Ball
	int ballX;
	int ballY;
	int ballWidth;
	int ballHeight;
	int initialY;
	float velocityX;
	float velocityY;
	int ballAngle;
	Bitmap ballBitmap;
	// Sound Effects
	private SoundPool soundPool;
	private int ballHitWall;
	private int ballHitPaddle;
	private int lightningAttack;
	private int bigLightningAttack;
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	Paint redPaint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Random r = new Random();
	// Timing for Lightning 1
	long previousTime = 0;
	long currentTime;
	long deltaTime;
	
	long pTime = 0;
	long cTime;
	long dTime;
	// Lightning 1
	Bitmap lightningBitmap;
	int lightningX;
	int lightningY;
	int lightningWidth;
	int lightningHeight;
	int lightningVelocity;
	int lightningAngle = 0;
	// Timing for Lightning 2
	long p2Time = 0;
	long c2Time;
	long d2Time;
	// Lightning 2
	Bitmap lightning2Bitmap;
	int lightning2X;
	int lightning2Y;
	int lightning2Width;
	int lightning2Height;
	int lightning2Velocity;
	int lightning2Angle = 0;
	// Timing for BigLightning
	long p3Time = 0;
	long c3Time;
	long d3Time;
	// BigLightning 
	Bitmap bigLightningBitmap;
	int bigLightningX;
	int bigLightningY;
	int bigLightningWidth;
	int bigLightningHeight;
	int bigLightningVelocity;
	int bigLightningAngle = 0;
	// Explosion
	private Explosion explosion;
	private static final int EXPLOSION_SIZE = 200;
	long waitTime = 0;
	long explosionTime = 0;
	long dexplosionTime = 0;
	// Background Scroll Variables
	int backgroundScrollPosition;
	int backgroundScrollSpeed;
	int backgroundWidth;
	int backgroundHeight;
	Bitmap backgroundReverseBitmap;
	boolean reverseBackgroundFirst = false;
	
	public WoodGameView4(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.twisted_metal_bar);
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);	
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood_flooring_background);
		lightningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lightning);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		lightningAttack = soundPool.load(context, R.raw.lightning_sound_effect, 1);
		bigLightningAttack = soundPool.load(context, R.raw.big_lightning_sound_effect, 1);
		
		paint.setColor(Color.GREEN);
		paint.setTextSize(40);
		redPaint.setColor(Color.RED);
		redPaint.setTextSize(100);
		redPaint.setTextAlign(Align.CENTER);
		redPaint.setShadowLayer(5, 0, 0, Color.BLACK);
		goalPaint.setColor(Color.BLUE);
		goalPaint.setTextSize(40);
		
		getHolder().addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenWidth = w;
		screenHeight = h;
		backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenWidth, screenHeight, false);
		// initialize ball
		ballBitmap = Bitmap.createScaledBitmap(ballBitmap, screenWidth / 15, screenHeight / 20, false);
		ballWidth = ballBitmap.getWidth();
		ballHeight = ballBitmap.getHeight();
		ballAngle = 0;
		ballX = (int) (screenWidth / 2) - (ballWidth / 2);
		ballY = 0;
		velocityY = screenHeight / 100;
		if (r.nextInt(2) == 1) {
			velocityX = (screenWidth / 100);
		} else {
			velocityX = -1 * (screenWidth / 100);
		}
		// Initialize Paddle
		paddleBitmap = Bitmap.createScaledBitmap(paddleBitmap, screenWidth / 4, screenHeight / 40, false);
		paddleWidth = paddleBitmap.getWidth();
		paddleHeight = paddleBitmap.getHeight() * 4;
		paddleX = screenWidth / 2 - (paddleWidth / 2);
		paddleY = screenHeight - screenHeight / 10;
		// Initialize Lightning
		lightningBitmap = Bitmap.createScaledBitmap(lightningBitmap, screenWidth / 20, screenHeight / 6, false);
		lightningWidth = lightningBitmap.getWidth();
		lightningHeight = lightningBitmap.getHeight();
		lightningX = r.nextInt(screenWidth - lightningWidth);
		lightningY = r.nextInt(screenHeight / 20);
		lightningVelocity = screenHeight / 20;
		// Initialize Lightning 2
		lightning2Bitmap = Bitmap.createScaledBitmap(lightningBitmap, screenWidth / 20, screenHeight / 6, false);
		lightning2Width = lightningBitmap.getWidth();
		lightning2Height = lightningBitmap.getHeight();
		lightning2X = r.nextInt(screenWidth - lightningWidth);
		lightning2Y = r.nextInt(screenHeight / 20);
		lightning2Velocity = screenHeight / 24;
		// Initialize BigLightning
		bigLightningBitmap = Bitmap.createScaledBitmap(lightningBitmap, screenWidth / 10, screenHeight / 3, false);
		bigLightningWidth = lightningBitmap.getWidth();
		bigLightningHeight = lightningBitmap.getHeight();
		bigLightningX = r.nextInt(screenWidth - lightningWidth);
		bigLightningY = r.nextInt(screenHeight / 20);
		bigLightningVelocity = screenHeight / 48;
		// Initialize Background Animation
		backgroundScrollPosition = 0;
		backgroundScrollSpeed = 1;
		backgroundWidth = backgroundBitmap.getWidth();
		backgroundHeight = backgroundBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(1, -1);
		backgroundReverseBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0, screenWidth, screenHeight, matrix, true);
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {	
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				if (Math.abs(paddleX - (int) event.getX()) <= paddleWidth) {
					paddleX = (int) event.getX();
				}
				paddleMoving = true;
				break;
			}		
			case MotionEvent.ACTION_UP: {
				paddleMoving = false;
				break;
			}
		}
		return true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		// canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		//Draw scrolling background.
        Rect fromRect1 = new Rect(0, 0, backgroundWidth, backgroundHeight - backgroundScrollPosition);
        Rect toRect1 = new Rect(0, backgroundScrollPosition, backgroundWidth, backgroundHeight);

        Rect fromRect2 = new Rect(0, backgroundHeight - backgroundScrollPosition, backgroundWidth, backgroundHeight);
        Rect toRect2 = new Rect(0, 0, backgroundWidth, backgroundScrollPosition);

        if (!reverseBackgroundFirst) {
            canvas.drawBitmap(backgroundBitmap, fromRect1, toRect1, null);
            canvas.drawBitmap(backgroundReverseBitmap, fromRect2, toRect2, null);
        }
        else{
            canvas.drawBitmap(backgroundBitmap, fromRect2, toRect2, null);
            canvas.drawBitmap(backgroundReverseBitmap, fromRect1, toRect1, null);
        }

        //Next value for the background's position.
        if ( (backgroundScrollPosition += backgroundScrollSpeed) >= backgroundHeight) {
            backgroundScrollPosition = 0;
            reverseBackgroundFirst = !reverseBackgroundFirst;
        }
		// Paddle 
		if (!pause) {
			if (paddleX > screenWidth - paddleWidth) {
				paddleX = screenWidth - paddleWidth;
			}
		canvas.drawBitmap(paddleBitmap, paddleX, paddleY, null);
		// Ball Velocity Update
		ballY += (int) velocityY;
		ballX += (int) velocityX;
		}
		// Ball Rotation
		if (ballAngle++ > 360) {
			ballAngle = 0;
		}
		canvas.save();
		canvas.rotate(ballAngle, ballX + (ballWidth / 2), ballY + (ballHeight / 2));
		canvas.drawBitmap(ballBitmap, ballX, ballY, null);
		canvas.restore();
		// Bounce off Paddle Left Edge
		if (velocityY > 0 && ballY + ballHeight < paddleY + paddleHeight && ballY + ballHeight > paddleY && ballX + ballWidth > paddleX && ballX < paddleX + (paddleWidth * .25)) {
			velocityY = (-1) * screenHeight / 100;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle Middle
		if (velocityY >= 0 && ballY + ballHeight < paddleY + paddleHeight && ballY + ballHeight >= paddleY && ballX + ballWidth >= paddleX + (paddleWidth * .25) && ballX <= paddleX + (paddleWidth * .75)) {
			velocityY = (-1) * screenHeight / 100;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 100);
			} else {
				velocityX = screenWidth / 100;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle Right Edge
		if (velocityY > 0 && ballY + ballHeight < paddleY + paddleHeight && ballY + ballHeight > paddleY && ballX + ballWidth > paddleX + (paddleWidth * .75) && ballX < paddleX + paddleWidth) {
			velocityY = (-1) * screenHeight / 100;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Top Wall
		if  (ballY < 0) {
			velocityY = (-1) * velocityY;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}	
		// Bounce off Left/Right Wall
		if (velocityX < 0 && ballX < 0 || velocityX > 0 && ballX + ballWidth > screenWidth) {
			velocityX = (-1) * velocityX;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}
		// Lose Condition
		if (ballY > (screenHeight - ballHeight)) {
			thread.setRunning(false);
			pause = true;
			((WoodGameActivity4)getContext()).loseScreen();
		}
		
		// Win Condition
		if (ballHits == 50) {
			thread.setRunning(false);
			pause = true;
			((WoodGameActivity4)getContext()).winScreen();
		}
		// Hits and Goal Text
		canvas.drawText("Hits : " + ballHits, screenWidth / 20, screenHeight / 20, paint);
		canvas.drawText("Goal : 50", screenWidth / 2 + screenWidth / 4, screenHeight / 20, goalPaint);
		// Setting Warning Text
		currentTime = System.currentTimeMillis();
		deltaTime = currentTime - previousTime;
		if (deltaTime >= 10000) {
			canvas.drawText("Warning", screenWidth / 2, screenHeight / 3, redPaint);
			if (deltaTime >= 12000) {
				previousTime = currentTime;
			}
		}		
		// Setting Lightning Enemy
		cTime = System.currentTimeMillis();
		dTime = cTime - pTime;
		if (dTime >= 11000) {
			soundPool.play(lightningAttack, 1, 1, 1, 0, 1);
			// Rotating Lightning
			if (lightningAngle++ > 5) {
				lightningAngle = -5;
			}
			canvas.save();
			canvas.rotate(lightningAngle, lightningX + (lightningWidth / 2), lightningY + (lightningHeight / 2));
			canvas.drawBitmap(lightningBitmap, lightningX, lightningY, null);
			canvas.restore();
			
			lightningY += (int) lightningVelocity;
			if (dTime >= 12000) {
				pTime = cTime;
				lightningY = r.nextInt(400);
				lightningX = r.nextInt(screenWidth - 10);
			}
		}		
		// Lose Condition if Lightning Touches paddle
		if (lightningY + lightningHeight - 30 >= paddleY &&
			lightningY <= paddleY &&
			lightningX + lightningWidth <= paddleX + paddleWidth &&
			lightningX + lightningWidth >= paddleX) {
				waitTime = System.currentTimeMillis();
				dexplosionTime = waitTime - explosionTime;
				pause = true;
				
				explosion = new Explosion(EXPLOSION_SIZE, paddleX + (paddleWidth / 2), paddleY);
				if (explosion != null && explosion.isAlive()) {
					explosion.update(getHolder().getSurfaceFrame());
				}
				if (explosion != null) {
					explosion.draw(canvas);
				}
				if (dexplosionTime >= 3000) {
					thread.setRunning(false);
					((WoodGameActivity4)getContext()).loseScreen();
				}
			}
		// Setting Lightning 2 Enemy
		c2Time = System.currentTimeMillis();
		d2Time = c2Time - p2Time;
		if (d2Time >= 8000) {
			soundPool.play(lightningAttack, 1, 1, 1, 0, 1);
			// Rotating Lightning
			if (lightning2Angle++ > 5) {
				lightning2Angle = -5;
			}
			canvas.save();
			canvas.rotate(lightning2Angle, lightning2X + (lightning2Width / 2), lightning2Y + (lightning2Height / 2));
			canvas.drawBitmap(lightning2Bitmap, lightning2X, lightning2Y, null);
			canvas.restore();
			
			lightning2Y += (int) lightning2Velocity;
			if (d2Time >= 9000) {
				p2Time = c2Time;
				lightning2Y = r.nextInt(200);
				lightning2X = r.nextInt(screenWidth - 10);
			}
		}
		// Lose Condition if Lightning 2 Touches paddle
		if (lightning2Y + lightning2Height - 30 >= paddleY &&
			lightning2Y <= paddleY &&
			lightning2X + lightning2Width <= paddleX + paddleWidth &&
			lightning2X + lightning2Width >= paddleX) {
				waitTime = System.currentTimeMillis();
				dexplosionTime = waitTime - explosionTime;
				pause = true;
				
				explosion = new Explosion(EXPLOSION_SIZE, paddleX + (paddleWidth / 2), paddleY);
				if (explosion != null && explosion.isAlive()) {
					explosion.update(getHolder().getSurfaceFrame());
				}
				if (explosion != null) {
					explosion.draw(canvas);
				}
				if (dexplosionTime >= 3000) {
					thread.setRunning(false);
					((WoodGameActivity4)getContext()).loseScreen();
				}
			}
		// Setting Lightning 2 Enemy
		c3Time = System.currentTimeMillis();
		d3Time = c3Time - p3Time;
		if (d3Time >= 12000) {
			soundPool.play(bigLightningAttack, 1, 1, 1, 0, 1);
			// Rotating Lightning
			if (bigLightningAngle++ > 5) {
				bigLightningAngle = -5;
			}
			canvas.save();
			canvas.rotate(bigLightningAngle, bigLightningX + (bigLightningWidth / 2), bigLightningY + (bigLightningHeight / 2));
			canvas.drawBitmap(bigLightningBitmap, bigLightningX, bigLightningY, null);
			canvas.restore();
			
			bigLightningY += (int) bigLightningVelocity;
			if (d3Time >= 13000) {
				p3Time = c3Time;
				bigLightningY = r.nextInt(200);
				bigLightningX = r.nextInt(screenWidth - 10);
			}
		}
		// Lose Condition if Lightning 2 Touches paddle
		if (bigLightningY + bigLightningHeight - 30 >= paddleY &&
			bigLightningY <= paddleY &&
			bigLightningX + bigLightningWidth <= paddleX + paddleWidth &&
			bigLightningX + bigLightningWidth >= paddleX) {
				waitTime = System.currentTimeMillis();
				dexplosionTime = waitTime - explosionTime;
				pause = true;
				
				explosion = new Explosion(EXPLOSION_SIZE, paddleX + (paddleWidth / 2), paddleY);
				if (explosion != null && explosion.isAlive()) {
					explosion.update(getHolder().getSurfaceFrame());
				}
				if (explosion != null) {
					explosion.draw(canvas);
				}
				if (dexplosionTime >= 3000) {
					thread.setRunning(false);
					((WoodGameActivity4)getContext()).loseScreen();
				}
			}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new WoodGameThread4(getHolder(), this);
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