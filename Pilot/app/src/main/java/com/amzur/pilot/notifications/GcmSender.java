package com.amzur.pilot.notifications;

import android.util.Log;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// NOTE:
// This class emulates a server for the purposes of this sample,
// but it's not meant to serve as an example for a production app server.
// This class should also not be included in the client (Android) application
// since it includes the server's API key. For information on GCM server
// implementation see: https://developers.google.com/cloud-messaging/server
public class GcmSender {

    public static final String API_KEY = "AIzaSyBfYjWBSjV9_zKrI31VFkJyWwHyhwKA_HY";

    public static void main(String[] args) {
     /*   if (args.length < 1 || args.length > 2 || args[0] == null) {
            System.err.println("usage: ./gradlew run -Pmsg=\"testing\" [-Pto=\"DEVICE_TOKEN\"]");
            System.err.println("");
            System.err.println("Specify a test message to broadcast via GCM. If a device's GCM registration token is\n" +
                    "specified, the message will only be sent to that device. Otherwise, the message \n" +
                    "will be sent to all devices subscribed to the \"global\" topic.");
            System.err.println("");
            System.err.println("Example (Broadcast):\n" +
                    "On Windows:   .\\gradlew.bat run -Pmsg=\"<Your_Message>\"\n" +
                    "On Linux/Mac: ./gradlew run -Pmsg=\"<Your_Message>\"");
            System.err.println("");
            System.err.println("Example (Unicast):\n" +
                    "On Windows:   .\\gradlew.bat run -Pmsg=\"<Your_Message>\" -Pto=\"<Your_Token>\"\n" +
                    "On Linux/Mac: ./gradlew run -Pmsg=\"<Your_Message>\" -Pto=\"<Your_Token>\"");
            System.exit(1);
        }*/
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", "ramesh");
            jGcmData.put("to", "/topics/global");
            // Where to send GCM message.
          /*  if (args.length > 1 && args[1] != null) {
                jGcmData.put("to", args[1].trim());
            } else {
                jGcmData.put("to", "/topics/global");
            }*/
            // What to send in GCM message.
            jGcmData.put("data", jData);

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp =toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (Exception e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
    }
    public static String toString(InputStream response)
    {
        InputStream inputStream;
        try {
            inputStream =response;
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            String bufferedStrChunk;

            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk);
            }

            Log.i("response", stringBuilder.toString());
            return stringBuilder.toString();
        } catch (IllegalStateException | IOException e) {

            e.printStackTrace();
        }
        return "";

    }

}