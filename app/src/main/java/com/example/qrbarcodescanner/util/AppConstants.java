package com.example.qrbarcodescanner.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by ntf-19 on 25/1/18.
 */

public class AppConstants {

    private static final String SpreadSheetId_more_apps = "1le4TIOy_yVBACLu4U4jd8Bncknjmi9tyJWDJ-rUtY9A";
    private static final String category_moreapps = "Sheet1";
    public static final String fullURL_moreapps = "https://sheets.googleapis.com/v4/spreadsheets/" + SpreadSheetId_more_apps + "/values/" + category_moreapps + "!A2:C?majorDimension=ROWS&fields=values&key=AIzaSyAU0e9_jAboXOmysKfCaRqhdTMvpPNWlGs";

    private static final String SpreadSheetId_coupons = "1xTrtyt-kDZm0uawylznQS7hGdDVdvWsrt44N_9STQhM";
    private static final String category_coupons = "Sheet1";
    public static final String fullURL_coupons = "https://sheets.googleapis.com/v4/spreadsheets/" + SpreadSheetId_coupons + "/values/" + category_coupons + "!A2:C?majorDimension=ROWS&fields=values&key=AIzaSyAU0e9_jAboXOmysKfCaRqhdTMvpPNWlGs";

    private static final String rating_SheetId = "163aZYgbHq3qhXU_hMtWDuZ7jtriQGgUJd-WtpwsJu-M";
    private static final String rating_category = "Sheet1";
    public static final String rating_url="https://sheets.googleapis.com/v4/spreadsheets/" + rating_SheetId + "/values/" + rating_category + "!A2:C?majorDimension=ROWS&fields=values&key=AIzaSyAU0e9_jAboXOmysKfCaRqhdTMvpPNWlGs";

    public static boolean bool_more_app_for_resume;

    public static String app_id_link="https://play.google.com/store/apps/details?id=com.universaltech.couponswalmart";
    public static String developer_id_link="https://play.google.com/store/apps/developer?id=";

    public static void codeForBannerAd(final Context mContext, AdView mAdView){
        AdRequest adRequest = new AdRequest.Builder()
               /* .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(mContext.getResources().getString(
                        R.string.test_device_id
                ))*/
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                // Toast.makeText(mContext, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //  Toast.makeText(mContext, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Toast.makeText(mContext, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            System.out.println("Internet Connection Not Present");
            return false;
        }
    }
}
