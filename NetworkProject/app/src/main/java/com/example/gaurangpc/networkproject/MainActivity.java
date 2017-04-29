package com.example.gaurangpc.networkproject;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;


public class MainActivity extends AppCompatActivity {


    static int check=0;
    private static final String LOG_TAG = "Main_Activity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    check++;
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                    if (check!=0)
                        onStopp();
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    TextView tv;
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);


        TextView tv = (TextView) findViewById(R.id.ipAddress);


//        getExternalFilesDir(CONTEXt)
        // Record to the external cache directory for visibility
        mFileName = getCacheDir().getAbsolutePath();
//        Log.d(MY_LOG_TAG, mFileName);
        mFileName += "/audiorecordtest.3gp";
        Log.d(LOG_TAG, "Intial File Mame: "+mFileName);

        LinearLayout ll = new LinearLayout(this);

        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        Button myButton = new Button(this);
        myButton.setText("Send");
        myButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Log.d(MY_LOG_TAG , "try function callled");
                tryfunc();
            }
        });
        ll.addView(myButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);


    }

    public String MY_LOG_TAG = "Main_Activity";
    public Sender sender;
    public void tryfunc(){
        Log.d(MY_LOG_TAG, "hhhhhh");
        Log.d(MY_LOG_TAG, getCacheDir() +"");

        try {
            File[] files = new File(getCacheDir().getAbsolutePath()).listFiles();
            Log.d(LOG_TAG, "Files in the directory");

            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                    Log.d(LOG_TAG, file.getName());
                }
            }



            File outputFile = File.createTempFile("my_new_file", ".3gp", getCacheDir());
            Log.d(LOG_TAG , "Correct Path : "+ outputFile.getAbsolutePath());
//            outputFile.deleteOnExit();
            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
            fileoutputstream.write(decodedByteArray);
            fileoutputstream.close();







            Log.d(LOG_TAG , "Before relase");
//            mPlayer.release();
            Log.d(LOG_TAG , "After 1st relase");
            /*mPlayer = new MediaPlayer();
            Log.d(LOG_TAG , "After md");
//            mPlayer.release();
            Log.d(LOG_TAG , "After 2nd release");
            try {
                Log.d(LOG_TAG , "before start");
                mPlayer.setDataSource(getCacheDir().getAbsolutePath()+"/"+outputFile.getName());
                Log.d(LOG_TAG, "Main Directory : "+getCacheDir().getAbsolutePath()+"/"+outputFile.getName());
                mPlayer.prepare();
                mPlayer.start();
                Log.d(LOG_TAG, "Playing..........");
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }*/
            sender = new Sender(MainActivity.this, outputFile);
            Log.d(LOG_TAG, "After Sender");
//            tv.setText(getIpAddress());
            Log.d(LOG_TAG, "After Set TExt");

        }catch(Exception e){e.printStackTrace();}
    }


    public void onStopp(){
        try{
            display();
            doEncoding();
        } catch (IOException e) {

        }
    }

    byte[] encodedByteArray;
    byte[] decodedByteArray;
//    byte[] decodedByteArrayWrong;

    public void doEncoding(){
        Log.d(LOG_TAG, "Do encodning");
        int code[] = {-1,1,1,-1,1,-1,-1,-1};
        byte  byteCode = 7;
        byte reverseCode = -8;
        Manipulate obj = new Manipulate(byteArray,byteCode ,reverseCode );
        encodedByteArray = obj.encoding();
        Log.d(LOG_TAG, Arrays.toString(encodedByteArray));
        Log.d(LOG_TAG , "Done encoding");
        Manipulate obj2 = new Manipulate(byteArray, (byte)(-8), (byte)7);
        decodedByteArray = obj.decoding(encodedByteArray);
        Log.d(LOG_TAG, Arrays.toString(decodedByteArray));
    }

    @Override
    public void onStop() {

        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    byte[] byteArray;
    public void display() throws IOException{
        Log.d(LOG_TAG, "Display Function Called");
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(mFileName);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while (fis.available() > 0) {
                bos.write(fis.read());
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }



        byte[] bytes = bos.toByteArray();
        byteArray=bytes;

        Log.d(LOG_TAG,"length = "+bytes.length );
        Log.d(LOG_TAG,Arrays.toString(bytes));



//        try {
//
//            File outputFile = File.createTempFile("audioFile", "3gp", getCacheDir());
//            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
//            fileoutputstream.write(bytes);
//            fileoutputstream.close();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }


    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sender == null) {
            return;
        }
        ServerSocket serverSocket = sender.getServerSocket();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
