package com.example.timetable.customs.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timetable.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CustomAudioPlayer extends LinearLayout {
    private static final String TAG = "CustomAudioPlayer";
    private View root;
    private Context context;
    private AudioPlayerListener listener;
    private FloatingActionButton fab;
    private SeekBar seekBar;
    private TextView remainingTimeTextView;
    private int duration;
    private boolean showDuration = true;
    private MediaPlayer mediaPlayer = null;
    private Timer timer;
    private boolean isPlaying = false;

    public CustomAudioPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        root = inflate(context, R.layout.layout_audio_player, this);
        fab = root.findViewById(R.id.fab_play_sound);
        seekBar = root.findViewById(R.id.seekBar_sound_progress);
        remainingTimeTextView = root.findViewById(R.id.txt_sound_progress);

        //initial attributes
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.audioPlayer_attrs, 0, 0);
        fab.setBackgroundTintList(ColorStateList.valueOf(ta.getColor(R.styleable.audioPlayer_attrs_android_buttonTint, Color.BLUE)));
        seekBar.setThumbTintList(ColorStateList.valueOf(ta.getColor(R.styleable.audioPlayer_attrs_android_thumbTint, Color.BLUE)));
        seekBar.setProgressTintList(ColorStateList.valueOf(ta.getColor(R.styleable.audioPlayer_attrs_android_progressTint, Color.BLUE)));
        showDuration = ta.getBoolean(R.styleable.audioPlayer_attrs_showDuration, true);
        seekBar.setProgress(ta.getInt(R.styleable.audioPlayer_attrs_android_progress, 0));
        ta.recycle();
    }

    public void setupAudioPlayer(String fileName) {
//        File musicDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        if (musicDirectory == null) {
//            Log.e(TAG, "setupMediaPlayer: directory not found!");
//            return;
//        }
//        File music = new File(musicDirectory.getPath(), fileName + ".mp3");
        mediaPlayer = new MediaPlayer();

//        if (music.exists()) {
        try {
            mediaPlayer.setDataSource(context, Uri.parse("https://irsv.upmusics.com/Downloads/Musics/Hamed%20Zamani%20%7C%20Saranjaam%20(128).mp3"));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    duration = mediaPlayer.getDuration();
                    remainingTimeTextView.setText(formatDuration(duration));
                    seekBar.setMax(duration);
                    setupViews();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    fab.setImageResource(R.drawable.ic_round_play_arrow_24);
                    if (listener != null) listener.onFinished();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
//        } else {
//            Toast.makeText(context, "Audio not found", Toast.LENGTH_LONG).show();
//        }
    }

    private void setupViews() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    if (listener != null) listener.onPause();
                    mediaPlayer.pause();
                    fab.setImageResource(R.drawable.ic_round_play_arrow_24);
                } else {
                    if (listener != null) listener.onPlay();
                    mediaPlayer.start();
                    fab.setImageResource(R.drawable.ic_round_pause_24);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    if (listener != null) listener.onProgressChanged();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            remainingTimeTextView.setText(formatDuration(duration - progress));
                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer.isPlaying())isPlaying=true;
                else isPlaying=false;
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPlaying) mediaPlayer.start();
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        remainingTimeTextView.setText(
                                formatDuration(duration - mediaPlayer.getCurrentPosition()));
                    }
                });
            }
        }, 0, 1000);
    }

    private String formatDuration(long duration) {
        int seconds = (int) (duration / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
    }

    public void setOnAudioListener(@NonNull AudioPlayerListener listener) {
        this.listener = listener;
    }

    public void pause() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void destroyAudioPlayer() {
        if (timer == null || mediaPlayer == null) return;
        timer.purge();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public boolean isAudioSetup() {
        return mediaPlayer != null;
    }

    public interface AudioPlayerListener {
        void onPlay();

        void onPause();

        void onProgressChanged();

        void onFinished();
    }
}
