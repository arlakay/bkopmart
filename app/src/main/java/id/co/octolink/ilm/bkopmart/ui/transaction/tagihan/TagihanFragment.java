package id.co.octolink.ilm.bkopmart.ui.transaction.tagihan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.Tagihan;
import id.co.octolink.ilm.bkopmart.model.TagihanResponse;
import id.co.octolink.ilm.bkopmart.ui.transaction.DetilTransaksiActivity;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TagihanFragment extends Fragment {
    @BindView(R.id.recycler_view_transaksi)RecyclerView recyclerView;
    @BindView(R.id.txt_kosong)TextView textListKosong;

    private static final String TAG = TagihanFragment.class.getSimpleName();
    private SessionManager sessionManager;
    private RecyclerView.LayoutManager layoutManager;
    private TagihanAdapter adapter;
    private List<Tagihan> tagihanList;

    public TagihanFragment() {
        // Required empty public constructor
    }

    public static TagihanFragment newInstance() {
        return new TagihanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tagihan, container, false);
        ButterKnife.bind(this, rootView);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getLoginDetails();
        String cID = user.get(SessionManager.KEY_CUSTOMER_ID);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getAllTransaksiTagihan(cID);

        return rootView;

    }

    private void getAllTransaksiTagihan(String cust_id){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "loading...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<TagihanResponse> call = apiService.getAllTransactionBilling(cust_id);
        call.enqueue(new Callback<TagihanResponse>() {
            @Override
            public void onResponse(Call<TagihanResponse>call, Response<TagihanResponse> response) {
                dialog.dismiss();

                Log.d(TAG, "Status Code = " + response.code());
                Log.d(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body().getStatus()) {
                    tagihanList = response.body().getData();
                    textListKosong.setVisibility(View.GONE);

                    adapter = new TagihanAdapter(tagihanList, R.layout.card_list_transaksi, getActivity().getApplicationContext(), new TagihanAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Tagihan model) {
                            String tID = model.getId();
                            String cID = model.getCashierId();
                            String oID = model.getOutletId();
                            String tDate = model.getTransactionDate();
                            String pType = model.getPaymentType();
                            String tendered = model.getCustTendered();
                            String change = model.getCustChange();
                            String email = model.getCustEmail();
                            String status = model.getRecordStatus();

                            Intent i = new Intent(getActivity(), DetilTransaksiActivity.class);
                            i.putExtra("tid", tID);
                            i.putExtra("cid", cID);
                            i.putExtra("oid", oID);
                            i.putExtra("tDate", tDate);
                            i.putExtra("pType", pType);
                            i.putExtra("tender", tendered);
                            i.putExtra("change", change);
                            i.putExtra("email", email);
                            i.putExtra("status", status);
                            startActivity(i);
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
            public void onFailure(Call<TagihanResponse>call, Throwable t) {
                dialog.dismiss();

                Log.e(TAG, t.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
                alertDialog.show();
            }
        });
    }

}
