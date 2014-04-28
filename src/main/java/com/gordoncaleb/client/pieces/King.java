package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;

public class King {
	private static int[][] KINGMOVES = { { 1, 1, -1, -1, 1, -1, 0, 0 }, { 1, -1, 1, -1, 0, 0, 1, -1 } };

	private King() {

	}

	public static String getName() {
		return "King";
	}

	public static String getStringID() {
		return "K";
	}

	// @Override
	// public void generateMoves(Board board, ArrayList<Long> moves) {
	// int nextRow;
	// int nextCol;
	// PositionStatus pieceStatus;
	//
	// int moveVal = 0;
	// if (!moved && (!board.farRookHasMoved(player) ||
	// !board.nearRookHasMoved(player))) {
	// moveVal = Values.CASTLE_ABILITY_LOST_VALUE;
	// }
	//
	// for (int d = 0; d < 8; d++) {
	// nextRow = row + KINGMOVES[0][d];
	// nextCol = col + KINGMOVES[1][d];
	// pieceStatus = board.checkPiece(nextRow, nextCol, player);
	//
	// if (pieceStatus == PositionStatus.NO_PIECE) {
	// moves.add(Move.moveLong(row, col, nextRow, nextCol, moveVal,
	// MoveNote.NONE));
	// }
	//
	// if (pieceStatus == PositionStatus.ENEMY) {
	// moves.add(Move.moveLong(row, col, nextRow, nextCol,
	// board.getPieceValue(nextRow, nextCol) + moveVal, MoveNote.NONE,
	// board.getPiece(nextRow, nextCol)));
	// }
	//
	// }
	//
	// // long allPosBitBoard = posBitBoard[0] | posBitBoard[1];
	// //
	// // if (!board.isInCheck()) {
	// // // add possible castle move
	// // if (canCastleFar(p, board, player, nullMoveInfo, allPosBitBoard)) {
	// // if (isValidMove(currentRow, 2, nullMoveInfo)) {
	// // if (currentCol > 3) {
	// // validMoves.add(Move.moveLong(currentRow, currentCol, currentRow, 2,
	// // Values.FAR_CASTLE_VALUE, MoveNote.CASTLE_FAR));
	// // } else {
	// // validMoves.add(Move.moveLong(currentRow,
	// // board.getRookStartingCol(player, 0), currentRow, 3,
	// // Values.FAR_CASTLE_VALUE,
	// // MoveNote.CASTLE_FAR));
	// // }
	// // }
	// // }
	// //
	// // if (canCastleNear(p, board, player, nullMoveInfo, allPosBitBoard)) {
	// // if (isValidMove(currentRow, 6, nullMoveInfo)) {
	// // if (currentCol < 5) {
	// // validMoves.add(Move.moveLong(currentRow, currentCol, currentRow, 6,
	// // Values.NEAR_CASTLE_VALUE, MoveNote.CASTLE_NEAR));
	// // } else {
	// // validMoves.add(Move.moveLong(currentRow,
	// // board.getRookStartingCol(player, 1), currentRow, 5,
	// // Values.NEAR_CASTLE_VALUE,
	// // MoveNote.CASTLE_NEAR));
	// // }
	// // }
	// // }
	// // }
	// }

	public static void generateValidMoves(Piece piece, Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();
		boolean moved = piece.hasMoved();

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;
		Long moveLong;

		for (int d = 0; d < 8; d++) {
			nextRow = row + KINGMOVES[0][d];
			nextCol = col + KINGMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus == PositionStatus.NO_PIECE) {

				if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {
					if (!moved && (!board.farRookHasMoved(player) || !board.nearRookHasMoved(player))) {
						// The player loses points for losing the ability to
						// castle
						moveLong = Move.moveLong(row, col, nextRow, nextCol, Values.CASTLE_ABILITY_LOST_VALUE);
					} else {
						moveLong = Move.moveLong(row, col, nextRow, nextCol, 0, MoveNote.NONE);
					}

					validMoves.add(moveLong);
				}
			}

			if (pieceStatus == PositionStatus.ENEMY) {
				if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {
					moveLong = Move.moveLong(row, col, nextRow, nextCol, board.getPieceValue(nextRow, nextCol), MoveNote.NONE,
							board.getPiece(nextRow, nextCol));
					validMoves.add(moveLong);
				}
			}

		}

		long allPosBitBoard = posBitBoard[0] | posBitBoard[1];

		if (!board.isInCheck()) {
			// add possible castle move
			if (canCastleFar(piece, board, player, nullMoveInfo, allPosBitBoard)) {
				if (piece.isValidMove(row, 2, nullMoveInfo)) {
					if (col > 3) {
						validMoves.add(Move.moveLong(row, col, row, 2, Values.FAR_CASTLE_VALUE, MoveNote.CASTLE_FAR));
					} else {
						validMoves.add(Move.moveLong(row, board.getRookStartingCol(player, 0), row, 3, Values.FAR_CASTLE_VALUE, MoveNote.CASTLE_FAR));
					}
				}
			}

			if (canCastleNear(piece, board, player, nullMoveInfo, allPosBitBoard)) {
				if (piece.isValidMove(row, 6, nullMoveInfo)) {
					if (col < 5) {
						validMoves.add(Move.moveLong(row, col, row, 6, Values.NEAR_CASTLE_VALUE, MoveNote.CASTLE_NEAR));
					} else {
						validMoves
								.add(Move.moveLong(row, board.getRookStartingCol(player, 1), row, 5, Values.NEAR_CASTLE_VALUE, MoveNote.CASTLE_NEAR));
					}
				}
			}
		}

	}

	public static void getNullMoveInfo(Piece piece, Board board, long[] nullMoveInfo) {
		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		for (int i = 0; i < 8; i++) {
			if (board.checkPiece(row + KINGMOVES[0][i], col + KINGMOVES[1][i], player) != PositionStatus.OFF_BOARD) {
				nullMoveInfo[0] |= BitBoard.getMask(row + KINGMOVES[0][i], col + KINGMOVES[1][i]);
			}
		}

	}

	public static long getKingCheckVectors(long king, long updown, long left, long right) {
		long temp = king;

		long checkVectors = king;

		// up
		while ((temp = (temp >>> 8 & updown)) != 0) {
			checkVectors |= temp;
		}

		temp = king;

		// down
		while ((temp = (temp << 8 & updown)) != 0) {
			checkVectors |= temp;
		}

		temp = king;

		// going left
		if ((king & 0x0101010101010101L) == 0) {

			while ((temp = (temp >>> 1 & left)) != 0) {
				checkVectors |= temp;
			}

			temp = king;

			while ((temp = (temp >>> 9 & left)) != 0) {
				checkVectors |= temp;
			}

			temp = king;

			while ((temp = (temp << 7 & left)) != 0) {
				checkVectors |= temp;
			}

			temp = king;

		}

		// going right
		if ((king & 0x8080808080808080L) == 0) {

			while ((temp = (temp << 1 & right)) != 0) {
				checkVectors |= temp;
			}

			temp = king;

			while ((temp = (temp >> 7 & right)) != 0) {
				checkVectors |= temp;
			}

			temp = king;

			while ((temp = (temp << 9 & right)) != 0) {
				checkVectors |= temp;
			}

		}

		return checkVectors;
	}

	public static boolean isValidMove(int toRow, int toCol, long[] nullMoveInfo) {
		long mask = BitBoard.getMask(toRow, toCol);

		// String nullmove0 = BitBoard.printBitBoard(nullMoveInfo[0]);
		// String nullmove1 = BitBoard.printBitBoard(nullMoveInfo[1]);
		// String nullmove2 = BitBoard.printBitBoard(nullMoveInfo[2]);

		if ((mask & (nullMoveInfo[0] | nullMoveInfo[2])) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean canCastleFar(Piece piece, Board board, Side player, long[] nullMoveInfo, long allPosBitBoard) {

		int row = piece.getRow();
		int col = piece.getCol();

		if (board.kingHasMoved(player) || board.farRookHasMoved(player)) {
			return false;
		}

		long kingToCastleMask = BitBoard.getCastleMask(col, 2, player);

		int rookCol = board.getRookStartingCol(player, 0);
		long rookToCastleMask = BitBoard.getCastleMask(rookCol, 3, player);

		allPosBitBoard ^= BitBoard.getMask(row, rookCol) | piece.getBit();

		if ((kingToCastleMask & nullMoveInfo[0]) == 0) {
			if (((kingToCastleMask | rookToCastleMask) & allPosBitBoard) == 0) {
				return true;
			}
		}

		return false;

	}

	public static boolean canCastleNear(Piece piece, Board board, Side player, long[] nullMoveInfo, long allPosBitBoard) {

		int row = piece.getRow();
		int col = piece.getCol();

		if (board.kingHasMoved(player) || board.nearRookHasMoved(player)) {
			return false;
		}

		long kingToCastleMask = BitBoard.getCastleMask(col, 6, player);

		int rookCol = board.getRookStartingCol(player, 1);
		long rookToCastleMask = BitBoard.getCastleMask(rookCol, 5, player);

		allPosBitBoard ^= BitBoard.getMask(row, rookCol) | piece.getBit();

		// System.out.println(BitBoard.printBitBoard(kingToCastleMask));
		// System.out.println(BitBoard.printBitBoard(rookToCastleMask));
		// System.out.println(BitBoard.printBitBoard(allPosBitBoard));

		if ((kingToCastleMask & nullMoveInfo[0]) == 0) {
			if (((kingToCastleMask | rookToCastleMask) & allPosBitBoard) == 0) {
				return true;
			}
		}

		return false;
	}

}
