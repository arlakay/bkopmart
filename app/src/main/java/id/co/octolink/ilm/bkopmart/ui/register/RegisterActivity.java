package id.co.octolink.ilm.bkopmart.ui.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.RegisterResponse;
import id.co.octolink.ilm.bkopmart.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.octolink.ilm.bkopmart.R.id.radioGroupJK;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_email)EditText etEmail;
    @BindView(R.id.et_password)EditText etPassword;
    @BindView(R.id.et_name)EditText etName;
    @BindView(R.id.et_birthdate)EditText etBirthdate;
    @BindView(radioGroupJK)RadioGroup rgJK;
    @BindView(R.id.radioJKFemale)RadioButton rbFemale;
    @BindView(R.id.radioJKMale)RadioButton rbMale;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private String currentDateTimeString, checkedJKRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        rgJK.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId) {
                    case R.id.radioJKMale:
                        checkedJKRadio = "M";
                        Log.e(TAG, checkedJKRadio);
                        break;
                    case R.id.radioJKFemale:
                        checkedJKRadio = "M";
                        Log.e(TAG, checkedJKRadio);
                        break;
                }
            }
        });

    }

    @OnClick(R.id.btn_back)
    public void backToLoginFromRegister(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.buttonRegister)
    public void daftar(View view) {
        String e = etEmail.getText().toString().trim();
        String p = etPassword.getText().toString().trim();
        String n = etName.getText().toString().trim();
        String b = etBirthdate.getText().toString().trim();
        String g = checkedJKRadio ;

        if(e.length() > 0 && p.length() > 0 && n.length() > 0 && b.length() > 0 && g.length() > 0 ){
            registration(e,p,g,n,b);
        }else{
            Log.e(TAG, "Harap isi semua field yang ada !!!");
        }
    }

    private void registration(String email, String pass, String gender, String name, String birthdate) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Creating New Account...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<RegisterResponse> call = apiService.registration(email,pass,birthdate,name,gender);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse>call, Response<RegisterResponse> response) {
                dialog.dismiss();

                if (response.code() == 200 && response.isSuccessful() ) {

                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.show();

                    currentDateTimeString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                    Log.e("JAM Register", currentDateTimeString);

//                    Bundle params = new Bundle();
//                    params.putString("technician_id", tCode);
//                    params.putString("technician_name", fName + " " + lName);
//                    params.putString("tech_login_time", currentDateTimeString);
//                    mFirebaseAnalytics.logEvent("technician_login", params);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(response.body().getMessages().getEmail());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out!");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

            }
        });
    }

}
