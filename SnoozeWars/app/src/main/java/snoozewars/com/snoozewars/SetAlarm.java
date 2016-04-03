package snoozewars.com.snoozewars;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    MainActivity act = new MainActivity();

    private Socket socket;
    private static final int PORT = 5000;
    private static final String SERVER_IP = "10.9.174.184";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void cancelAlarm(View view) {
        act.A.clearAlarm();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void confirmAlarm(View view) {
        TimePicker alarmPicker = (TimePicker) findViewById(R.id.alarmPicker);
        int hour = alarmPicker.getCurrentHour();
        int min = alarmPicker.getCurrentMinute();
        int total;
        act.A.setAlarm(hour,min);
        Calendar c = Calendar.getInstance();

        new setTask().execute(hour, min);

        int cHour = c.get(Calendar.HOUR_OF_DAY);
        Log.d("hour",Integer.toString((cHour)));

        int cMin = c.get(Calendar.MINUTE);
        Log.d("min",Integer.toString((cMin)));

        if(hour == cHour && min == cMin) {
            total = 1;
        }
        else {

            int cSec = c.get(Calendar.SECOND);
            Log.d("sec", Integer.toString((cSec)));

            int cMil = c.get(Calendar.MILLISECOND);
            Log.d("mil", Integer.toString((cMil)));

            int aTime = (60 * hour + min) * 60 * 1000; //alarm time in milliseconds
            int cTime = ((60 * cHour + cMin) * 60 + cSec) * 1000 + cMil; //current time in milliseconds

            Log.d("aTime", Integer.toString(aTime));
            Log.d("cTime", Integer.toString(cTime));
            if (cTime > aTime) {
                total = 24 * 60 * 60 * 1000 - cTime + aTime;
            } else {
                total = aTime - cTime;
            }
        }
        Log.d("total",Integer.toString(total)); //in milliseconds

        Intent startApplicationIntent = new Intent(getBaseContext(), StartAlarm.class);
        //startApplicationIntent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,
                startApplicationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + total, intent);
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    private class setTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... ints) {
            try {
                socket = new Socket(SERVER_IP, PORT);

                String outMsg = "join " + ints[0].toString() + " " + ints[1].toString();
                socket.getOutputStream().write(outMsg.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SetAlarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://snoozewars.com.snoozewars/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SetAlarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://snoozewars.com.snoozewars/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
