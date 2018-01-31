package project1movies.android.com.project1movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class JsonFilter {

    public static String[][] getSimpleStringsFromJson(String MovieJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject MoviesJson = new JSONObject(MovieJsonStr);

        /* Is there an error? */
        if (MoviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = MoviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray MoviesArray = MoviesJson.getJSONArray(RESULTS);

        String[][] parsedMoviesData = new String[MoviesArray.length()][5];

        for (int i = 0; i < MoviesArray.length(); i++) {

            JSONObject MovieInfo = MoviesArray.getJSONObject(i);

            parsedMoviesData[i][0] = MovieInfo.getString(TITLE);
            parsedMoviesData[i][1] = MovieInfo.getString(RELEASE_DATE);
            parsedMoviesData[i][2] = Double.toString(MovieInfo.getDouble(VOTE_AVERAGE));
            parsedMoviesData[i][3] = MovieInfo.getString(OVERVIEW);
            parsedMoviesData[i][4] = MovieInfo.getString(POSTER_PATH);

        }

        return parsedMoviesData;
    }

}
