package com.gordoncaleb.client.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.BoardMaker;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.pieces.Piece;
import com.gordoncaleb.client.shapes.animation.transitions.LineTo;
import com.gordoncaleb.client.shapes.animation.transitions.MoveTo;
import com.gordoncaleb.client.shapes.animation.transitions.Path;
import com.gordoncaleb.client.shapes.animation.transitions.PathTransition;
import com.gordoncaleb.client.shapes.animations.Animation;
import com.gordoncaleb.client.util.CanvasUtils;
import com.gordoncaleb.client.util.ImageLoader;

public class ChessBoardLayer extends Group implements MouseMoveHandler, MouseDownHandler {

	private Group boardLayer;
	private Group pieceLayer;

	private Rectangle[][] squares = new Rectangle[8][8];
	private Sprite[][] pieces = new Sprite[8][8];
	private CssColor lightColor, darkColor;

	private int waiting = 0;

	private int[] selected;

	private int width, height;
	private int mouseX, mouseY, clickX, clickY;

	private Board board;
	private List<Long> validMoves;

	public ChessBoardLayer(int width, int height, CssColor lightColor, CssColor darkColor) {

		board = BoardMaker.getStandardChessBoard();
		board.makeNullMove();
		validMoves = board.generateValidMoves();

		boardLayer = new Group();
		pieceLayer = new Group();

		this.add(boardLayer);
		this.add(pieceLayer);

		this.width = width;
		this.height = height;
		this.lightColor = lightColor;
		this.darkColor = darkColor;

		int w = width / 8;
		int h = height / 8;

		Piece pModel;
		Rectangle s;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {

				s = new Rectangle(((r % 2) == (c % 2)) ? lightColor : darkColor, CanvasUtils.BLACK, c * w, r * h, w, h);

				squares[r][c] = s;

				boardLayer.add(s);

				pModel = board.getPiece(r, c);

				if (pModel != null) {
					String imgName = ImageLoader.getImageName(pModel.getPieceID(), pModel.getSide());
					pieces[r][c] = new Sprite(imgName, s.getPosition().getX() + 1, s.getPosition().getY() + 1, w - 5, h - 5);
					pieceLayer.add(pieces[r][c]);
				} else {
					pieces[r][c] = null;
				}
			}
		}

	}

	private List<Long> getValidMovesFrom(int r, int c) {
		List<Long> moves = new ArrayList<Long>();

		for (Long move : validMoves) {
			if (c == Move.getFromCol(move) && r == Move.getFromRow(move)) {
				moves.add(move);
			}
		}

		return moves;
	}

	private Long isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
		for (Long move : validMoves) {
			if (fromCol == Move.getFromCol(move) && fromRow == Move.getFromRow(move) && toRow == Move.getToRow(move) && toCol == Move.getToCol(move)) {
				return move;
			}
		}

		return null;
	}

	private int getRowPosition(int row) {
		return row * (height / 8);
	}

	private int getColPosition(int col) {
		return col * (width / 8);
	}

	private void hoverSquare(int x, int y) {
		setAllStrokeColors(CanvasUtils.BLACK);
		if (x < width && y < height) {
			squares[getRowAt(y)][getColAt(x)].setStrokeColor(CanvasUtils.DARKBLUE);
		}
	}

	private void selectSquare(int x, int y) {
		setAllStrokeColors(CanvasUtils.BLACK);
		setAllFillColors(lightColor, darkColor);

		if (x < width && y < height) {

			int col = getColAt(x);
			int row = getRowAt(y);

			List<Long> moves = getValidMovesFrom(row, col);
			if (!moves.isEmpty()) {
				selected = new int[] { row, col };

				squares[row][col].setFillColor(CanvasUtils.GRAY);
				for (Long move : moves) {
					squares[Move.getToRow(move)][Move.getToCol(move)].setFillColor(CanvasUtils.YELLOW);
				}
			} else {
				if (selected != null) {
					Long move = isValidMove(selected[0], selected[1], row, col);
					if (move != null) {
						board.makeMove(move);
						board.makeNullMove();
						board.generateValidMoves();

						animateMove(move);
					}
				}
				selected = null;
			}
		}
	}

	public void animateMove(Long move) {
		int toRow = Move.getToRow(move);
		int toCol = Move.getToCol(move);

		if (pieces[toRow][toCol] != null) {
			pieceLayer.remove(pieces[toRow][toCol]);
		}

		pieces[toRow][toCol] = pieces[selected[0]][selected[1]];
		pieces[selected[0]][selected[1]] = null;

		double tx = getColPosition(toCol) + 1;
		double ty = getRowPosition(toRow) + 1;
		double fx = getColPosition(toCol) + 1;
		double fy = getRowPosition(toRow) + 1;

		Animation a = PathTransition.linear(fx, fy, tx, ty);
		a.setDuration(5000);
		//a.setAutoReverse(true);
		a.play();

		
		//Logger.getLogger("").log(Level.INFO, "Animating from " + imagePath);
		
		pieces[toRow][toCol].setAnimation(a);

		// pieces[row][col].setPosition(getColPosition(col) + 1,
		// getRowPosition(row) + 1);
	}

	private int getRowAt(int y) {
		int h = height / 8;
		return y / h;
	}

	private int getColAt(int x) {
		int w = width / 8;
		return x / w;
	}

	private void setAllStrokeColors(CssColor color) {
		for (Rectangle[] rects : squares) {
			for (Rectangle rect : rects) {
				rect.setStrokeColor(color);
			}
		}
	}

	private void setAllFillColors(CssColor lightColor, CssColor darkColor) {
		for (int r = 0; r < squares.length; r++) {
			for (int c = 0; c < squares[r].length; c++) {
				squares[r][c].setFillColor(((r % 2) == (c % 2)) ? lightColor : darkColor);
			}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		mouseX = e.getRelativeX(e.getRelativeElement());
		mouseY = e.getRelativeY(e.getRelativeElement());
		hoverSquare(mouseX, mouseY);
	}

	@Override
	public void onMouseDown(MouseDownEvent e) {
		if (waiting == 0) {
			clickX = e.getRelativeX(e.getRelativeElement());
			clickY = e.getRelativeY(e.getRelativeElement());
			selectSquare(clickX, clickY);
		}
	}

}
