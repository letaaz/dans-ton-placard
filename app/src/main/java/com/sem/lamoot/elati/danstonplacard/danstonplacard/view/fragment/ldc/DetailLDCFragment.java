package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.LdcProduitDefaut;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.AjouterProduitFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DetailLDCFragment extends Fragment {

    final static String ARG_LDC = "ARG_LDC";
    private Integer mLdc;
    private Context mContext;

    private RecyclerView ldcProductRecyclerview, historyListRecyclerView;
    private ImageButton btnEditLdc, btnRecycleLdc;
    private RelativeLayout ldcLabel;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARG_LDC, id);
        DetailLDCFragment detailLdcFragment = new DetailLDCFragment();
        detailLdcFragment.setArguments(args);
        return detailLdcFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mLdc = getArguments().getInt(ARG_LDC);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_ldc_fragment, container, false);

        ldcLabel = view.findViewById(R.id.ldc_label);

        ldcProductRecyclerview = view.findViewById(R.id.ldc_product_list_recyclerview);
        LDCProductAdapter ldcProductAdapter = new LDCProductAdapter(mContext);
        ldcProductAdapter.setData(generateProduct());
        ldcProductRecyclerview.setAdapter(ldcProductAdapter);
        RecyclerView.LayoutManager ldcProductLayoutManager = new LinearLayoutManager(mContext);
        ldcProductRecyclerview.setLayoutManager(ldcProductLayoutManager);

        btnEditLdc = view.findViewById(R.id.btn_edit_ldc);
        btnEditLdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_ldc_frame, LDCEditFragment.newInstance(mLdc));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnRecycleLdc = view.findViewById(R.id.btn_archive_ldc);
        btnRecycleLdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set archive field to 1
                getFragmentManager().popBackStack();
            }
        });

        LinearLayout ldcBottomControl = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(ldcBottomControl);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@android.support.annotation.NonNull View view, int newState) {
                ImageView arrow_up = ldcBottomControl.findViewById(R.id.view_show_history);
                ImageView arrow_down = ldcBottomControl.findViewById(R.id.view_show_ldc_list);
                ConstraintLayout peekLayout = ldcBottomControl.findViewById(R.id.bottom_sheet_control);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    arrow_down.setVisibility(View.VISIBLE);
                    peekLayout.setBackgroundColor(Color.DKGRAY);
                    ldcProductRecyclerview.setVisibility(View.GONE);
                    arrow_up.setVisibility(View.GONE);
                    ldcLabel.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    arrow_up.setVisibility(View.VISIBLE);
                    ldcLabel.setVisibility(View.VISIBLE);
                    peekLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    ldcProductRecyclerview.setVisibility(View.VISIBLE);
                    arrow_down.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@android.support.annotation.NonNull View view, float v) { }
        });

        historyListRecyclerView = ldcBottomControl.findViewById(R.id.bottom_sheet_content_ldc_history_recyclerview);
        LDCProductAdapter historyAdapter = new LDCProductAdapter(mContext);
        historyAdapter.setData(generateProduct2());
        historyListRecyclerView.setAdapter(historyAdapter);
        RecyclerView.LayoutManager historyLayoutManager = new LinearLayoutManager(mContext);
        historyListRecyclerView.setLayoutManager(historyLayoutManager);

        return view;
    }

    private List<LdcProduitDefaut> generateProduct2() {
        List<LdcProduitDefaut> prods = new ArrayList<>();
        prods.add(new LdcProduitDefaut("Thon Caumartin", 5.4f, 2));
        prods.add(new LdcProduitDefaut("Oeufs - Demigros de chez Auchan Villeneuve d'Ascq Market", 3.3f, 1));

        return prods;
    }

    private List<LdcProduitDefaut> generateProduct() {
        List<LdcProduitDefaut> prods = new ArrayList<>();
        prods.add(new LdcProduitDefaut("Viande de chèvre", 5.4f, 2));
        prods.add(new LdcProduitDefaut("Beurre St Michel", 1.35f, 3));
        prods.add(new LdcProduitDefaut("Oeufs - Demigros de chez Auchan Villeneuve d'Ascq Market", 3.3f, 1));
        prods.add(new LdcProduitDefaut("Shampoing Gel Douche AXE Sensation", 3.4f, 15));
        prods.add(new LdcProduitDefaut("Fromage Emmental Entremont", 2.4f, 2));
        prods.add(new LdcProduitDefaut("500g Saumon fumé Piquet", 12.4f, 1));
        return prods;
    }


}
