package id.co.octolink.ilm.bkopmart.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.model.ViewDetilTransaksi;

/**
 * Created by ERD on 02/11/2016.
 */

public class ViewDetilTransaksiAdapter extends RecyclerView.Adapter<ViewDetilTransaksiAdapter.VersionViewHolder> {

    private List<ViewDetilTransaksi> merchantList;
    private int rowLayout;
    Context context;
    OnItemClickListener clickListener;

    public ViewDetilTransaksiAdapter(List<ViewDetilTransaksi> login, int rowLayout, Context context, OnItemClickListener listener) {
        this.merchantList = login;
        this.rowLayout = rowLayout;
        this.context = context;
        this.clickListener = listener;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        final ViewDetilTransaksi model = merchantList.get(i);
        versionViewHolder.bind(model, clickListener, i);
    }

    @Override
    public int getItemCount() {
        return merchantList == null ? 0 : merchantList.size();
    }

    public void animateTo(List<ViewDetilTransaksi> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ViewDetilTransaksi> newModels) {
        for (int i = merchantList.size() - 1; i >= 0; i--) {
            final ViewDetilTransaksi model = merchantList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ViewDetilTransaksi> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ViewDetilTransaksi model = newModels.get(i);
            if (!merchantList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ViewDetilTransaksi> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ViewDetilTransaksi model = newModels.get(toPosition);
            final int fromPosition = merchantList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ViewDetilTransaksi removeItem(int position) {
        final ViewDetilTransaksi model = merchantList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ViewDetilTransaksi model) {
        merchantList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ViewDetilTransaksi model = merchantList.remove(fromPosition);
        merchantList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_item_sku)TextView txtItemSKU;
        @BindView(R.id.txt_id)TextView txtID;
        @BindView(R.id.txt_item_name)TextView txtItemName;
        @BindView(R.id.txt_item_qty)TextView txtItemQty;
        @BindView(R.id.txt_item_price)TextView txtItemPrice;
        @BindView(R.id.txt_item_tax)TextView txtItemTax;
        @BindView(R.id.txt_item_service)TextView txtItemService;

        public VersionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ViewDetilTransaksi model, final OnItemClickListener listener, int position) {
            txtItemSKU.setText(model.getItemId());
            txtID.setText(model.getId());
            txtItemName.setText(model.getVarianName());
            txtItemQty.setText("Qty : " + model.getItemQty());
            txtItemPrice.setText(model.getItemPrice());
            txtItemTax.setText(model.getItemTax());
            txtItemService.setText(model.getItemService());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);

                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(ViewDetilTransaksi model);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}