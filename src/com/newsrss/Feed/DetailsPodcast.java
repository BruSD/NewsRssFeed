package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.actionbarsherlock.view.Menu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 4/10/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsPodcast extends Activity {

    Podcast currentPodcast = null;
    MediaPlayer cast_player;
    boolean first_play,play,seeking,end_of_play;
    String podcastUri;
    Object mutex = new Object();
    TextView titleText,dateText;
    TextView playedTime,allTime;
    ImageButton seek10Button,seek30Button,play_pauseButton,backButton;
    SeekBar castSeekbar;
    int podcastBufferedTime;


    @Override
    public void onDestroy(){
        end_of_play=true;
        cast_player.stop();
        cast_player.release();
        cast_player=null;
        super.onDestroy();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_podcast);

        titleText = (TextView)findViewById(R.id.podcast_titleText);
        dateText = (TextView)findViewById(R.id.podcast_dateText);
        playedTime = (TextView)findViewById(R.id.podcast_playedTime);
        allTime = (TextView)findViewById(R.id.podcast_allTime);
        WebView podcastDecription = (WebView) findViewById(R.id.podcast_description);
        seek10Button = (ImageButton) findViewById(R.id.podcast_seek10Button);
        seek30Button = (ImageButton) findViewById(R.id.podcast_seek30Button);
        backButton = (ImageButton) findViewById(R.id.podcast_backButton);
        play_pauseButton = (ImageButton) findViewById(R.id.podcast_play_pauseButton);
        castSeekbar = (SeekBar) findViewById(R.id.podcast_seekbar);


        Intent startDetailArticl = getIntent();
        int position = startDetailArticl.getIntExtra("position", -1);
        currentPodcast = DataStorage.getPodcastList().get(position);

        play_pauseButton.setImageResource(R.drawable.play_button);
        seek10Button.setImageResource(R.drawable.seek_button2);
        seek30Button.setImageResource(R.drawable.seek_button1);
        podcastDecription.setBackgroundColor(Color.parseColor("#EFEEEA"));
        podcastDecription.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        podcastUri=currentPodcast.getMP3Link().toString();
        titleText.setText(currentPodcast.getTitle());
        String date_str = new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(currentPodcast.getPubDate());
        dateText.setText(date_str);
        podcastDecription.loadData("<html><body>" + currentPodcast.getDescription() + "</body></html>", "text/html", "UTF-8");
        castSeekbar.setEnabled(false);


        cast_player = new MediaPlayer();
        cast_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        first_play=true;
        play=false;
        seeking=false;
        end_of_play=false;


        SeekBar.OnSeekBarChangeListener ocSeek = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (!play) {
                    playedTime.setText(ConvertToTimeString(i));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seeking = false;
                synchronized (mutex) {
                    mutex.notifyAll();
                }
                if(!first_play)
                {
                    if (seekBar.getProgress()>podcastBufferedTime)
                    {
                        cast_player.seekTo(podcastBufferedTime - 5000);
                        seekBar.setProgress(cast_player.getCurrentPosition());
                    }
                    else
                        cast_player.seekTo(seekBar.getProgress());
                }

            }
        };


        MediaPlayer.OnCompletionListener ocEnd = new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                play_pauseButton.setImageResource(R.drawable.play_button);
                play=false;
            }
        };

        ImageButton.OnClickListener ocBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        ImageButton.OnClickListener ocPlay_pause = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_play)
                {
                    cast_player.reset();
                    try {
                        cast_player.setDataSource(podcastUri);
                        cast_player.prepare();
                    } catch (IOException e) {
                        System.out.println("30 sec past");
                    }


                    if(playSound()) {
                        first_play=false;
                        timeThread.start();
                        int song_dur=cast_player.getDuration();
                        castSeekbar.setMax(song_dur);
                        castSeekbar.setSecondaryProgress(song_dur);
                        allTime.setText(ConvertToTimeString(song_dur));
                        castSeekbar.setEnabled(true);
                    }
                    return;
                }
                if (!play) playSound();
                else pauseSound();
            }
        };

        ImageButton.OnClickListener ocSeek_10 = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!first_play){
                    cast_player.seekTo(cast_player.getCurrentPosition()-10000);
                        if (!play){
                                    playedTime.setText(ConvertToTimeString(cast_player.getCurrentPosition()));
                                    castSeekbar.setProgress(cast_player.getCurrentPosition());
                        }
                }
            }
        };

        ImageButton.OnClickListener ocSeek_30 = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!first_play){
                    if (cast_player.getCurrentPosition()+30000>podcastBufferedTime){
                        cast_player.seekTo(podcastBufferedTime - 5000);
                    }
                    else cast_player.seekTo(cast_player.getCurrentPosition()+30000);
                    if (!play){
                         playedTime.setText(ConvertToTimeString(cast_player.getCurrentPosition()));
                         castSeekbar.setProgress(cast_player.getCurrentPosition());
                    }
                }
            }
        };


        MediaPlayer.OnBufferingUpdateListener ocBuffer = new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                podcastBufferedTime=castSeekbar.getMax()*percent/100;
                castSeekbar.setSecondaryProgress(podcastBufferedTime);
            }
        };

        MediaPlayer.OnPreparedListener ocPrepare = new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {

            }
        };



        play_pauseButton.setOnClickListener(ocPlay_pause);
        seek10Button.setOnClickListener(ocSeek_10);
        seek30Button.setOnClickListener(ocSeek_30);
        backButton.setOnClickListener(ocBack);
        castSeekbar.setOnSeekBarChangeListener(ocSeek);
        cast_player.setOnCompletionListener(ocEnd);
        cast_player.setOnBufferingUpdateListener(ocBuffer);
        cast_player.setOnPreparedListener(ocPrepare);
    }


    protected String ConvertToTimeString(int time)
    {
        String time_str;
        time=time/1000;
        if (time/60>9)
            time_str = String.valueOf( (int) time/60);
        else
            time_str ="0"+String.valueOf( (int) time/60);
        time_str += ":";
        if (time%60>9)
            time_str = time_str+String.valueOf( (int) time%60);
        else time_str =time_str+ "0"+ String.valueOf( (int) time%60);
        return time_str;
    }


    Thread timeThread = new Thread(
            new Runnable() {
                public void run() {
                    int dur = cast_player.getDuration();

                    boolean flag = cast_player!=null;
                    if (flag)
                        flag = flag && cast_player.getCurrentPosition() <=dur;
                    else
                    return;

                    while(flag) {
                        if(play) {
                            final int song_dur = cast_player.getCurrentPosition();
                            castSeekbar.post(new Runnable() {
                                public void run() {
                                    if (!seeking)
                                        castSeekbar.setProgress(song_dur);
                                    playedTime.setText(ConvertToTimeString(song_dur));
                                }

                            });
                            try { if(seeking)
                                synchronized (mutex) {
                                    mutex.wait();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {	try
                        {
                            synchronized (mutex)
                            {
                                if (play)
                                    mutex.wait();
                            }
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        }

                        flag = (cast_player!=null&&!end_of_play);
                        if (flag)
                            flag = flag && cast_player.getCurrentPosition() <=dur;
                        else end_of_play=false;
                    }
                }
            }
    );


    protected void pauseSound() {
        if (cast_player.isPlaying()) {
            cast_player.pause();
            play_pauseButton.setImageResource(R.drawable.play_button);
            play=false;
        }
    }


    protected boolean playSound() {
        cast_player.start();
        if (cast_player.isPlaying()) {
            synchronized (mutex) {
                mutex.notifyAll();
            }
            play=true;
            play_pauseButton.setImageResource(R.drawable.pause_button);
            return true;
        }

        else {
            first_play=true;
            return false;
        }

    }


}
