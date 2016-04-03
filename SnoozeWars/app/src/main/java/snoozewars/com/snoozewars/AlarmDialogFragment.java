package snoozewars.com.snoozewars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Admin on 4/2/2016.
 */
public class AlarmDialogFragment extends DialogFragment {

    private Socket socket;
    private ServerSocket serverSocket;

    private static final int PORT = 5000;
    private static final String SERVER_IP = "10.9.174.184";
    private static final int client_PORT = 45000;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_layout, null))
                .setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new snoozeTask().execute();
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
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

        protected void onPostExecute(String result) {
            new listeningTask().execute();
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
            if (result == "snooze") {
                DialogFragment dialog = new AlarmDialogFragment();
                dialog.show(getFragmentManager(), "alarm");
            }
        }
    }
}
