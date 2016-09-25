package gui;

import javafx.scene.paint.Color;

public class ThreeColorMapSegregation extends ColorMap {

	@Override
	public Color getColor(int seq) {
		if(seq==0) {
			return Color.WHITE;
		} else if(seq==1) {
			return Color.RED;
		} else if(seq==2) {
			return Color.BLUE;
		} else {
			throw new RuntimeException("Unsupported color sequence!");
		}
	}
}
