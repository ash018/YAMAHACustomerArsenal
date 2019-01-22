package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.DisplayServiceAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.Service;
import com.aci.yamaha.yamahacustomerarsenal.model.ServiceResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DispalyServiceActivity extends AppCompatActivity {
    @BindView(R.id.servicesListView)
    ListView servicesListView;
    AppPreferences pref;
    DisplayServiceAdapter displayServiceAdapter;
    boolean itemAvailable = true;
    int from = 0;
    int serviceStatus = 0;
    ArrayList<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pref = new AppPreferences(DispalyServiceActivity.this);
        serviceStatus = getIntent().getIntExtra("SERVICE_STATUS", 0);
        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.footer, null);
        TextView footer = footerLayout.findViewById(R.id.footer);
        servicesListView.addFooterView(footerLayout);

        serviceList = new ArrayList<>();
        displayServiceAdapter = new DisplayServiceAdapter(DispalyServiceActivity.this, footer, serviceList, serviceStatus);
        servicesListView.setAdapter(displayServiceAdapter);
        loadData();
    }

    private void loadData() {
        if (itemAvailable == false) {
            return;
        }
        if (!Utility.isConnected(DispalyServiceActivity.this)) {
            Utility.snack(DispalyServiceActivity.this, findViewById(android.R.id.content), "No internet connection");
            return;
        }
        JSONArray dataArray;
        JSONObject child1 = new JSONObject();
        try {
            child1.put("userid", pref.getUserId());
            child1.put("status", serviceStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataArray = new JSONArray();
        dataArray.put(child1);
       /*
        * conversion end
        */

        APIService service = ApiClient.getYamahaClient().create(APIService.class);
        Call<ServiceResponse> serviceCall = service.getServices(dataArray.toString());
        serviceCall.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                serviceList.addAll(response.body().getServices());
                displayServiceAdapter.notifyDataSetChanged();
                displayServiceAdapter.notifyNoMoreItems();
                /*from += response.body().getServices().size();
                if (response.body().getServices().size() == 0) {
                    itemAvailable = false;
                    displayServiceAdapter.notifyNoMoreItems();
                }
                loadData();*/
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {

            }
        });

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
        }
        return super.onOptionsItemSelected(item);
    }

}
