package ssk.project.Basic_Pong.Level_Forest_2;
import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class ForestGameThread2 extends Thread {
	
	private SurfaceHolder holder;
	private ForestGameView2 gameView;
	private boolean running = false;
	
	public ForestGameThread2(SurfaceHolder holder, ForestGameView2 gameView) {
		this.holder = holder;
		this.gameView = gameView;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public SurfaceHolder getSurfaceHolder() {
		return holder;
	}

	@Override
	public void run() {
		Canvas canvas;
		while (running) {
			canvas = null;
			try {
				canvas = holder.lockCanvas();
				synchronized (holder) {
					gameView.draw(canvas);
				}		
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
