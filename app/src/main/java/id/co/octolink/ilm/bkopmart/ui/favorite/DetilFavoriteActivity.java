package id.co.octolink.ilm.bkopmart.ui.favorite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.UnMarkFavoriteResponse;
import id.co.octolink.ilm.bkopmart.realm.TransactionItem;
import id.co.octolink.ilm.bkopmart.ui.cart.CartActivity;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetilFavoriteActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.txt_detail_item_name)TextView txtItemName;
    @BindView(R.id.txt_description)TextView txtCategory;
    @BindView(R.id.img_header)ImageView imgHeader;
    @BindView(R.id.img_pic)ImageView imgPicLeft;
    @BindView(R.id.btn_detail_purchase)Button btnUnMarkFav;
    @BindView(R.id.btn_beli)Button btnBeli;
    @BindView(R.id.txt_price)TextView txtPrice;

    private static final String TAG = DetilFavoriteActivity.class.getSimpleName();
    private String iName, iPic, iCat, cid, itemId, iPrice, au;
    private SessionManager sessionManager;
    private int i=1;
    private int price = 0;
    private int hitungQty, qtyTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_favorite);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getLoginDetails();
        cid = user.get(SessionManager.KEY_CUSTOMER_ID);

        setupToolbar();
        getData();

        btnUnMarkFav.setText("Hapus dari Favorite");

        txtItemName.setText(iName);
        txtCategory.setText(iCat);
        txtPrice.setText(iPrice);

        byte[] decodedString = Base64.decode(iPic, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        imgHeader.setImageBitmap(decodedByte);
        imgPicLeft.setImageBitmap(decodedByte);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }
    }

    private void getData(){
        Intent i = getIntent();
        iName = i.getStringExtra("item_name");
        iPic = i.getStringExtra("item_pic");
        iCat = i.getStringExtra("item_category");
        itemId = i.getStringExtra("item_id");
        iPrice = i.getStringExtra("varian_price");
    }

    @OnClick(R.id.btn_beli)
    public void buy(View view) {
        final Dialog d = new Dialog(DetilFavoriteActivity.this);
        d.setContentView(R.layout.dialog_item_quantity);
        d.setTitle("Quantity");
        ImageButton bPlusQuantity = (ImageButton) d.findViewById(R.id.btn_tambah_quantity);
        ImageButton bMinQuantity = (ImageButton) d.findViewById(R.id.btn_kurang_quantity);
        Button bSubmitQuantity = (Button) d.findViewById(R.id.btn_submit_quantity);

        final EditText txtQuantity = (EditText) d.findViewById(R.id.txt_quantity);
        txtQuantity.setText(String.valueOf(i));

        bPlusQuantity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                i += 1;
                txtQuantity.setText(String.valueOf(i)); //set the value to textview
//                d.dismiss();
            }
        });

        bMinQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i -= 1;
                txtQuantity.setText(String.valueOf(i));
//              d.dismiss(); // dismiss the dialog
            }
        });

        bSubmitQuantity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                au = txtQuantity.getText().toString().trim();
                Log.e("QUANTITY : ", au);

                qtyTemp = Integer.parseInt(au);

                price = Integer.parseInt(iPrice);
                hitungQty = qtyTemp * price;

                if(qtyTemp > 0){
                    saveItemToDB(cid, itemId, iName, price, au, iPic, hitungQty);
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(DetilFavoriteActivity.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Failed add to cart , quantity minimum 1 !");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }

                d.dismiss();
            }
        });

        d.show();

    }

    @OnClick(R.id.btn_detail_purchase)
    public void unMarkFavorite(View view) {
        unMarkFavorite(cid, itemId);
    }

    private void unMarkFavorite(String uid, String iid) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Unmarking From Favorite...");

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<UnMarkFavoriteResponse> call = apiService.unMarkAsFavorite(uid, iid);
        call.enqueue(new Callback<UnMarkFavoriteResponse>() {
            @Override
            public void onResponse(Call<UnMarkFavoriteResponse>call, Response<UnMarkFavoriteResponse> response) {
                dialog.dismiss();

                Log.e(TAG, "Status Code = " + response.code());
                Log.e(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.code() == 200 && response.body().getStatus() ) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DetilFavoriteActivity.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(DetilFavoriteActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<UnMarkFavoriteResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(DetilFavoriteActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Request Timed Out"+t.getMessage());
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

            }
        });
    }

    private void saveItemToDB(String cid, String iid, String iName, int iPrice, String iQty,
                              String iPic, int subTotal){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        TransactionItem ti = realm.createObject(TransactionItem.class);
        ti.setTransactionItemCustomerID(cid);
        ti.setTransactionItemID(iid);
        ti.setTransactionItemName(iName);
        ti.setTransactionItemPrice(iPrice);
        ti.setTransactionItemQuantity(iQty);
        ti.setTransactionItemPicture(iPic);
        ti.setTransactionItemSubTotal(subTotal);
        realm.commitTransaction();

        realm.beginTransaction();
        TransactionItem realmUser = realm.copyToRealm(ti);
        realm.commitTransaction();

        final Dialog d = new Dialog(DetilFavoriteActivity.this);
        d.setContentView(R.layout.dialog_success_add_to_cart);
        d.setTitle("Success");

        Button btnLanjutBelanja = (Button) d.findViewById(R.id.btn_lanjut_belanja);
        Button btnCart = (Button) d.findViewById(R.id.btn_to_cart);
        TextView txtMessage = (TextView) d.findViewById(R.id.txt_message);

        txtMessage.setText("Item berhasil ditambahkan...");

        btnLanjutBelanja.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
                finish();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetilFavoriteActivity.this, CartActivity.class));
                finish();
            }
        });

        d.show();


    }

}
