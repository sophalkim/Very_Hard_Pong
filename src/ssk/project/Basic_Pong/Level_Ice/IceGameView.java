package ssk.project.Basic_Pong.Level_Ice;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ssk.project.Basic_Pong.Level_Ice.Objects.IceBlock;
import ssk.project.Basic_Pong.Level_Ice.Objects.SolidBlock;
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


public class IceGameView extends SurfaceView implements SurfaceHolder.Callback {
	
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
	float velocityX;
	float velocityY;
	int ballAngle;
	Bitmap ballBitmap;
	// Sound Effects
	private SoundPool soundPool;
	private int ballHitWall;
	private int ballHitPaddle;
	private int ballHitIceBlock;
	private int ballHitSolidBlock;
	// IceBlocks
	Bitmap iceblock;
	Bitmap iceblockCracked;
	int iceHit;
	List<IceBlock> iceBlocks = new ArrayList<IceBlock>();
	// Level Variables
	int ballHits = 0;
	int iceblockQuantity = 16;
	Paint paint = new Paint();
	boolean pause = false;
	Random r = new Random();
	// SolidBlock
	SolidBlock solidBlock;
	Bitmap solidBlockBitmap;
	boolean isSolidBlockVisible = false;
	long pTime = 0;
	long cTime;
	long dTime;
		
	public IceGameView(Context context) {
		super(context);

		paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ice_paddle);		
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_ball);		
		iceblock = BitmapFactory.decodeResource(getResources(), R.drawable.iceblock);
		iceblockCracked = BitmapFactory.decodeResource(getResources(), R.drawable.iceblock_cracked);		
		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ice_cave);
		solidBlockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.solidblock);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		ballHitWall = soundPool.load(context, R.raw.bounce_wall, 1);
		ballHitPaddle = soundPool.load(context, R.raw.bounce_paddle, 1);
		ballHitIceBlock = soundPool.load(context, R.raw.ice_cracking_sound_effect, 1);
		ballHitSolidBlock = soundPool.load(context, R.raw.solid_block_sound_effect, 1);
		
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
		// Initialize Ball
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
		// Initialize IceBlocks
		iceblock = Bitmap.createScaledBitmap(iceblock, screenWidth / 10, screenWidth / 10, false);
		iceblockCracked = Bitmap.createScaledBitmap(iceblockCracked, screenWidth / 10, screenWidth / 10, false);
		for (int i = 0; i < (iceblockQuantity / 2); i++) {
			IceBlock ice = new IceBlock();
			ice.iceBlockWidth = iceblock.getWidth();
			ice.iceBlockHeight = iceblock.getHeight();
			ice.iceBlockY = screenHeight / 2 - iceblock.getHeight();
			ice.iceBlockX = (i * iceblock.getWidth() + 10);
			iceBlocks.add(ice);
		}
		for (int i = 0; i < (iceblockQuantity / 2); i++) {
			IceBlock ice = new IceBlock();
			ice.iceBlockWidth = iceblock.getWidth();
			ice.iceBlockHeight = iceblock.getHeight();
			ice.iceBlockY = screenHeight / 2;
			ice.iceBlockX = (i * iceblock.getWidth() + 10);
			iceBlocks.add(ice);
		}
		// Initialize SolidBlock
		solidBlockBitmap = Bitmap.createScaledBitmap(solidBlockBitmap, screenWidth / 10, screenWidth / 10, false);
		solidBlock = new SolidBlock();
		solidBlock.solidBlockWidth = solidBlockBitmap.getWidth();
		solidBlock.solidBlockHeight = solidBlockBitmap.getHeight();
		solidBlock.solidBlockX = r.nextInt(screenWidth - solidBlock.solidBlockWidth);
		solidBlock.solidBlockY = screenHeight / 2 + screenHeight / 8;
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
		// Win Condition
		if (iceHit == (iceblockQuantity * 2)) {
			thread.setRunning(false);
			pause = true;
			((IceGameActivity)getContext()).winScreen();
		}			
		// Lose Condition
		if (ballY > (screenHeight - ballHeight)) {
			thread.setRunning(false);
			pause = true;
			((IceGameActivity)getContext()).loseScreen();
		}
		// Hit Text
		canvas.drawText("Hits : " + ballHits, 50, 50, paint);
		// Drawing IceBlocks
		for (int i = 0; i < iceBlocks.size(); i++) {
			if (iceBlocks.get(i).iceBlockState == 2) {
				canvas.drawBitmap(iceblock, iceBlocks.get(i).iceBlockX, iceBlocks.get(i).iceBlockY, null);
			} else if (iceBlocks.get(i).iceBlockState == 1) {
				canvas.drawBitmap(iceblockCracked, iceBlocks.get(i).iceBlockX, iceBlocks.get(i).iceBlockY, null);
			}
		}		
		// IceBlocks Collision
		for (int i = 0; i < iceBlocks.size(); i++) {
			// Bounce off IceBlock Top
			if (iceBlocks.get(i).iceBlockState > 0 && velocityY > 0 && Math.abs(ballY + ballHeight - iceBlocks.get(i).iceBlockY) <= 10 && ballX + ballWidth > iceBlocks.get(i).iceBlockX && ballX < iceBlocks.get(i).iceBlockX + iceBlocks.get(i).iceBlockWidth) {
				velocityY = (-1) * velocityY;
				iceBlocks.get(i).iceBlockState--;
				iceHit++;
				soundPool.play(ballHitIceBlock, 1, 1, 1, 0, 1);
			}		
			// Bounce off IceBlock Bottom
			if (iceBlocks.get(i).iceBlockState > 0 && velocityY < 0 && ballX + ballWidth >= iceBlocks.get(i).iceBlockX && ballX <= iceBlocks.get(i).iceBlockX + iceBlocks.get(i).iceBlockWidth && Math.abs(ballY - (iceBlocks.get(i).iceBlockY + iceBlocks.get(i).iceBlockHeight)) <= 10) {
				velocityY = (-1) * velocityY;
				iceBlocks.get(i).iceBlockState--;
				iceHit++;
				soundPool.play(ballHitIceBlock, 1, 1, 1, 0, 1);
			}	
			// Bounce off IceBlock Left
			if (iceBlocks.get(i).iceBlockState > 0 && velocityX > 0 && ballY + ballHeight >= iceBlocks.get(i).iceBlockY && ballY <= iceBlocks.get(i).iceBlockY + iceBlocks.get(i).iceBlockHeight && Math.abs(ballX + ballWidth - iceBlocks.get(i).iceBlockX) <= 10) {
				velocityX = (-1) * velocityX;
				iceBlocks.get(i).iceBlockState--;
				iceHit++;
				soundPool.play(ballHitIceBlock, 1, 1, 1, 0, 1);
			}		
			// Bounce off IceBlock Right
			if (iceBlocks.get(i).iceBlockState > 0 && velocityX < 0 && ballY + ballHeight >= iceBlocks.get(i).iceBlockY && ballY <= iceBlocks.get(i).iceBlockY + iceBlocks.get(i).iceBlockHeight && Math.abs(ballX - (iceBlocks.get(i).iceBlockX + iceBlocks.get(i).iceBlockWidth)) <= 10) {
				velocityX = (-1) * velocityX;
				iceBlocks.get(i).iceBlockState--;
				iceHit++;
				soundPool.play(ballHitIceBlock, 1, 1, 1, 0, 1);
			}
		}
		// Setting Up Solid Block
		cTime = System.currentTimeMillis();
		dTime = cTime - pTime;
		if (dTime >= 5000) {
			canvas.drawBitmap(solidBlockBitmap, solidBlock.solidBlockX, solidBlock.solidBlockY, null);
			isSolidBlockVisible = true;
			if (dTime >= 9000) {
				pTime = cTime;
				solidBlock.solidBlockX = r.nextInt(screenWidth - solidBlock.solidBlockWidth);
				isSolidBlockVisible = false;
			}
		}
		// SolidBlock Collisions
		if (isSolidBlockVisible) {
			// Bounce off SolidBlock Top
			if (velocityY > 0 && Math.abs(ballY + ballHeight - solidBlock.solidBlockY) <= 10 && ballX + ballWidth > solidBlock.solidBlockX && ballX < solidBlock.solidBlockX + solidBlock.solidBlockWidth) {
				velocityY = (-1) * velocityY;
				soundPool.play(ballHitSolidBlock, 1, 1, 1, 0, 1);
			}		
			// Bounce off SolidBlock Bottom
			if (velocityY < 0 && ballX + ballWidth >= solidBlock.solidBlockX && ballX <= solidBlock.solidBlockX + solidBlock.solidBlockWidth && Math.abs(ballY - (solidBlock.solidBlockY + solidBlock.solidBlockHeight)) <= 10) {
				velocityY = (-1) * velocityY;
				soundPool.play(ballHitSolidBlock, 1, 1, 1, 0, 1);
			}	
			// Bounce off SolidBlock Left
			if (velocityX > 0 && ballY + ballHeight >= solidBlock.solidBlockY && ballY <= solidBlock.solidBlockY + solidBlock.solidBlockHeight && Math.abs(ballX + ballWidth - solidBlock.solidBlockX) <= 10) {
				velocityX = (-1) * velocityX;
				soundPool.play(ballHitSolidBlock, 1, 1, 1, 0, 1);
			}		
			// Bounce off SolidBlock Right
			if (velocityX < 0 && ballY + ballHeight >= solidBlock.solidBlockY && ballY <= solidBlock.solidBlockY + solidBlock.solidBlockHeight && Math.abs(ballX - (solidBlock.solidBlockX + solidBlock.solidBlockWidth)) <= 10) {
				velocityX = (-1) * velocityX;
				soundPool.play(ballHitSolidBlock, 1, 1, 1, 0, 1);
			}
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