package com.gordoncaleb.client.pieces;

import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public class PieceUtils {

	public static Piece fromString(String stringPiece, int row, int col) {
		Side player;

		boolean hasMoved = false;

		try {
			if (Integer.parseInt(stringPiece.substring(1, 2)) % 2 != 0) {
				hasMoved = true;
			}
		} catch (Exception e) {

		}

		if (stringPiece.charAt(0) < 'a') {
			player = Side.BLACK;
		} else {
			player = Side.WHITE;
		}

		PieceID id;
		char type = stringPiece.toUpperCase().charAt(0);

		id = charIDtoPieceID(type);

		if (id != null) {

			switch (id) {
			case ROOK:
				return new Rook(id, player, row, col, hasMoved);
			case KNIGHT:
				return new Knight(id, player, row, col, hasMoved);
			case BISHOP:
				return new Bishop(id, player, row, col, hasMoved);
			case QUEEN:
				return new Queen(id, player, row, col, hasMoved);
			case KING:
				return new King(id, player, row, col, hasMoved);
			case PAWN:
				return new Pawn(id, player, row, col, hasMoved);
			default:
				return null;
			}

		} else {
			return null;
		}

	}

	public static Piece buildPiece(PieceID id, Side player, int row, int col, boolean hasMoved) {

		switch (id) {
		case ROOK:
			return new Rook(id, player, row, col, hasMoved);
		case KNIGHT:
			return new Knight(id, player, row, col, hasMoved);
		case BISHOP:
			return new Bishop(id, player, row, col, hasMoved);
		case QUEEN:
			return new Queen(id, player, row, col, hasMoved);
		case KING:
			return new King(id, player, row, col, hasMoved);
		case PAWN:
			return new Pawn(id, player, row, col, hasMoved);
		default:
			return null;
		}
	}

	public static PieceID charIDtoPieceID(char type) {

		PieceID id;

		switch (type) {
		case 'R':
			id = PieceID.ROOK;
			break;
		case 'N':
			id = PieceID.KNIGHT;
			break;
		case 'B':
			id = PieceID.BISHOP;
			break;
		case 'Q':
			id = PieceID.QUEEN;
			break;
		case 'K':
			id = PieceID.KING;
			break;
		case 'P':
			id = PieceID.PAWN;
			break;
		default:
			id = null;
		}

		return id;
	}

}
