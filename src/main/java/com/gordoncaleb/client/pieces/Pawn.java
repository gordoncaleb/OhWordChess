package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public class Pawn {

	private Pawn() {

	}

	public static String getName() {
		return "Pawn";
	}

	public static String getStringID() {
		return "P";
	}

	public static void generateValidMoves(Piece piece, Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {
		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();
		boolean moved = piece.hasMoved();

		int dir;
		int fifthRank;
		int value;
		int myValue = board.getPieceValue(row, col);
		Long moveLong;

		// System.out.println("pawn " + p.getCol() + " null move info");
		// System.out.println(BitBoard.printBitBoard(nullMoveInfo[0]));
		// System.out.println(BitBoard.printBitBoard(nullMoveInfo[1]));
		// System.out.println(BitBoard.printBitBoard(nullMoveInfo[2]));

		int[] lr = { 1, -1 };

		if (player == Side.WHITE) {
			dir = -1;
			fifthRank = 3;
		} else {
			dir = 1;
			fifthRank = 4;
		}

		if (board.checkPiece(row + dir, col, player) == PositionStatus.NO_PIECE) {

			if (piece.isValidMove(row + dir, col, nullMoveInfo)) {

				moveLong = Move.moveLong(row, col, row + dir, col, 0, MoveNote.NONE);

				value = PositionBonus.getPawnMoveBonus(row, col, row + dir, col, player);

				if ((row + dir) == 0 || (row + dir) == 7) {
					moveLong = Move.setNote(moveLong, MoveNote.NEW_QUEEN);
					value = Values.QUEEN_VALUE;
				}

				if ((nullMoveInfo[0] & BitBoard.getMask(row + dir, col)) != 0) {
					value = -myValue >> 1;
				}

				moveLong = Move.setValue(moveLong, value);

				validMoves.add(moveLong);

			}

			if (!moved && board.checkPiece(row + 2 * dir, col, player) == PositionStatus.NO_PIECE) {

				if (piece.isValidMove(row + 2 * dir, col, nullMoveInfo)) {

					value = PositionBonus.getPawnMoveBonus(row, col, row + 2 * dir, col, player);

					if ((nullMoveInfo[0] & BitBoard.getMask(row + 2 * dir, col)) != 0) {
						value = -myValue >> 1;
					}

					validMoves.add(Move.moveLong(row, col, row + 2 * dir, col, value, MoveNote.PAWN_LEAP));

				}
			}

		}

		// Check left and right attack angles
		for (int i = 0; i < lr.length; i++) {
			if (board.checkPiece(row + dir, col + lr[i], player) == PositionStatus.ENEMY) {

				if (piece.isValidMove(row + dir, col + lr[i], nullMoveInfo)) {

					moveLong = Move.moveLong(row, col, row + dir, col + lr[i]);

					value = PositionBonus.getPawnMoveBonus(row, col, row + dir, col, player);

					if ((row + dir) == 0 || (row + dir) == 7) {
						moveLong = Move.setNote(moveLong, MoveNote.NEW_QUEEN);
						value = Values.QUEEN_VALUE;
					}

					if ((nullMoveInfo[0] & BitBoard.getMask(row + dir, col + lr[i])) != 0) {
						value = board.getPieceValue(row + dir, col + lr[i]) - myValue >> 1;
					} else {
						value += board.getPieceValue(row + dir, col + lr[i]);
					}

					moveLong = Move.setValue(moveLong, value);

					moveLong = Move.setPieceTaken(moveLong, board.getPiece(row + dir, col + lr[i]));
					validMoves.add(moveLong);
				}

			}
		}

		// Check left and right en passant rule
		if (row == fifthRank && board.getLastMoveMade() != 0) {
			for (int i = 0; i < lr.length; i++) {
				if (board.checkPiece(fifthRank, col + lr[i], player) == PositionStatus.ENEMY) {

					if ((Move.getToCol(board.getLastMoveMade()) == (col + lr[i])) && Move.getNote(board.getLastMoveMade()) == MoveNote.PAWN_LEAP) {

						if (piece.isValidMove(row + dir, col + lr[i], nullMoveInfo)) {

							value = board.getPieceValue(fifthRank, col + lr[i]);

							if ((nullMoveInfo[0] & BitBoard.getMask(row + dir, col + lr[i])) != 0) {
								value -= myValue >> 1;
							}

							moveLong = Move.moveLong(row, col, row + dir, col + lr[i], value, MoveNote.ENPASSANT,
									board.getPiece(fifthRank, col + lr[i]));
							validMoves.add(moveLong);
						}

					}
				}
			}
		}

	}

	public static void getNullMoveInfo(Piece piece, Board board, long[] nullMoveInfo) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		int dir;
		PositionStatus pieceStatus;

		if (player == Side.WHITE) {
			dir = -1;
		} else {
			dir = 1;
		}

		pieceStatus = board.checkPiece(row + dir, col - 1, player);

		if (pieceStatus != PositionStatus.OFF_BOARD) {

			if (board.getPieceID(row + dir, col - 1) == PieceID.KING && pieceStatus == PositionStatus.ENEMY) {
				nullMoveInfo[1] &= piece.getBit();
			}

			nullMoveInfo[0] |= BitBoard.getMask(row + dir, col - 1);
		}

		pieceStatus = board.checkPiece(row + dir, col + 1, player);

		if (pieceStatus != PositionStatus.OFF_BOARD) {

			if (board.getPieceID(row + dir, col + 1) == PieceID.KING && pieceStatus == PositionStatus.ENEMY) {
				nullMoveInfo[1] &= piece.getBit();
			}

			nullMoveInfo[0] |= BitBoard.getMask(row + dir, col + 1);
		}

	}

}
