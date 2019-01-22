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
import com.aci.yamaha.yamahacustomerarsenal.adapter.InquiryListAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.Inquiry;
import com.aci.yamaha.yamahacustomerarsenal.model.UserInquiryResponse;
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

public class InquiryViewActivity extends AppCompatActivity {
    @BindView(R.id.inquiryRecyclerView)
    RecyclerView inquiryRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView emptyTextView;
    AppPreferences pref;
    ProgressDialog pDialog;
    private InquiryListAdapter mAdapter;
    private ArrayList<Inquiry> inquiryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pref = new AppPreferences(InquiryViewActivity.this);

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        mAdapter = new InquiryListAdapter(inquiryArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        inquiryRecyclerView.setLayoutManager(mLayoutManager);
        inquiryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inquiryRecyclerView.setAdapter(mAdapter);
        prepareMessagesData();
    }

    private void prepareMessagesData() {
        if (!Utility.isConnected(InquiryViewActivity.this)) {
            Utility.snack(InquiryViewActivity.this, findViewById(android.R.id.content), "No internet connection");
            return;
        }
        pDialog = new ProgressDialog(InquiryViewActivity.this);
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
        Call<UserInquiryResponse> inquiryCall = service.getUserInquiry(dataArray.toString());
        inquiryCall.enqueue(new Callback<UserInquiryResponse>() {
            @Override
            public void onResponse(Call<UserInquiryResponse> call, Response<UserInquiryResponse> response) {
                hidepDialog();
                if ((response.body() != null) && (response.body().getSuccess() == 1)) {
                    inquiryArrayList.addAll(response.body().getInquiry());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utility.snack(InquiryViewActivity.this, findViewById(android.R.id.content), "An error occurred");
                }

            }

            @Override
            public void onFailure(Call<UserInquiryResponse> call, Throwable t) {
                Utility.snack(InquiryViewActivity.this, findViewById(android.R.id.content), "An error occurred");
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
