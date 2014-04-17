package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Side;

public abstract class Piece {

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

		if (piece.getRow() == row && piece.getCol() == col
				&& piece.getSide() == player
				&& piece.getPieceID() == this.getPieceID()) {
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

	public abstract String getStringID();

	public abstract String getName();

	public abstract void generateValidMoves(Board board, long[] nullMoveInfo,
			long[] posBitBoard, ArrayList<Long> validMoves);

	public abstract void getNullMoveInfo(Board board, long[] nullMoveInfo,
			long updown, long left, long right, long kingBitBoard,
			long kingCheckVectors, long friendly);

	public abstract void getNullMoveInfo(Board board, long[] nullMoveBitBoards);

	public abstract Piece getCopy();

}
