package id.co.octolink.ilm.bkopmart.ui.favorite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.model.Favorite;
import id.co.octolink.ilm.bkopmart.realm.TransactionItem;
import id.co.octolink.ilm.bkopmart.ui.cart.CartActivity;
import io.realm.Realm;

import static id.co.octolink.ilm.bkopmart.ui.home.DetilProductActivity.au;
import static id.co.octolink.ilm.bkopmart.ui.home.DetilProductActivity.cid;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Favorite> movies = new ArrayList<>();
    private OnClickListener onClickListener;
    private int i=1;
    private int price = 0;
    private int hitungQty, qtyTemp;
    Context mContext;

    public FavoriteAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void setMoviesData(List<Favorite> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FavoriteViewHolder) {
            FavoriteViewHolder movieViewHolder = (FavoriteViewHolder) holder;

            movieViewHolder.bind(movies.get(position).getPict(), movies.get(position).getItemName(), movies.get(position).getVarianPrice());
            movieViewHolder.setItemClickListener(onClickListener, movies.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(@LayoutRes int resId, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }
    }

    public class FavoriteViewHolder extends Holder {

        @BindView(R.id.poster) ImageView posterView;
        @BindView(R.id.release_date) TextView releaseDateView;
        @BindView(R.id.btn_beli)Button btnBeli;
        @BindView(R.id.harga)TextView txtHarga;

        public FavoriteViewHolder(ViewGroup parent) {
            super(R.layout.card_grid_movie, parent);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String posterUrl, String releaseDate, String price_cuk) {
            byte[] decodedString = Base64.decode(posterUrl, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            posterView.setImageBitmap(decodedByte);
            releaseDateView.setText(releaseDate);
            txtHarga.setText(price_cuk);
        }


        public void setItemClickListener(final OnClickListener onClickListener, final Favorite movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClickListener(movie.getId(), movie);
                }
            });

            btnBeli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog d = new Dialog(mContext);
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

                            price = Integer.parseInt(movie.getVarianPrice());
                            hitungQty = qtyTemp * price;

                            if(qtyTemp > 0){
                                saveItemToDB(cid, movie.getItemId(), movie.getItemName(), price, au, movie.getPict(), hitungQty);
                            }else {
                                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
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
            });

        }
    }

    public interface OnClickListener {
        void onItemClickListener(String id, Favorite movie);
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

        final Dialog d = new Dialog(mContext);
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
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CartActivity.class));
            }
        });

        d.show();


    }

}
