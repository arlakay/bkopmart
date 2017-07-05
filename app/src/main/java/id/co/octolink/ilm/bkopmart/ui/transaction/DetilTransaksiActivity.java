package id.co.octolink.ilm.bkopmart.ui.transaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.GeneralResponse;
import id.co.octolink.ilm.bkopmart.model.ViewDetilTransaksi;
import id.co.octolink.ilm.bkopmart.model.ViewDetilTransaksiResponse;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetilTransaksiActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.recycler_view_detil_transaksi)RecyclerView recyclerView;
    @BindView(R.id.txt_kosong)TextView textListKosong;
    @BindView(R.id.txt_transact_id)TextView txtTID;
    @BindView(R.id.txt_transact_date)TextView txtTDate;
    @BindView(R.id.txt_email)TextView txtEmail;
    @BindView(R.id.txt_status)TextView txtStatus;
    @BindView(R.id.txt_payment_type)TextView txtPType;
    @BindView(R.id.txt_tendered)TextView txtTendered;
    @BindView(R.id.txt_change)TextView txtChange;
    @BindView(R.id.btn_confirm_tagihan)Button btnConfirm;

    private static final String TAG = DetilTransaksiActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private RecyclerView.LayoutManager layoutManager;
    private ViewDetilTransaksiAdapter adapter;
    private List<ViewDetilTransaksi> viewDetilTransaksiList;
    private String tid, cid, oid, tDate, pType, tender, change, email, status, cID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_transaksi);
        ButterKnife.bind(this);

        setupToolbar();
        getData();

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getLoginDetails();
        cID = user.get(SessionManager.KEY_CUSTOMER_ID);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        viewDetailTransaksi(tid);

        txtTID.setText("Nomor Order : " + tid);
        txtTDate.setText(tDate);
        txtEmail.setText(email);
        txtStatus.setText("Status Order : " + status);
        txtPType.setText(pType);
        txtTendered.setText(tender);
        txtChange.setText(change);

        if (status.equalsIgnoreCase("1")){
            btnConfirm.setVisibility(View.GONE);
        }else{
            btnConfirm.setVisibility(View.VISIBLE);
        }

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }
    }

    private void getData(){
        Intent i = getIntent();
        tid = i.getStringExtra("tid");
        cid = i.getStringExtra("cid");
        oid = i.getStringExtra("oid");
        tDate = i.getStringExtra("tDate");
        pType = i.getStringExtra("pType");
        tender = i.getStringExtra("tender");
        change = i.getStringExtra("change");
        email = i.getStringExtra("email");
        status = i.getStringExtra("status");
    }

    private void viewDetailTransaksi(String transact_id){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<ViewDetilTransaksiResponse> call = apiService.viewTransaction(transact_id);
        call.enqueue(new Callback<ViewDetilTransaksiResponse>() {
            @Override
            public void onResponse(Call<ViewDetilTransaksiResponse>call, Response<ViewDetilTransaksiResponse> response) {
                dialog.dismiss();

                Log.d(TAG, "Status Code = " + response.code());
                Log.d(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body().getStatus()) {
                    viewDetilTransaksiList = response.body().getData();
                    textListKosong.setVisibility(View.GONE);

                    adapter = new ViewDetilTransaksiAdapter(viewDetilTransaksiList, R.layout.card_list_detil_transaksi, getApplicationContext(), new ViewDetilTransaksiAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ViewDetilTransaksi model) {
                            //Do Nothing
                        }
                    });
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    textListKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ViewDetilTransaksiResponse>call, Throwable t) {
                dialog.dismiss();

                Log.e(TAG, t.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(DetilTransaksiActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void confirmBilling(String cid, String transact_id){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<GeneralResponse> call = apiService.doTransactionCheckout(cid, transact_id);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse>call, Response<GeneralResponse> response) {
                dialog.dismiss();

                Log.d(TAG, "Status Code = " + response.code());
                Log.d(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body().getStatus()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DetilTransaksiActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(DetilTransaksiActivity.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage(response.body().getMessage());
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

                Log.e(TAG, t.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(DetilTransaksiActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @OnClick(R.id.btn_confirm_tagihan)
    public void confirmCheckout(){
        confirmBilling(cID, tid);
    }
}
