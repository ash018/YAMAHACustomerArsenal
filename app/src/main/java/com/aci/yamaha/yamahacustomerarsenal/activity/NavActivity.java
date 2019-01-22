package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.DatabaseAdapter;
import com.aci.yamaha.yamahacustomerarsenal.adapter.MyAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.LocationPermissionListener;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.ReadStoragePermissionListener;
import com.aci.yamaha.yamahacustomerarsenal.model.UpgradeResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ReadStoragePermissionListener,
        LocationPermissionListener {
    private static final int PERMISSION_ALL = 1;
    ProgressDialog mDialog;
    AppPreferences pref;
    DatabaseAdapter dbHelper;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;

    /* download updated app*/
    String appURI = "";
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    Context context;
    @BindView(R.id.inquiryLL)
    LinearLayout inquiryLL;
    @BindView(R.id.complainLL)
    LinearLayout complainLL;
    @BindView(R.id.serviceLL)
    LinearLayout serviceLL;
    @BindView(R.id.contactsLL)
    LinearLayout contactsLL;


    private String[] versionCode;
    private DownloadManager downloadManager;
    private long downloadReference;
    private GoogleApiClient googleApiClient;
    // broadcast receiver to get notification about ongoing downloads
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {

                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = downloadManager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            //start the installation of the latest version
                            Uri uri = Uri.parse(uriString);
                            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
                            promptInstall.setDataAndType(uri, "application/vnd.android.package-archive");
                            promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(promptInstall);
                        }
                    }
                }
            }
        }
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new AppPreferences(NavActivity.this);
        dbHelper = new DatabaseAdapter(NavActivity.this);
        ButterKnife.bind(this);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            String vc = pInfo.versionName.toString();
            vc = vc.replace(".", "_");
            versionCode = vc.split("_");
        }

        /*
         * check permissions
         */

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            /*
             *all permission granted /do whatever
             */
            listenLocationPermission(true);
            listenReadStoragePermission(true);
        }
        /*
        **prompt user to open gps if not enabled
         */
        if (!pref.isBasicInfoSubmitted()) {
            if (!((LocationManager) NavActivity.this.getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
        }

        /*
        **end
         */

        mDialog = new ProgressDialog(NavActivity.this);
        mDialog.setMessage("Please wait...");

        serviceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavActivity.this, ServiceStatusActivity.class));
            }
        });
        inquiryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavActivity.this, InquiryActivity.class));
            }
        });
        complainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavActivity.this, ComplainActivity.class));
            }
        });
        contactsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavActivity.this, ContactActivity.class));
            }
        });
        /* recycler view starts*/
        String TITLES[] = {"Inquiries", "Complains", "Upgrade", "Log out", "About"};
        int ICONS[] = {android.R.drawable.ic_dialog_email, android.R.drawable.ic_dialog_email,
                R.drawable.ic_action_logout, R.drawable.ic_action_upgrade,
                android.R.drawable.ic_dialog_alert};
        String NAME = getResources().getString(R.string.aci);
        String EMAIL = getResources().getString(R.string.slogan);
        int PROFILE = R.drawable.logo;

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        Drawer.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
        final GestureDetector mGestureDetector = new GestureDetector(NavActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();

                    int childpos = recyclerView.getChildAdapterPosition(child);
                    switch (childpos) {
                        case 1:
                            startActivity(new Intent(NavActivity.this, InquiryViewActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(NavActivity.this, ComplainViewActivity.class));
                            break;
                        case 3:
                            upgrade();
                            break;
                        case 4:
                            AlertDialog.Builder alert = new AlertDialog.Builder(NavActivity.this);
                            alert.setTitle("Alert");
                            alert.setMessage("Are you sure you want to log out?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    pref.setLoggedIn(false);
                                    dbHelper.deleteTables();
                                    finish();
                                    Intent intent = new Intent(NavActivity.this, YCAApp.class);
                                    startActivity(intent);

                                }
                            });

                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alert.show();
                            break;
                        case 5:
                            about();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public void upgrade() {
        // Broadcast receiver for the download manager
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
        /////

        if (new Utility().isConnected(getApplicationContext())) {
            getAppDowloadUri();
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavActivity.this);
            alertDialog.setTitle("Upgrade");
            alertDialog
                    .setMessage("Please enable mobile data/wifi connectivity.");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

        }

    }

    private void getAppDowloadUri() {
        APIService service = ApiClient.getYamahaClient().create(APIService.class);
        Call<UpgradeResponse> upgradeCall = service.getAppDownloadUri();
        upgradeCall.enqueue(new Callback<UpgradeResponse>() {
            @Override
            public void onResponse(Call<UpgradeResponse> call, Response<UpgradeResponse> response) {
                String lv = response.body().getLatestVersion().replace(".", "_");
                String[] latestVersion = lv.split("_");
                final String appURI = response.body().getAppURI();
                boolean isNew = false;
                if (Integer.parseInt(latestVersion[2]) > Integer
                        .parseInt(versionCode[2])) {
                    isNew = true;
                } else if (Integer.parseInt(latestVersion[1]) > Integer
                        .parseInt(versionCode[1])) {
                    isNew = true;
                } else if (Integer.parseInt(latestVersion[0]) > Integer
                        .parseInt(versionCode[0])) {
                    isNew = true;
                }

                if (isNew) {
                    // oh yeah we do need an upgrade, let the user know send
                    // an alert message
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            NavActivity.this);
                    builder.setMessage(
                            "There is newer version of this application available, click OK to upgrade now?")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        // if the user agrees to upgrade
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {
                                            // start downloading the file
                                            // using the download manager
                                            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                            Uri Download_Uri = Uri
                                                    .parse(appURI);
                                            DownloadManager.Request request = new DownloadManager.Request(
                                                    Download_Uri);
                                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                                    | DownloadManager.Request.NETWORK_MOBILE);
                                            request.setAllowedOverRoaming(false);
                                            request.setTitle("Yamaha customer arsenal app download");
                                            request.setDestinationInExternalFilesDir(
                                                    NavActivity.this,
                                                    Environment.DIRECTORY_DOWNLOADS,
                                                    "yca.apk");
                                            downloadReference = downloadManager
                                                    .enqueue(request);

                                        }
                                    })
                            .setNegativeButton("Remind Later",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {
                                            dialog.dismiss();
                                        }
                                    });
                    // show the alert message
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<UpgradeResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void about() {
        TextView message = new TextView(NavActivity.this);
        message.setText(getResources().getString(R.string.app_name) + "\nPowered by MIS, ACI");
        message.setPadding(50, 20, 20, 20);

        new AlertDialog.Builder(NavActivity.this)
                .setTitle("About")
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", null)
                .setView(message)
                .create().show();

    }

    private void showProgress(boolean show) {
        if (show) {
            mDialog.show();
        } else {
            mDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listenLocationPermission(true);
                } else if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    listenReadStoragePermission(true);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Message")
                            .setMessage("Location Services  and Device State Permission required for this app")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(NavActivity.this, PERMISSIONS, PERMISSION_ALL);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(NavActivity.class.getSimpleName(), "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                String address = getAddress(lat, lon);
                pref.setLatitude(String.valueOf(lat));
                pref.setLongitude(String.valueOf(lon));
                pref.setAddress(address);
                Log.e("Address", address + "\n");
            }

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            listenReadStoragePermission(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(NavActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(NavActivity.this, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            Log.e("IGA", "Address" + add + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }

    @Override
    public void listenReadStoragePermission(boolean granted) {
        if (granted) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String number = telephonyManager.getLine1Number();
            pref.setImei(telephonyManager.getDeviceId());
            pref.setMobileNo(number);
        }
    }

    @Override
    public void listenLocationPermission(boolean granted) {
        if (granted) {
            googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NavActivity.this);
        alertDialogBuilder
                .setMessage(
                        "GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

