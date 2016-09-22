package cellsociety_team06;
import java.util.*;

/**
 * This rule represents a binary board pattern in which a cell 
 * will be populated when a specific number of neighbor cells are 
 * populated. 
 * Cell value of 0 is unoccupied, and cell value of 1 is occupied. 
 * @author ZYL
 *
 */
public class ReproductionRule extends Rule {

	private ArrayList<Integer> liveNeighborCounts;

	private ArrayList<Integer> emergeNeighborCounts;
	
	public ReproductionRule(ArrayList<Integer> liveCounts, ArrayList<Integer> emergeCounts) {
		this.liveNeighborCounts = liveCounts;
		this.emergeNeighborCounts = emergeCounts;
	}
	
	public ArrayList<Integer> getLiveNeighborCounts() {
		return liveNeighborCounts;
	}

	public ArrayList<Integer> getEmergeNeighborCounts() {
		return emergeNeighborCounts;
	}
	
}
