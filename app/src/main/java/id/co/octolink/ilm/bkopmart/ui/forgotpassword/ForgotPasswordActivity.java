package id.co.octolink.ilm.bkopmart.ui.forgotpassword;

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
import id.co.octolink.ilm.bkopmart.model.GeneralResponse;
import id.co.octolink.ilm.bkopmart.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.et_email)EditText etEmail;
    @BindView(R.id.et_old_password)EditText etOldPass;
    @BindView(R.id.et_new_password)EditText etNewPass;

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private String currentDateTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_back)
    public void backToLoginFromForgotPass(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btn_kirim_forgot_pass)
    public void forgotPassword(View view) {
        String e = etEmail.getText().toString().trim();
        String op = etOldPass.getText().toString().trim();
        String np = etNewPass.getText().toString().trim();

        if (e.length() > 0 && op.length() > 0 && np.length() > 0){
            forgotPass(e, op, np);
        }else{
            Log.e(TAG, "Harap semua field diisi !!!");
        }
    }

    private void forgotPass(String email, String oldPass, String newPass) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Changing Account Password...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<GeneralResponse> call = apiService.changePassword(email, oldPass, newPass);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse>call, Response<GeneralResponse> response) {
                dialog.dismiss();

                if (response.code() == 200 && response.isSuccessful() ) {

                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage(response.body().getMessages());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.show();

                    currentDateTimeString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                    Log.e("JAM Change Password: ", currentDateTimeString);

//                    Bundle params = new Bundle();
//                    params.putString("technician_id", tCode);
//                    params.putString("technician_name", fName + " " + lName);
//                    params.putString("tech_login_time", currentDateTimeString);
//                    mFirebaseAnalytics.logEvent("technician_login", params);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
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
            public void onFailure(Call<GeneralResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
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
