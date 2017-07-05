package id.co.octolink.ilm.bkopmart.ui.transaction.tagihan;

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
import id.co.octolink.ilm.bkopmart.model.Tagihan;

/**
 * Created by ERD on 02/11/2016.
 */

public class TagihanAdapter extends RecyclerView.Adapter<TagihanAdapter.VersionViewHolder> {

    private List<Tagihan> merchantList;
    private int rowLayout;
    Context context;
    OnItemClickListener clickListener;

    public TagihanAdapter(List<Tagihan> login, int rowLayout, Context context, OnItemClickListener listener) {
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
        final Tagihan model = merchantList.get(i);
        versionViewHolder.bind(model, clickListener, i);
    }

    @Override
    public int getItemCount() {
        return merchantList == null ? 0 : merchantList.size();
    }

    public void animateTo(List<Tagihan> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Tagihan> newModels) {
        for (int i = merchantList.size() - 1; i >= 0; i--) {
            final Tagihan model = merchantList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Tagihan> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Tagihan model = newModels.get(i);
            if (!merchantList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Tagihan> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Tagihan model = newModels.get(toPosition);
            final int fromPosition = merchantList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Tagihan removeItem(int position) {
        final Tagihan model = merchantList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Tagihan model) {
        merchantList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Tagihan model = merchantList.remove(fromPosition);
        merchantList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_no_order)TextView txtNoOrder;
        @BindView(R.id.txt_order_status)TextView txtStatusOrder;

        public VersionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Tagihan model, final OnItemClickListener listener, int position) {
            txtNoOrder.setText("Nomor Order : " + model.getId());
            txtStatusOrder.setText("Status Order : " + model.getRecordStatus());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);

                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(Tagihan model);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}