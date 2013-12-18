package ssk.project.Basic_Pong.Level_Forest;
import java.util.Random;

import ssk.project.BaseClasses.BaseThread;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class ForestGameView2 extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenWidth;
	int screenHeight;
	Bitmap backgroundBitmap;
	BaseThread thread;
	// Paddle 1
	int paddleX;
	int paddleY;
	int paddleWidth;
	int paddleHeight;
	int paddlePointer;
	int paddleIndex;
	Bitmap paddleBitmap;
	// Paddle 2
	int paddle2X;
	int paddle2Y;
	int paddle2Width;
	int paddle2Height;
	int paddle2Pointer;
	int paddle2Index;
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
	// Level Variables
	int ballHits = 0;
	Paint paint = new Paint();
	boolean pause = false;
	Random r = new Random();
	private SparseArray<PointF> mActivePointers;
	
	
	public ForestGameView2(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava_paddle);		
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		
		mActivePointers = new SparseArray<PointF>();
		
		paint.setColor(Color.GREEN);
		paint.setTextSize(40);
		
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
		// Initialize Paddle 1
		paddleBitmap = Bitmap.createScaledBitmap(paddleBitmap, screenWidth / 4, screenHeight / 40, false);
		paddleWidth = paddleBitmap.getWidth();
		paddleHeight = paddleBitmap.getHeight();
		paddleX = screenWidth / 2 - (paddleWidth / 2);
		paddleY = screenHeight - screenHeight / 10;
		// Initialize Paddle 2
		paddle2Width = paddleWidth * 2;
		paddle2Height = paddleHeight;
		paddle2X = screenWidth / 2 - (paddleWidth / 2);
		paddle2Y = screenHeight / 10 + 50;
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);
		int action = event.getActionMasked();
		
	    switch (action) {
	    case MotionEvent.ACTION_DOWN: { 	
	    }
	    
	    case MotionEvent.ACTION_POINTER_DOWN: {
	    	PointF f = new PointF();
	    	f.x = event.getX(pointerIndex);
	    	f.y = event.getY(pointerIndex);
	    	mActivePointers.put(pointerId, f);
	    	break;
	    }
	    
	    case MotionEvent.ACTION_MOVE: {
	    	for (int size = event.getPointerCount(), i = 0; i < size; i++) {
	    		PointF point = mActivePointers.get(event.getPointerId(i));
	    		if (point != null) {
	    			point.x = event.getX(i);
	    			point.y = event.getY(i);
	    			if (i == 0 && Math.abs(paddleX - point.x) <= paddleWidth && point.y >= screenHeight / 2) {
	    				paddleX = (int) point.x;
	    		    }
	    			if (i == 1 && Math.abs(paddle2X - point.x) <= paddle2Width && point.y <= screenHeight / 2) {
	    				paddle2X = (int) point.x; 
	    		    }
	    		}
	    	}
	    	
	    	break;
	    }

	    case MotionEvent.ACTION_UP: {
	    }
	    
	    case MotionEvent.ACTION_CANCEL: {
	    	mActivePointers.remove(pointerId);
	        break;
	    }
	    
	    case MotionEvent.ACTION_POINTER_UP: {
	    }
	    }
		return true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);		
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		// Paddle 1
		if (paddleX > screenWidth - paddleWidth) {
			paddleX = screenWidth - paddleWidth;
		}
		canvas.drawBitmap(paddleBitmap, paddleX, paddleY, null);
		// Paddle 2
		if (paddle2X > screenWidth - paddle2Width) {
			paddle2X = screenWidth - paddle2Width;
		}
		canvas.drawBitmap(paddleBitmap, paddle2X, paddle2Y, null);
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
		// Bounce off Paddle 2 Left Edge
		if (velocityY < 0 && ballY + ballHeight < paddleY + paddleHeight && ballY < paddle2Y + paddle2Height && ballX + ballWidth > paddle2X && ballX < paddle2X + (paddle2Width * .25)) {
			velocityY = (-1) * velocityY;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle 2 Middle
		if (velocityY <= 0 && ballY + ballHeight < paddleY + paddleHeight && ballY <= paddle2Y + paddle2Height && ballX + ballWidth >= paddle2X + (paddle2Width * .25) && ballX <= paddle2X + (paddle2Width * .75)) {
			velocityY = (-1) * velocityY;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 100);
			} else {
				velocityX = screenWidth / 100;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Paddle 2 Right Edge
		if (velocityY < 0 && ballY + ballHeight < paddleY + paddleHeight && ballY < paddle2Y + paddle2Height && ballX + ballWidth > paddle2X + (paddle2Width * .75) && ballX < paddle2X + paddle2Width) {
			velocityY = (-1) * velocityY;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}	
		// Bounce off Left/Right wall
		if (ballX < 0 || ballX + ballWidth > screenWidth) {
			velocityX = (-1) * velocityX;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}
		// Lose Condition
		if (ballY > (screenHeight - ballHeight) || ballY < 0 || ball2Y > (screenHeight - ballHeight) || ball2Y < 0) {
			thread.setRunning(false);
			pause = true;
			((ForestGameActivity2)getContext()).loseScreen();
		}		
		// Hit Text
		canvas.drawText("Hits : " + ballHits, 50, 50, paint);
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
		if (velocity2Y > 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y + ball2Height > paddleY && ball2X + ball2Width > paddleX && ball2X < paddleX + (paddleWidth * .25)) {
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
		if (velocity2Y >= 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y + ball2Height >= paddleY && ball2X + ball2Width >= paddleX + (paddleWidth * .25) && ball2X <= paddleX + (paddleWidth * .75)) {
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
		if (velocity2Y > 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y + ball2Height > paddleY && ball2X + ball2Width > paddleX + (paddleWidth * .75) && ball2X < paddleX + paddleWidth) {
			velocity2Y = -1 * velocity2Y;;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Ball 2 Bounce off Paddle 2 Left Edge
		if (velocity2Y < 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y < paddle2Y + paddle2Height && ball2X + ball2Width > paddle2X && ball2X < paddle2X + (paddle2Width * .25)) {
			velocity2Y = -1 * velocity2Y;;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Ball 2 Bounce off Paddle 2 Middle
		if (velocity2Y <= 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y <= paddle2Y + paddle2Height && ball2X + ball2Width >= paddle2X + (paddle2Width * .25) && ball2X <= paddle2X + (paddle2Width * .75)) {
			velocity2Y = -1 * velocity2Y;;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 100);
			} else {
				velocityX = screenWidth / 100;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Ball 2 Bounce off Paddle 2 Right Edge
		if (velocity2Y < 0 && ballY + ballHeight < paddleY + paddleHeight && ball2Y < paddle2Y + paddle2Height && ball2X + ball2Width > paddle2X + (paddle2Width * .75) && ball2X < paddle2X + paddle2Width) {
			velocity2Y = -1 * velocity2Y;;
			if (velocityX < 0) {
				velocityX = -1 * (screenWidth / 75);
			} else {
				velocityX = screenWidth / 75;
			}
			ballHits++;
			soundPool.play(ballHitPaddle, 1, 1, 1, 0, 1);
		}
		// Bounce off Left/Right wall
		if (ball2X < 0 || ball2X + ball2Width > screenWidth) {
			velocity2X = (-1) * velocity2X;
			soundPool.play(ballHitWall, 1, 1, 1, 0, 1);
		}	
		// Rotating ball
		if (ball2Angle++ > 360) {
			ball2Angle = 0;
		}
		if (secondBallDrawing) {
		canvas.save();
		canvas.rotate(ball2Angle, ball2X + (ball2Width / 2), ball2Y + (ball2Height / 2));
		canvas.drawBitmap(ball2Bitmap, ball2X, ball2Y, null);
		canvas.restore();
		}
		// Lose Condition Ball 2
		if (ball2Y > (screenHeight - ballHeight)) {
			thread.setRunning(false);
			pause = true;
			((ForestGameActivity2)getContext()).loseScreen();
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