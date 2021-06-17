package com.example.android.miwokapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter<Word> {

        //Resource Id for background color for the list of words.
        private int mcolorResourceId;

        public WordAdapter(NumbersActivity context, ArrayList<Word> NumbersList, int colorResourceId) {

            super(context, 0, NumbersList);
            mcolorResourceId = colorResourceId;
        }

        public WordAdapter(ColorsActivity context, ArrayList<Word> ColorsList, int colorResourceId){

            super(context, 0, ColorsList);
            mcolorResourceId = colorResourceId;
        }

        public WordAdapter(FamilyActivity context, ArrayList<Word> FamilyList,int colorResourceId){

            super(context, 0, FamilyList);
            mcolorResourceId = colorResourceId;
        }

        public WordAdapter(PhrasesActivity context, ArrayList<Word> PhrasesList, int colorResourceId){

            super(context, 0, PhrasesList);
            mcolorResourceId = colorResourceId;
        }


        //Overriding method for displaying lists
            @Override
            public View getView(int position, View convertView, ViewGroup parent){


            //Making View object as listItemView

            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_view, parent, false);
            }

            Word currentWord = getItem(position);

            // Find the TextView in the list_view.xml layout with the ID miwok_name
            TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_name);
            miwokTextView.setText(currentWord.getMiwokTranslation());

            //Find the TextView in the list_view.xml layout with ID default_name
            TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_name);
            defaultTextView.setText(currentWord.getDefaultTranslation());


            //Find the ImageView in the list_view.xml layout with ID image
            ImageView iconView = (ImageView) listItemView.findViewById(R.id.image);


            //Use the method hasImage from Word.java to see if an Image is associated with it.
            if(currentWord.hasImage()) {
                iconView.setImageResource(currentWord.getResourceId());

                //Set the view to be visibile
                iconView.setVisibility(View.VISIBLE);
            }

            //Otherwise hide the ImageView (set visibility to GONE)
            else{
                iconView.setVisibility(View.GONE);
            }

            //Set the theme color for the list item
                View textContainer = listItemView.findViewById(R.id.text_container);

            //Find the color that the resource Id maps to
                int color = ContextCompat.getColor(getContext(), mcolorResourceId);

                //Set the background color of text container view
                textContainer.setBackgroundColor(color);

            return listItemView;
    }
}
