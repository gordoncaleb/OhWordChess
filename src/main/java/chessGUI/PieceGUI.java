package chessGUI;

import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public interface PieceGUI {
	public PieceID getPieceID();
	public Side getPlayer();
	public void showChessPiece(PieceID pieceID, Side player);
	public void showAsSelected(boolean selected);
}
