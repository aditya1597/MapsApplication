package com.example.aditya.myapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class HttpClientTest {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) throws IOException {

        // NOTE: you must manually construct wml_credentials hash map below
        // using information retrieved from your IBM Cloud Watson Machine Learning Service instance.

        Map<String, String> wml_credentials = new HashMap<String, String>()
        {{
            put("url", "https://eu-gb.ml.cloud.ibm.com/v3/wml_instances/2dec08bd-6155-4740-b070-67ea9f101843/deployments/453caad6-a63f-41cd-854d-8e96acb92968/online");
            put("username", "vivek.jain@ingrammicro.com");
            put("password", "Olduser@1234");
        }};

        String wml_auth_header = "Basic " +
                Base64.getEncoder().encodeToString((wml_credentials.get("username") + ":" +
                        wml_credentials.get("password")).getBytes(StandardCharsets.UTF_8));
        String wml_url = wml_credentials.get("url") + "/v3/identity/token";
        HttpURLConnection tokenConnection = null;
        HttpURLConnection scoringConnection = null;
        BufferedReader tokenBuffer = null;
        BufferedReader scoringBuffer = null;
        try {
            // Getting WML token
            URL tokenUrl = new URL(wml_url);
            tokenConnection = (HttpURLConnection) tokenUrl.openConnection();
            tokenConnection.setDoInput(true);
            tokenConnection.setDoOutput(true);
            tokenConnection.setRequestMethod("GET");
            tokenConnection.setRequestProperty("Authorization", wml_auth_header);
            tokenBuffer = new BufferedReader(new InputStreamReader(tokenConnection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = tokenBuffer.readLine()) != null) {
                jsonString.append(line);
            }
            // Scoring request
            URL scoringUrl = new URL("https://eu-gb.ml.cloud.ibm.com/v3/wml_instances/2dec08bd-6155-4740-b070-67ea9f101843/deployments/453caad6-a63f-41cd-854d-8e96acb92968/online");
            String wml_token = "Bearer " +
                    jsonString.toString()
                            .replace("\"","")
                            .replace("}", "")
                            .split(":")[1];
            scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
            scoringConnection.setDoInput(true);
            scoringConnection.setDoOutput(true);
            scoringConnection.setRequestMethod("POST");
            scoringConnection.setRequestProperty("Accept", "application/json");
            scoringConnection.setRequestProperty("Authorization", wml_token);
            scoringConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");

            // NOTE: manually define and pass the array(s) of values to be scored in the next line
            String payload = "{\"fields\": [\"Distance\", \"Rain\"], \"values\": [array_of_values_to_be_scored, another_array_of_values_to_be_scored]}";
            writer.write(payload);
            writer.close();

            scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
            StringBuffer jsonStringScoring = new StringBuffer();
            String lineScoring;
            while ((lineScoring = scoringBuffer.readLine()) != null) {
                jsonStringScoring.append(lineScoring);
            }
            System.out.println(jsonStringScoring);
        } catch (IOException e) {
            System.out.println("The URL is not valid.");
            System.out.println(e.getMessage());
        }
        finally {
            if (tokenConnection != null) {
                tokenConnection.disconnect();
            }
            if (tokenBuffer != null) {
                tokenBuffer.close();
            }
            if (scoringConnection != null) {
                scoringConnection.disconnect();
            }
            if (scoringBuffer != null) {
                scoringBuffer.close();
            }
        }
    }
}
