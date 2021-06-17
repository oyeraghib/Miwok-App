package com.example.android.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
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
     *
     * @param savedInstanceState
     */

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
// Now that the sound file has finished playing, release the media player resources.
            releasedMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

//Create and setup the Audio Manager to request the Audio Focus
mAudioManager = (AudioManager) getSystemService(NumbersActivity.AUDIO_SERVICE);

        final ArrayList<Word> NumbersList = new ArrayList<Word>();

        NumbersList.add(new Word("one", "lutti",
                R.drawable.number_one, R.raw.number_one));
        NumbersList.add(new Word("two", "ottiko",
                R.drawable.number_two, R.raw.number_two));
        NumbersList.add(new Word("three", "tolookosu",
                R.drawable.number_three, R.raw.number_three));
        NumbersList.add(new Word("four", "oyyisa",
                R.drawable.number_four, R.raw.number_four));
        NumbersList.add(new Word("five", "massokka",
                R.drawable.number_five, R.raw.number_five));
        NumbersList.add(new Word("six", "temmokka",
                R.drawable.number_six, R.raw.number_six));
        NumbersList.add(new Word("seven", "kenekaku",
                R.drawable.number_seven, R.raw.number_seven));
        NumbersList.add(new Word("eight", "kawinta",
                R.drawable.number_eight, R.raw.number_eight));
        NumbersList.add(new Word("nine", "wo'e",
                R.drawable.number_nine, R.raw.number_nine));
        NumbersList.add(new Word("ten", "na'aacha",
                R.drawable.number_ten, R.raw.number_ten));


        WordAdapter adapter = new WordAdapter(this, NumbersList, R.color.category_numbers);

        ListView listView;
        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = NumbersList.get(position);

                //Toast to display message when Item is clicked
                Toast.makeText(NumbersActivity.this, "Number is Tapped", Toast.LENGTH_SHORT).show();


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

                    //Create and setup the {Media Player} assocaited with the current word
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());

                    //Starting the audio
                    mMediaPlayer.start();


                    //Setup a listener on media player, so that we can stop and release the
                    // media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped release the media player because we won't be playing
        //any more sounds.
        releasedMediaPlayer();


    }


    /**
     * Clean up the media Player by releasing its resources
     */
    private void releasedMediaPlayer() {
        //If the media player is not null, then it may be currently playing a sound
        if (mMediaPlayer != null) {
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








