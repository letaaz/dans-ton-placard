package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;

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
