package com.example.finalprojectadirnyahelgamessearch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.finalprojectadirnyahelgamessearch.R;

import java.io.Serializable;
import java.util.List;

public class Fragment_Details extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_THUMBNAIL = "thumbnail";

    private static final String ARG_GENRE = "genre";
    private static final String ARG_DEVELOPER = "developer";
    private static final String ARG_RELEASEDATE = "releasedate";
    private static final String ARG_DESCRIPTION = "shortdescription";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String title;
    private String thumbnail;
    private String description;
    private String genre;
    private String releasedate;
    private String developer;
    private List<String[]> currentFilters;

    public Fragment_Details() {
        // Required empty public constructor
    }

    public static Fragment_Details newInstance(String param1, String param2) {
        Fragment_Details fragment = new Fragment_Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            thumbnail = getArguments().getString(ARG_THUMBNAIL);
            description = getArguments().getString(ARG_DESCRIPTION);
            developer = getArguments().getString(ARG_DEVELOPER);
            releasedate = getArguments().getString(ARG_RELEASEDATE);
            genre = getArguments().getString(ARG_GENRE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey("currentFilters")) {
            currentFilters = (List<String[]>) args.getSerializable("currentFilters");
        }

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView textTitle = view.findViewById(R.id.textTitlePoster);
        TextView textDescription = view.findViewById(R.id.textDescription);
        TextView textReleasedate = view.findViewById(R.id.textReleaseDate);
        TextView textGenre = view.findViewById(R.id.textGenre);
        TextView textDeveloper = view.findViewById(R.id.textDeveloper);

        // Set the data on the views here, using the arguments passed to the fragment
        Glide.with(this).load(thumbnail).into(imageView);
        textTitle.setText(title);
        textDescription.setText(description);
        textReleasedate.setText("Release date: " +releasedate);
        textGenre.setText("Genre: " + genre);
        textDeveloper.setText("Developer: " + developer);


        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("currentFilters", (Serializable) currentFilters);

                    Fragment fragmentGame = new Fragment_game();
                    fragmentGame.setArguments(bundle);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragmentGame);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }

}
