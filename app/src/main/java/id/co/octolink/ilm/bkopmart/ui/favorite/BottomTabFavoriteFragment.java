package id.co.octolink.ilm.bkopmart.ui.favorite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.Favorite;
import id.co.octolink.ilm.bkopmart.model.FavoriteResponse;
import id.co.octolink.ilm.bkopmart.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomTabFavoriteFragment extends Fragment {
    @BindView(R.id.recyclerview)RecyclerView recyclerView;
    @BindView(R.id.loading)ProgressBar loadingView;
    @BindView(R.id.txt_kosong)TextView txtKosong;

    private final String TAG = BottomTabFavoriteFragment.class.getSimpleName();
    public static final int SPAN_COUNT = 2;
    private SessionManager sessionManager;
    private FavoriteAdapter adapter;
    private View rootView;

    public BottomTabFavoriteFragment() {
        // Required empty public constructor
    }

    public static BottomTabFavoriteFragment newInstance() {
        return new BottomTabFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bottom_tab_favorite, container, false);
        ButterKnife.bind(this, rootView);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getLoginDetails();
        String cID = user.get(SessionManager.KEY_CUSTOMER_ID);

        getFavoriteByCustomerId(cID);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new FavoriteAdapter(getActivity());
        adapter.setOnClickListener(new FavoriteAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(String id, Favorite movie) {
                Intent i = new Intent(getActivity(), DetilFavoriteActivity.class);
                i.putExtra("item_id",movie.getItemId());
                i.putExtra("item_name",movie.getItemName());
                i.putExtra("item_pic",movie.getPict());
                i.putExtra("item_category",movie.getCategoryName());
                i.putExtra("varian_price",movie.getVarianPrice());
                startActivity(i);
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void getFavoriteByCustomerId(String cust_id) {
        loadingView.setVisibility(View.VISIBLE);

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<FavoriteResponse> call = apiService.getAllFavoriteProductByCustomerId(cust_id);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse>call, Response<FavoriteResponse> response) {
                loadingView.setVisibility(View.GONE);

                Log.e(TAG, "Status Code = " + response.code());
                Log.e(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.code() == 200 && response.body().getStatus()){
                    adapter.setMoviesData(response.body().getData());
                    txtKosong.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    txtKosong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse>call, Throwable t) {
                loadingView.setVisibility(View.GONE);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Kesalahan Jaringan" + t.getMessage());
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

}
