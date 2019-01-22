package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.ComplainListAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.Complain;
import com.aci.yamaha.yamahacustomerarsenal.model.UserComplainResponse;
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

public class ComplainViewActivity extends AppCompatActivity {
    @BindView(R.id.inquiryRecyclerView)
    RecyclerView inquiryRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView emptyTextView;
    AppPreferences pref;
    ProgressDialog pDialog;
    private ComplainListAdapter mAdapter;
    private ArrayList<Complain> complainArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pref = new AppPreferences(ComplainViewActivity.this);

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        mAdapter = new ComplainListAdapter(complainArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        inquiryRecyclerView.setLayoutManager(mLayoutManager);
        inquiryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inquiryRecyclerView.setAdapter(mAdapter);
        prepareMessagesData();
    }

    private void prepareMessagesData() {
        if (!Utility.isConnected(ComplainViewActivity.this)) {
            Utility.snack(ComplainViewActivity.this, findViewById(android.R.id.content), "No internet connection");
            return;
        }
        pDialog = new ProgressDialog(ComplainViewActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);

        showpDialog();
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
        Call<UserComplainResponse> inquiryCall = service.getUserComplain(dataArray.toString());
        inquiryCall.enqueue(new Callback<UserComplainResponse>() {
            @Override
            public void onResponse(Call<UserComplainResponse> call, Response<UserComplainResponse> response) {
                hidepDialog();
                if ((response.body() != null) && (response.body().getSuccess() == 1)) {
                    complainArrayList.addAll(response.body().getComplain());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utility.snack(ComplainViewActivity.this, findViewById(android.R.id.content), "An error occurred");
                }

            }

            @Override
            public void onFailure(Call<UserComplainResponse> call, Throwable t) {
                Utility.snack(ComplainViewActivity.this, findViewById(android.R.id.content), "An error occurred");
            }
        });
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
