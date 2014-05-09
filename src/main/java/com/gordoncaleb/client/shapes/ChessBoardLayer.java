package com.gordoncaleb.client.shapes;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.gordoncaleb.client.chess.Board;
import com.gordoncaleb.client.chess.BoardMaker;
import com.gordoncaleb.client.chess.Move;
import com.gordoncaleb.client.chess.Move.MoveNote;
import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece;
import com.gordoncaleb.client.shapes.animation.transitions.PathTransition;
import com.gordoncaleb.client.shapes.animation.transitions.SequentialTransition;
import com.gordoncaleb.client.shapes.animations.AnimationEvent;
import com.gordoncaleb.client.shapes.animations.EventHandler;
import com.gordoncaleb.client.shapes.animations.interpolator.Interpolator;
import com.gordoncaleb.client.util.CanvasUtils;

public class ChessBoardLayer extends Group implements MouseMoveHandler, MouseDownHandler {

	private Group boardLayer;
	private Group pieceLayer;

	private Group movingPieces;
	private List<EventHandler<AnimationEvent>> eventHandlers;

	private Rectangle[][] squares = new Rectangle[8][8];
	private ChessPiece[][] pieces = new ChessPiece[8][8];
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
		movingPieces = new Group();

		eventHandlers = new ArrayList<EventHandler<AnimationEvent>>();

		this.add(boardLayer);
		this.add(pieceLayer);
		this.add(movingPieces);

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
					pieces[r][c] = new ChessPiece(pModel.getPieceID(), pModel.getSide(), s.getPosition().getX() + 1, s.getPosition().getY() + 1,
							w - 5, h - 5);
					pieces[r][c].setPiece(pModel);
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

						animateMove(move);

						board.makeMove(move);
						board.makeNullMove();
						board.generateValidMoves();
					}
				}
				selected = null;
			}
		}
	}

	public void animateMove(Long move) {
		final int toRow = Move.getToRow(move);
		final int toCol = Move.getToCol(move);
		final int fromRow = Move.getFromRow(move);
		final int fromCol = Move.getFromCol(move);

		final UIObject2D movingPiece = pieces[fromRow][fromCol];

		final int takenCol = Move.getPieceTakenCol(move);
		final int takenRow = Move.getPieceTakenRow(move);

		final UIObject2D takenPiece = Move.hasPieceTaken(move) ? pieces[takenRow][takenCol] : null;

		SequentialTransition seqTrans = new SequentialTransition();

		double tx = getColPosition(toCol) + 1;
		double ty = getRowPosition(toRow) + 1;
		double fx = getColPosition(fromCol) + 1;
		double fy = getRowPosition(fromRow) + 1;

		PathTransition a = PathTransition.linear(fx, fy, tx, ty);

		a.setDuration(500);
		a.setInterpolator(Interpolator.EASE_BOTH);
		// a.setAutoReverse(true);`

		// Logger.getLogger("").log(Level.INFO, "Animating from " + imagePath);

		a.setOnFinished(new EventHandler<AnimationEvent>() {

			@Override
			public void handle(AnimationEvent event) {

				eventHandlers.add(new EventHandler<AnimationEvent>() {

					@Override
					public void handle(AnimationEvent event) {

						if (takenPiece != null) {
							pieceLayer.remove(takenPiece);
						}

						pieces[toRow][toCol] = pieces[fromRow][fromCol];
						pieces[fromRow][fromCol] = null;

						movingPieces.remove(movingPiece);
						pieceLayer.add(movingPiece);
					}

				});

			}

		});

		movingPieces.add(movingPiece);
		pieceLayer.remove(movingPiece);

		movingPiece.setAnimation(a);
		
		seqTrans.add(a);

		MoveNote note = Move.getNote(move);

		switch (note) {
		case CASTLE_FAR:
			final Side turn = board.getTurn();
			final UIObject2D rook = pieces[board.getMaterialRow(turn)][board.getFarRookStartingCol(turn)];
			final int rookToCol = 3;
			final int rookToRow = toRow;
			final int rookFromCol = board.getFarRookStartingCol(turn);
			final int rookFromRow = fromRow;
			
			tx = getColPosition(rookToCol) + 1;
			ty = getRowPosition(rookToRow) + 1;
			fx = getColPosition(rookFromCol) + 1;
			fy = getRowPosition(rookFromRow) + 1;

			PathTransition r = PathTransition.linear(fx, fy, tx, ty);

			r.setDuration(500);
			r.setInterpolator(Interpolator.EASE_BOTH);
			
			r.setOnFinished(new EventHandler<AnimationEvent>() {

				@Override
				public void handle(AnimationEvent event) {

					eventHandlers.add(new EventHandler<AnimationEvent>() {

						@Override
						public void handle(AnimationEvent event) {

							pieces[rookToRow][rookToCol] = pieces[rookFromRow][rookFromCol];
							pieces[rookFromRow][rookFromCol] = null;

							movingPieces.remove(rook);
							pieceLayer.add(rook);
						}

					});

				}

			});

			movingPieces.add(rook);
			pieceLayer.remove(rook);
			
			rook.setAnimation(r);
			
			seqTrans.add(r);

			break;
		case CASTLE_NEAR:
			break;
		case NEW_QUEEN:
			break;
		default:
			break;
		}

		//a.play();
		
		seqTrans.play();

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

	@Override
	public void propagateAndAnimate(double elapsedTime) {
		super.propagateAndAnimate(elapsedTime);
		for (EventHandler<AnimationEvent> event : eventHandlers) {
			event.handle(null);
		}

		eventHandlers.clear();
	}

	@Override
	public void draw(Context2d context) {
		super.draw(context);
	}

}
