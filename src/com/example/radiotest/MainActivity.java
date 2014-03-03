package com.example.radiotest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

	private String TAG = getClass().getSimpleName();
	private MediaPlayer mp = null;

	private Button play;
	private Button pause;
	private Button stop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		play = (Button) findViewById(R.id.btnPlay);
		pause = (Button) findViewById(R.id.btnPause);
		stop = (Button) findViewById(R.id.btnStop);
	
	
		play.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				play();
			}
		});
	
		pause.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view) 
			{
				pause();
			}
		});
		
		stop.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				stop();
			}
		});
	}
	
	private void play() 
	{
		//Uri myUri = Uri.parse("http://fr3.ah.fm:9000/");
		Uri myUri = Uri.parse("rtsp://stream.rtm.swiftserve.com/live/rtm/rtm-ch010");
		
		try 
		{
			if (mp == null) 
			{
				this.mp = new MediaPlayer();
			} 
			else 
			{
				mp.stop();
				mp.reset();
			}
			
			mp.setDataSource(this, myUri); // Go to Initialized state
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mp.setOnPreparedListener(this);
		    mp.setOnBufferingUpdateListener(this);
		
		    mp.setOnErrorListener(this);
		    mp.prepareAsync();

		    Log.d(TAG, "LoadClip Done");
		} 
		catch (Throwable t) 
		{
			Log.d(TAG, t.toString());
		}
	}

	private void pause() 
	{
		mp.pause();
	}

	private void stop() 
	{
		mp.stop();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		stop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) 
	{
		Log.d(TAG, "PlayerService onBufferingUpdate : " + percent + "%");
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Media Player Error: ");
		switch (what) 
		{
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				sb.append("Not Valid for Progressive Playback");
				break;
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				sb.append("Server Died");
				break;
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				sb.append("Unknown");
				break;
			default:
				sb.append(" Non standard (");
				sb.append(what);
				sb.append(")");
		}
		
		sb.append(" (" + what + ") ");
		sb.append(extra);
		Log.e(TAG, sb.toString());
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Stream is prepared");
		mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		stop();
	}

}
