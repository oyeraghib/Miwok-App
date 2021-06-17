package com.example.android.miwokapp;

import androidx.annotation.NonNull;

public class Word {

     private String mMiwokTranslation;
     private String mDefaultTranslation;

     private static final int NO_IMAGE_PROVIDED = -1;
     private int mImageResourceId = NO_IMAGE_PROVIDED ;
     private int mAudioResourceId;


     /**
      * @param defaultTranslation is for the Default Translation
      * @param miwokTranslation is for the Miwok Translation
      * @param audioFile is for the audio for each item in ListView


      **/
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId){

        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;


    }

    public Word(String defaultTranslation, String miwokTranslation, int resourceId, int audioResourceId){

        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = resourceId;
        mAudioResourceId = audioResourceId;

    }


    //Method to get Miwok Translation
    public String getMiwokTranslation(){return mMiwokTranslation; }


    //Method to get Default Langauge
    public String getDefaultTranslation(){return mDefaultTranslation; }


    //Method to get Image
    public int getResourceId() {return mImageResourceId; }

    //Method to get AudioId
    public int getAudioResourceId(){ return mAudioResourceId;}

    //Return whether or not there is image for the Word present in Item List of List View
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }


    @NonNull
    @Override
    public String toString() {


        return "Word{" +
                "mDefaultTranslation= '" +mDefaultTranslation + '\''+
                "mMiwokTranslation = '" + mMiwokTranslation + '\''+
                "mImageResourceId = '" + mImageResourceId + '\''+
                "mAudioResourceId = '" + mAudioResourceId + '}';

    }
}
