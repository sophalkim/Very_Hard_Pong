package ssk.project.Basic_Pong.Modular;

import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class PowerUp extends GameUnit {

	float angle;
	float scale = 1;
	int powerUpSfx;

	public PowerUp() {}
	public PowerUp(Context context) {
		powerUpSfx = soundPool.load(context, R.raw.power_up_sound_effect, 1);
	}
	public PowerUp(int x, int y, int w, int h, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rect = new Rect(x, y, x + w, y + h);
		this.bitmap = bitmap;
	}
	
	public void playPowerUpSfx() {
		soundPool.play(powerUpSfx, 1, 1, 1, 0, 1);
	}
}