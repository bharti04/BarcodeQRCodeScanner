package com.example.qrbarcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;


public class ScannedBarcodeActivity extends AppCompatActivity  {

    private static final String TAG = "A123";

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    private Button switchFlashlightButton;
    private boolean isFlashLightOn = false;
    private Camera camera = null;
    boolean flashmode=false;
    String content,typeofcontent, smsboby;
    private Menu mOptionsMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.main2, menu);
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
            flashOnButton();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
       // switchFlashlightButton = findViewById(R.id.btnAction);


        /*switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* if (intentData.length() > 0) {
                    if (isEmail)
                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    }
                }
*//*
                flashOnButton();

            }
        });*/
    }
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    private void flashOnButton() {
        camera=getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
                if(flashmode){
                    mOptionsMenu.findItem(R.id.action_settings).setIcon(R.drawable.flash_on);
                    Toast.makeText(getApplicationContext(),"Flash Switched ON",Toast.LENGTH_LONG).show();

                }
                else {
                    mOptionsMenu.findItem(R.id.action_settings).setIcon(R.drawable.flash_off);

                    Toast.makeText(getApplicationContext(),"Flash Switched Off",Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                   /* if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                            cameraSource.start(surfaceView.getHolder());


                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }*/
                    if(checkPermission(Manifest.permission.CAMERA)) {
                        cameraSource.start(surfaceView.getHolder());

                    }else {
                        Toast.makeText(ScannedBarcodeActivity.this, "Permission Camera denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
               // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                          //  String content,typeofcontent;
                          //  intentData = barcodes.valueAt(0).displayValue;
                            for (int index = 0; index < barcodes.size(); index++) {

                                Barcode code = barcodes.valueAt(index);
                                Log.i(TAG, "1111" + code.displayValue);


                                int type = barcodes.valueAt(index).valueFormat;
                                switch (type) {
                                    case Barcode.CONTACT_INFO:
                                        Log.i(TAG, "1" + code.displayValue);
                                        content=code.displayValue;
                                        typeofcontent="Contact Information";
                                        break;
                                    case Barcode.EMAIL:
                                        Log.i(TAG, "2" + code.displayValue);
                                        content=code.displayValue;
                                        typeofcontent="Email";
                                        break;
                                    case Barcode.ISBN:
                                        Log.i(TAG, "3" + code.rawValue);
                                         content=code.rawValue;
                                        typeofcontent="ISBN";
                                        break;
                                    case Barcode.PHONE:
                                        Log.i(TAG, "4" + code.phone.number);
                                        content=code.phone.number;
                                        typeofcontent="Phone";
                                        break;
                                    case Barcode.PRODUCT:
                                        Log.i(TAG, "5" + code.rawValue);
                                        content=code.rawValue;
                                        typeofcontent="Product";
                                        break;
                                    case Barcode.SMS:
                                        Log.i(TAG, "6" + code.sms.message +code.sms.phoneNumber);
                                        content=code.sms.message +" "+ code.sms.phoneNumber; ;
                                       // smsboby=code.sms.phoneNumber;
                                        typeofcontent="SMS";
                                        break;
                                    case Barcode.TEXT:
                                        Log.i(TAG, "7" + code.displayValue);
                                        content=code.displayValue;
                                        typeofcontent="TEXT";
                                        break;
                                    case Barcode.URL:
                                        Log.i(TAG, "url8: " + code.URL);
                                        content=code.displayValue;
                                        typeofcontent="Link";
                                        break;
                                    case Barcode.WIFI:
                                        Log.i(TAG, "9" + code.wifi.ssid);
                                        content="Encryption Type->"+code.wifi.encryptionType+"\n"+"Network->"+code.wifi.ssid+"\n"+"Password->"+code.wifi.password;
                                        typeofcontent="WIFI";
                                        break;
                                    case Barcode.GEO:
                                        Log.i(TAG, "10" + code.geoPoint.lat + ":" + code.geoPoint.lng);
                                        content="GEO"+" "+code.geoPoint.lat+" " +code.geoPoint.lng ;
                                        typeofcontent="GEO";
                                        break;
                                    case Barcode.CALENDAR_EVENT:
                                        Log.i(TAG, "11" + code.displayValue);
                                        content=code.displayValue+"\n"+code.calendarEvent.location+"\n"+code.calendarEvent.organizer;
                                        typeofcontent="Calendar Event";
                                        break;
                                    case Barcode.DRIVER_LICENSE:
                                        Log.i(TAG, "12" + code.driverLicense.licenseNumber);
                                        content=code.driverLicense.licenseNumber;
                                        typeofcontent="Driver License";
                                        break;
                                    default:
                                        Log.i(TAG, "13" + code.rawValue);
                                        content=code.rawValue;
                                        typeofcontent="TEXT";
                                        break;
                                }
                            }

                           /* if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                            }*/
                        }
                    });

                    if(content!=null) {
                        txtBarcodeValue.setText("Barcode detected");
                        Intent in = new Intent(ScannedBarcodeActivity.this, DecodeActivity.class);
                        in.putExtra("Code", content);
                        in.putExtra("type", typeofcontent);
                        startActivity(in);
                        finish();
                    }
                   /* if(content!=null && smsboby!=null) {
                        txtBarcodeValue.setText("Barcode detected");
                        Intent in = new Intent(ScannedBarcodeActivity.this, DecodeActivity.class);
                        in.putExtra("Code", content);
                        in.putExtra("type", typeofcontent);
                        in.putExtra("type", typeofcontent);
                        startActivity(in);
                        finish();
                    }
*/
                }else {
                    txtBarcodeValue.setText("No barcode could be detected.");

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CAMERA_PERMISSION :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "You can scan QR & Barcode Code", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
