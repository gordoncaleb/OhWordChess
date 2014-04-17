package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;

public class Knight extends Piece {

	private static int[][] KNIGHTMOVES = { { 2, 2, -2, -2, 1, -1, 1, -1 }, { 1, -1, 1, -1, 2, 2, -2, -2 } };

	public Knight(PieceID id, Side player, int row, int col, boolean moved) {
		super(id, player, row, col, moved);
		// TODO Auto-generated constructor stub
	}

	public PieceID getPieceID() {
		return PieceID.KNIGHT;
	}

	public String getName() {
		return "Knight";
	}

	public String getStringID() {
		return "N";
	}

	@Override
	public void generateValidMoves(Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {
		int nextRow;
		int nextCol;
		int value;
		int bonus;
		int myValue = board.getPieceValue(row, col);
		PositionStatus pieceStatus;
		Long moveLong;

		for (int i = 0; i < 8; i++) {
			nextRow = row + KNIGHTMOVES[0][i];
			nextCol = col + KNIGHTMOVES[1][i];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus != PositionStatus.OFF_BOARD) {
				bonus = PositionBonus.getKnightMoveBonus(row, col, nextRow, nextCol, player);

				if (pieceStatus == PositionStatus.NO_PIECE) {
					if (isValidMove(nextRow, nextCol, nullMoveInfo)) {

						if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
							value = -myValue >> 1;
						} else {
							value = bonus;
						}

						moveLong = Move.moveLong(row, col, nextRow, nextCol, value);
						validMoves.add(moveLong);
					}
				}

				if (pieceStatus == PositionStatus.ENEMY) {
					if (isValidMove(nextRow, nextCol, nullMoveInfo)) {
						value = board.getPieceValue(nextRow, nextCol);

						if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
							value -= myValue >> 1;
						} else {
							value += bonus;
						}

						moveLong = Move.moveLong(row, col, nextRow, nextCol, value, MoveNote.NONE, board.getPiece(nextRow, nextCol));
						validMoves.add(moveLong);
					}
				}

			}
		}

	}

	@Override
	public void getNullMoveInfo(Board board, long[] nullMoveInfo, long updown, long left, long right, long kingBitBoard, long kingCheckVectors,
			long friendly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNullMoveInfo(Board board, long[] nullMoveInfo) {

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;

		for (int i = 0; i < 8; i++) {
			nextRow = row + KNIGHTMOVES[0][i];
			nextCol = col + KNIGHTMOVES[1][i];

			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus != PositionStatus.OFF_BOARD) {

				if (board.getPieceID(nextRow, nextCol) == PieceID.KING && pieceStatus == PositionStatus.ENEMY) {
					nullMoveInfo[1] &= getBit();
				}

				nullMoveInfo[0] |= BitBoard.getMask(nextRow, nextCol);
			}
		}

	}

	@Override
	public Piece getCopy() {
		return new Knight(id, player, row, col, moved);
	}

}
