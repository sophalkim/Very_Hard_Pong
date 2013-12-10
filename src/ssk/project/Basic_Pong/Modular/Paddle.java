package ssk.project.Basic_Pong.Modular;

import android.graphics.Bitmap;
import android.graphics.Rect;


public class Paddle extends GameUnit {

	public Paddle() {}
	public Paddle(int x, int y, int w, int h, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rect = new Rect(x, y, x + w, y + h);
		this.bitmap = bitmap;
	}
}