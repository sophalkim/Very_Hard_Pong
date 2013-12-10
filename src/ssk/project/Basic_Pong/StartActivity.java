package ssk.project.Basic_Pong;

import ssk.project.Basic_Pong.Level_Beach_1.BeachGameActivity1;
import ssk.project.Basic_Pong.Level_Beach_2.BeachGameActivity2;
import ssk.project.Basic_Pong.Level_Beach_3.BeachGameActivity3;
import ssk.project.Basic_Pong.Level_Beach_4.BeachGameActivity4;
import ssk.project.Basic_Pong.Level_Ice.IceGameActivity;
import ssk.project.Basic_Pong.Level_Ice_2.IceGameActivity2;
import ssk.project.Basic_Pong.Level_Ice_3.IceGameActivity3;
import ssk.project.Basic_Pong.Level_Ice_4.IceGameActivity4;
import ssk.project.Basic_Pong.Level_Volcano.VolcanoGameActivity;
import ssk.project.Basic_Pong.Level_Volcano_2.VolcanoGameActivity2;
import ssk.project.Basic_Pong.Level_Volcano_3.VolcanoGameActivity3;
import ssk.project.Basic_Pong.Level_Volcano_4.VolcanoGameActivity4;
import ssk.project.Basic_Pong.Level_Wood.WoodGameActivity;
import ssk.project.Basic_Pong.Level_Wood_2.WoodGameActivity2;
import ssk.project.Basic_Pong.Level_Wood_3.WoodGameActivity3;
import ssk.project.Basic_Pong.Level_Wood_4.WoodGameActivity4;
import ssk.project.Basic_Pong.Modular.TestGameActivity;
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

public class StartActivity extends FragmentActivity {

	StartView gameView;
	static Typeface tf1;
	static Typeface tf2;
	Intent i;
	MediaPlayer mp;
	SharedPreferences sp;
	Editor edit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(gameView = new StartView(this));
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mp = MediaPlayer.create(this, R.raw.starting_jingle);
		mp.setLooping(true);
        mp.start();
        tf1 = Typeface.createFromAsset(getAssets(), "fonts/pirataone.ttf");
        tf2 = Typeface.createFromAsset(getAssets(), "fonts/iceland.ttf");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
		edit = sp.edit();
        
    }
    
    @Override
	public void onPause() {
		super.onPause();
		gameView.thread.setRunning(false);
		mp.release();
	}
    
    @Override
	public void onDestroy(){
		super.onDestroy();
		mp.release();
	}
    
    public void startWood() {
    	i = new Intent(StartActivity.this, WoodGameActivity.class);
    	startActivity(i);
    }
    
    public void startWood2() {
    	i = new Intent(StartActivity.this, WoodGameActivity2.class);
    	startActivity(i);
    }
    
    public void startWood3() {
    	i = new Intent(StartActivity.this, WoodGameActivity3.class);
    	startActivity(i);
    }
    
    public void startWood4() {
    	i = new Intent(StartActivity.this, WoodGameActivity4.class);
    	startActivity(i);
    }
    
    public void startVolcano() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity.class);
    	startActivity(i);
    }
    
    public void startVolcano2() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity2.class);
    	startActivity(i);
    }
    
    public void startVolcano3() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity3.class);
    	startActivity(i);
    }
    
    public void startVolcano4() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity4.class);
    	startActivity(i);
    }
    
    public void startIce() {
    	i = new Intent(StartActivity.this, IceGameActivity.class);
    	startActivity(i);
    }
    
    public void startIce2() {
    	i = new Intent(StartActivity.this, IceGameActivity2.class);
    	startActivity(i);
    }
    
    public void startIce3() {
    	i = new Intent(StartActivity.this, IceGameActivity3.class);
    	startActivity(i);
    }
    
    public void startIce4() {
    	i = new Intent(StartActivity.this, IceGameActivity4.class);
    	startActivity(i);
    }
    
    public void startBeach1() {
    	i = new Intent(StartActivity.this, TestGameActivity.class);
    	startActivity(i);
    }
    
    public void startBeach2() {
    	i = new Intent(StartActivity.this, BeachGameActivity2.class);
    	startActivity(i);
    }
    
    public void startBeach3() {
    	i = new Intent(StartActivity.this, BeachGameActivity3.class);
    	startActivity(i);
    }
    
    public void startBeach4() {
    	i = new Intent(StartActivity.this, BeachGameActivity4.class);
    	startActivity(i);
    }
    
}