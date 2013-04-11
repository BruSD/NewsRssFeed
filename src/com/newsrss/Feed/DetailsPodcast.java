package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
public class DetailPodcast extends Activity {

    Podcast currentPodcast = null;
    MediaPlayer cast_player;
    boolean first_play,play,seeking,end_of_play;
    String myUri;
    Object mutex = new Object();
    TextView TitleText,DateText;
    TextView playedTime,allTime;
    ImageButton seek10Button,seek30Button,play_pauseButton;
    SeekBar castSeekbar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details_podcast);

        TitleText = (TextView)findViewById(R.id.PodcastTitleText);
        DateText = (TextView)findViewById(R.id.PodcastDateText);
        playedTime = (TextView)findViewById(R.id.PodcastPlayedTime);
        allTime = (TextView)findViewById(R.id.PodcastAllTime);
        WebView podcastDecription = (WebView) findViewById(R.id.PodcastDescription);
        seek10Button = (ImageButton) findViewById(R.id.PodcastSeek10Button);
        seek30Button = (ImageButton) findViewById(R.id.PodcastSeek30Button);
        play_pauseButton = (ImageButton) findViewById(R.id.PodcastPlay_pauseButton);
        castSeekbar = (SeekBar) findViewById(R.id.PodcastSeekbar);


        Intent startDetailArticl = getIntent();
        int position = startDetailArticl.getIntExtra("position", -1);
        currentPodcast = DataStorage.getPodcastList().get(position);

        myUri=currentPodcast.getMP3Link().toString();
        TitleText.setText(currentPodcast.getTitle());
        String date_str = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(currentPodcast.getPubDate());
        DateText.setText(date_str);
        podcastDecription.loadData("<html><body>" + currentPodcast.getDescription() + "</body></html>", "text/html", "UTF-8");

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
                    playedTime.setText(ConvertToTimeString(i));}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seeking = false;
                if(!first_play)
                {
                    if ((seekBar.getProgress()>=seekBar.getSecondaryProgress())&&(seekBar.getProgress()!=seekBar.getMax()))
                    {
                        cast_player.seekTo(seekBar.getSecondaryProgress()-5000);
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
                //play_pauseButton.setText("Play");
                play=false;
            }
        };

        ImageButton.OnClickListener ocPlay_pause = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_play)
                {
                    cast_player.reset();
                    try {
                        cast_player.setDataSource(myUri);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }


                    if(PlaySound())
                    {
                        first_play=false;
                        timeThread.start();
                        int song_dur=cast_player.getDuration();
                        castSeekbar.setMax(song_dur);
                        castSeekbar.setSecondaryProgress(song_dur);
                        allTime.setText(ConvertToTimeString(song_dur));}
                    return;
                }
                if (!play) PlaySound();
                else PauseSound();
            }
        };

        ImageButton.OnClickListener ocSeek_10 = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                cast_player.seekTo(cast_player.getCurrentPosition()-10000);
                if (!play)
                {
                    playedTime.setText(ConvertToTimeString(cast_player.getCurrentPosition()));
                    castSeekbar.setProgress(cast_player.getCurrentPosition());
                }
            }
        };

        ImageButton.OnClickListener ocSeek_30 = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                cast_player.seekTo(cast_player.getCurrentPosition()+30000);
                if (!play)
                {
                    playedTime.setText(ConvertToTimeString(cast_player.getCurrentPosition()));
                    castSeekbar.setProgress(cast_player.getCurrentPosition());
                }
            }
        };


        MediaPlayer.OnBufferingUpdateListener ocBuffer = new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                castSeekbar.setSecondaryProgress((int) (cast_player.getDuration()*percent/100));

            }
        };

        MediaPlayer.OnPreparedListener ocPrepare = new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                Toast toast = Toast.makeText(getApplicationContext(),"Мож бавити",Toast.LENGTH_SHORT);
                toast.show();

            }
        };

        TextView.OnClickListener ocCall = new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0977572892"));
                startActivity(callIntent);
            }
        };


        play_pauseButton.setOnClickListener(ocPlay_pause);
        seek10Button.setOnClickListener(ocSeek_10);
        seek30Button.setOnClickListener(ocSeek_30);
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

    Thread playCast = new Thread(
            new Runnable(){

                @Override
                public void run() {
                    if (!PlaySound())
                    {
                        first_play=true;
                        Toast toast = Toast.makeText(getApplicationContext(),"Error with playing",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            });



    Thread timeThread = new Thread(
            new Runnable() {
                public void run() {
                    int dur = cast_player.getDuration();
                    while(cast_player!=null && cast_player.getCurrentPosition() <=dur)
                    {
                        if(play)
                        {
                            final int song_dur = cast_player.getCurrentPosition();
                            castSeekbar.post(new Runnable() {
                                public void run() {
                                    if (!seeking)
                                        castSeekbar.setProgress(song_dur);
                                    playedTime.setText(ConvertToTimeString(song_dur));
                                }

                            });
                            try { if(seeking)
                                Thread.sleep(200);
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
                    }
                }
            }
    );


    protected void PauseSound() {
        if (cast_player.isPlaying())
        {
            cast_player.pause();
            //play_pauseButton.setText("Play");
            play=false;
        }
    }


    protected boolean PlaySound() {
        try {
            cast_player.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cast_player.start();


        if (cast_player.isPlaying())
        {
            synchronized (mutex) {

                mutex.notifyAll();
            }
            play=true;
            //play_pause.setText("Pause");
            return true;
        }

        else
        {first_play=true;
            Toast toast = Toast.makeText(getApplicationContext(),"Error with playing",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

    }


}
