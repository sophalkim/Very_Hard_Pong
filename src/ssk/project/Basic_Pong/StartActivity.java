
package ssk.project.Basic_Pong;

import ssk.project.BaseClasses.BaseActivity;
import ssk.project.Basic_Pong.DialogScreens.AudioPreferences;
import ssk.project.Pong_Basic.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

public class StartActivity extends FragmentActivity implements AudioPreferences.Listener {

	StartView gameView;
	static Typeface tf1;
	static Typeface tf2;
	Intent i;
	MediaPlayer mp;
	boolean playSound = true;
	boolean playMusic = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(gameView = new StartView(this));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        loadPreferences();
        tf1 = Typeface.createFromAsset(getAssets(), "fonts/pirataone.ttf");
        tf2 = Typeface.createFromAsset(getAssets(), "fonts/iceland.ttf");
        mp = MediaPlayer.create(this, R.raw.starting_jingle);
		mp.setLooping(true);
        if (playMusic) {
	        mp.start();
        }
    }
    
    @Override
	public void onPause() {
		super.onPause();
		gameView.thread.setRunning(false);
		if (playMusic) {
			mp.stop();
		}
	}
    
    @Override
	public void onResume() {
		super.onResume();
		if (playMusic) {
	        mp.start();
        }
	}
    
    @Override
	public void onDestroy(){
		super.onDestroy();
		if (playMusic) {
			mp.release();
		}
	}
    public void startIce() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 1);
    	i.putExtra("Music", 1);
    	startActivity(i);
    }
    
    public void startIce2() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 2);
    	i.putExtra("Music", 1);
    	startActivity(i);
    }
    
    public void startIce3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 3);
    	i.putExtra("Music", 1);
    	startActivity(i);
    }
    
    public void startIce4() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 4);
    	i.putExtra("Music", 1);
    	startActivity(i);
    }
    
    public void startVolcano() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 5);
    	i.putExtra("Music", 2);
    	startActivity(i);
    }
    
    public void startVolcano2() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 6);
    	i.putExtra("Music", 2);
    	startActivity(i);
    }
    
    public void startVolcano3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 7);
    	i.putExtra("Music", 2);
    	startActivity(i);
    }
    
    public void startVolcano4() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 8);
    	i.putExtra("Music", 2);
    	startActivity(i);
    }
    
    public void startWood() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 9);
    	i.putExtra("Music", 3);
    	startActivity(i);
    }
    
    public void startWood2() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 10);
    	i.putExtra("Music", 3);
    	startActivity(i);
    }
    
    public void startWood3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 11);
    	i.putExtra("Music", 3);
    	startActivity(i);
    }
    
    public void startWood4() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 12);
    	i.putExtra("Music", 3);
    	startActivity(i);
    }
    
    public void startBeach1() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 13);
    	i.putExtra("Music", 4);
    	startActivity(i);
    }
    
    public void startBeach2() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 13);
    	i.putExtra("Music", 4);
    	startActivity(i);
    }
    
    public void startBeach3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 13);
    	i.putExtra("Music", 4);
    	startActivity(i);
    }
    
    public void startBeach4() {
    	AudioPreferences audioDialog = new AudioPreferences();
		audioDialog.show(getSupportFragmentManager(), "missisles");
    }

	@Override
	public void onClickMusic(boolean music) {
		playMusic = music;
		if (music) {
			mp.start();
		} else {
			mp.pause();
		}
		savePreferences("MUSIC", music);
	}
	
	@Override
	public void onClickSound(boolean sound) {
		gameView.playSound = sound;
	}
	
	public void loadPreferences() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		playSound = sp.getBoolean("SOUND", true);
		playMusic = sp.getBoolean("MUSIC", true);
	}
	
	public void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
}