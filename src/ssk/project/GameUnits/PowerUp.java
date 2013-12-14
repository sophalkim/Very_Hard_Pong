package ssk.project.GameUnits;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class PowerUp extends GameUnit {

	float angle;
	float scale = 1;
	int powerUpSfx;
	boolean available = true;
	boolean visible = false;
	boolean reversing = false;
	boolean clicked = false;
	boolean scaling = false;

	public PowerUp(View v, Context context, int screenW, int screenH) {
		this.screenW = screenW;
		this.screenH = screenH;
		powerUpSfx = soundPool.load(context, R.raw.power_up_sound_effect, 1);
		bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.power_up);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenW / 5, screenH / 8, false);
		w = bitmap.getWidth();
		h = bitmap.getHeight();
		x = (int) (screenW - w * 1.2);
		y = screenH / 2;
		
	}
	
	public void playPowerUpSfx() {
		soundPool.play(powerUpSfx, 1, 1, 1, 0, 1);
	}
	
	public void rotate() {
		if (visible) {
			if (!reversing) {
				angle++;
			}
			if (reversing) {
				angle--;
			}
			if (Math.abs(angle) == 10) {
				reversing = !reversing;
			}
		}
	}
	
	public void scale(Canvas canvas) {
		if (visible) {
			// Scale PowerUP
			if (!scaling) {
				scale = (float) (scale + .1);
			}
			if (scaling) {
				scale = (float) (scale - .1);
			}
			if (Math.abs(scale) == 1.5) {
				scaling = !scaling;
			}
			canvas.save();
			canvas.rotate(angle, x + (w / 2), y + (h / 2));
			canvas.drawBitmap(bitmap, x, y, null);
			canvas.restore();
		}
	}
	
	public void show() {
		playPowerUpSfx();
		visible = true;	
	}
	
	public boolean isAvailable() {
		return (!visible && available);
	}
	
	public void consume(Paddle p) {
		if (clicked) {
			p.bitmap = Bitmap.createScaledBitmap(p.bitmap, screenW / 3, screenH / 40, false);
			p.w = p.bitmap.getWidth();
			p.h = p.bitmap.getHeight();
			playPowerUpSfx();
			clicked = false;
			available = false;
			visible = false;
		}
	}
	
	public void update(Canvas canvas, Paddle p) {
		rotate();
		scale(canvas);
		consume(p);
	}
	
	public void onClick(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if (visible) {
					if (event.getX() >= x && event.getX() <= x + w && event.getY() >= y && event.getY() <= y + h) {
							clicked = true;
							break;
					}
				}
			}
		}
	}
	
}