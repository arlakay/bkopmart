package id.co.octolink.ilm.bkopmart.ui.transaction;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.octolink.ilm.bkopmart.R;

public class BottomTabTransaksiFragment extends Fragment {

    public BottomTabTransaksiFragment() {
        // Required empty public constructor
    }

    public static BottomTabTransaksiFragment newInstance() {
        return new BottomTabTransaksiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_tab_transaksi, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPager.setAdapter(new TabsAdapter(getFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
