package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.DatabaseAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.ChassisValidationResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.CommonResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.DealerInfo;
import com.aci.yamaha.yamahacustomerarsenal.model.DetailsInfo;
import com.aci.yamaha.yamahacustomerarsenal.model.LocationInfo;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {
    @BindView(R.id.problemEditText)
    EditText problemEditText;
    @BindView(R.id.locationSpinner)
    Spinner locationSpinner;
    @BindView(R.id.serviceCenterSpinner)
    Spinner serviceCenterSpinner;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.chassisEditText)
    EditText chassisEditText;
    @BindView(R.id.bikeDetailTextView)
    TextView bikeDetailTextView;
    /* objects */
    DatabaseAdapter dbHelper;
    ProgressDialog pDialog;
    AppPreferences preferences;
    ArrayList<DealerInfo> dealerArrayList;
    ArrayAdapter<DealerInfo> serviceCenterArrayAdapter;
    ArrayList<LocationInfo> locationInfoArrayList;
    ArrayAdapter<LocationInfo> userLocationArrayAdapter;

    /* global variables */
    String locationId = "";
    String dealerId = "";
    String bikeEngineNo = "";
    String bikeChassisNo = "";
    String bikeModel = "";
    String message = "";
    boolean chassisValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        /*
         * initialize instances
         */
        dbHelper = new DatabaseAdapter(ServiceActivity.this);
        preferences = new AppPreferences(ServiceActivity.this);

        /*
         * add location data to spinner ui
         */
        locationInfoArrayList = new ArrayList<>();
        locationInfoArrayList = dbHelper.getLocations();

        userLocationArrayAdapter = new ArrayAdapter<>(ServiceActivity.this, android.R.layout.simple_spinner_item, locationInfoArrayList);
        userLocationArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        locationSpinner.setAdapter(userLocationArrayAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationInfo locationInfo = (LocationInfo) parent.getSelectedItem();
                locationId = locationInfo.getLocationID();
                /*
                 * add service center data to spinner ui
                 */
                dealerArrayList = new ArrayList<>();
                dealerArrayList = dbHelper.getServiceCenter(locationId);

                serviceCenterArrayAdapter = new ArrayAdapter<>(ServiceActivity.this, android.R.layout.simple_spinner_item, dealerArrayList);
                serviceCenterArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                serviceCenterSpinner.setAdapter(serviceCenterArrayAdapter);

                serviceCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DealerInfo dealerInfo = (DealerInfo) parent.getSelectedItem();
                        dealerId = dealerInfo.getUserID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        chassisEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utility.isConnected(ServiceActivity.this)) {
                    String chassis = editable.toString();
                    checkValidChassis(chassis);
                } else {
                    Utility.snack(ServiceActivity.this, findViewById(android.R.id.content), "Please connect your device to internet");
                }
            }

            private void checkValidChassis(final String chassis) {
                JSONArray dataArray;
                JSONObject child1 = new JSONObject();
                try {
                    child1.put("chassis", chassis);
                    child1.put("userid", preferences.getUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataArray = new JSONArray();
                dataArray.put(child1);

                APIService service = ApiClient.getYamahaClient().create(APIService.class);
                Call<ChassisValidationResponse> chassisCheckCall = service.checkValidChassis(dataArray.toString());
                chassisCheckCall.enqueue(new Callback<ChassisValidationResponse>() {
                    @Override
                    public void onResponse(Call<ChassisValidationResponse> call, Response<ChassisValidationResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getSuccess() == 1) {
                                chassisValid = true;
                                List<DetailsInfo> detailsInfos = response.body().getDetailsInfo();
                                bikeDetailTextView.setText(detailsInfos.get(0).getProductName()
                                        + "Product code : " + detailsInfos.get(0).getProductCode()
                                );
                                bikeEngineNo = detailsInfos.get(0).getEngineNo();
                                bikeChassisNo = detailsInfos.get(0).getChassisNo();
                                bikeModel = detailsInfos.get(0).getProductName();
                            } else {
                                chassisValid = false;
                                bikeDetailTextView.setText("Invalid chassis");
                            }
                        } else {
                            Log.e("Chassis >> ", String.valueOf(chassis));
                        }
                    }

                    @Override
                    public void onFailure(Call<ChassisValidationResponse> call, Throwable t) {
                        chassisValid = false;
                        bikeDetailTextView.setText("Invalid chassis");
                    }
                });
            }
        });
        /*
         * handle service ticket raise
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(ServiceActivity.this)) {
                    message = problemEditText.getText().toString();
                    String error = validate(dealerId, message, chassisValid);
                    if (!TextUtils.isEmpty(error)) {
                        onRaisingServiceTicketFailed(error);
                        return;
                    }
                    raiseServiceTicketToServer();
                } else {
                    Utility.snack(ServiceActivity.this, findViewById(android.R.id.content), "Please connect your device to internet");
                }

            }

            private void raiseServiceTicketToServer() {
                pDialog = new ProgressDialog(ServiceActivity.this);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("please wait...");
                pDialog.setCancelable(false);

                showpDialog();
                /*
                ** convert data to json
                 */
                JSONArray dataArray;
                JSONObject child1 = new JSONObject();
                try {
                    child1.put("locationid", locationId);
                    child1.put("engineno", bikeEngineNo);
                    child1.put("chassisno", bikeChassisNo);
                    child1.put("model", bikeModel);
                    child1.put("dealerid", dealerId);
                    child1.put("message", message);
                    child1.put("userid", preferences.getUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataArray = new JSONArray();
                dataArray.put(child1);
                /*
                 * conversion end
                 */

                APIService service = ApiClient.getYamahaClient().create(APIService.class);
                Call<CommonResponse> serviceTicketRaiseCall = service.serviceTicketRaiseCall(dataArray.toString());
                serviceTicketRaiseCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body() == null) {
                            onRaisingServiceTicketFailed("An error occurred");
                        }
                        if (response.body().getSuccess() == 1) {
                            chassisEditText.setText("");
                            problemEditText.setText("");
                        }
                        Utility.snack(ServiceActivity.this, findViewById(android.R.id.content), response.body().getMessage());
                        hidepDialog();
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        hidepDialog();
                        onRaisingServiceTicketFailed(t.toString());
                    }
                });
            }


        });
    }

    private String validate(String dealerId, String message, boolean chassisValid) {
        String error = "";
        if (chassisValid == false) {
            error += ":: Error: invalid chassis";
        }
        if (TextUtils.isEmpty(dealerId)) {
            error += ":: Please select a service center";
        }
        if (TextUtils.isEmpty(message)) {
            error += ":: Enter problem description";
        }
        return error;
    }

    private void onRaisingServiceTicketFailed(String error) {
        Utility.snack(ServiceActivity.this, findViewById(android.R.id.content), "Error >>" + error);
        sendButton.setEnabled(true);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
