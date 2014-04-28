package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public class Knight {

	private static int[][] KNIGHTMOVES = { { 2, 2, -2, -2, 1, -1, 1, -1 }, { 1, -1, 1, -1, 2, 2, -2, -2 } };

	private Knight() {

	}

	public static String getName() {
		return "Knight";
	}

	public static String getStringID() {
		return "N";
	}

	public static void generateValidMoves(Piece piece, Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

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
					if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {

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
					if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {
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

	public static void getNullMoveInfo(Piece piece, Board board, long[] nullMoveInfo) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;

		for (int i = 0; i < 8; i++) {
			nextRow = row + KNIGHTMOVES[0][i];
			nextCol = col + KNIGHTMOVES[1][i];

			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus != PositionStatus.OFF_BOARD) {

				if (board.getPieceID(nextRow, nextCol) == PieceID.KING && pieceStatus == PositionStatus.ENEMY) {
					nullMoveInfo[1] &= piece.getBit();
				}

				nullMoveInfo[0] |= BitBoard.getMask(nextRow, nextCol);
			}
		}

	}

}
