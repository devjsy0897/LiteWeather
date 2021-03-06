package com.jsy.LiteWeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;

import static com.jsy.LiteWeather.GetWeather.weather;


public class MainActivity extends AppCompatActivity {
    AdView adView;
    LinearLayout layout;
    TextView tvreg,tvtemp,tvcondi;
    ImageView ivicon;



    // GpsTracker ↓
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // GpsTracker ↑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        tvreg = findViewById(R.id.tvreg);
        tvtemp = findViewById(R.id.tvtemp);
        tvcondi = findViewById(R.id.tvcondi);
        ivicon = (ImageView)findViewById(R.id.ivicon);

        //광고↓
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //광고↑

        // GpsTracker ↓
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();

        }else {

            checkRunTimePermission();
        }






        gpsTracker = new GpsTracker(MainActivity.this);

        final double latitude = gpsTracker.getLatitude();

        final double longitude = gpsTracker.getLongitude();

        //Toast.makeText(MainActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        // GpsTracker ↑

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                GetWeather getWeather = new GetWeather();
                try {
                    getWeather.weather(latitude,longitude);
                    int temp = getWeather.getTemp();
                    //tvtemp.setText(temp+"°");
                    tvtemp.setText("25°");
                    String region = getWeather.getRegion();
                    tvreg.setText(region);
                    String weather = getWeather.getWeather();
                    //Log.i("temptest1",weather);
                    switch (weather){
                        case "Clear":
                            tvcondi.setText("Sunny");
                            layout.setBackground(getDrawable(R.drawable.clear));
                            break;
                        case "Thunderstorm":
                            tvcondi.setText("Thunderstorm");
                            layout.setBackground(getDrawable(R.drawable.thundersorm));
                            break;
                        case "Drizzle":
                            tvcondi.setText("Drizzle");
                            layout.setBackground(getDrawable(R.drawable.drizzle));
                            break;
                        case "Rain":
                            tvcondi.setText("Rain");
                            layout.setBackground(getDrawable(R.drawable.rain));
                            break;
                        case "Snow":
                            tvcondi.setText("Snow");
                            layout.setBackground(getDrawable(R.drawable.snow));
                            break;
                        case "Clouds":
                            tvcondi.setText("Clouds");
                            layout.setBackground(getDrawable(R.drawable.clouds));
                            break;
                        case "Mist":
                            tvcondi.setText("Mist");
                            layout.setBackground(getDrawable(R.drawable.mist));
                            break;
                        case "Dust":
                            tvcondi.setText("Dust");
                            layout.setBackground(getDrawable(R.drawable.dust));
                            break;
                        case "Haze":
                            tvcondi.setText("Haze");
                            layout.setBackground(getDrawable(R.drawable.haze));
                            break;
                        default:
                            tvcondi.setText(weather);
                            layout.setBackground(getDrawable(R.drawable.snow));
                    }
                    icon();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        t.start();
    }

    public void icon(){
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        String condition = (String)tvcondi.getText();

                        switch (condition){
                            case "Sunny":
                                ivicon.setImageResource(R.drawable.ic_sunny);
                                break;
                            case "Thunderstorm":
                                ivicon.setImageResource(R.drawable.ic_thunder);
                                break;
                            case "Drizzle":
                            case "Rain":
                            case "Mist":
                                ivicon.setImageResource(R.drawable.ic_rain);
                                break;
                            case "Snow":
                                ivicon.setImageResource(R.drawable.ic_snow);
                                break;
                            case "Clouds":
                                ivicon.setImageResource(R.drawable.ic_cloud);
                                break;
                            case "Dust":
                            case "Haze":
                                ivicon.setImageResource(R.drawable.ic_hail);
                                break;
                            default:
                                break;
                        }

                    }
                }
        );
    }

    // Gps ↓
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "위치 권한 허용 후 어플을 다시 시작해 주세요", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        //Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    // Gps ↑



}




