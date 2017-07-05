package id.co.octolink.ilm.bkopmart.ui.cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.realm.TransactionItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by e_er_de on 29/05/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    protected LayoutInflater inflater;
    private List<TransactionItem> cities = null;
    private Context mContext;
    private RecyclerView mAttechedRecyclerView;
    private int mClickedPosition = -1 ;
    TransactionItem userdatabase;

    public CartAdapter(Context context) {
        super();
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttechedRecyclerView = recyclerView;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new item view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_keranjang_belanja, mAttechedRecyclerView,false);

        // create ViewHolder
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        View currentView = null;
        TransactionItem city = cities.get(position);
        if (city != null) {
            byte[] decodedString = Base64.decode(city.getTransactionItemPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.getTvName().setText(city.getTransactionItemName());
            holder.getTvQty().setText("Qty : "+city.getTransactionItemQuantity());
            holder.getTvPrice().setText("Rp "+city.getTransactionItemSubTotal());
            holder.getImgItem().setImageBitmap(decodedByte);
            holder.getRemoveItem();
        }
    }

    public void setData(List<TransactionItem> details) {
        this.cities = details;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0;
        }
        return cities.size();
    }


    public void updateCities() {
        Realm realm = Realm.getDefaultInstance();

        // Pull all the cities from the realm
        RealmResults<TransactionItem> cities = realm.where(TransactionItem.class).findAll();

        // Put these items in the Adapter
        setData(cities);
        //notifyDataSetChanged();
        notifyItemRemoved(mClickedPosition);
        notifyItemInserted(0);
        mAttechedRecyclerView.scrollToPosition(0);
        mAttechedRecyclerView.invalidate();
        realm.close();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvName;
        TextView txtItemQty;
        TextView txtItemPrice;
        ImageView imgPicItem;
        ImageButton imgRemoveItem;

        public TextView getTvName() {
            return mTvName;
        }

        public TextView getTvQty() {
            return txtItemQty;
        }

        public TextView getTvPrice() {
            return txtItemPrice;
        }

        public ImageView getImgItem() {
            return imgPicItem;
        }

        public ImageButton getRemoveItem(){
            return imgRemoveItem;
        }

        public ViewHolder(View itemView) {
            super(itemView);
//            imgRemoveItem.setOnClickListener(this);
            mTvName = (TextView) itemView.findViewById(R.id.txt_item_name);
            txtItemQty = (TextView) itemView.findViewById(R.id.txt_item_quantity);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txt_item_price);
            imgPicItem = (ImageView) itemView.findViewById(R.id.image_background);
            imgRemoveItem = (ImageButton) itemView.findViewById(R.id.btn_remove_item);

            imgRemoveItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Realm realm = Realm.getDefaultInstance();
            mClickedPosition = this.getAdapterPosition();

            final RealmResults<TransactionItem> students = realm.where(TransactionItem.class).findAll();

            userdatabase = students
                    .where()
                    .equalTo("transactionItemID", cities.get(mClickedPosition).getTransactionItemID())
                    .equalTo("transactionItemName", cities.get(mClickedPosition).getTransactionItemName())
                    .equalTo("transactionItemQuantity", cities.get(mClickedPosition).getTransactionItemQuantity())
                    .findFirst();

            if(userdatabase!=null){

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        userdatabase.deleteFromRealm();
                    }
                });
            }

//            TransactionItem modifiedCity = (TransactionItem) getItem(getAdapterPosition());
//            mClickedPosition = this.getAdapterPosition();
            // Update the realm object affected by the user

            // Acquire the list of realm cities matching the name of the clicked City.
//            TransactionItem city = realm.where(TransactionItem.class).equalTo(TransactionItem.ID, modifiedCity.getId()).findFirst();

            // Create a transaction to increment the vote count for the selected City in the realm
//            realm.beginTransaction();
//            city.setVotes(city.getVotes() + 1);
//            city.setTimestamp(System.currentTimeMillis());
//            realm.commitTransaction();
//            realm.close();
//            updateCities();


        }

    }

    public Object getItem(int position) {
        if (cities == null || cities.get(position) == null) {
            return null;
        }
        return cities.get(position);
    }

}
