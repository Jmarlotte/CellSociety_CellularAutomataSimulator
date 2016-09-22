package cellsociety_team06;
import java.util.*;

public class SimulationController {

	private ArrayList<Cell> board;
	private SimulationDisplay display;
	private Timer timer;
	public int interval = 1000;
	
	public SimulationController(ArrayList<Cell> bd, SimulationDisplay sd) {
		timer = new Timer();
		board = bd;
		display = sd; 
	}
	
	public void startTask() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				step();
			}
		}, 0, interval);
	}
	
	/**
	 * Update once. 
	 */
	private void step() {
		for(Cell c : board) {
			c.prepareForUpdate();
		}
		for(Cell c : board) {
			c.update();
		}
		display.updateScreen(board);
	}
	
	
	
}
