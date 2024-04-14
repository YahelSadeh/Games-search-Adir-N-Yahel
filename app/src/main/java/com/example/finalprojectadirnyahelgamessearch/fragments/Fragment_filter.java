package com.example.finalprojectadirnyahelgamessearch.fragments;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.example.finalprojectadirnyahelgamessearch.R;

public class Fragment_filter extends Fragment {
    private Button btnStartDate, btnEndDate;
    private String startDate, endDate;
    public Fragment_filter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        Button submitFiltersButton = view.findViewById(R.id.submitFiltersButton);
        Button platformChooseButton = view.findViewById(R.id.PlatformChoose);
        Button genderChooseButton = view.findViewById(R.id.GenderChoose);
        btnStartDate = view.findViewById(R.id.btnStartDate);
        btnEndDate = view.findViewById(R.id.btnEndDate);
        btnStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        btnEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        genderChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenreSelectionDialog();
            }
        });
        platformChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlatformSelectionDialog();
            }
        });
        submitFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the stored filter choices from the fragment's arguments
                Bundle bundle = getArguments();

                // Pass the filter choices to Fragment_game and perform the fragment transaction
                Fragment_game fragmentGame = new Fragment_game();
                fragmentGame.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, fragmentGame);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
    private void showGenreSelectionDialog() {
        String[] genres = {"Shooter", "Card Game", "MMORPG", "Strategy", "Action RPG", "Fighting", "MOBA", "Action Game",
                "Battle Royale", "Sports", "Racing", "MMO"};
        boolean[] selectedGenres = new boolean[genres.length];
        List<String> selectedGenresList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Genres");

        builder.setMultiChoiceItems(genres, selectedGenres, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedGenresList.add(genres[which]);
                } else {
                    selectedGenresList.remove(genres[which]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the existing bundle or create a new one if none exists
                Bundle bundle = getArguments();
                if (bundle == null) {
                    bundle = new Bundle();  // If there were no previous arguments, create a new Bundle
                }
                bundle.putStringArrayList("selectedGenres", new ArrayList<>(selectedGenresList));
                setArguments(bundle);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void showPlatformSelectionDialog() {
        String[] platforms = {"Web Browser", "PC (Windows)"};
        boolean[] selectedPlatforms = new boolean[platforms.length];
        List<String> selectedPlatformsList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Platforms");
        builder.setMultiChoiceItems(platforms, selectedPlatforms, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedPlatformsList.add(platforms[which]);
                } else {
                    selectedPlatformsList.remove(platforms[which]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = getArguments();
                if (bundle == null) {
                    bundle = new Bundle(); // Create a new Bundle if none exist
                }

                // Update the platforms list in the bundle
                bundle.putStringArrayList("selectedPlatforms", new ArrayList<>(selectedPlatformsList));
                setArguments(bundle); // Set the updated bundle as the fragment's arguments
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void showDatePickerDialog(final boolean isStartDate) {
        Calendar cal = Calendar.getInstance();
        List<String> selectedDatesmsList = new ArrayList<>();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    Bundle bundle = getArguments();
                    if (bundle == null) {
                        bundle = new Bundle();
                    }

                    if (isStartDate) {
                        bundle.putString("startDate", formattedDate);

                    } else {
                        bundle.putString("endDate", formattedDate);
                    }
                    setArguments(bundle);

                    if (isStartDate) {
                        Button btnStartDate = getView().findViewById(R.id.btnStartDate);
                        btnStartDate.setText(formattedDate);
                    } else {
                        Button btnEndDate = getView().findViewById(R.id.btnEndDate);
                        btnEndDate.setText(formattedDate);
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

}





