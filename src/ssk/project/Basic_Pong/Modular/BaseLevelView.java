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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BaseLevelView extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenW;
	int screenH;
	Bitmap bgBitmap;
	BaseGameThread thread;
	Paddle p;
	Ball b;
	int ballHits = 0;
	Paint paint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Random r = new Random();
	Context context;
	
	public BaseLevelView(Context context) {
		super(context);
		this.context = context;
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano);
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
		b = new Ball(context);
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
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
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
	
	public void updateBall(Canvas canvas) {
		b.y += (int) b.vY;
		b.x += (int) b.vX;
		b.rotateBall(canvas);
		b.bounceWall(screenH, screenW);
		if (b.bouncePaddle(p, screenH, screenW)) {
			ballHits++;
		}
	}
	
	public void updatePaddle(Canvas canvas) {
		if (p.x > screenW - p.w) {
			p.x = screenW - p.w;
		}
		p.render(canvas);
	}
	
	public void winCondition() {
		if (ballHits == 15) {
			thread.setRunning(false);
			pause = true;
			((TestGameActivity)getContext()).winScreen();
		}
	}
	
	public void loseCondition() {
		if (b.y > (screenH - b.h)) {
			thread.setRunning(false);
			pause = true;
			((TestGameActivity)getContext()).loseScreen();
		}
	}
	
	public void updateStatusText(Canvas canvas) {
		canvas.drawText("Hits : " + ballHits, 50, 50, paint);
		canvas.drawText("Goal : 15", screenW / 2 + screenW / 4, screenH / 20, goalPaint);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		updatePaddle(canvas);
		updateBall(canvas);
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
		thread = new BaseGameThread(getHolder(), this);
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