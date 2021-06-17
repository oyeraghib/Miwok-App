package com.example.android.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class ColorsActivity extends AppCompatActivity {

    //Creating object of MediaPlayer class
    MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

     private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
         @Override
         public void onAudioFocusChange(int focusChange) {

             if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                     focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                 //The AUDIO_FOCUS_TRANSIENT state means that we have lost audio focus for
                 //short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_MAY_DUCK means that
                 //the audio is playing at very slow volume.

                 //We have used these method because our files have smaller time period

                 //Pause playback and restart MediaPlayer to the start of the file
                 //that way we can play the word from the beginning when we resume playback

                 mMediaPlayer.pause();
                 mMediaPlayer.seekTo(0);
             } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                 //The AUDIOFOCUS_GAIN case means that we have regained focus and can resume
                 //playback
                 mMediaPlayer.start();

             } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                 //The AUDIOFOCUS_LOSS case means that we have lost the audio focus
                 //and stop playing and cleanup resources
                 releasedMediaPlayer();

             }

         }
     };

    /**
     * This listener gets triggered when the {@Link Media Player} has completed
     * playing the audio file
     * @param savedInstanceState
     */

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releasedMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Create and setup the Audio Manager to request the Audio Focus
        mAudioManager = (AudioManager) getSystemService(NumbersActivity.AUDIO_SERVICE);

        final ArrayList<Word> ColorsList = new ArrayList<Word>();

        ColorsList.add(new Word("red", "weṭeṭṭi",
                R.drawable.color_red, R.raw.color_red));
        ColorsList.add(new Word("green", "chokokki",
                R.drawable.color_green, R.raw.color_green));
        ColorsList.add(new Word("brown", "ṭakaakki",
                R.drawable.color_brown, R.raw.color_brown));
        ColorsList.add(new Word("gray", "ṭopoppi",
                R.drawable.color_gray, R.raw.color_gray));
        ColorsList.add(new Word("black", "kululli",
                R.drawable.color_black, R.raw.color_black));
        ColorsList.add(new Word("white", "kelelli",
                R.drawable.color_white, R.raw.color_white));
        ColorsList.add(new Word("dusty yellow", "ṭopiisә",
                R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        ColorsList.add(new Word("mustard yellow", "chiwiiṭә",
                R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));


        WordAdapter adapter = new WordAdapter(this, ColorsList, R.color.category_colors);

        ListView listView;
        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //Getting the position associated with the tapped item in list
                Word word = ColorsList.get(position);

                Toast.makeText(ColorsActivity.this, "Color is Tapped", Toast.LENGTH_SHORT).show();

                //Release the media player if it currently exists because we are about to
                //play a different sound file.
                releasedMediaPlayer();

                //Request audio focus playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //After getting access to audio focus we can play music

                    //Create and setup the media player associated with each word
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceId());

                    //Starting audio
                    mMediaPlayer.start();


                    //Setup a listener on media player, so that we can stop and release the
                    // media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }

    @Override
    protected void onStop(){
        super.onStop();
        // Release the media player when the activity is closed because
        // we won't need any more audios to be played

        releasedMediaPlayer();

    }


        /**
         * Clean up the media Player by releasing its resources
         */
        private void releasedMediaPlayer(){
            //If the media player is not null, then it may be currently playing a sound
            if(mMediaPlayer != null) {
                //Regardless of the current state of the media player release its resources
                //because we no longer need it.
                mMediaPlayer.release();

                //Set up the media player back to null. For our code we have decided that
                //setting the media player is an easy way to tell that media player is not
                //configured to play an audio file at that moment.

                mMediaPlayer = null;

                // Regardless of whether or not we were granted audio focus, abandon it.
                //This also unregisters the audioFocusChangeListener so we don't get
                // anymore callbacks
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);


            }
        }




    }


