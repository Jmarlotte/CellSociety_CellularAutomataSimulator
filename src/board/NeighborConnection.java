package board;

/**
 * Neighbor connection class
 * @author ZYL
 *
 */
public class NeighborConnection {

	private NeighborConnectionType type;
	private boolean wrap;
	
	public NeighborConnection(NeighborConnectionType t, boolean wrap) {
		type = t;
		this.wrap = wrap;
	}
	
	public NeighborConnectionType getType() {
		return type;
	}

	public boolean isWrap() {
		return wrap;
	}
	
}
