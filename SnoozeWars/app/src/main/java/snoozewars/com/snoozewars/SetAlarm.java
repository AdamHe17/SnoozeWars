package snoozewars.com.snoozewars;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    MainActivity act = new MainActivity();

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
        act.A.setAlarm(hour, min);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Calendar c = Calendar.getInstance();
        Log.d("hour", Integer.toString(act.A.hour));
        Log.d("min", Integer.toString(act.A.min));
        if(act.A.isSet && act.A.hour==c.get(Calendar.HOUR_OF_DAY) && act.A.min==c.get(Calendar.MINUTE)) {
            Log.d("success", "SUCCESS");
            try {
                Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                MediaPlayer mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, alert);
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                }
            } catch(Exception e) {
            }
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
