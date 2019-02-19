package com.example.qrbarcodescanner.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrbarcodescanner.PictureBarcodeActivity;
import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.ScannedBarcodeActivity;
import com.example.qrbarcodescanner.util.AppConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.FileNotFoundException;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ntf-19 on 21/7/18.
 */

public class ScanFragment extends Fragment implements View.OnClickListener{
    Button btnTakePicture, btnScanBarcode;
    private BarcodeDetector detector;
    private Uri imageUri;
    String content,typeofcontent;
    SparseArray<Barcode> barcodes;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final String TAG = "API123";

    InterstitialAd mInterstitialAd;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        btnTakePicture = view.findViewById(R.id.btnTakePicture);
        btnScanBarcode = view.findViewById(R.id.btnScanBarcode);
        btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTakePicture:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    takeBarcodePicture();

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                }
                break;
            case R.id.btnScanBarcode:
                if(AppConstants.checkInternetConnection(getContext())){
                    showFullScreenAd();
                    }else{
                    startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));

                }

                break;
        }


    }
    private final boolean hasStoragePermissions() {
        return EasyPermissions.hasPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"});
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                imageUri = data.getData();
                if(imageUri!=null) {
                    Intent in = new Intent(getActivity(), PictureBarcodeActivity.class);
                    in.putExtra("Image", imageUri);
                    startActivity(in);
                }

              //  launchMediaScanIntent();

                //imageView.setImageURI(imageUri);

               /* try {


                    Bitmap bitmap = decodeBitmapUri(getActivity(), imageUri);
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
                                    typeofcontent="CONTACT_INFO";
                                    break;
                                case Barcode.EMAIL:
                                    Log.i(TAG, "2" + code.displayValue);
                                    content=code.displayValue;
                                    typeofcontent="EMAIL";
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
                                    typeofcontent="PRODUCT";
                                    break;
                                case Barcode.SMS:
                                    Log.i(TAG, "6" + code.sms.message);
                                    content=code.sms.message;
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
                                    content=code.wifi.ssid;
                                    typeofcontent="WIFI";
                                    break;
                                case Barcode.GEO:
                                    Log.i(TAG, "10" + code.geoPoint.lat + ":" + code.geoPoint.lng);
                                    content=code.geoPoint.lat+" " +code.geoPoint.lng ;
                                    typeofcontent="GEO";
                                    break;
                                case Barcode.CALENDAR_EVENT:
                                    Log.i(TAG, "11" + code.calendarEvent.description);
                                    content=code.calendarEvent.description;
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
                    Toast.makeText(getActivity(), "Failed to load Image", Toast.LENGTH_SHORT)
                            .show();
                    Log.e(TAG, e.toString());
                }*/
            }
        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takeBarcodePicture();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                }
        }
    }
    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        getActivity().sendBroadcast(mediaScanIntent);
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
    public void showFullScreenAd(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Log.e("FullAd ","  is Failed"+111);
                }
            });
        }else{
            startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
        }
    }
    @Override
    public void onResume() {
        loadFullAd();
        super.onResume();
    }

    public void loadFullAd(){
        mInterstitialAd = new InterstitialAd(getContext());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }
}
