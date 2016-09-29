package io;

/**
 * Custom connection type
 * @author ZYL
 *
 */
public class CustomNeighborConnection extends NeighborConnection {

	private boolean[] connect;
	

	public CustomNeighborConnection(boolean[] c) {
		super(NeighborConnectionType.CUSTOM);
		connect = c;
	}

	public boolean[] getConnect() {
		return connect;
	}
	
}
