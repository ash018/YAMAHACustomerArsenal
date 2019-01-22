package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.ServiceStatResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceStatusActivity extends AppCompatActivity {

    Boolean mShowUnit = true;
    String TAG = "80";

    @BindView(R.id.newCircleProgressView)
    ImageView newCircleProgressView;
    @BindView(R.id.pendingCircleProgressView)
    CircleProgressView pendingCircleProgressView;
    @BindView(R.id.doneCircleProgressView)
    CircleProgressView doneCircleProgressView;
    @BindView(R.id.totalTextView)
    TextView totalTextView;

    /* Variables   */
    AppPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pref = new AppPreferences(ServiceStatusActivity.this);
        if (Utility.isConnected(ServiceStatusActivity.this)) {
            refreshStats();
        } else {
            setStatToView();
        }

        newCircleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceStatusActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });
        pendingCircleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceStatusActivity.this, DispalyServiceActivity.class);
                intent.putExtra("SERVICE_STATUS", 1);//1 stands for pending services
                startActivity(intent);
            }
        });
        doneCircleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceStatusActivity.this, DispalyServiceActivity.class);
                intent.putExtra("SERVICE_STATUS", 2);//2 stands for done services
                startActivity(intent);
            }
        });
        totalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceStatusActivity.this, DispalyServiceActivity.class);
                intent.putExtra("SERVICE_STATUS", 0);//0 stands for all services
                startActivity(intent);
            }
        });

    }

    private void setStatToView() {
        setDataToViews(pendingCircleProgressView, pref.getPendingServiceCount());
        setDataToViews(doneCircleProgressView, pref.getReceivedServiceCount());
        totalTextView.setText(String.valueOf(pref.getTotalServiceCount()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (Utility.isConnected(ServiceStatusActivity.this)) {
                refreshStats();
            } else {
                Utility.snack(ServiceStatusActivity.this, findViewById(android.R.id.content), "Please connect to internet");
            }
            return true;
        } else if (id == R.id.action_close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshStats() {
        JSONArray dataArray;
        JSONObject child1 = new JSONObject();
        try {
            child1.put("userid", pref.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataArray = new JSONArray();
        dataArray.put(child1);

        APIService service = ApiClient.getYamahaClient().create(APIService.class);
        Call<ServiceStatResponse> serviceStatCall = service.serviceStatus(dataArray.toString());
        serviceStatCall.enqueue(new Callback<ServiceStatResponse>() {
            @Override
            public void onResponse(Call<ServiceStatResponse> call, Response<ServiceStatResponse> response) {
                if ((response.body() != null) && (response.body().getSuccess() == 1)) {
                    pref.setPendingServiceCount(response.body().getNew());
                    pref.setReceivedServiceCount(response.body().getDone());
                    pref.setTotalServiceCount(response.body().getTotal());
                }
                setStatToView();
            }

            @Override
            public void onFailure(Call<ServiceStatResponse> call, Throwable t) {
                setStatToView();
            }
        });
    }

    private void setDataToViews(CircleProgressView circleProgressView, int value) {
        circleProgressView.setBarColor(ContextCompat.getColor(ServiceStatusActivity.this, R.color.lLight),
                ContextCompat.getColor(ServiceStatusActivity.this, R.color.dark));
        //circleProgressView.setBlockCount(100);
        //circleProgressView.setFillCircleColor(ContextCompat.getColor(ServiceStatusActivity.this,R.color.colorPrimary));
        circleProgressView.setRimColor(ContextCompat.getColor(ServiceStatusActivity.this, R.color.colorAsh));
        //growing/rotating counter-clockwise
        circleProgressView.setDirection(Direction.CCW);
        circleProgressView.setUnit("%");
        circleProgressView.setMaxValue(pref.getTotalServiceCount());
        circleProgressView.setValue(0);
        circleProgressView.setValueAnimated(value);
        //show unit

        circleProgressView.setUnitVisible(mShowUnit);

        //text sizes
        circleProgressView.setTextSize(50); // text size set, auto text size off
        circleProgressView.setUnitSize(40); // if i set the text size i also have to set the unit size
        //circleProgressView.setAutoTextSize(true); // enable auto text size, previous values are overwritten
        //if you want the calculated text sizes to be bigger/smaller you can do so via
        circleProgressView.setUnitScale(0.9f);
        circleProgressView.setTextScale(0.9f);
    }
}
