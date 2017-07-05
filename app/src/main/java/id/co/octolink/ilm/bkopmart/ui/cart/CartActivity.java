package id.co.octolink.ilm.bkopmart.ui.cart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.BillingResponse;
import id.co.octolink.ilm.bkopmart.realm.TransactionItem;
import id.co.octolink.ilm.bkopmart.ui.BaseActivity;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {
    @BindView(R.id.recycler_cart)RecyclerView mRecyclerView;
    @BindView(R.id.txt_total_belanja)TextView txtTotal;

    private RecyclerView.LayoutManager layoutManager;
    private CartAdapter mAdapter;
    private Realm realm;
    private List<TransactionItem> test;
    private SessionManager sessionManager;
    private String cid;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        RealmResults<TransactionItem> cobadulu = realm.where(TransactionItem.class).findAll();
        test = new ArrayList<TransactionItem>();
        test = cobadulu;

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getLoginDetails();
        cid = user.get(SessionManager.KEY_CUSTOMER_ID);

        Number total = realm.where(TransactionItem.class).sum("transactionItemSubTotal");

        Log.e("NUMBER : ", String.valueOf(total));

        txtTotal.setText("Rp "+String.valueOf(total));

        // Load from file "cities.json" first time
        if (mAdapter == null) {
            mAdapter = new CartAdapter(this);
            RealmResults<TransactionItem> cities = realm.where(TransactionItem.class).findAll();

            if (cities.size() > 0) {
                //This is the recyclerview adapter from realm;
                mAdapter.setData(cities);
            } else {
                //This is the recyclerview adapter from json(init data);
//                List<TransactionItem> citiesFromJson = loadCities();
//                mAdapter.setData(citiesFromJson);
            }
            //This is the GridView which will display the list of cities
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();
            mRecyclerView.invalidate();

            final RealmResults<TransactionItem> allSorted = realm.where(TransactionItem.class).findAll();
            mAdapter.setData(allSorted);

        }

        jsonArray = new JSONArray();
        for (int i = 0; i < cobadulu.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("item_id", test.get(i).getTransactionItemID());
                object.put("varian_name", test.get(i).getTransactionItemName());
                object.put("item_price", test.get(i).getTransactionItemSubTotal());
                object.put("item_qty", test.get(i).getTransactionItemQuantity());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(object);
        }

        Log.e("TAGATAGA", jsonArray.toString());

    }

    private void placeOrderToGetBilling(String cid, String transaction_json) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Processing Order to Billing...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<BillingResponse> call = apiService.doTransactionBilling(cid, transaction_json);
        call.enqueue(new Callback<BillingResponse>() {
            @Override
            public void onResponse(Call<BillingResponse>call, Response<BillingResponse> response) {
                dialog.dismiss();

                if (response.code() == 200 && response.body().getStatus() ) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.deleteAll();
                                }
                            });
                            startActivity(getIntent());
                            finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    String messageError;
                    if (response.body().getMessages().getCustomerId() == null){
                        messageError = response.body().getMessages().getJsonTransaction();
                    }else{
                        messageError = response.body().getMessages().getCustomerId();
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(messageError);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<BillingResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
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

    @OnClick(R.id.btn_checkout)
    public void PlaceOrder(){
        placeOrderToGetBilling(cid, jsonArray.toString());
    }


}
