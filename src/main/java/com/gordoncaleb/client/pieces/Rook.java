package com.gordoncaleb.client.pieces;

import java.util.ArrayList;

import com.gordoncaleb.client.chess.BitBoard;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;

public class Rook extends Piece {

	private static int[][] ROOKMOVES = { { 1, -1, 0, 0 }, { 0, 0, 1, -1 } };

	public Rook(PieceID id, Side player, int row, int col, boolean moved) {
		super(id, player, row, col, moved);
	}

	public PieceID getPieceID() {
		return PieceID.ROOK;
	}

	public String getName() {
		return "Rook";
	}

	public String getStringID() {
		return "R";
	}

	public void generateMoves(Board board, ArrayList<Long> moves) {
		int nextRow;
		int nextCol;
		int value;
		PositionStatus pieceStatus;
		Long moveLong;

		int i = 1;
		for (int d = 0; d < 4; d++) {
			nextRow = row + i * ROOKMOVES[0][d];
			nextCol = col + i * ROOKMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			while (pieceStatus == PositionStatus.NO_PIECE) {

				if (!moved && !board.kingHasMoved(player)) {
					value = Values.CASTLE_ABILITY_LOST_VALUE;
				} else {
					value = 0;
				}

				moveLong = Move.moveLong(row, col, nextRow, nextCol, value);

				moves.add(moveLong);

				i++;
				nextRow = row + i * ROOKMOVES[0][d];
				nextCol = col + i * ROOKMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

			}

			if (pieceStatus == PositionStatus.ENEMY) {

				if (!moved && !board.kingHasMoved(player)) {
					value = board.getPieceValue(nextRow, nextCol) + Values.CASTLE_ABILITY_LOST_VALUE;
				} else {
					value = board.getPieceValue(nextRow, nextCol);
				}

				moveLong = Move.moveLong(row, col, nextRow, nextCol, value, MoveNote.NONE, board.getPiece(nextRow, nextCol));

				moves.add(moveLong);

			}

			i = 1;
		}

	}

	public void generateValidMoves(Board board, long[] nullMoveInfo, long[] posBitBoard, ArrayList<Long> validMoves) {
		int nextRow;
		int nextCol;
		int value;
		PositionStatus pieceStatus;
		Long moveLong;

		int i = 1;
		for (int d = 0; d < 4; d++) {
			nextRow = row + i * ROOKMOVES[0][d];
			nextCol = col + i * ROOKMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			while (pieceStatus == PositionStatus.NO_PIECE) {

				if (isValidMove(nextRow, nextCol, nullMoveInfo)) {

					if (!moved && !board.kingHasMoved(player)) {
						value = Values.CASTLE_ABILITY_LOST_VALUE;
					} else {
						value = 0;
					}

					if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
						value -= Values.ROOK_VALUE >> 1;
					}

					moveLong = Move.moveLong(row, col, nextRow, nextCol, value);

					validMoves.add(moveLong);
				}

				i++;
				nextRow = row + i * ROOKMOVES[0][d];
				nextCol = col + i * ROOKMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

			}

			if (pieceStatus == PositionStatus.ENEMY) {
				if (isValidMove(nextRow, nextCol, nullMoveInfo)) {

					if (!moved && !board.kingHasMoved(player)) {
						value = board.getPieceValue(nextRow, nextCol) + Values.CASTLE_ABILITY_LOST_VALUE;
					} else {
						value = board.getPieceValue(nextRow, nextCol);
					}

					if ((nullMoveInfo[0] & BitBoard.getMask(nextRow, nextCol)) != 0) {
						value -= Values.ROOK_VALUE >> 1;
					}

					moveLong = Move.moveLong(row, col, nextRow, nextCol, value, MoveNote.NONE, board.getPiece(nextRow, nextCol));

					validMoves.add(moveLong);
				}
			}

			i = 1;
		}

	}

//	public static void main(String[] args) {
//		String b = "R0,N0,B0,Q0,K0,__,N0,R0," + "P0,P0,P0,P0,__,P0,P0,P0," + "__,__,__,__,__,__,__,__," + "__,__,__,__,P1,__,__,__,"
//				+ "__,B0,__,__,__,__,__,__," + "__,__,n0,p1,__,__,__,__," + "p0,p0,p0,__,p0,p0,p0,p0," + "r0,__,b0,q0,k0,b0,n0,r0,";
//
//		b = "<board>\n<setup>\n" + b + "</setup>\n<turn>WHITE</turn>\n</board>";
//
//		Board board = XMLParser.XMLToBoard(b);
//
//		Piece piece = board.getPiece(4, 1);
//		long[] nullMoveInfo = new long[3];
//
//		Side turn = board.getTurn();
//		long updown = ~(board.getAllPosBitBoard()[0] | board.getAllPosBitBoard()[1]);
//		long friendly = board.getAllPosBitBoard()[turn.ordinal()];
//		long kingBitBoard = board.getPosBitBoard()[PieceID.KING.ordinal()][turn.ordinal()];
//
//		long left = 0xFEFEFEFEFEFEFEFEL & updown;
//		long right = 0x7F7F7F7F7F7F7F7FL & updown;
//
//		long kingCheckVectors = King.getKingCheckVectors(board.getPosBitBoard()[PieceID.KING.ordinal()][turn.ordinal()], updown, left, right);
//
//		piece.getNullMoveInfo(board, nullMoveInfo, updown, left, right, kingBitBoard, kingCheckVectors, friendly);
//
//		System.out.println("updown\n" + BitBoard.printBitBoard(updown));
//		System.out.println("left\n" + BitBoard.printBitBoard(left));
//		System.out.println("right\n" + BitBoard.printBitBoard(right));
//
//		System.out.println("kingCheckVectors\n" + BitBoard.printBitBoard(kingCheckVectors));
//
//		System.out.println(BitBoard.printBitBoard(nullMoveInfo[0]));
//
//	}

	public void getNullMoveInfo(Board board, long[] nullMoveInfo, long updown, long left, long right, long kingBitBoard, long kingCheckVectors,
			long friendly) {

		long bitPiece = getBit();

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

		}

	}

	public void getNullMoveInfo(Board board, long[] nullMoveInfo) {
		long bitAttackVector = 0;
		long bitAttackCompliment = 0;
		boolean inCheck = false;
		Piece blockingPiece;

		int nextRow;
		int nextCol;
		PositionStatus pieceStatus;

		long bitPosition = getBit();

		int i = 1;
		for (int d = 0; d < 4; d++) {
			nextRow = row + i * ROOKMOVES[0][d];
			nextCol = col + i * ROOKMOVES[1][d];
			pieceStatus = board.checkPiece(nextRow, nextCol, player);

			if (pieceStatus == PositionStatus.OFF_BOARD) {
				continue;
			}

			while (pieceStatus == PositionStatus.NO_PIECE) {
				bitAttackVector |= BitBoard.getMask(nextRow, nextCol);
				i++;
				nextRow = row + i * ROOKMOVES[0][d];
				nextCol = col + i * ROOKMOVES[1][d];
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
				nextRow = row + i * ROOKMOVES[0][d];
				nextCol = col + i * ROOKMOVES[1][d];
				pieceStatus = board.checkPiece(nextRow, nextCol, player);

				while (pieceStatus == PositionStatus.NO_PIECE) {
					bitAttackCompliment |= BitBoard.getMask(nextRow, nextCol);
					i++;
					nextRow = row + i * ROOKMOVES[0][d];
					nextCol = col + i * ROOKMOVES[1][d];
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

	@Override
	public Piece getCopy() {
		return new Rook(id, player, row, col, moved);
	}

}
