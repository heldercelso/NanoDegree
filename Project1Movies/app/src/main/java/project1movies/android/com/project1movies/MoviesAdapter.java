package project1movies.android.com.project1movies;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private String[][] mMoviesData;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(int position);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView mMoviePoster;
        public final Context context;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = view.findViewById(R.id.movie_poster);
            context = itemView.getContext();

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }

        void bind(String posterUrl) {
            NetworkUtils.setPosterImage(context, posterUrl, mMoviePoster);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        String posterUrl = NetworkUtils.POSTER_URL +
                NetworkUtils.POSTER_SIZE_URL +
                mMoviesData[position][4];

        moviesAdapterViewHolder.bind(posterUrl);
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) return 0;
        return mMoviesData.length;
    }

    public void setMovieData(String[][] MovieData) {
        mMoviesData = MovieData;
        notifyDataSetChanged();
    }

    public String[] getMovie(int index) {
        return mMoviesData[index];
    }
}
