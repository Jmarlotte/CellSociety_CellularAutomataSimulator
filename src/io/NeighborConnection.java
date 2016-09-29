package io;

/**
 * Neighbor connection class
 * @author ZYL
 *
 */
public class NeighborConnection {

	private NeighborConnectionType type;
	
	public NeighborConnectionType getType() {
		return type;
	}

	public NeighborConnection(NeighborConnectionType t) {
		type = t;
	}
	
}
