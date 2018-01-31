package project1movies.android.com.project1movies;

import java.net.URL;

import android.content.Context;
import android.net.Uri;
import java.net.MalformedURLException;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_URL = "w185";

    private static final String MOVIES_TOP_RATED = "/top_rated";
    private static final String MOVIES_BY_POPULARITY = "/popular";

    private static final String API_KEY = "";
    private final static String API_PARAM = "api_key";

    public static URL buildUrl(String sortBy) {

        String Movies_URL = BASE_URL;
        if (sortBy.equals("TopRated")) Movies_URL += MOVIES_TOP_RATED;
        if (sortBy.equals("Popular")) Movies_URL += MOVIES_BY_POPULARITY;

        Uri builtUri = Uri.parse(Movies_URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static void setPosterImage(Context context, String posterUrl, ImageView imageView){
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(posterUrl).into(imageView);
    }
}
