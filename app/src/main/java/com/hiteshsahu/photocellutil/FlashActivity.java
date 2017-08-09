package com.hiteshsahu.photocellutil;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FlashActivity extends AppCompatActivity {

    private static Camera cam;
    private Camera.Parameters p;

    public void turnOnFlashLight() {
        if (cam != null) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
        }

    }

    public void turnOffFlashLight() {
        if (cam != null) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
        }
    }

    public void prepareCamera() {
        if (cam == null) {
            try {
                cam = Camera.open();
                p = cam.getParameters();
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                    cam.startPreview();
                    //On the Galaxy Tab
                    //https://stackoverflow.com/questions/5503480/use-camera-flashlight-in-android
                    cam.autoFocus(new Camera.AutoFocusCallback() {
                        public void onAutoFocus(boolean success, Camera camera) {
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Exception throws in turning on flashlight." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void runFlashSpeedTest() {
        prepareCamera();
        long startTm = System.nanoTime();
        for (int x = 0; x < 100; ++x) {
            if (x % 2 == 0)
                turnOnFlashLight();
            else
                turnOffFlashLight();
        }
        long elapsed = System.nanoTime() - startTm;
        ((TextView) findViewById(R.id.result)).setText(String.format("%.3f seconds", elapsed / 1e9));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Starting Flash Light", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                runFlashSpeedTest();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash, menu);
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
}
