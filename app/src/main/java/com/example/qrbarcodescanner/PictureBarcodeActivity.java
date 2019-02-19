package com.example.qrbarcodescanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.FileNotFoundException;

import pub.devrel.easypermissions.EasyPermissions;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureBarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnScan ,btnClose;
    TextView txtResultBody;

    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CAMERA_REQUEST = 101;
    private static final String TAG = "API123";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private int RESULT_LOAD_IMAGE=20;
    ImageView imageView;
    String content,typeofcontent;
    SparseArray<Barcode> barcodes;
    PhotoViewAttacher pAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_picture);
        imageView = findViewById(R.id.imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*ActivityCompat.requestPermissions(PictureBarcodeActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
*/
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();

        initViews();

      /*  if (savedInstanceState != null) {
            if (imageUri != null) {
                imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
                txtResultBody.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
            }
        }*/

      Intent in=getIntent();
      imageUri = in.getParcelableExtra("Image");
        Log.d(TAG,"value"+imageUri);
      imageView.setImageURI(imageUri);

      launchMediaScanIntent();


        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        if (!detector.isOperational()) {
            txtResultBody.setText("Detector initialisation failed");
            return;
        }



        ScanImage();


    }
    private void ScanImage(){
        try {


            Bitmap bitmap = decodeBitmapUri(this, imageUri);
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                barcodes = detector.detect(frame);
                for (int index = 0; index < barcodes.size(); index++) {
                    Barcode code = barcodes.valueAt(index);
                    // txtResultBody.setText(txtResultBody.getText() + "\n" + code.displayValue + "\n");

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
                if (barcodes.size() != 0 ) {
                    Log.i(TAG, "Code" + content);
                    Log.i(TAG, "type" + typeofcontent);

                    // txtResultBody.setText("No barcode could be detected. Please try again.");
                }
            } else {
                //txtResultBody.setText("Detector initialisation failed");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, e.toString());
        }
    }

    private void initViews() {
        //txtResultBody = findViewById(R.id.txtResultsBody);
        btnScan = findViewById(R.id.btnScan);
        btnClose = findViewById(R.id.btncancle);
        btnScan.setOnClickListener(this);
        btnClose.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnScan:
               /* ActivityCompat.requestPermissions(PictureBarcodeActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);*/
               /* if(hasStoragePermissions()){{
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }}*/
               if(barcodes!=null)
                   if (barcodes.size() != 0 && content != null && typeofcontent != null) {
                       Intent in = new Intent(PictureBarcodeActivity.this, DecodeActivity.class);
                       in.putExtra("Code", content);
                       in.putExtra("type", typeofcontent);
                       startActivity(in);
                   } else {
                       Toast.makeText(getApplicationContext(), "No barcode could be detected.", Toast.LENGTH_SHORT)
                               .show();

                   }
               break;
            case R.id.btncancle:
                  takeBarcodePicture();
             break;

        }

    }
    private final boolean hasStoragePermissions() {
        return EasyPermissions.hasPermissions((Context)this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"});
    }
    public void showInfoMessageDialog(String message)
    {
        new AlertDialog.Builder(this)

   .setMessage(message)
   .setPositiveButton("OK",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which)
        {
            dialog.cancel();
            takeBarcodePicture();
        }
    })
            .show();
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takeBarcodePicture();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(PictureBarcodeActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {

                if (requestCode == 1) {

                    // currImageURI is the global variable I'm using to hold the content:// URI of the image
                    imageUri = data.getData();
                    launchMediaScanIntent();

                    imageView.setImageURI(imageUri);

                    try {


                        Bitmap bitmap = decodeBitmapUri(this, imageUri);
                        if (detector.isOperational() && bitmap != null) {
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                            barcodes = detector.detect(frame);
                            for (int index = 0; index < barcodes.size(); index++) {
                                Barcode code = barcodes.valueAt(index);
                               // txtResultBody.setText(txtResultBody.getText() + "\n" + code.displayValue + "\n");

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
                                        Log.i(TAG, "6" + code.sms.message);
                                        content=code.sms.message +"\n"+ code.sms.phoneNumber;
                                        typeofcontent="SMS";
                                        break;
                                    case Barcode.TEXT:
                                        Log.i(TAG, "7" + code.displayValue);
                                        content=code.displayValue;
                                        typeofcontent="TEXT";
                                        break;
                                    case Barcode.URL:
                                        Log.i(TAG, "url8: " + code.displayValue);
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
                            if (barcodes.size() != 0 ) {
                                Log.i(TAG, "Code" + content);
                                Log.i(TAG, "type" + typeofcontent);

                                // txtResultBody.setText("No barcode could be detected. Please try again.");
                            }
                        } else {
                            //txtResultBody.setText("Detector initialisation failed");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT)
                                .show();
                        Log.e(TAG, e.toString());
                    }
                }
            }




    }

    private void takeBarcodePicture() {
       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
        imageUri = FileProvider.getUriForFile(PictureBarcodeActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);*/
       /* Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);

    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, txtResultBody.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }*/

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
