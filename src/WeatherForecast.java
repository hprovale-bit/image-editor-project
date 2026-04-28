import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast {
    public static void main(String[] args) throws Exception {
        //build url string
        //include lat and long, temp, units, and timezone
        String urlString = "https://api.open-meteo.com/v1/forecast?"
                + "latitude=39.168804"
                + "&longitude=-86.536659"
                + "&hourly=temperature_2m"
                + "&temperature_unit=fahrenheit"
                + "&timezone=EST";

        //convert string into url object
        URL url = new URL(urlString);

        //open connection to url so I can send request to API
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //use get since I need to retrieve the data
        conn.setRequestMethod("GET");

        //ask server for response and 200 means it works
        int responseCode = conn.getResponseCode();
        //if request is not successful throw an IOException since it's from an external source (the API)
        if (responseCode != 200) {
            throw new IOException("Request failed");
        }


        //create bufferedreader to read the data that is returned from the API
        //reads from the connections InputStream
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;
        //loop all returned data into a string using stringbuilder
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
    }
}