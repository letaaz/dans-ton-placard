package com.danstonplacard;

import com.danstonplacard.database.model.Piece;

/**
 * Class that represents the defaults products that are displayed during a search in the suggested products bar.
 */
public class PieceIcone {

    private int idDrawable;
    private Piece piece;

    public PieceIcone(int idDrawable, Piece piece)
    {
        this.idDrawable = idDrawable;
        this.piece = piece;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
