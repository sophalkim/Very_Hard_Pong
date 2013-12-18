package ssk.project.GameUnits;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class Paddle extends GameUnit {

	public Paddle(View v, Context context, int screenW, int screenH) {
		this.screenW = screenW;
		this.screenH = screenH;
		bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.lava_paddle);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenW / 4, screenH / 40, false);
		x = screenW / 2 - (bitmap.getWidth() / 2);
		y = screenH - screenH / 10;
		w = bitmap.getWidth();
		h = bitmap.getHeight();
	}
	
	public void update(Canvas canvas) {
		if (x > screenW - w) {
			x = screenW - w;
		}
		render(canvas);
	}
	
	public void onTouch(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				if (Math.abs(x - (int) event.getX()) <= w) {
					x = (int) event.getX();
				}
				isMoving = true;
				break;
			}		
			case MotionEvent.ACTION_UP: {
				isMoving = false;
				break;
			}
		}
	}

}