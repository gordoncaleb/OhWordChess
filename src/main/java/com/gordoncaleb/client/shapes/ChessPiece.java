package com.gordoncaleb.client.shapes;

import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece;
import com.gordoncaleb.client.pieces.Piece.PieceID;
import com.gordoncaleb.client.util.ResourceLoader;

public class ChessPiece extends Sprite {

	private Piece piece;

	public ChessPiece(PieceID pieceId, Side side, double x, double y, double width, double height) {
		super(ResourceLoader.getImage(pieceId, side), x, y, width, height);
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

}
