package com.gordoncaleb.client.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.gordoncaleb.client.chess.Side;
import com.gordoncaleb.client.pieces.Piece.PieceID;

public class ImageLoader {

	public static final String BLACKBISHOP = "pieces/black_bishop.png";
	public static final String BLACKKING = "pieces/black_king.png";
	public static final String BLACKKNIGHT = "pieces/black_knight.png";
	public static final String BLACKPAWN = "pieces/black_pawn.png";
	public static final String BLACKQUEEN = "pieces/black_queen.png";
	public static final String BLACKROOK = "pieces/black_rook.png";
	public static final String WHITEBISHOP = "pieces/white_bishop.png";
	public static final String WHITEKING = "pieces/white_king.png";
	public static final String WHITEKNIGHT = "pieces/white_knight.png";
	public static final String WHITEPAWN = "pieces/white_pawn.png";
	public static final String WHITEQUEEN = "pieces/white_queen.png";
	public static final String WHITEROOK = "pieces/white_rook.png";

	public static Map<String, ImageElement> images = new HashMap<String, ImageElement>();

	public static void loadAllImages() {

		List<String> imageNames = new ArrayList<String>();

		imageNames.add(BLACKBISHOP);
		imageNames.add(BLACKKING);
		imageNames.add(BLACKKNIGHT);
		imageNames.add(BLACKPAWN);
		imageNames.add(BLACKQUEEN);
		imageNames.add(BLACKROOK);

		imageNames.add(WHITEBISHOP);
		imageNames.add(WHITEKING);
		imageNames.add(WHITEKNIGHT);
		imageNames.add(WHITEPAWN);
		imageNames.add(WHITEQUEEN);
		imageNames.add(WHITEROOK);

		for (String name : imageNames) {
			loadImage(name);
		}

	}

	public static void loadImage(final String name) {
		final Image img = new Image(name);
		img.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event) {
				images.put(name, (ImageElement) img.getElement().cast());
			}
		});
	}

	public static ImageElement getImage(String name) {
		return images.get(name);
	}

	public static void loadImage(final String name, final LoadHandler handler) {
		final Image img = new Image(name);
		img.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event) {
				images.put(name, (ImageElement) img.getElement().cast());
				handler.onLoad(event);
			}
		});
	}

	public static String getImageName(PieceID id, Side side) {

		switch (id) {
		case BISHOP:
			return (side == Side.BLACK) ? BLACKBISHOP : WHITEBISHOP;
		case KING:
			return (side == Side.BLACK) ? BLACKKING : WHITEKING;
		case KNIGHT:
			return (side == Side.BLACK) ? BLACKKNIGHT : WHITEKNIGHT;
		case PAWN:
			return (side == Side.BLACK) ? BLACKPAWN : WHITEPAWN;
		case QUEEN:
			return (side == Side.BLACK) ? BLACKQUEEN : WHITEQUEEN;
		case ROOK:
			return (side == Side.BLACK) ? BLACKROOK : WHITEROOK;
		default:
			return null;
		}
	}
}
