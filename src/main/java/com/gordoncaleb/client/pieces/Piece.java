package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Side;

public class Piece {

	public static enum PieceID {
		ROOK, KNIGHT, BISHOP, QUEEN, KING, PAWN
	}

	protected int row;
	protected int col;
	protected Side player;
	protected boolean moved;
	protected long blockingVector;
	protected PieceID id;

	public Piece(PieceID id, Side player, int row, int col, boolean moved) {
		this.id = id;
		this.moved = moved;
		this.player = player;
		this.row = row;
		this.col = col;
		this.blockingVector = BitBoard.ALL_ONES;

		if (id == null) {
			System.out.println("WTF");
		}

	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public long getBit() {
		return BitBoard.getMask(row, col);
	}

	public void setPos(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void move(long newMove) {
		setPos(Move.getToRow(newMove), Move.getToCol(newMove));
		moved = true;
	}

	public void reverseMove(long newMove) {
		setPos(Move.getFromRow(newMove), Move.getFromCol(newMove));
	}

	public Side getSide() {
		return player;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public void setBlockingVector(long blockingVector) {
		this.blockingVector = blockingVector;
	}

	public void clearBlocking() {
		blockingVector = BitBoard.ALL_ONES;
	}

	public long getBlockingVector() {
		return blockingVector;
	}

	public String toString() {
		String id;

		if (this.getSide() == Side.BLACK) {
			id = this.getStringID();
		} else {
			id = this.getStringID().toLowerCase();
		}

		return id;
	}

	public String toXML() {
		String xmlPiece = "";

		xmlPiece += "<piece>\n";
		xmlPiece += "<id>" + toString() + "</id>\n";
		xmlPiece += "<has_moved>" + hasMoved() + "</has_moved>\n";
		xmlPiece += "<position>" + getRow() + "," + getCol() + "</position>\n";
		xmlPiece += "</piece>\n";

		return xmlPiece;
	}

	public boolean equals(Piece piece) {

		if (piece == null) {
			return false;
		}

		if (piece.getRow() == row && piece.getCol() == col && piece.getSide() == player && piece.getPieceID() == this.getPieceID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidMove(int toRow, int toCol, long[] nullMoveInfo) {
		long mask = BitBoard.getMask(toRow, toCol);

		if ((mask & nullMoveInfo[1] & blockingVector) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public PieceID getPieceID() {
		return id;
	}

	public void setPieceID(PieceID id) {
		this.id = id;
	}

	public String getStringID() {

		switch (id) {
		case BISHOP:
			return Bishop.getStringID();
		case KING:
			return King.getStringID();
		case KNIGHT:
			return Knight.getStringID();
		case PAWN:
			return Pawn.getStringID();
		case QUEEN:
			return Queen.getStringID();
		case ROOK:
			return Rook.getStringID();
		default:
			return "";
		}

	}

	public String getName() {

		switch (id) {
		case BISHOP:
			return Bishop.getName();
		case KING:
			return King.getName();
		case KNIGHT:
			return Knight.getName();
		case PAWN:
			return Pawn.getName();
		case QUEEN:
			return Queen.getName();
		case ROOK:
			return Rook.getName();
		default:
			return "";
		}

	}

	public void generateValidMoves(Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {
		switch (id) {
		case BISHOP:
			Bishop.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		case KING:
			King.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		case KNIGHT:
			Knight.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		case PAWN:
			Pawn.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		case QUEEN:
			Queen.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		case ROOK:
			Rook.generateValidMoves(this, board, nullMoveInfo, posBitBoard, validMoves);
		}
	}

	public void getNullMoveInfo(Board board, long[] nullMoveInfo, long updown, long left, long right, long kingBitBoard, long kingCheckVectors,
			long friendly) {
		switch (id) {
		case BISHOP:
			Bishop.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right, kingBitBoard, kingCheckVectors, friendly);
		case KING:
			// King.getNullMoveInfo(this, board, nullMoveInfo, updown, left,
			// right, kingBitBoard, kingCheckVectors, friendly);
		case KNIGHT:
			// Knight.getNullMoveInfo(this, board, nullMoveInfo, updown, left,
			// right, kingBitBoard, kingCheckVectors, friendly);
		case PAWN:
			// Pawn.getNullMoveInfo(this, board, nullMoveInfo, updown, left,
			// right, kingBitBoard, kingCheckVectors, friendly);
		case QUEEN:
			Queen.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right, kingBitBoard, kingCheckVectors, friendly);
		case ROOK:
			Rook.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right, kingBitBoard, kingCheckVectors, friendly);
		}
	}

	// public void getNullMoveInfo(Board board, long[] nullMoveBitBoards) {
	// switch (id) {
	// case BISHOP:
	// Bishop.getNullMoveInfo(piece, board, nullMoveInfo);
	// case KING:
	// //King.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right,
	// kingBitBoard, kingCheckVectors, friendly);
	// case KNIGHT:
	// //Knight.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right,
	// kingBitBoard, kingCheckVectors, friendly);
	// case PAWN:
	// //Pawn.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right,
	// kingBitBoard, kingCheckVectors, friendly);
	// case QUEEN:
	// Queen.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right,
	// kingBitBoard, kingCheckVectors, friendly);
	// case ROOK:
	// Rook.getNullMoveInfo(this, board, nullMoveInfo, updown, left, right,
	// kingBitBoard, kingCheckVectors, friendly);
	// }
	// }

	public Piece getCopy() {
		return new Piece(id, player, row, col, moved);
	}

}
