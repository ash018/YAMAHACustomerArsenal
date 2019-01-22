package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.CommonResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryActivity extends AppCompatActivity {

    @BindView(R.id.inquirySubjectEditText)
    EditText inquirySubjectEditText;
    @BindView(R.id.inquiryEditText)
    EditText inquiryEditText;
    @BindView(R.id.submitButton)
    Button submitButton;
    ProgressDialog pDialog;

    String inquiryString = "";
    String inquirySubjectString = "";
    AppPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(InquiryActivity.this);
        preferences = new AppPreferences(InquiryActivity.this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                inquirySubjectString = inquirySubjectEditText.getText().toString();
                inquiryString = inquiryEditText.getText().toString();

                String error = "";
                if (TextUtils.isEmpty(inquiryString)) {
                    error += "Enter your inquiry";
                }
                if (TextUtils.isEmpty(inquirySubjectString)) {
                    error += " | Enter inquiry subject";
                }

                if (!TextUtils.isEmpty(error)) {
                    onValidationFailed(error);
                    return;
                }
                sendInquiryToServer();
            }

            private void sendInquiryToServer() {
                pDialog = new ProgressDialog(InquiryActivity.this);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Sending inquiry...");
                pDialog.setCancelable(false);

                showpDialog();
                /*
                ** convert data to json
                 */
                JSONArray dataArray;
                JSONObject child1 = new JSONObject();
                try {
                    child1.put("title", inquirySubjectString);
                    child1.put("message", inquiryString);
                    child1.put("userid", preferences.getUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataArray = new JSONArray();
                dataArray.put(child1);
                /*
                ** conversion end
                 */

                APIService service = ApiClient.getYamahaClient().create(APIService.class);
                Call<CommonResponse> inquiryCall = service.userInquiry(dataArray.toString());
                inquiryCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        hidepDialog();
                        if (response.body() != null) {
                            if (response.body().getSuccess() == 1) {
                                inquirySubjectEditText.setText("");
                                inquiryEditText.setText("");
                            }
                            Utility.snack(InquiryActivity.this, findViewById(android.R.id.content), response.body().getMessage());
                        } else {
                            Utility.snack(InquiryActivity.this, findViewById(android.R.id.content), "An error occurred");
                        }
                        submitButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        hidepDialog();
                        Utility.snack(InquiryActivity.this, findViewById(android.R.id.content), t.getMessage());
                        submitButton.setEnabled(true);
                    }
                });
            }


        });
    }

    private void onValidationFailed(String error) {
        Utility.snack(InquiryActivity.this, findViewById(android.R.id.content), "Couldn't send inquiry >>" + error);
        submitButton.setEnabled(true);
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
