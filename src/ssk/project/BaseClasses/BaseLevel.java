package ssk.project.BaseClasses;

import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.GameText;
import ssk.project.GameUnits.Paddle;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
	
	public BaseLevel(Context context) {
		super(context);
		this.context = context;
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano);
		getHolder().addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, false);
		gt = new GameText();
		b = new Ball(this, context, screenW, screenH);
		p = new Paddle(this, context, screenW, screenH);
	}
	
	public void winCondition() {
		if (ballHits == 15) {
			b.soundPool.release();
			b.soundPool = null;
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	public void loseCondition() {
		if (b.y > (b.screenH - b.h)) {
			b.soundPool.release();
			b.soundPool = null;
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
	public void updateBall(Canvas canvas) {
		b.update(canvas);
		if (b.bouncePaddle(p)) {
			ballHits++;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		gt.updateText(canvas, screenW, screenH, ballHits);
		p.update(canvas);
		updateBall(canvas);
		winCondition();
		loseCondition();
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