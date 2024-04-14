package com.example.finalprojectadirnyahelgamessearch.mainactivities;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalprojectadirnyahelgamessearch.R;
import com.example.finalprojectadirnyahelgamessearch.fragments.Fragment_filter;
import com.example.finalprojectadirnyahelgamessearch.fragments.Fragment_game;
//import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    Adapter_games adapterGames;
    ArrayList<VideoGames> videoGames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Fragment_game fragmentGame = new Fragment_game();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragmentGame);
        transaction.commit();
    }





}