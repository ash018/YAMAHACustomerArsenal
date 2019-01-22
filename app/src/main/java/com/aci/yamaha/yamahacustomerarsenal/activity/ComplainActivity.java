package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.CommonResponse;
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

public class ComplainActivity extends AppCompatActivity {
    @BindView(R.id.issueSpinner)
    Spinner issueSpinner;
    @BindView(R.id.complainEditText)
    EditText complainEditText;
    @BindView(R.id.submitButton)
    Button submitButton;
    ProgressDialog pDialog;
    AppPreferences preferences;
    ArrayList<String> complainIssues;
    ArrayAdapter<String> complainIssueAdapter;
    String complainString = "";
    String complainIssueString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(ComplainActivity.this);
        preferences = new AppPreferences(ComplainActivity.this);
        /*
        **initialization end
         */
        complainIssues = new ArrayList<>();
        complainIssues.add("SELECT...");
        complainIssues.add("DEALER / SHOWROOM");
        complainIssues.add("SERVICE");
        complainIssues.add("PURCHASE");
        complainIssues.add("OTHERS");

        complainIssueAdapter = new ArrayAdapter<>(ComplainActivity.this, android.R.layout.simple_spinner_item, complainIssues);
        complainIssueAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        issueSpinner.setAdapter(complainIssueAdapter);

        issueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complainIssueString = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                complainString = complainEditText.getText().toString();

                String error = "";
                if (TextUtils.isEmpty(complainString)) {
                    error += "Please write your complain. :: ";
                }
                if (complainIssueString.equalsIgnoreCase("SELECT...")) {
                    error += "Please select a complain issue";
                }
                if (!TextUtils.isEmpty(error)) {
                    onValidationFailed(error);
                    return;
                }
                sendComplainToServer();
            }

            private void sendComplainToServer() {
                pDialog = new ProgressDialog(ComplainActivity.this);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Sending complain...");
                pDialog.setCancelable(false);

                showpDialog();
                /*
                ** convert data to json
                 */
                JSONArray dataArray;
                JSONObject child1 = new JSONObject();
                try {
                    child1.put("category", complainIssueString);
                    child1.put("message", complainString);
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
                Call<CommonResponse> complainCall = service.userComplain(dataArray.toString());
                complainCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        hidepDialog();
                        if (response.body() != null) {
                            if (response.body().getSuccess() == 1) {
                                complainEditText.setText("");
                                issueSpinner.setSelection(0);
                            }
                            Utility.snack(ComplainActivity.this, findViewById(android.R.id.content), response.body().getMessage());
                        } else {
                            Utility.snack(ComplainActivity.this, findViewById(android.R.id.content), "An error occurred");
                        }
                        submitButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        hidepDialog();
                        Utility.snack(ComplainActivity.this, findViewById(android.R.id.content), t.getMessage());
                        submitButton.setEnabled(true);
                    }
                });
            }


        });
    }

    private void onValidationFailed(String error) {
        Utility.snack(ComplainActivity.this, findViewById(android.R.id.content), "Couldn't send complain >>" + error);
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
