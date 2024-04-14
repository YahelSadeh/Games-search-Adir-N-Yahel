    package com.example.finalprojectadirnyahelgamessearch.fragments;

    import android.os.Bundle;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.SearchView;
    import android.widget.Toast;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonArrayRequest;
    import com.example.finalprojectadirnyahelgamessearch.R;
    import com.example.finalprojectadirnyahelgamessearch.mainactivities.Adapter_games;
    import com.example.finalprojectadirnyahelgamessearch.mainactivities.VideoGames;
    import com.example.finalprojectadirnyahelgamessearch.mainactivities.VolleySingletone;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.Serializable;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import java.util.stream.Collectors;

    public class Fragment_game extends Fragment {
        private static List<VideoGames> videoGamesList = new ArrayList<>();
        private  List<VideoGames> filteredGamesList = new ArrayList<>();

        private RecyclerView recViewGames;
        private RequestQueue requestQueue;
        private Adapter_games adapterGames;
        private static List<String[]> filters;
        public Fragment_game() {
            // Required empty public constructor
        }
        private static boolean isDataFetched = false;


        private Adapter_games.OnItemClickListener onItemClickListener = new Adapter_games.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VideoGames selectedGame;
                // Use the filtered list to get the correct game based on the adapter's position
                if (filteredGamesList.isEmpty())
                {
                     selectedGame = videoGamesList.get(position);
                }
                else
                {
                     selectedGame=filteredGamesList.get(position);
                }
                Fragment detailFragment = new Fragment_Details();
                Bundle bundle = new Bundle();

                bundle.putString("genre", selectedGame.getGenre());
                bundle.putString("developer", selectedGame.getDeveloper());
                bundle.putString("releasedate", selectedGame.getReleaseDate());
                bundle.putString("shortdescription", selectedGame.getShort_description());
                bundle.putString("title", selectedGame.getTitle());
                bundle.putString("thumbnail", selectedGame.getThumbnail());
                bundle.putSerializable("currentFilters", (Serializable) filters);
                detailFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_game, container, false);
            filteredGamesList.clear();
            recViewGames = view.findViewById(R.id.resViewGames);
            requestQueue = VolleySingletone.getmInstance(getActivity()).getRequestQueue();
            filters = bundleHandler(getArguments());

            if (areFiltersEmpty(filters)) {
                fetchVideoGame();
            }
            else{
                setupRecyclerView(filters);
            }
            ImageButton filterBtn = view.findViewById(R.id.filterBtn);
            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment_filter fragmentFilter = new Fragment_filter();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragmentFilter);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            SearchView searchView = view.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterGames.getFilter().filter(newText);
                    return false;
                }
            });

            return view;
        }
        private List<String[]> bundleHandler(Bundle bundle) {
            List<String[]> _filters = new ArrayList<>();

            if (bundle != null) {
                if(bundle.containsKey("currentFilters")&& !bundle.containsKey("selectedPlatforms"))
                {
                    Toast.makeText(getContext(), "This is a toast message!", Toast.LENGTH_SHORT).show();
                    _filters=(List<String[]>) bundle.getSerializable("currentFilters");
                    return _filters;
                }
                // Log here to check the content of the bundle
                List<String> selectedGenres = bundle.getStringArrayList("selectedGenres");
                List<String> selectedPlatforms = bundle.getStringArrayList("selectedPlatforms");
                String startDate = bundle.getString("startDate", "");
                String endDate = bundle.getString("endDate", "");
                _filters.add(selectedGenres != null ? selectedGenres.toArray(new String[0]) : new String[0]);
                _filters.add(selectedPlatforms != null ? selectedPlatforms.toArray(new String[0]) : new String[0]);
                _filters.add(new String[]{startDate, endDate});
                Log.d("FilterDebug", "Filters retrieved from bundle: Genres - " + Arrays.toString(_filters.get(0)));
            } else {
                Log.d("FilterDebug", "Bundle is null");
            }
            return _filters;
        }
        private void setupRecyclerView() {
            adapterGames = new Adapter_games(getActivity(), videoGamesList);
            adapterGames.setOnItemClickListener(onItemClickListener); // Use the member variable here
            recViewGames.setLayoutManager(new LinearLayoutManager(getActivity()));
            recViewGames.setAdapter(adapterGames);

        }
        private void setupRecyclerView(List<String[]> filters) {
            getFilteredGameList(filters);
            filteredGamesList = removeDuplicates(filteredGamesList);
            if (adapterGames == null) {
                adapterGames = new Adapter_games(getActivity(), filteredGamesList);
                adapterGames.setOnItemClickListener(onItemClickListener);
                recViewGames.setLayoutManager(new LinearLayoutManager(getActivity()));
                recViewGames.setAdapter(adapterGames);
            } else {
                adapterGames.setGames(filteredGamesList);
                adapterGames.notifyDataSetChanged();
            }

        }
        private void getFilteredGameList(List<String[]> filters) {
            filteredGamesList.clear();
                Log.d("FilterDebug", "Starting filtering process");

            if (filters == null || filters.isEmpty()) {
                Log.d("FilterDebug", "No filters applied");
                 // return the original list if no filters are set
                }

            String[] selectedGenres = filters.get(0);
            String[] selectedPlatforms = filters.get(1);
            String[] dateRange = filters.get(2);  // Contains start date at index 0 and end date at index 1
            String startDate = dateRange[0];
            String endDate = dateRange[1];
            LocalDate now = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now = LocalDate.now();
            }

            for (VideoGames game : videoGamesList) {

                boolean genreMatches = selectedGenres.length == 0 || Arrays.asList(selectedGenres).contains(game.getGenre());
                boolean platformMatches = selectedPlatforms.length == 0 || Arrays.asList(selectedPlatforms).contains(game.getPlatform());
                String gameReleaseDate = game.getReleaseDate(); // Get the release date as a string

                // Adjust the date range based on input
                boolean dateMatches = true;
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    dateMatches = (gameReleaseDate.compareTo(startDate) >= 0) && (gameReleaseDate.compareTo(endDate) <= 0);
                } else if (!startDate.isEmpty()) {
                    dateMatches = (gameReleaseDate.compareTo(startDate) >= 0) && (gameReleaseDate.compareTo(now.toString()) <= 0);
                } else if (!endDate.isEmpty()) {
                    dateMatches = (gameReleaseDate.compareTo("0000-01-01") >= 0) && (gameReleaseDate.compareTo(endDate) <= 0);  // Assuming "0000-01-01" as a far past date
                }

                if (genreMatches && platformMatches && dateMatches) {
                    filteredGamesList.add(game);
                    Log.d("FilterDebug", "Game added: " + game.getTitle());

                }
            }

            }
        private void fetchVideoGame() {
            String url = "https://www.freetogame.com/api/games";
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String title = jsonObject.getString("title");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String genre = jsonObject.getString("genre");
                                    String platform = jsonObject.getString("platform");
                                    String developer = jsonObject.getString("developer");
                                    String releaseDate = jsonObject.getString("release_date");
                                    String shortDescription = jsonObject.getString("short_description");// Assuming the date key is "release_date"

                                    // Create a new VideoGames object with the release date
                                    VideoGames videoGames = new VideoGames(title, thumbnail, genre, platform, releaseDate,shortDescription, developer);
                                    videoGamesList.add(videoGames);
                                } catch (JSONException e) {
                                    e.printStackTrace();  // Log or handle JSON parsing error
                                }
                            }
                            setupRecyclerView();  // Setup the RecyclerView with the fetched data
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            requestQueue.add(jsonArrayRequest);  // Add the request to the Volley request queue
        }
        private boolean areFiltersEmpty(List<String[]> filters) {
            if (filters.isEmpty()) return true;
            for (String[] filter : filters) {
                if (filter.length > 0) return false;
            }
            return true;
        }
        public List<VideoGames> removeDuplicates(List<VideoGames> originalList) {
            List<VideoGames> noDuplicates = new ArrayList<>();
            for (VideoGames game : originalList) {
                boolean isDuplicate = false;
                for (VideoGames uniqueGame : noDuplicates) {
                    if (game.getTitle().equals(uniqueGame.getTitle()) &&
                            game.getGenre().equals(uniqueGame.getGenre()) &&
                            game.getPlatform().equals(uniqueGame.getPlatform()) &&
                            game.getReleaseDate().equals(uniqueGame.getReleaseDate())) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    noDuplicates.add(game);
                }
            }
            return noDuplicates;
        }
    }
