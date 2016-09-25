package gui;

import javafx.scene.paint.Color;

public class ThreeColorMapFire extends ColorMap {

	@Override
	public Color getColor(int seq) {
		if(seq==0) {
			return Color.WHITE;
		} else if(seq==1) {
			return Color.DARKGREEN;
		} else if(seq==2) {
			return Color.RED;
		} else {
			throw new RuntimeException("Unsupported color sequence!");
		}
	}
}
