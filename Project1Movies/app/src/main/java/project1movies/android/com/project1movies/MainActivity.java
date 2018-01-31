package project1movies.android.com.project1movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = findViewById(R.id.error_message);
        mLoadingIndicator = findViewById(R.id.loading_bar);
        mRecyclerView = findViewById(R.id.movies_recycler);

        int DefineMoviesInColumn = getScreenOrientation() + 1;
        final boolean reverseLayout = false;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, DefineMoviesInColumn, LinearLayoutManager.VERTICAL, reverseLayout);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        loadMoviesData("TopRated");
    }

    private int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String SortBy = "TopRated";
        if(id == R.id.sort_by_popularity) SortBy = "Popular";
        else if(id == R.id.sort_by_average_rate) SortBy = "TopRated";

        loadMoviesData(SortBy);

        return true;
    }

    private void loadMoviesData(String SortBy) {
        showMoviesDataView();
        new MoviesQueryTask().execute(SortBy);
    }

    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void onClick(int position){
        String[] movie = mMoviesAdapter.getMovie(position);
        Intent intent = new Intent(this, MovieInfo.class);
        intent.putExtra(String.class.toString(), movie);
        startActivity(intent);
    }

    public class MoviesQueryTask extends AsyncTask<String, Void, String[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... sortBy) {

            if (sortBy.length == 0) {
                return null;
            }

            String sortedBy = sortBy[0];
            URL RequestUrl = NetworkUtils.buildUrl(sortedBy);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(RequestUrl);

                return JsonFilter
                        .getSimpleStringsFromJson(jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] MoviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (MoviesData != null) {
                showMoviesDataView();
                mMoviesAdapter.setMovieData(MoviesData);
            } else {
                showErrorMessage();
            }
        }
    }
}
