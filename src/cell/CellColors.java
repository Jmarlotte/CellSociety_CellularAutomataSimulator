package cell;
import javafx.scene.paint.Color;

public class CellColors{
	
	public static Color[] fireColors(){
		return new Color[]{Color.RED, Color.BLACK, Color.WHITE};
	}
	
	public static Color[] waTorColors(){
		return new Color[]{Color.BLUE, Color.ORANGE, Color.GRAY};
	}
	
	public static Color[] segregationColors(){
		return new Color[]{Color.GRAY, Color.RED, Color.BLUE};
	}
	
	public static Color[] reproductionColors(){
		return new Color[]{Color.GRAY, Color.GREEN};
	}
}
