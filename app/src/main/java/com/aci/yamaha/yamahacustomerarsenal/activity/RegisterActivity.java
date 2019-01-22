package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.DatabaseAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.LocationPermissionListener;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.ReadStoragePermissionListener;
import com.aci.yamaha.yamahacustomerarsenal.model.LocationInfo;
import com.aci.yamaha.yamahacustomerarsenal.model.LocationResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.RegistrationResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ReadStoragePermissionListener,
        LocationPermissionListener {

    private static final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    /* Views */
    @BindView(R.id.customerNameEditText)
    EditText customerNameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.customerMobileEditText)
    EditText customerMobileEditText;
    @BindView(R.id.customerAddressEditText)
    EditText customerAddressEditText;
    @BindView(R.id.registerButton)
    Button registerButton;
    @BindView(R.id.syncLocationImageButton)
    ImageButton syncLocationImageButton;

    @BindView(R.id.locationSpinnner)
    Spinner locationSpinnner;
    ArrayAdapter<LocationInfo> locationAdapter;
    ArrayList<LocationInfo> locationsArrayList;

    DatabaseAdapter dbHelper;
    Context context;
    ProgressDialog pDialog;
    /* variables */
    String customerName;
    String customerMobileNo;
    String customerAddress;
    String locationId = "";
    String password = "";
    /* Instances */
    private GoogleApiClient googleApiClient;
    private AppPreferences preferences;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
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
        setContentView(R.layout.activity_registration);
        context = RegisterActivity.this;
        preferences = new AppPreferences(this);
        dbHelper = new DatabaseAdapter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
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
        /*sync location*/
        boolean locationDataSynced = preferences.isLocationDataSynced();
        if (locationDataSynced) {
            syncLocationImageButton.setVisibility(View.GONE);
        }
        syncLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(RegisterActivity.this)) {
                    getLocationData();
                } else {
                    Utility.snack(context, findViewById(android.R.id.content), "Please connect your device to internet");
                }
            }
        });


        /*
        **start registration specific task
         */
        locationsArrayList = dbHelper.getLocations();
        locationAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, locationsArrayList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        locationSpinnner.setAdapter(locationAdapter);

        locationSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationInfo locationInfo = (LocationInfo) parent.getSelectedItem();
                if (TextUtils.isEmpty(locationInfo.getLocationID())) {
                    locationId = "";
                } else {
                    locationId = locationInfo.getLocationID();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(RegisterActivity.this)) {
                    customerName = customerNameEditText.getText().toString();
                    customerMobileNo = customerMobileEditText.getText().toString();
                    customerAddress = customerAddressEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    String error = validate(customerName, customerMobileNo, customerAddress, locationId, password);
                    if (!TextUtils.isEmpty(error)) {
                        onRegistrationFailed(error);
                        return;
                    }
                    registerByServer();
                } else {
                    Utility.snack(context, findViewById(android.R.id.content), "Please connect your device to internet");
                }

            }

            private void registerByServer() {
                pDialog = new ProgressDialog(context);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Creating Account...");
                pDialog.setCancelable(false);

                showpDialog();
                /*
                ** convert data to json
                 */
                JSONArray dataArray;
                JSONObject child1 = new JSONObject();
                try {
                    child1.put("customername", customerName);
                    child1.put("customermobileno", customerMobileNo);
                    child1.put("customeraddress", customerAddress);
                    child1.put("locationid", locationId);
                    child1.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataArray = new JSONArray();
                dataArray.put(child1);
                /*
                 * conversion end
                 */

                APIService service = ApiClient.getYamahaClient().create(APIService.class);
                Call<RegistrationResponse> registerCall = service.userRegistration(dataArray.toString());
                registerCall.enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        preferences.setUserId(response.body().getUserid());
                        //preferences.setLoggedIn(true);
                        hidepDialog();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("contact", customerMobileNo);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        hidepDialog();
                        onRegistrationFailed(t.toString());
                    }
                });
            }

            private String validate(String customerName, String customerMobileNo, String customerAddress,
                                    String locationId, String password) {
                String error = "";
                if (TextUtils.isEmpty(customerName)) {
                    error += "Enter your name";
                }
                if (TextUtils.isEmpty(customerMobileNo)) {
                    error += "::Enter your mobile number";
                }
                if (TextUtils.isEmpty(customerAddress)) {
                    error += "::Enter your address";
                }
                if (!Utility.isValidMobile(customerMobileNo)) {
                    error += "::Enter valid mobile number";
                }
                if (TextUtils.isEmpty(password) || password.length() < 4) {
                    error += "::Enter valid password";
                }
                if (TextUtils.isEmpty(locationId)) {
                    error += "::Select a location";
                }

                return error;
            }
        });
    }

    private void onRegistrationFailed(String error) {
        Utility.snack(context, findViewById(android.R.id.content), "Registration failed >>" + error);
        registerButton.setEnabled(true);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
                            .setMessage("LocationInfo Services  and Device State Permission required for this app")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, PERMISSION_ALL);
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
        Log.i(RegisterActivity.class.getSimpleName(), "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {

                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                String address = getAddress(lat, lon);
                customerAddressEditText.setText(address);
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
        Log.i(RegisterActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            /*add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            Log.e("IGA", "Address" + add);
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            preferences.setImei(telephonyManager.getDeviceId());
            Log.e("IMEI", preferences.getImei() + "\n");
        }
    }

    @Override
    public void listenLocationPermission(boolean granted) {
        if (granted) {
            googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }

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

    public void getLocationData() {
        APIService service = ApiClient.getYamahaClient().create(APIService.class);
        Call<LocationResponse> locationCall = service.fetchLocations("");
        locationCall.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.body().getSuccess() == 1) {
                    preferences.setLocationDataSynced(true);
                    dbHelper.ftLocationData(response.body().getLocationInfo());
                    locationsArrayList = dbHelper.getLocations();
                    locationAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, locationsArrayList);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    locationSpinnner.setAdapter(locationAdapter);
                } else {
                    Utility.snack(RegisterActivity.this, findViewById(android.R.id.content), "" + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {

            }
        });
    }
}
