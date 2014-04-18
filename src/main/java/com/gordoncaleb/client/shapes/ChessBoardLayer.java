package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.BoardMaker;
import com.gordoncaleb.client.pieces.Piece;
import com.gordoncaleb.client.util.CanvasUtils;

public class ChessBoardLayer implements Drawable, MouseMoveHandler, MouseDownHandler {

	private Layer boardLayer;
	private Layer pieceLayer;

	private Rectangle[][] squares = new Rectangle[8][8];
	private CssColor lightColor, darkColor;

	private Rectangle selectedSquare;

	private int width, height;
	private int mouseX, mouseY, clickX, clickY;

	private Board board;

	public ChessBoardLayer(int width, int height, CssColor lightColor, CssColor darkColor) {

		board = BoardMaker.getStandardChessBoard();

		boardLayer = new Layer();
		pieceLayer = new Layer();

		this.width = width;
		this.height = height;
		this.lightColor = lightColor;
		this.darkColor = darkColor;

		int w = width / 8;
		int h = height / 8;

		Piece pModel;
		Rectangle s;
		Sprite pView;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {

				s = new Rectangle(((r % 2) == (c % 2)) ? lightColor : darkColor, CanvasUtils.BLACK, c * w, r * h, w, h);

				squares[r][c] = s;

				boardLayer.addDrawable(s);

				pModel = board.getPiece(r, c);
				String imgName = ImageLoader.getImageName(pModel.getPieceID(), pModel.getSide());
				pView = new Sprite(imgName);
				pView.setPosition(s.getPosition().getX(), s.getPosition().getY());

				pieceLayer.addDrawable(pView);
			}
		}

	}

	private void hoverSquare(int x, int y) {
		setAllStrokeColors(CanvasUtils.BLACK);
		getSquareAt(x, y).setStrokeColor(CanvasUtils.YELLOW);
	}

	private void selectSquare(int x, int y) {
		setAllStrokeColors(CanvasUtils.BLACK);
		setAllFillColors(lightColor, darkColor);

		selectedSquare = getSquareAt(x, y);
		selectedSquare.setFillColor(CanvasUtils.YELLOW);
	}

	private Rectangle getSquareAt(int x, int y) {
		int w = width / 8;
		int h = height / 8;

		int col = x / w;
		int row = y / h;

		return squares[row][col];
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
	public void draw(Context2d context) {
		boardLayer.draw(context);
		pieceLayer.draw(context);
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		mouseX = e.getRelativeX(e.getRelativeElement());
		mouseY = e.getRelativeY(e.getRelativeElement());
		hoverSquare(mouseX, mouseY);
	}

	@Override
	public void onMouseDown(MouseDownEvent e) {
		clickX = e.getRelativeX(e.getRelativeElement());
		clickY = e.getRelativeY(e.getRelativeElement());
		selectSquare(clickX, clickY);
	}

}
