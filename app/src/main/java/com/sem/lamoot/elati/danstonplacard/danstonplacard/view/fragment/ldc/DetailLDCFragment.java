package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.LdcProduitDefaut;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCoursesDefaut;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.DetailProduitFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DetailLDCFragment extends Fragment {

    final static String ARG_LDC = "ARG_LDC";
    private Integer mLdc;
    private Context mContext;

    private RecyclerView ldcProductRecyclerview;
    private ImageButton btnEditLdc, btnRecycleLdc;

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

        ldcProductRecyclerview = view.findViewById(R.id.ldc_product_list_recyclerview);
        LDCProductAdapter ldcProductAdapter = new LDCProductAdapter(mContext);
        ldcProductAdapter.setData(generateProduct());
        ldcProductRecyclerview.setAdapter(ldcProductAdapter);
        RecyclerView.LayoutManager ldcProductLayoutManager = new LinearLayoutManager(getActivity());
        ldcProductRecyclerview.setLayoutManager(ldcProductLayoutManager);

        btnEditLdc = view.findViewById(R.id.btn_edit_ldc);
        btnEditLdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Edition...", Toast.LENGTH_SHORT).show();
                // Launch edit ldc fragment
            }
        });
        btnRecycleLdc = view.findViewById(R.id.btn_archive_ldc);
        btnRecycleLdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Archive...", Toast.LENGTH_SHORT).show();
                // Set archive field to 1
            }
        });
        return view;
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
