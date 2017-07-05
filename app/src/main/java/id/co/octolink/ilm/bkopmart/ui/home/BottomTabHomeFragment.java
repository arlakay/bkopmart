package id.co.octolink.ilm.bkopmart.ui.home;

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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.api.ApiService;
import id.co.octolink.ilm.bkopmart.api.RestApi;
import id.co.octolink.ilm.bkopmart.model.Product;
import id.co.octolink.ilm.bkopmart.model.ProductResponse;
import id.co.octolink.ilm.bkopmart.model.Slider;
import id.co.octolink.ilm.bkopmart.model.SliderResponse;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;
import ss.com.bannerslider.views.indicators.IndicatorShape;

public class BottomTabHomeFragment extends Fragment {
    private final String TAG = BottomTabHomeFragment.class.getSimpleName();
    public static final int SPAN_COUNT = 2;
    private static final String DEFAULT_SORT = "popular";
    public static final String KEY_SORT = "key_sort";

    @BindView(R.id.recyclerview)RecyclerView recyclerView;
    @BindView(R.id.loading)ProgressBar loadingView;

    private ProductAdapter adapter;
    private BannerSlider bannerSlider;
    private View rootView;
    private List<Slider> sliderList = new ArrayList<>();
    private List<String> imageUrlList;

    public BottomTabHomeFragment() {
        // Required empty public constructor
    }

    public static BottomTabHomeFragment newInstance() {
        return new BottomTabHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bottom_tab_home, container, false);
        ButterKnife.bind(this, rootView);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();

        setupViews();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ProductAdapter(getActivity());
        adapter.setOnClickListener(new ProductAdapter
                .OnClickListener() {
            @Override
            public void onItemClickListener(String id, Product movie) {
                Intent i = new Intent(getActivity(), DetilProductActivity.class);
                i.putExtra("item_id",movie.getItemId());
                i.putExtra("item_name",movie.getItemName());
                i.putExtra("item_pic",movie.getPict());
                i.putExtra("item_category",movie.getCategoryName());
                i.putExtra("iprice",movie.getVarianPrice());
                startActivity(i);
            }
        });
        recyclerView.setAdapter(adapter);

        getListAllProduct();

        return rootView;
    }

    private void setupViews() {
        setupBannerSlider();
        setupPageIndicatorChooser();
        setupSettingsUi();
    }

    private void setupSettingsUi() {
        bannerSlider.setInterval(3000);
        bannerSlider.setIndicatorSize(12);
        bannerSlider.setLoopSlides(true);
        bannerSlider.setMustAnimateIndicators(true);
        bannerSlider.setHideIndicators(false);
    }

    private void setupBannerSlider(){
        bannerSlider = (BannerSlider) rootView.findViewById(R.id.banner_slider1);
        addBanners();

        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBanners(){
//        bannerSlider.addBanner(new RemoteBanner(
//                "https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png"
//        ));

        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_example));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_example));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_example));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_example));
    }

    private void setupPageIndicatorChooser(){
        bannerSlider.setDefaultIndicator(IndicatorShape.CIRCLE);
//        bannerSlider.setDefaultIndicator(IndicatorShape.DASH);
//        bannerSlider.setDefaultIndicator(IndicatorShape.ROUND_SQUARE);
//        bannerSlider.setDefaultIndicator(IndicatorShape.SQUARE);
//        bannerSlider.setCustomIndicator(VectorDrawableCompat.create(getResources(),
//                R.drawable.selected_slide_indicator, null),
//                VectorDrawableCompat.create(getResources(),
//                        R.drawable.unselected_slide_indicator, null));

    }

    private void getListAllProduct() {
        loadingView.setVisibility(View.VISIBLE);

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<ProductResponse> call = apiService.getAllProduct();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse>call, Response<ProductResponse> response) {
                loadingView.setVisibility(View.GONE);

                Log.e(TAG, "Status Code = " + response.code());
                Log.e(TAG, "Data received: " + new Gson().toJson(response.body()));

                if (response.code() == 200 && response.isSuccessful() && response.body().getStatus()){
                    adapter.setMoviesData(response.body().getData());

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Product Tidak Ada");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse>call, Throwable t) {
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

    private void getSlider() {
        loadingView.setVisibility(View.VISIBLE);

        ApiService apiService =
                RestApi.getClient().create(ApiService.class);

        Call<SliderResponse> call = apiService.getAllSlider();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse>call, Response<SliderResponse> response) {
                loadingView.setVisibility(View.GONE);

                Log.e(TAG, "Status Code = " + response.code());
                Log.e(TAG, "Data Slider received: " + new Gson().toJson(response.body()));

                if (response.code() == 200 && response.isSuccessful()){
                    //Response Sukses
                    sliderList = response.body().getData();

                    Log.e(TAG, new Gson().toJson(sliderList));

//                    for (int i=0; i<=sliderList.size(); i++){
//                        imageUrlList = new ArrayList<>();
//                        imageUrlList.add(response.body().getData().get(i).getImg());
//
//                        byte[] decodedString = Base64.decode(imageUrlList.get(i), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                        bannerSlider.addBanner(new RemoteBanner(decodedByte));
//                    }

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
            public void onFailure(Call<SliderResponse>call, Throwable t) {
                loadingView.setVisibility(View.GONE);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

}
