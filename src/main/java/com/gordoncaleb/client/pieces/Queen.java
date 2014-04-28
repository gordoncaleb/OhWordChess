package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public class Queen {
	public static int[][] QUEENMOVES = { { 1, 1, -1, -1, 1, -1, 0, 0 }, { 1, -1, 1, -1, 0, 0, 1, -1 } };

	private Queen() {

	}

	public static String getName() {
		return "Queen";
	}

	public static String getStringID() {
		return "Q";
	}

	public static void generateMoves(Piece piece, Board board, ArrayList<Long> moves) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;

		int i = 1;
		for (int d = 0; d < 8; d++) {
			nextRow = row + i * QUEENMOVES[0][d];
			nextCol = col + i * QUEENMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			while (pieceStatus == PositionStatus.NO_PIECE) {

				moves.add(Move.moveLong(row, col, nextRow, nextCol, 0, MoveNote.NONE));

				i++;
				nextRow = row + i * QUEENMOVES[0][d];
				nextCol = col + i * QUEENMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

			}

			if (pieceStatus == PositionStatus.ENEMY) {
				moves.add(Move.moveLong(row, col, nextRow, nextCol, board.getPieceValue(nextRow, nextCol), MoveNote.NONE,
						board.getPiece(nextRow, nextCol)));
			}

			i = 1;
		}
	}

	public static void generateValidMoves(Piece piece, Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		int nextRow;
		int nextCol;
		int value;
		PositionStatus pieceStatus;

		int i = 1;
		for (int d = 0; d < 8; d++) {
			nextRow = row + i * QUEENMOVES[0][d];
			nextCol = col + i * QUEENMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			while (pieceStatus == PositionStatus.NO_PIECE) {

				if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {

					if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
						value = -Values.QUEEN_VALUE >> 1;
					} else {
						value = 0;
					}

					validMoves.add(Move.moveLong(row, col, nextRow, nextCol, value, MoveNote.NONE));
				}

				i++;
				nextRow = row + i * QUEENMOVES[0][d];
				nextCol = col + i * QUEENMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

			}

			if (pieceStatus == PositionStatus.ENEMY) {
				if (piece.isValidMove(nextRow, nextCol, nullMoveInfo)) {

					value = board.getPieceValue(nextRow, nextCol);

					if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
						value -= Values.QUEEN_VALUE >> 1;
					}

					Long moveLong = Move.moveLong(row, col, nextRow, nextCol, value, MoveNote.NONE, board.getPiece(nextRow, nextCol));
					validMoves.add(moveLong);
				}
			}

			i = 1;
		}

	}

	public static void main(String[] args) {
		long[] nullMoveInfo = new long[3];

		nullMoveInfo[1] = -1L;

		Piece queen = new Piece(PieceID.QUEEN, Side.WHITE, 6, 0, false);
		long piece = queen.getBit();
		long kingBitBoard = BitBoard.getMask(1, 0);

		long friendly = kingBitBoard | BitBoard.getMask(1, 5);// |
																// BitBoard.getMask(4,
																// 0);
		long enemy = piece | BitBoard.getMask(4, 4);
		long bb = friendly | enemy;

		long updown = ~bb;
		long left = 0xFEFEFEFEFEFEFEFEL & ~bb;
		long right = 0x7F7F7F7F7F7F7F7FL & ~bb;

		System.out.println("pos\n" + BitBoard.printBitBoard(bb));

		long kingCheckVectors = King.getKingCheckVectors(kingBitBoard, updown, left, right);

		System.out.println("king check\n" + BitBoard.printBitBoard(kingCheckVectors));

		queen.getNullMoveInfo(null, nullMoveInfo, updown, left, right, kingBitBoard, kingCheckVectors, friendly);

		System.out.println("king\n" + BitBoard.printBitBoard(kingBitBoard));

		System.out.println("[0]\n" + BitBoard.printBitBoard(nullMoveInfo[0]));
		System.out.println("[1]\n" + BitBoard.printBitBoard(nullMoveInfo[1]));
		System.out.println("[2]\n" + BitBoard.printBitBoard(nullMoveInfo[2]));
	}

	public static void getNullMoveInfo(Piece piece, Board board, long[] nullMoveInfo, long updown, long left, long right, long kingBitBoard,
			long kingCheckVectors, long friendly) {

		int row = piece.getRow();
		int col = piece.getCol();
		long bitPiece = piece.getBit();

		// up ------------------------------------------------------------
		long temp = bitPiece;
		long temp2 = bitPiece;
		int r = row;
		int c = col;
		long attackVector = 0;

		while ((temp2 = (temp2 >>> 8 & updown)) != 0) {
			attackVector |= temp2;
			temp = temp2;
			r--;
		}

		temp = temp >>> 8;
		nullMoveInfo[0] |= attackVector | temp;

		// check to see if king collision is possible
		if ((BitBoard.getColMask(c) & kingBitBoard) != 0) {

			if ((temp & kingBitBoard) != 0) {
				nullMoveInfo[1] &= attackVector | bitPiece;
				nullMoveInfo[2] |= temp >>> 8;
			} else {
				if ((temp & friendly) != 0) {
					temp = temp >>> 8;
					if ((temp & kingCheckVectors) != 0) {
						board.getPiece(r - 1, c).setBlockingVector(BitBoard.getColMask(c));
					}
				}
			}
		}

		// down-----------------------------------------------------------
		temp = bitPiece;
		temp2 = bitPiece;
		r = row;
		attackVector = 0;

		while ((temp2 = (temp2 << 8 & updown)) != 0) {
			attackVector |= temp2;
			temp = temp2;
			r++;
		}

		temp = temp << 8;
		nullMoveInfo[0] |= attackVector | temp;

		// check to see if king collision is possible
		if ((BitBoard.getColMask(c) & kingBitBoard) != 0) {

			if ((temp & kingBitBoard) != 0) {
				nullMoveInfo[1] &= attackVector | bitPiece;
				nullMoveInfo[2] |= temp << 8;
			} else {
				if ((temp & friendly) != 0) {
					temp = temp << 8;
					if ((temp & kingCheckVectors) != 0) {
						board.getPiece(r + 1, c).setBlockingVector(BitBoard.getColMask(c));
					}
				}
			}
		}

		// going westward -----------------------------------------------------
		if ((bitPiece & 0x0101010101010101L) == 0) {

			// west
			temp = bitPiece;
			temp2 = bitPiece;
			r = row;
			attackVector = 0;

			while ((temp2 = (temp2 >>> 1 & left)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				c--;
			}

			temp = temp >>> 1;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getRowMask(r) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp >>> 1;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp >>> 1;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r, c - 1).setBlockingVector(BitBoard.getRowMask(r));
						}
					}
				}
			}

			// northwest
			temp2 = bitPiece;
			temp = bitPiece;
			c = col;
			attackVector = 0;

			while ((temp2 = (temp2 >>> 9 & left)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				r--;
				c--;
			}
			temp = temp >>> 9;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getNegSlope(r, c) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp >>> 9;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp >>> 9;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r - 1, c - 1).setBlockingVector(BitBoard.getNegSlope(r, c));
						}
					}
				}
			}

			// south west
			temp = bitPiece;
			temp2 = bitPiece;
			r = row;
			c = col;
			attackVector = 0;

			while ((temp2 = (temp2 << 7 & left)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				r++;
				c--;
			}

			temp = temp << 7;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getPosSlope(r, c) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp << 7;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp << 7;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r + 1, c - 1).setBlockingVector(BitBoard.getPosSlope(r, c));
						}
					}
				}
			}

		}

		// going eastward
		if ((bitPiece & 0x8080808080808080L) == 0) {

			// east
			temp = bitPiece;
			temp2 = bitPiece;
			r = row;
			c = col;
			attackVector = 0;

			while ((temp2 = (temp2 << 1 & right)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				c++;
			}

			temp = temp << 1;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getRowMask(r) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp << 1;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp << 1;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r, c + 1).setBlockingVector(BitBoard.getRowMask(r));
						}
					}
				}
			}

			// northeast
			temp = bitPiece;
			temp2 = bitPiece;
			c = col;
			attackVector = 0;

			while ((temp2 = (temp2 >> 7 & right)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				c++;
				r--;
			}

			temp = temp >> 7;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getPosSlope(r, c) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp >> 7;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp >> 7;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r - 1, c + 1).setBlockingVector(BitBoard.getPosSlope(r, c));
						}
					}
				}
			}

			// southeast
			temp = bitPiece;
			temp2 = bitPiece;
			c = col;
			r = row;
			attackVector = 0;

			while ((temp2 = (temp2 << 9 & right)) != 0) {
				attackVector |= temp2;
				temp = temp2;
				c++;
				r++;
			}

			temp = temp << 9;
			nullMoveInfo[0] |= attackVector | temp;

			// check to see if king collision is possible
			if ((BitBoard.getNegSlope(r, c) & kingBitBoard) != 0) {

				if ((temp & kingBitBoard) != 0) {
					nullMoveInfo[1] &= attackVector | bitPiece;
					nullMoveInfo[2] |= temp << 9;
				} else {
					if ((temp & friendly) != 0) {
						temp = temp << 9;
						if ((temp & kingCheckVectors) != 0) {
							board.getPiece(r + 1, c + 1).setBlockingVector(BitBoard.getNegSlope(r, c));
						}
					}
				}
			}

		}

	}

	public static void getNullMoveInfo(Piece piece, Board board, long[] nullMoveInfo) {
		long bitAttackVector = 0;
		long bitAttackCompliment = 0;
		boolean inCheck = false;
		Piece blockingPiece;

		int row = piece.getRow();
		int col = piece.getCol();
		Side player = piece.getSide();

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;

		long bitPosition = piece.getBit();

		int i = 1;
		for (int d = 0; d < 8; d++) {
			nextRow = row + i * QUEENMOVES[0][d];
			nextCol = col + i * QUEENMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus == PositionStatus.OFF_BOARD) {
				continue;
			}

			while (pieceStatus == PositionStatus.NO_PIECE) {
				bitAttackVector |= BitBoard.getMask(nextRow, nextCol);
				i++;
				nextRow = row + i * QUEENMOVES[0][d];
				nextCol = col + i * QUEENMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);
			}

			if (pieceStatus != PositionStatus.OFF_BOARD) {
				bitAttackVector |= BitBoard.getMask(nextRow, nextCol);
			}

			if (pieceStatus == PositionStatus.ENEMY) {

				blockingPiece = board.getPiece(nextRow, nextCol);

				if (blockingPiece.getPieceID() == PieceID.KING) {
					nullMoveInfo[1] &= (bitAttackVector | bitPosition);
					inCheck = true;
				}

				i++;
				nextRow = row + i * QUEENMOVES[0][d];
				nextCol = col + i * QUEENMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

				while (pieceStatus == PositionStatus.NO_PIECE) {
					bitAttackCompliment |= BitBoard.getMask(nextRow, nextCol);
					i++;
					nextRow = row + i * QUEENMOVES[0][d];
					nextCol = col + i * QUEENMOVES[1][d];
					pieceStatus = board.checkPiece(nextRow, nextCol, player);
				}

				if (pieceStatus != PositionStatus.OFF_BOARD) {
					if (board.getPieceID(nextRow, nextCol) == PieceID.KING && board.getPiece(nextRow, nextCol).getSide() != player) {
						blockingPiece.setBlockingVector(bitAttackCompliment | bitAttackVector | bitPosition);
					}
				}

				if (pieceStatus == PositionStatus.FRIEND) {
					bitAttackCompliment |= BitBoard.getMask(nextRow, nextCol);
				}

			}

			nullMoveInfo[0] |= bitAttackVector;

			if (inCheck) {
				nullMoveInfo[2] |= bitAttackCompliment;
				inCheck = false;
			}

			bitAttackCompliment = 0;
			bitAttackVector = 0;

			i = 1;
		}

	}

}
