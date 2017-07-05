package id.co.octolink.ilm.bkopmart.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.ProfileResponse;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomTabProfileFragment extends Fragment {
    @BindView(R.id.btn_logout)Button btnLogout;
    @BindView(R.id.img_profile)ImageView imgProfile;
    @BindView(R.id.txt_profile_nama)TextView txtNama;
    @BindView(R.id.txt_profile_tgl_lahir)TextView txtBirthDate;
    @BindView(R.id.txt_profile_jk)TextView txtJK;
    @BindView(R.id.txt_profile_email)TextView txtEmail;
    @BindView(R.id.spin_outlet_location)Spinner spinOutlet;

    private static final String TAG = BottomTabProfileFragment.class.getSimpleName();
    private SessionManager sessionManager;
    private String cID, cEmail, newDate, valueOutlet;
    private ArrayAdapter<CharSequence> adapterOutlet;

    public BottomTabProfileFragment() {
        // Required empty public constructor
    }

    public static BottomTabProfileFragment newInstance() {
        return new BottomTabProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_tab_profile, container, false);
        ButterKnife.bind(this, rootView);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getLoginDetails();
        cID = user.get(SessionManager.KEY_CUSTOMER_ID);
        cEmail = user.get(SessionManager.KEY_EMAIL);

        viewProfile(cEmail);

        return rootView;

    }

    private void viewProfile(String cust_email) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "loading...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<ProfileResponse> call = apiService.viewProfile(cust_email);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                dialog.dismiss();

                Log.d(TAG, "Status Code = " + response.code());
                Log.d(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.code() == 200 && response.body().getStatus() && response.body().getMessages().contains("Success view profile")) {
                    txtNama.setText(response.body().getData().getName());
                    txtBirthDate.setText(convertDate(response.body().getData().getBirthdate()));
                    txtJK.setText(response.body().getData().getGender());
                    txtEmail.setText(response.body().getData().getEmail());

                    String pic_url = response.body().getData().getAvatar();

                    if (pic_url == null || pic_url.equalsIgnoreCase("")){
                        imgProfile.setImageResource(R.drawable.ic_account_circle_orange);
                    }else{
//                        Picasso.with(getActivity().getApplicationContext())
//                                .load(RestApi.BASE_IMG_URL + pic_url)
//                                .resize(144, 144)
//                                .centerCrop()
//                                .transform(new CircleTransform())
//                                .into(imgProfile);
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Maaf Profile tidak berhasil dimuat, harap ulangi",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse>call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(
                        getActivity(),
                        "Maaf Profile tidak ditemukan",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public String convertDate(String date){
        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = null;
        try {
            dt = sd1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sd2 = new SimpleDateFormat("d MMM yyyy");
        newDate = sd2.format(dt);

        return newDate;
    }

    public String convertDateTime(String date){
        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = sd1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sd2 = new SimpleDateFormat("d MMM yyyy   HH:mm");
        newDate = sd2.format(dt);

        return newDate;
    }

    @OnClick(R.id.btn_logout)
    public void logout(View view) {
        if(sessionManager.isLoggedIn()){
            sessionManager.setLogin(false);
            sessionManager.logoutUser();
        }else{
            sessionManager.checkLogin();
        }
    }

}
