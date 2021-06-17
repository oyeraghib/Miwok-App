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

public class PhrasesActivity extends AppCompatActivity {

    //Creating MediaPlayer Class object
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
        public void onCompletion(MediaPlayer mp) {
            releasedMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Create and setup the Audio Manager to request the Audio Focus
        mAudioManager = (AudioManager) getSystemService(NumbersActivity.AUDIO_SERVICE);


        final ArrayList<Word> PhrasesList = new ArrayList<Word>();

        PhrasesList.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        PhrasesList.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        PhrasesList.add(new Word("My name is.", "oyaaset...", R.raw.phrase_my_name_is));
        PhrasesList.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        PhrasesList.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        PhrasesList.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        PhrasesList.add(new Word("Yes, I am coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        PhrasesList.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        PhrasesList.add(new Word("Let’s go.", "yoowuti", R.raw.phrase_lets_go));
        PhrasesList.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));


        WordAdapter adapter = new WordAdapter(this, PhrasesList, R.color.category_phrases);

        ListView listView;
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = PhrasesList.get(position);

                //Toast to show message when item is clicked
                Toast.makeText(PhrasesActivity.this, "Phrase is Tapped", Toast.LENGTH_SHORT).show();

                releasedMediaPlayer();

                //Request audio focus playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //After getting access to audio focus we can play music

                    //Create and setup {Media Player} with the associated word object
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());

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

        //When the activity is stopped release the media player because we won't be
        //Playing any more sounds
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



