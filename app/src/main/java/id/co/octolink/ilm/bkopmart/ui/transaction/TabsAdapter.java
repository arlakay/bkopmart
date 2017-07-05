package id.co.octolink.ilm.bkopmart.ui.transaction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.co.octolink.ilm.bkopmart.R;
import id.co.octolink.ilm.bkopmart.ui.transaction.pembelian.PembelianFragment;
import id.co.octolink.ilm.bkopmart.ui.transaction.tagihan.TagihanFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {

    public static final int TOTAL_TABS = 2;
    public BottomTabTransaksiFragment mContext;

    public TabsAdapter(FragmentManager fm, BottomTabTransaksiFragment context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TagihanFragment().newInstance();
            case 1:
            default:
                return new PembelianFragment().newInstance();
        }
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.fragment_first_title);
            case 1:
            default:
                return mContext.getString(R.string.fragment_second_title);
        }
    }

}
