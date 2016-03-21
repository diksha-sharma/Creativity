package com.curiosity.creativity.creativity;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton[] myPads;
    private boolean [] isAltImage; //place in ActivityMain
    private boolean has_flashlight;
    private boolean is_blinking;
    private SoundPool sp;
    int[] sound_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPads = new ImageButton[]{
                //When your view is "inflated," (rendered)
                //in setContentView, the views in the
                //XML file specified will become available
                (ImageButton) findViewById(R.id.pad1),
                (ImageButton) findViewById(R.id.pad2),
                (ImageButton) findViewById(R.id.pad3),
                (ImageButton) findViewById(R.id.pad4),
                (ImageButton) findViewById(R.id.pad5),
                (ImageButton) findViewById(R.id.pad6)
        };
        isAltImage = new boolean[myPads.length]; //place in onCreate
        has_flashlight = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);//add this line to onCreate
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setupSoundpool();
    }

    public void doButtonThing(View view) {
        switch (view.getId()){
            case R.id.pad1: ChangeImage(1); playSound(5); blinkyLight(); break;
            case R.id.pad2: blinkyLight(); playSound(1); break;
            case R.id.pad3: ChangeImage(0); blinkyLight(); playSound(2); break;
            case R.id.pad4: blinkyLight(); playSound(3);break;
            case R.id.pad5: ChangeImage(2); blinkyLight(); playSound(4);break;
            case R.id.pad6: blinkyLight(); playSound(0); break;
            default: break;
        }
    }

    private void ChangeImage(int padindex) {
       /* if(!isAltImage[padindex]) {
            myPads[padindex].setImageResource(R.drawable.business_cat);
            isAltImage[padindex]=true;
        }else{
            myPads[padindex].setImageResource(R.drawable.pad);
            isAltImage[padindex]=false;
        }*/

        switch(padindex){
            //sp.play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
            case 0: myPads[padindex].setImageResource(R.drawable.business_cat); break;
            case 1: myPads[padindex].setImageResource(R.drawable.cheering); break;
            case 2: myPads[padindex].setImageResource(R.drawable.bell); break;
            default: break;
        }

        Log.d("TEXT DEBUG", "Now I might want to use the references to ImageButtons");
    }

    private void blinkyLight() {
        if(has_flashlight && !is_blinking){
            AsyncBlink b = new AsyncBlink();
            b.execute();
        }
        Log.d("TEXT DEBUG", "Time for a disco party");
    }

    public final int CHEERING=0, BELL=1, BIRDS=2, TAKEOFF=3,LANDING=4,MEOW=5;
    private void setupSoundpool() { //call this in onCreate
        sound_ids = new int[7];
        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        sound_ids[CHEERING] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/clap", "raw", getPackageName()), 1);
        sound_ids[LANDING] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/cowbell", "raw", getPackageName()), 1);
        sound_ids[TAKEOFF] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/zap", "raw", getPackageName()), 1);
        sound_ids[BELL] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/ride", "raw", getPackageName()), 1);
        sound_ids[BIRDS] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/snare", "raw", getPackageName()), 1);
        sound_ids[MEOW] = sp.load(getApplicationContext(),
                getResources().getIdentifier("raw/kick", "raw", getPackageName()), 1);
    }

    private void playSound(int instrument) {
        switch(instrument){
            //sp.play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
            case CHEERING: sp.play(sound_ids[CHEERING], 1, 1, 0, 0, 1); break;
            case MEOW: sp.play(sound_ids[MEOW], 1, 1, 0, 0, 1); break;
            case BIRDS: sp.play(sound_ids[BIRDS], 1, 1, 0, 0, 1); break;
            case TAKEOFF: sp.play(sound_ids[TAKEOFF], 1, 1, 0, 0, 1); break;
            case LANDING: sp.play(sound_ids[LANDING], 1, 1, 0, 0, 1); break;
            case BELL: sp.play(sound_ids[BELL], 1, 1, 0, 0, 1); break;
            default: break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AsyncBlink extends AsyncTask<Void, Void, Void> {

        Camera cam; //the camera on the device
        Camera.Parameters p; //setup params for the camera

        @Override
        protected void onPreExecute() {
            is_blinking = true;
            cam = Camera.open();
            p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                int number_of_pulses=0;
                cam.startPreview();
                while(number_of_pulses<10){

                    Thread.sleep(30);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);

                    Thread.sleep(30);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);

                    number_of_pulses++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            is_blinking = false;
            cam.stopPreview();
            cam.release();
        }
    }
}
