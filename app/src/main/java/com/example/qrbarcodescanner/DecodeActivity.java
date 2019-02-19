package com.example.qrbarcodescanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrbarcodescanner.database.DatabaseHandler;
import com.example.qrbarcodescanner.dto.ScanData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ntf-19 on 20/7/18.
 */

public class DecodeActivity extends AppCompatActivity {
    TextView content, contentType;
    ScanData scanDataArrayList;
    Button search, copy,download;
    String encodeData, encodeDatatype;
    DatabaseHandler databaseHandler;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 201;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decodecode_layout);
        content = findViewById(R.id.content1);
        contentType = findViewById(R.id.type1);
        search = findViewById(R.id.search);
        copy = findViewById(R.id.copy);

        download = findViewById(R.id.download_bt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler(this);
        scanDataArrayList = new ScanData();

        Intent in = getIntent();
        encodeData = in.getStringExtra("Code");

        encodeDatatype = in.getStringExtra("type");

        if (encodeDatatype.equals("TEXT")||encodeDatatype.equals("ISBN")||encodeDatatype.equals("Product")||encodeDatatype.equals("WIFI")||encodeDatatype.equals("GEO")||encodeDatatype.equals("Calendar Event")||encodeDatatype.equals("Driver License")) {
            search.setText("Web Search");
        } else if (encodeDatatype.equals("Link")) {
            search.setText("Open Link");
        } else if (encodeDatatype.equals("Phone")) {
            search.setText("Dial Number");
        }
       else if (encodeDatatype.equals("Contact Information")) {
            search.setText("Add Contact");
        }
       else if (encodeDatatype.equals("SMS")) {
            search.setText("Send SMS");
        }
        else if (encodeDatatype.equals("Email")) {
            search.setText("Send Mail");
        }
        content.setText(encodeData);
        contentType.setText(encodeDatatype);
        Log.d("bharti", "value11" + content + contentType);


        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("bharti", "value12" + date);
        scanDataArrayList.setContent(encodeData);
        scanDataArrayList.setDataFormat(encodeDatatype);
        scanDataArrayList.setDate_time(date);

        databaseHandler.addEncodeData(scanDataArrayList);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (encodeDatatype.equals("TEXT")||encodeDatatype.equals("ISBN")||encodeDatatype.equals("Product")||encodeDatatype.equals("WIFI")||encodeDatatype.equals("GEO")||encodeDatatype.equals("Calendar Event")||encodeDatatype.equals("Driver License")) {
                    String escapedQuery = null;
                    try {
                        escapedQuery = URLEncoder.encode(encodeData, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else if (encodeDatatype.equals("Link")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(encodeData));
                    startActivity(intent);
                }
                else if (encodeDatatype.equals("Phone")) {
                    if(checkPermission(Manifest.permission.CALL_PHONE)) {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + encodeData));

                        startActivity(intent);
                    }else {
                        Toast.makeText(DecodeActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(DecodeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
                    }
                }
                else if(encodeDatatype.equals("Contact Information")){
                    Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    contactIntent
                            .putExtra(ContactsContract.Intents.Insert.NAME, encodeData);

                    startActivity(contactIntent);

                }
                else if(encodeDatatype.equals("SMS")){
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setData(Uri.parse("sms:" + encodeData));
                    smsIntent.putExtra("sms_body", "Hello Msg Tst Txt");
                    startActivity(smsIntent);
                }
                else if(encodeDatatype.equals("Email")){
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",encodeData, null));
                   /* emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");*/
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }

            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipBoard();
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT)
                        .show();

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(DecodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DecodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveData(encodeDatatype+"\n",encodeData);
                  /*  Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT)
                            .show();*/

                } else {
                       Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show();

                    ActivityCompat.requestPermissions(DecodeActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                }



            }
        });



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
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    private void copyToClipBoard()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", encodeData);
        clipboard.setPrimaryClip(clip);
        Log.d("bharti","copy data"+clip.toString());
    }
    private void saveData(String sType, String sBody){

        try {
            File fileDir = getDirectory();
            if (fileDir.exists()) {
                File myExternalFile = new File(getDirectory(), getFileName());
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(sType.getBytes());
                fos.write(sBody.getBytes());
                fos.close();
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT)
                        .show();

            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        return "TXT_" + timeStamp + "_.txt";
    }
    public File getDirectory() {
        String directory = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(directory, "QR SCANNER CODE");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        } else {
            return mFolder;
        }
        return mFolder;
    }
}
