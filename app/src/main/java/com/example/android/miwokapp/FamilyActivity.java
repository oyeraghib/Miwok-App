package com.example.android.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class FamilyActivity extends AppCompatActivity {

    //Creating object of Media Player file
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

        final ArrayList<Word> FamilyList = new ArrayList<Word>();

        FamilyList.add(new Word("father", "әpә",
                R.drawable.family_father, R.raw.family_father));
        FamilyList.add(new Word("mother", "әṭa",
                R.drawable.family_mother, R.raw.family_mother));
        FamilyList.add(new Word("son", "angsi",
                R.drawable.family_son, R.raw.family_son));
        FamilyList.add(new Word("daughter", "tune",
                R.drawable.family_daughter, R.raw.family_daughter));
        FamilyList.add(new Word("older brother", "taachi",
                R.drawable.family_older_brother, R.raw.family_older_brother));
        FamilyList.add(new Word("younger brother", "chalitti",
                R.drawable.family_younger_brother, R.raw.family_younger_brother));
        FamilyList.add(new Word("older sister", "teṭe",
                R.drawable.family_older_sister, R.raw.family_older_sister));
        FamilyList.add(new Word("younger sister", "kolliti",
                R.drawable.family_younger_sister, R.raw.family_younger_sister));
        FamilyList.add(new Word("grandmother", "ama",
                R.drawable.family_grandmother, R.raw.family_grandmother));
        FamilyList.add(new Word("grandfather", "paapa",
                R.drawable.family_grandfather, R.raw.family_grandfather));


        WordAdapter adapter = new WordAdapter(this, FamilyList, R.color.category_family);

        ListView listView;
        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Creating Toast to display the message when word is tapped



                Word word = FamilyList.get(position);

                Toast.makeText(FamilyActivity.this, "Family Item is Tapped", Toast.LENGTH_SHORT).show();

                releasedMediaPlayer();

                //Request audio focus playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //After getting access to audio focus we can play music

                    //Create and setup the {Media Player} associated with the word file
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());

                    //Starting media
                    mMediaPlayer.start();


                    //Setup a listener on media player, so that we can stop and release the
                    // media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);

                }

            }
        });
    }


        @Override
        protected void onStop()
        {
            super.onStop();

            //Release the media player when the activity is stopped becuase we won't be playing
            // any more sounds

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
