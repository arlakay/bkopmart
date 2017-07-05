package id.co.octolink.ilm.bkopmart.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.LoginResponse;
import id.co.octolink.ilm.bkopmart.ui.forgotpassword.ForgotPasswordActivity;
import id.co.octolink.ilm.bkopmart.ui.main.MainActivity;
import id.co.octolink.ilm.bkopmart.ui.register.RegisterActivity;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.uid)EditText etUid;
    @BindView(R.id.password)EditText etPass;

    private SessionManager sessionManager;
    private String currentDateTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @OnClick(R.id.txt_lupa_password)
    public void toForgotPassFromLogin(View view) {
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.txt_daftar)
    public void toRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.buttonlogin)
    public void auth(View view) {
        String u = etUid.getText().toString().trim();
        String p = etPass.getText().toString().trim();

        userAuth(u, p);
    }

    private void userAuth(String email, String pass) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Sign In...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<LoginResponse> call = apiService.authentification(email, pass);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse>call, Response<LoginResponse> response) {
                dialog.dismiss();

                if (response.code() == 200 && response.body().getStatus() ) {
                    String cID = response.body().getData().getCustomerId();
                    String cName = response.body().getData().getName();
                    String cEmail = response.body().getData().getEmail();
                    String cPassword = response.body().getData().getPassword();
                    String cBirthdate = response.body().getData().getBirthdate();
                    String cGender = response.body().getData().getGender();
                    String cAvatar = response.body().getData().getAvatar();
                    String cCreateAt = response.body().getData().getCreateAt();
                    String cRecordStatus = response.body().getData().getRecordStatus();

                    sessionManager.setLogin(true);
                    sessionManager.createLoginSession(cID, cName, cEmail, cPassword, cBirthdate,
                            cGender, cAvatar, cCreateAt, cRecordStatus);

                    currentDateTimeString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                    Log.e("JAM LOGOUT", currentDateTimeString);

//                    Bundle params = new Bundle();
//                    params.putString("technician_id", tCode);
//                    params.putString("technician_name", fName + " " + lName);
//                    params.putString("tech_login_time", currentDateTimeString);
//                    mFirebaseAnalytics.logEvent("technician_login", params);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(response.body().getMessages());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

            }
        });
    }

}
