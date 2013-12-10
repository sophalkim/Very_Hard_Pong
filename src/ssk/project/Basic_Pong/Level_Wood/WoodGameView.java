package ssk.project.Basic_Pong.Level_Wood;
import java.util.Random;

import ssk.project.Basic_Pong.model.Explosion;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class WoodGameView extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenWidth;
	int screenHeight;
	Bitmap backgroundBitmap;
	WoodGameThread thread;
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
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	Paint redPaint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Random r = new Random();
	// Timing for Lightning
	long previousTime = 0;
	long currentTime;
	long deltaTime;
	
	long pTime = 0;
	long cTime;
	long dTime;
	// Lightning
	Bitmap lightningBitmap;
	int lightningX;
	int lightningY;
	int lightningWidth;
	int lightningHeight;
	int lightningVelocity;
	int lightningAngle = 0;
	// Explosion
	private Explosion explosion;
	private static final int EXPLOSION_SIZE = 200;
	long waitTime = 0;
	long explosionTime = 0;
	long dexplosionTime = 0;
	
	public WoodGameView(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.twisted_metal_bar);
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);	
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood_flooring_background);
		lightningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lightning);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		lightningAttack = soundPool.load(context, R.raw.lightning_sound_effect, 1);
		
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
		paddleHeight = paddleBitmap.getHeight();
		paddleX = screenWidth / 2 - (paddleWidth / 2);
		paddleY = screenHeight - screenHeight / 10;
		// Initialize Lightning
		lightningBitmap = Bitmap.createScaledBitmap(lightningBitmap, screenWidth / 20, screenHeight / 6, false);
		lightningWidth = lightningBitmap.getWidth();
		lightningHeight = lightningBitmap.getHeight();
		lightningX = r.nextInt(screenWidth - lightningWidth);
		lightningY = r.nextInt(screenHeight / 20);
		lightningVelocity = screenHeight / 24;
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
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
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
			((WoodGameActivity)getContext()).loseScreen();
		}
		
		// Win Condition
		if (ballHits == 15) {
			thread.setRunning(false);
			pause = true;
			((WoodGameActivity)getContext()).winScreen();
		}
		// Hits and Goal Text
		canvas.drawText("Hits : " + ballHits, screenWidth / 20, screenHeight / 20, paint);
		canvas.drawText("Goal : 15", screenWidth / 2 + screenWidth / 4, screenHeight / 20, goalPaint);
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
				lightningY = r.nextInt(200);
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
					((WoodGameActivity)getContext()).loseScreen();
				}
			}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new WoodGameThread(getHolder(), this);
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