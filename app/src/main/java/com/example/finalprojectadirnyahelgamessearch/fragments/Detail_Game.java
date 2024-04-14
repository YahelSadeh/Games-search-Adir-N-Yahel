package com.example.finalprojectadirnyahelgamessearch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalprojectadirnyahelgamessearch.R;

public class Detail_Game extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_THUMBNAIL = "thumbnail";
    private String title;
    private String thumbnail;
    public static Fragment_Details newInstance(String title, String thumbnail) {
        Fragment_Details fragment = new Fragment_Details();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_THUMBNAIL, thumbnail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            thumbnail = getArguments().getString(ARG_THUMBNAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item, container, false);

        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView textTitle = view.findViewById(R.id.textTitlePoster);
        TextView textDescription = view.findViewById(R.id.textDescription);
        TextView textGenre = view.findViewById(R.id.textGenre);
        TextView textReleaseDate = view.findViewById(R.id.textReleaseDate);
        TextView textDeveloper = view.findViewById(R.id.textDeveloper);

        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                String mImage = bundle.getString("image");
                String mTitle = bundle.getString("title");
                String mDescription = bundle.getString("description");
                String mGenre = bundle.getString("genre");
                String mReleaseDate = bundle.getString("releasedate");
                String mDeveloper = bundle.getString("developer");

                Glide.with(this).load(mImage).into(imageView);
                textTitle.setText(mTitle);
                textDescription.setText(mDescription);
                textGenre.setText(mGenre);
                textReleaseDate.setText(mReleaseDate);
                textDeveloper.setText(mDeveloper);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        textTitle.setText("title");
        Glide.with(this).load(thumbnail).into(imageView);

        return view;
    }
}
