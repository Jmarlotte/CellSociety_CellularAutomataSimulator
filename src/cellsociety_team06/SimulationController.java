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
				// cell can locally make decisions if game rule is fire or reproduction
				if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
					stepLocal();
				} else if(board.get(0).getRule() instanceof WaTorRule) {
					stepWaTor();
				}
			}
		}, 0, interval);
	}
	
	private void stepWaTor() {
		// stepping in WaTor scenario
		
	}

	/**
	 * Update once locally. 
	 */
	private void stepLocal() {
		for(Cell c : board) {
			c.prepareForUpdate();
		}
		for(Cell c : board) {
			c.update();
		}
		display.updateScreen(board);
	}
	
	
	
}
