package ssk.project.Basic_Pong.Modular;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Ball extends GameUnit {

	float vX;
	float vY;
	int angle;
	int paddleSfx;
	int wallSfx;
	
	public Ball() {}
	public Ball(Context context) {
		paddleSfx = soundPool.load(context, R.raw.bounce_paddle, 1);
		wallSfx = soundPool.load(context, R.raw.bounce_wall, 1);
	}
	public Ball(int x, int y, int w, int h, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rect = new Rect(x, y, x + w, y + h);
		this.bitmap = bitmap;
	}
	
	public void playPaddleSfx() {
		soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
	}
	
	public void playWallSfx() {
		soundPool.play(wallSfx, 1, 1, 1, 0, 1);
	}
}