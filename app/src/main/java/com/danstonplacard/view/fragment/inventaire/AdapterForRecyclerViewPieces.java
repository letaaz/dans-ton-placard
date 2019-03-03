package com.danstonplacard.view.fragment.inventaire;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.danstonplacard.PieceIcone;
import com.danstonplacard.R;
import com.danstonplacard.database.model.Piece;

import java.util.List;

/**
 * Adapt the recycle view of the parts screen (Contains the images / icons of each room)
 */
public class AdapterForRecyclerViewPieces extends RecyclerView.Adapter<AdapterForRecyclerViewPieces.PiecesViewHolder> {

    private Context mContext;
    private List<PieceIcone> mPiecesIntDrawables;

    public AdapterForRecyclerViewPieces(Context mContext, List<PieceIcone> mPiecesIntDrawables) {
        this.mContext = mContext;
        this.mPiecesIntDrawables = mPiecesIntDrawables;
    }

    @Override
    public PiecesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem_pieces_recyclercardview, parent, false);
        return new PiecesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final PiecesViewHolder holder, int position) {
        holder.mPieceImage.setImageDrawable(mContext.getResources().getDrawable(mPiecesIntDrawables.get(position).getIdDrawable()));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            /**
             * When users click on image of Room - Launch the Fragment corresponding to the Room
             * @param view View that contains images
             */
            @Override
            public void onClick(View view) {
                String piece = Piece.pieceToString(mPiecesIntDrawables.get(position).getPiece());

                // Start Fragment PieceFragment
                FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.root_inventaire_frame, PieceFragment.newInstance(piece), "PieceFragment");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack("toPiece");
                trans.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPiecesIntDrawables.size();
    }

    class PiecesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPieceImage;
        private CardView mCardView;

        PiecesViewHolder(View itemView) {
            super(itemView);
            mPieceImage = itemView.findViewById(R.id.ivImage);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}


