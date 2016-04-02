package com.macaron.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class MainActivity extends Activity {

    private Socket socket;

    private static final int PORT = 5000;
    private static final String SERVER_IP = "10.9.174.184";

    TextView tDebug = null;
    Button sButton = null;
    Button setButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tDebug = (TextView) findViewById(R.id.textDebugger);
        sButton = (Button) findViewById(R.id.snoozeButton);
        setButton = (Button) findViewById(R.id.setButton);

        sButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Thread t = new Thread(new snoozeThread());
                t.start();
                try {
                    t.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Thread t = new Thread(new setThread());
                t.start();
                try {
                    t.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class snoozeThread implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(SERVER_IP, PORT);
                //socket.bind(new InetSocketAddress(PORT));
                String outMsg = "snooze";
                socket.getOutputStream().write(outMsg.getBytes(Charset.forName("UTF-8")));

                byte[] buf = new byte[1024];
                socket.getInputStream().read(buf);
                String inMsg = new String(buf);
                tDebug.setText(inMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class setThread implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(SERVER_IP, PORT);
                //socket.bind(new InetSocketAddress(PORT));
                String outMsg = "join 0700";
                socket.getOutputStream().write(outMsg.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class listenThread implements Runnable {
        @Override
        public void run() {
            try {
                byte[] buf = new byte[1024];
                socket.getInputStream().read(buf);
                String inMsg = new String(buf);
                tDebug.setText(inMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    class ClientThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                byte[] buf = new byte[1024];
//                socket = new Socket(SERVER_IP, PORT);
//
//                String outMsg = "snooze";
//                socket.getOutputStream().write(outMsg.getBytes(Charset.forName("UTF-8")));
//
//                socket.getInputStream().read(buf);
//                String inMsg = new String(buf);
//                tDebug.setText(inMsg);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}