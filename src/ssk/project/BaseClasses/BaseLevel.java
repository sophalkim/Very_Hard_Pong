package ssk.project.BaseClasses;

import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.GameText;
import ssk.project.GameUnits.Paddle;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BaseLevel extends SurfaceView implements SurfaceHolder.Callback {
	
	public int screenW;
	public int screenH;
	public int ballHits = 0;
	public boolean pause = false;
	public Context context;
	public BaseThread thread;
	public Bitmap bgBitmap;
	public GameText gt;
	public Paddle p;
	public Ball b;
	
	public SoundPool soundPool;
	public int paddleSfx;
	public int wallSfx;
	
	SharedPreferences sp;
	public boolean playSound = true;
	
	public BaseLevel(Context context) {
		super(context);
		this.context = context;
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano);
		getHolder().addCallback(this);
		setFocusable(true);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		playSound = sp.getBoolean("SOUND", true);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		paddleSfx = soundPool.load(context, R.raw.bounce_paddle, 1);
		wallSfx = soundPool.load(context, R.raw.bounce_wall, 1);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, false);
		gt = new GameText();
		b = new Ball(this, context, screenW, screenH, playSound);
		p = new Paddle(this, context, screenW, screenH, Paddle.ICE);
	}
	
	public void winCondition() {
		if (ballHits == 15) {
			soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
			soundPool.release();
			soundPool = null;
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	public void loseCondition() {
		if (b.y > (b.screenH - b.h)) {
			soundPool.release();
			soundPool = null;
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
	public void updateBall(Canvas canvas) {
		b.update(canvas);
		if (b.bouncePaddle(p)) {
			ballHits++;
			if (playSound) {
				soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
			}
		}
		if (b.bounceWall() && playSound) {
			soundPool.play(wallSfx, 1, 1, 1, 0, 1);
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		winCondition();
		loseCondition();
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		gt.updateText(canvas, screenW, screenH, ballHits);
		p.update(canvas);
		updateBall(canvas);
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		p.onTouch(event);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {	
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