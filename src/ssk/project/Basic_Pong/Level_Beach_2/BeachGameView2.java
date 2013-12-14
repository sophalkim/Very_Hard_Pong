package ssk.project.Basic_Pong.Level_Beach_2;
import java.util.Random;

import ssk.project.Basic_Pong.Modular.BaseThread;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class BeachGameView2 extends SurfaceView implements SurfaceHolder.Callback {
	
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
	// Ball 1
	int ballX;
	int ballY;
	int ballWidth;
	int ballHeight;
	int initialY;
	float velocityX;
	float velocityY;
	int ballAngle;
	Bitmap ballBitmap;
	// Ball 2
	boolean secondBallStarting = false;
	boolean secondBallDrawing = false;
	int ball2X;
	int ball2Y;
	int ball2Width;
	int ball2Height;
	int initial2Y;
	float velocity2X;
	float velocity2Y;
	int ball2Angle;
	Bitmap ball2Bitmap;
	// Sound Effects
	private SoundPool soundPool;
	private int ballHitWall;
	private int ballHitPaddle;
	private int powerUpSound;
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Random r = new Random();
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
	
	public BeachGameView2(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beach_paddle);		
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beach_background);
		powerUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.power_up);
		
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
		screenWidth = w;
		screenHeight = h;
		backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenWidth, screenHeight, false);
		// Initialize ball
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
		// Initialize PowerUp
		powerUpBitmap = Bitmap.createScaledBitmap(powerUpBitmap, screenWidth / 5, screenHeight / 8, false);
		powerUpWidth = powerUpBitmap.getWidth();
		powerUpHeight = powerUpBitmap.getHeight();
		powerUpX = (int) (screenWidth - powerUpWidth * 1.2);
		powerUpY = screenHeight / 2;
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
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		if (isPowerUpClicked) {
			paddleBitmap = Bitmap.createScaledBitmap(paddleBitmap, screenWidth / 3, screenHeight / 40, false);
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
		// Paddle
		if (paddleX > screenWidth - paddleWidth) {
			paddleX = screenWidth - paddleWidth;
		}
		canvas.drawBitmap(paddleBitmap, paddleX, paddleY, null);	
		// Ball Velocity Update
		ballY += (int) velocityY;
		ballX += (int) velocityX;
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
		// win
		if (ballHits == 25) {
			thread.setRunning(false);
			pause = true;
			((BeachGameActivity2)getContext()).winScreen();
		}			
		// lose
		if (ballY > (screenHeight - ballHeight)) {
			thread.setRunning(false);
			pause = true;
			((BeachGameActivity2)getContext()).loseScreen();
		}
		// Hits Text
		canvas.drawText("Hits : " + ballHits, 50, 50, paint);
		canvas.drawText("Goal : 25", screenWidth / 2 + screenWidth / 4, screenHeight / 20, goalPaint);
		// Setting Second Ball
		if (ballHits == 4) {
			secondBallStarting = true;
		}	
		if (secondBallStarting) {
			ball2X = ballX;
			ball2Y = ballY - 20;
			ball2Width = ballWidth;
			ball2Height = ballHeight;
			ball2Bitmap = ballBitmap;
			velocity2X = (-1) * velocityX;
			velocity2Y = screenHeight / 150;
			ball2Angle = 0;
			secondBallStarting = false;
			secondBallDrawing = true;
		}	
		// Ball 2 Velocity Update
		ball2Y += (int) velocity2Y;
		ball2X += (int) velocity2X;	
		// Ball 2 Bounce off Paddle  Left Edge
		if (velocity2Y > 0 && ball2Y + ball2Height < paddleY + paddleHeight && ball2Y + ball2Height > paddleY && ball2X + ball2Width > paddleX && ball2X < paddleX + (paddleWidth * .25)) {
			velocity2Y = -1 * velocity2Y;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Ball 2 Bounce off Paddle  Middle
		if (velocity2Y >= 0 && ball2Y + ball2Height < paddleY + paddleHeight && ball2Y + ball2Height >= paddleY && ball2X + ball2Width >= paddleX + (paddleWidth * .25) && ball2X <= paddleX + (paddleWidth * .75)) {
			velocity2Y = -1 * velocity2Y;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 100);
			} else {
				velocityX = screenWidth / 100;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Ball 2 Bounce off Paddle Right Edge
		if (velocity2Y > 0 && ball2Y + ball2Height < paddleY + paddleHeight && ball2Y + ball2Height > paddleY && ball2X + ball2Width > paddleX + (paddleWidth * .75) && ball2X < paddleX + paddleWidth) {
			velocity2Y = -1 * velocity2Y;;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Top wall
		if  (ball2Y < 0) {
			velocity2Y = (-1) * velocity2Y;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}
		
		// Bounce off Left/Right Wall
		if (velocity2X < 0 && ball2X < 0 || velocity2X > 0 && ball2X + ball2Width > screenWidth) {
			velocity2X = (-1) * velocity2X;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}		
		// Ball Rotation
		if (ball2Angle++ > 360) {
			ball2Angle = 0;
		}
		if (secondBallDrawing) {
		canvas.save();
		canvas.rotate(ball2Angle, ball2X + (ball2Width / 2), ball2Y + (ball2Height / 2));
		canvas.drawBitmap(ball2Bitmap, ball2X, ball2Y, null);
		canvas.restore();
		}
		// Lose Condition if Second Ball is not hit
		if (ball2Y > (screenHeight - ballHeight)) {
			thread.setRunning(false);
			pause = true;
			((BeachGameActivity2)getContext()).loseScreen();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new BaseThread (getHolder(), this);
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