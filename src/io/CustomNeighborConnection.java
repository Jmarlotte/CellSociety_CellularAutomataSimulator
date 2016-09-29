package io;

public class CustomNeighborConnection extends NeighborConnection {

	private boolean[] connect;
	

	public CustomNeighborConnection(NeighborConnectionType t, boolean[] c) {
		super(t);
		connect = c;
	}


	public boolean[] getConnect() {
		return connect;
	}
	
}
