package snoozewars.com.snoozewars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;


public class StartAlarm extends AppCompatActivity {
    private Socket socket;
    private ServerSocket serverSocket;

    private static final int PORT = 5000;
    private static final String SERVER_IP = "10.9.174.184";
    private static final int client_PORT = 45000;

    Uri alert;
    MediaPlayer mMediaPlayer;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_alarm);

        try {
            serverSocket = new ServerSocket(client_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        Button snoozeButton = (Button) findViewById(R.id.snooze_button);
        snoozeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new snoozeTask().execute();
                mMediaPlayer.stop();
                finish();
                new listeningTask().execute();
                Log.d("this", "ran?");
            }
        });
    }

    private class snoozeTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                socket = new Socket(SERVER_IP, PORT);

                String outMsg = "snooze";
                socket.getOutputStream().write(outMsg.getBytes(Charset.forName("UTF-8")));

                return outMsg;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class listeningTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                Socket clientSocket = serverSocket.accept();

                byte[] buf = new byte[1024];
                clientSocket.getInputStream().read(buf);
                String inMsg = new String(buf);
                return inMsg;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "oops";
        }

        protected void onPostExecute(String result) {
            Log.d("received", result);
            Log.d("received", result.substring(0, 10));
            if ((result.substring(0, 10)).compareTo("Unsnoozed!") == 0) {
                try {
                    serverSocket.close();
                    Log.d("listening","serverSocket Active");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("done listening","restart activity");
                Intent intent = getIntent();
                startActivity(intent);
            }
        }
    }
}
