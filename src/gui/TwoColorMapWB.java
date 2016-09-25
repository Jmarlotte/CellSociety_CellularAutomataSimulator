package gui;

import javafx.scene.paint.Color;

public class TwoColorMapWB extends ColorMap {

	@Override
	public Color getColor(int seq) {
		if(seq==0) {
			return Color.WHITE;
		} else if(seq==1) {
			return Color.BLACK;
		} else {
			throw new RuntimeException("Unsupported color sequence!");
		}
	}

}
