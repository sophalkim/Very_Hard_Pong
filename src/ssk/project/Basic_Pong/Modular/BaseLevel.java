package ssk.project.Basic_Pong.Modular;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BaseLevel extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenW;
	int screenH;
	Bitmap bgBitmap;
	BaseThread thread;
	Paddle p;
	Ball b;
	int ballHits = 0;
	Paint paint = new Paint();
	Paint goalPaint = new Paint();
	boolean pause = false;
	Context context;
	String hitsText = "Hits : " ;
	String goalText = "Goal : 15";
	float goalTextLength;
	
	public BaseLevel(Context context) {
		super(context);
		this.context = context;
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano);
		paint.setColor(Color.GREEN);
		paint.setTextSize(40);
		goalPaint.setColor(Color.BLUE);
		goalPaint.setTextSize(40);
		goalTextLength = goalPaint.measureText(goalText);
		
		getHolder().addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, false);
		b = new Ball(this, context, screenW, screenH);
		p = new Paddle(this, context, screenW, screenH);
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		p.onTouch(event);
		return true;
	}
	
	public void updateBall(Canvas canvas) {
		b.update(canvas);
		if (b.bouncePaddle(p)) {
			ballHits++;
		}
	}
	
	public void winCondition() {
		if (ballHits == 15) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	public void loseCondition() {
		if (b.y > (screenH - b.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
	public void updateStatusText(Canvas canvas) {
		canvas.drawText(hitsText + ballHits, screenW / 20 , screenH / 20, paint);
		canvas.drawText(goalText, screenW - goalTextLength - screenW / 20, screenH / 20, goalPaint);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		p.update(canvas);
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