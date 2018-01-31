package project1movies.android.com.project1movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieInfo  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mockup);

        TextView mTitle = findViewById(R.id.movie_title);
        TextView mYear = findViewById(R.id.movie_year);
        TextView mRating = findViewById(R.id.movie_rating);
        TextView mSynopsis = findViewById(R.id.movie_synopsis);
        ImageView mPosterImage = findViewById(R.id.movie_poster);

        Intent intent = getIntent();
        String[] movie = (String[]) intent.getSerializableExtra(String.class.toString());

        mTitle.setText(movie[0]);
        mYear.setText(movie[1].substring(0,4));
        mRating.setText(movie[2]+"/10");
        mSynopsis.setText(movie[3]);

        String posterUrl = NetworkUtils.POSTER_URL + NetworkUtils.POSTER_SIZE_URL +
                movie[4];

        NetworkUtils.setPosterImage(this, posterUrl, mPosterImage);

    }
}
