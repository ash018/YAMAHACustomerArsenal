package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.adapter.DatabaseAdapter;
import com.aci.yamaha.yamahacustomerarsenal.interfaces.APIService;
import com.aci.yamaha.yamahacustomerarsenal.model.LoginResponse;
import com.aci.yamaha.yamahacustomerarsenal.service.ApiClient;
import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;
import com.aci.yamaha.yamahacustomerarsenal.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.userIdEditText)
    EditText userIdEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.registerButton)
    Button registerButton;
    AppPreferences pref;
    DatabaseAdapter dbHelper;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pref = new AppPreferences(this);
        dbHelper = new DatabaseAdapter(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("contact"))) {
            userIdEditText.setText(getIntent().getStringExtra("contact"));
            passwordEditText.setText(getIntent().getStringExtra("password"));
            if (Utility.isConnected(LoginActivity.this)) {
                login();
            }
        }
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utility.isConnected(LoginActivity.this)) {
                    login();
                } else {
                    Utility.snack(LoginActivity.this, findViewById(android.R.id.content), "Please connect your device to internet");
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void login() {
        loginButton.setEnabled(false);
        if (validate() == false) {
            loginButton.setEnabled(true);
            onLoginFailed("Login failed");
            return;
        }
        loginByServer(userIdEditText.getText().toString(), passwordEditText.getText().toString());
    }

    public void loginByServer(String userId, String password) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        showpDialog();


        APIService service = ApiClient.getYamahaClient().create(APIService.class);
        Call<LoginResponse> userCall = service.userLogIn(userId, password);

        userCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hidepDialog();

                if (response.body().getSuccess() == 1) {
                    pref.setLoggedIn(true);
                    pref.setUserId(response.body().getUserid());
                    pref.setUserTypeId(response.body().getUsertypeid());
                    pref.setContactNo(response.body().getContactno());
                    pref.setUsertName(response.body().getUsername());
                    pref.setLocationId(response.body().getLocationid());
                    dbHelper.ftLocationData(response.body().getLocationInfo());
                    dbHelper.ftDealerInfoData(response.body().getDealerInfo());
                    startActivity(new Intent(LoginActivity.this, NavActivity.class));
                    finish();
                } else {
                    Utility.snack(LoginActivity.this, findViewById(android.R.id.content), "" + response.body().getMessage());
                }
                loginButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hidepDialog();
                onLoginFailed(t.toString());
                loginButton.setEnabled(true);
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

    public void onLoginFailed(String msg) {
        Utility.snack(LoginActivity.this, findViewById(android.R.id.content), msg);
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = userIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            userIdEditText.setError("Enter a valid username");
            requestFocus(userIdEditText);
            valid = false;
        } else {
            userIdEditText.setError(null);
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is empty");
            requestFocus(passwordEditText);
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
