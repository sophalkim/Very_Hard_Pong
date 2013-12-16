package ssk.project.Basic_Pong.Level_Wood_3;
import java.util.Random;

import ssk.project.BaseClasses.BaseThread;
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


public class WoodGameView3 extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenWidth;
	int screenHeight;
	Bitmap backgroundBitmap;
	BaseThread thread;
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
	private int powerUpSound;
	private int clockPowerUpSound;
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	Paint redPaint = new Paint();
	Paint goalPaint = new Paint();
	Paint slowSpellPaint = new Paint();
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
	long previous2Time = 0;
	long current2Time;
	long delta2Time;
	
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
	// Power Up Button
	int powerUpX;
	int powerUpY;
	int powerUpWidth;
	int powerUpHeight;
	int powerUpAngle = 0;
	float powerUpScale = 1;
	boolean isPowerUpAvailable = true;
	boolean isPowerUpVisible = false;
	boolean isPowerUpReversing = false;
	boolean isPowerUpClicked = false;
	boolean isPowerUpScaling = false;
	Bitmap powerUpBitmap;
	// Clock Power Up Button
	int clockPowerUpX;
	int clockPowerUpY;
	int clockPowerUpWidth;
	int clockPowerUpHeight;
	int clockPowerUpAngle = 0;
	float clockPowerUpScale = 1;
	long clockPowerUpClickTime = 0;
	long clockPowerUpTextDuration = 0;
	boolean isClockPowerUpAvailable = true;
	boolean isClockPowerUpVisible = false;
	boolean isClockPowerUpReversing = false;
	boolean isClockPowerUpClicked = false;
	boolean isClockPowerUpScaling = false;
	boolean isClockPowerUpTextShowing = false;
	Bitmap clockPowerUpBitmap;
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
	
	public WoodGameView3(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.twisted_metal_bar);
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);	
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood_flooring_background);
		lightningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lightning);
		powerUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.power_up);
		clockPowerUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clock);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		lightningAttack = soundPool.load(context, R.raw.lightning_sound_effect, 1);
		powerUpSound = soundPool.load(context, R.raw.power_up_sound_effect, 1);
		clockPowerUpSound = soundPool.load(context, R.raw.clock_powerup, 1);
		
		paint.setColor(Color.GREEN);
		paint.setTextSize(40);
		redPaint.setColor(Color.RED);
		redPaint.setTextSize(100);
		redPaint.setTextAlign(Align.CENTER);
		redPaint.setShadowLayer(5, 0, 0, Color.BLACK);
		goalPaint.setColor(Color.BLUE);
		goalPaint.setTextSize(40);
		slowSpellPaint.setColor(Color.YELLOW);
		slowSpellPaint.setTextSize(50);
		slowSpellPaint.setTextAlign(Align.CENTER);
		slowSpellPaint.setShadowLayer(5, 0, 0, Color.MAGENTA);
		
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
		paddleHeight = paddleBitmap.getHeight();
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
		// Initialize PowerUp
		powerUpBitmap = Bitmap.createScaledBitmap(powerUpBitmap, screenWidth / 5, screenHeight / 8, false);
		powerUpWidth = powerUpBitmap.getWidth();
		powerUpHeight = powerUpBitmap.getHeight();
		powerUpX = (int) (screenWidth - powerUpWidth * 1.2);
		powerUpY = screenHeight / 2;
		// Initialize Clock PowerUp
		clockPowerUpBitmap = Bitmap.createScaledBitmap(clockPowerUpBitmap, screenWidth / 5, screenHeight / 8, false);
		clockPowerUpWidth = clockPowerUpBitmap.getWidth();
		clockPowerUpHeight = clockPowerUpBitmap.getHeight();
		clockPowerUpX = (int) (clockPowerUpWidth * 1.2);
		clockPowerUpY = screenHeight / 2;
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
			case MotionEvent.ACTION_DOWN: {
				if (isPowerUpVisible) {
					if (event.getX() >= powerUpX && event.getX() <= powerUpX + powerUpWidth && event.getY() >= powerUpY && event.getY() <= powerUpY + powerUpHeight) {
	 					isPowerUpClicked = true;
	 					break;
	 				}
				}
				if (isClockPowerUpVisible) {
					if (event.getX() >= clockPowerUpX && event.getX() <= clockPowerUpX + clockPowerUpWidth && event.getY() >= clockPowerUpY && event.getY() <= clockPowerUpY + clockPowerUpHeight) {
	 					isClockPowerUpClicked = true;
	 					break;
	 				}
				}
			}
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
//		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
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
			((WoodGameActivity3)getContext()).loseScreen();
		}
		
		// Win Condition
		if (ballHits == 35) {
			thread.setRunning(false);
			pause = true;
			((WoodGameActivity3)getContext()).winScreen();
		}
		// Hits and Goal Text
		canvas.drawText("Hits : " + ballHits, screenWidth / 20, screenHeight / 20, paint);
		canvas.drawText("Goal : 35", screenWidth / 2 + screenWidth / 4, screenHeight / 20, goalPaint);
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
					((WoodGameActivity3)getContext()).loseScreen();
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
					((WoodGameActivity3)getContext()).loseScreen();
				}
			}
		// Power Up
		if (isPowerUpClicked) {
			paddleBitmap = Bitmap.createScaledBitmap(paddleBitmap, screenWidth / 5, screenHeight / 40, false);
			paddleWidth = paddleBitmap.getWidth();
			paddleHeight = paddleBitmap.getHeight();
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
				powerUpAngle++;
			}
			if (isPowerUpReversing) {
				powerUpAngle--;
			}
			if (Math.abs(powerUpAngle) == 10) {
				isPowerUpReversing = !isPowerUpReversing;
			}
			// Scale PowerUP
			if (!isPowerUpScaling) {
				powerUpScale = (float) (powerUpScale + .1);
			}
			if (isPowerUpScaling) {
				powerUpScale = (float) (powerUpScale - .1);
			}
			if (Math.abs(powerUpScale) == 1.5) {
				isPowerUpScaling = !isPowerUpScaling;
			}
			canvas.save();
			canvas.rotate(powerUpAngle, powerUpX + (powerUpWidth / 2), powerUpY + (powerUpHeight / 2));
			canvas.drawBitmap(powerUpBitmap, powerUpX, powerUpY, null);
			canvas.restore();
		}
		// Clock Power UP
		if (isClockPowerUpClicked) {
			lightningVelocity = lightningVelocity / 2;
			lightning2Velocity = lightning2Velocity / 2;
			soundPool.play(clockPowerUpSound, 1, 1, 1, 0, 1);
			clockPowerUpClickTime = System.currentTimeMillis();
			isClockPowerUpTextShowing = true;
			isClockPowerUpClicked = false;
			isClockPowerUpAvailable = false;
			isClockPowerUpVisible = false;
		}
		
		if (isClockPowerUpTextShowing) {
			clockPowerUpTextDuration = System.currentTimeMillis();
			if (clockPowerUpTextDuration - clockPowerUpClickTime <= 1000) {
				canvas.drawText("SLOW SPELL", screenWidth / 4, screenHeight / 4, slowSpellPaint);
			} else {
				isClockPowerUpTextShowing = false;
			}
		}
		if (ballHits == 6 && !isClockPowerUpVisible && isClockPowerUpAvailable) {
			soundPool.play(clockPowerUpSound, 1, 1, 1, 0, 1);
			isClockPowerUpVisible = true;			
		}
		if (isClockPowerUpVisible) {
			// Rotating PowerUP
			if (!isClockPowerUpReversing) {
				clockPowerUpAngle++;
			}
			if (isClockPowerUpReversing) {
				clockPowerUpAngle--;
			}
			if (Math.abs(clockPowerUpAngle) == 10) {
				isClockPowerUpReversing = !isClockPowerUpReversing;
			}
			// Scale PowerUP
			if (!isClockPowerUpScaling) {
				clockPowerUpScale = (float) (clockPowerUpScale + .1);
			}
			if (isClockPowerUpScaling) {
				clockPowerUpScale = (float) (clockPowerUpScale - .1);
			}
			if (Math.abs(clockPowerUpScale) == 1.5) {
				isClockPowerUpScaling = !isClockPowerUpScaling;
			}
			canvas.save();
			canvas.rotate(clockPowerUpAngle, clockPowerUpX + (clockPowerUpWidth / 2), clockPowerUpY + (clockPowerUpHeight / 2));
			canvas.drawBitmap(clockPowerUpBitmap, clockPowerUpX, clockPowerUpY, null);
			canvas.restore();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new BaseThread(getHolder(), this);
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