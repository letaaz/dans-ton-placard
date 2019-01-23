package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.inventaire.ProduitAdapter;

import java.util.ArrayList;
import java.util.List;

public class LDCAdapter extends RecyclerView.Adapter<LDCAdapter.LDCViewHolder> {

    private ProduitDao produitDao;

    public interface OnItemClickListener {
        void onItemClickListener(ListeCourses ldcDefaut);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ListeCourses> data;
    private OnItemClickListener itemClickListener;
    private ListeCoursesDao listeCoursesDao;

    public LDCAdapter (Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemClickListener = listener;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public LDCAdapter.LDCViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.ldc_item, viewGroup, false);
        return new LDCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LDCAdapter.LDCViewHolder ldcViewHolder, int i) {

        listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();
        produitDao = RoomDB.getDatabase(mContext).produitDao();

        String alertMsg = mContext.getResources().getString(R.string.msgAlertDialogSupprimerLdc);
        String title = mContext.getResources().getString(R.string.titleAlertDialogSupprimerLdc);
        String positiveButtonTxt = mContext.getResources().getString(R.string.positiveButtonAlertDialogSupprimer);
        String negativeButtonTxt = mContext.getResources().getString(R.string.negativeButtonAlertDialogSupprimer);

        ldcViewHolder.bind(data.get(i));
        ldcViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(data.get(i));
            }
        });

        ldcViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle(title);

                alertDialog.setMessage(Html.fromHtml(alertMsg + " <b> " + data.get(i).getNom() + "</b>"));
                alertDialog.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                if(data.get(i).getId() != 1) {
                    alertDialog.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ListeCourses listeCourses = listeCoursesDao.getListeCoursesById(data.get(i).getId());
                            for (Produit produit : listeCourses.getProduitsPris()) {
                                Toast.makeText(mContext, "" + produit.getId(), Toast.LENGTH_SHORT).show();
                                produitDao.deleteProductById(produit.getId());
                            }
                            for (Produit produit : listeCourses.getProduitsAPrendre()) {
                                Toast.makeText(mContext, "" + produit.getId(), Toast.LENGTH_SHORT).show();
                                produitDao.deleteProductById(produit.getId());
                            }

                            listeCoursesDao.deleteListeCoursesById(data.get(i).getId());
                        }
                    });
                }
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    public void setData(List<ListeCourses> newData) {

        if(data != null)
        {
            LDCDiffCallback produitDiffCallback = new LDCDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(produitDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else{
            data = newData;
        }
    }

    public class LDCViewHolder extends RecyclerView.ViewHolder {

        private TextView ldcName;

        public LDCViewHolder(@NonNull View itemView) {
            super(itemView);

            ldcName = itemView.findViewById(R.id.ldc_name);
        }

        public void bind(ListeCourses value){
            if(value.getNom().equals(mContext.getResources().getString(R.string.label_titre_listecourse_auto_generee_automatique))){
                ldcName.setTypeface(null, Typeface.ITALIC);
     ///           itemView.setBackgroundColor(Color.LTGRAY);
            }
            ldcName.setText(value.getNom());
        }
    }


    public class LDCDiffCallback extends DiffUtil.Callback{

        private final List<ListeCourses> oldLDC, newLDC;

        LDCDiffCallback(List<ListeCourses> oldLDC, List<ListeCourses> newLDC) {
            this.oldLDC = oldLDC;
            this.newLDC = newLDC;
        }

        @Override
        public int getOldListSize() {
            return oldLDC.size();
        }

        @Override
        public int getNewListSize() {
            return newLDC.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return oldLDC.get(i).getId() == newLDC.get(i1).getId();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldLDC.get(i).equals(newLDC.get(i1));
        }
    }
}
