package com.example.finalprojectadirnyahelgamessearch.mainactivities;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.finalprojectadirnyahelgamessearch.R;
import java.util.ArrayList;
import java.util.List;

public class Adapter_games extends RecyclerView.Adapter<Adapter_games.MyViewHolder> implements Filterable{
    Context context;
    List<VideoGames> videoGamesList;
    List<VideoGames> videoGamesListFiltered;

    private static OnItemClickListener listener;
    public Adapter_games(Context context, List<VideoGames> videoGames) {
        this.context = context;
        this.videoGamesList = videoGames;
        this.videoGamesListFiltered = new ArrayList<>(videoGames);
    }
    public void setGames(List<VideoGames> games) {
        this.videoGamesList = games;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Adapter_games.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_games, parent, false);
        return new Adapter_games.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_games.MyViewHolder holder, int position) {
        VideoGames videoGame = videoGamesListFiltered.get(position);
        holder.textTitle.setText(videoGame.getTitle());
        Glide.with(context).load(videoGame.getThumbnail()).into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return videoGamesListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VideoGames> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(videoGamesList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (VideoGames game : videoGamesList) {
                        if (game.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(game);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                videoGamesListFiltered.clear();
                videoGamesListFiltered.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            imageView = itemView.findViewById(R.id.imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        Adapter_games.listener = listener;
    }

    }

