package ssk.project.Basic_Pong.Level_Forest;
import ssk.project.Basic_Pong.DialogScreens.LoseDialogFragment;
import ssk.project.Basic_Pong.DialogScreens.WinDialogFragment;
import ssk.project.Pong_Basic.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ForestGameActivity extends FragmentActivity {
	
	ForestGameView gameView;
	MediaPlayer mp;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(gameView = new ForestGameView(this));
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mp = MediaPlayer.create(this, R.raw.music3);
		mp.setLooping(true);
        mp.start();
	}
	
	public void loseScreen() {
		mp.release();
		mp = MediaPlayer.create(this, R.raw.lose_music);
        mp.start();
        LoseDialogFragment loseDialog = new LoseDialogFragment();
		loseDialog.show(getSupportFragmentManager(), "missisles");
	}
	
	public void winScreen() {
		mp.release();
		mp = MediaPlayer.create(this, R.raw.win_music);
        mp.start();
        WinDialogFragment winDialog = new WinDialogFragment();
        winDialog.show(getSupportFragmentManager(), "missiles");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mp.release();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		gameView.thread.setRunning(false);
		mp.pause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mp.start();
	}
}
