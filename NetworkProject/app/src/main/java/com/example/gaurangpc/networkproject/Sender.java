package com.example.gaurangpc.networkproject;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurangpc.networkproject.MainActivity;
import com.example.gaurangpc.networkproject.R;

public class Sender{

    //TextView infoIp, infoPort;

    static  int SocketServerPORT = 8081;
    ServerSocket serverSocket = null;

    ServerSocketThread serverSocketThread;
    Activity activity;
    File file;

    public Sender(Activity activity,File file) {
        this.activity = activity;
        this.file = file;
        //this.serverSocket=serverSocket;
//        this.serverSocketThread=serverSocketThread;
        //infoIp = (TextView) findViewById(R.id.infoip);
        //infoPort = (TextView) findViewById(R.id.infoport);

        //infoIp.setText(getIpAddress());

        serverSocketThread = new ServerSocketThread();
        System.out.println("Before Start !!!");
        serverSocketThread.start();
    }

    public ServerSocket getServerSocket() {
        return  serverSocket;
    }



    public class ServerSocketThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
//                SocketServerPORT = serverSocket.getLocalPort();
                System.out.println("Port : "+ SocketServerPORT);
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //infoPort.setText("I'm waiting here: "
                          //      + serverSocket.getLocalPort());
                    }});

                while (true) {
                    socket = serverSocket.accept();
                    FileTxThread fileTxThread = new FileTxThread(socket);
                    fileTxThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public class FileTxThread extends Thread {
        Socket socket;

        FileTxThread(Socket socket){
            this.socket= socket;
            System.out.println("Socket created");
        }

        @Override
        public void run() {
            System.out.println("Run run created!!!");

//            File file = new File(
//                    Environment.getExternalStorageDirectory(),
//                    "test.txt");

//            PrintWriter writer = null;
//            try {
//                writer = new PrintWriter("test.txt", "UTF-8");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            writer.println("The first line");
//            writer.println("The second line");
            System.out.println("File path : "+file.getAbsolutePath());
            byte[] bytes = new byte[(int) file.length()];
            BufferedInputStream bis;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytes, 0, bytes.length);
                OutputStream os = socket.getOutputStream();
                os.write(bytes, 0, bytes.length);
                os.flush();
                socket.close();

                final String sentMsg = "File sent to: " + socket.getInetAddress();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(),
                                sentMsg,
                                Toast.LENGTH_LONG).show();
                    }});

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}