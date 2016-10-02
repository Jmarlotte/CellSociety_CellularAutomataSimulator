package cell;

/**
 * This class represents an invalid, non-existent cell due to being a neighbor of a border cell
 * and wrapping is not enabled
 * This cell is used as a placeholder for non-totalistic rule to align neighbors
 * @author ZYL
 *
 */
public class InvalidCell extends Cell {

	public InvalidCell() {
		super(-1, null);
	}

	@Override
	public void prepareForUpdate() {
		assert false : "Invalid cell is not supposed to be updated"; // crash the program
	}
	
}
