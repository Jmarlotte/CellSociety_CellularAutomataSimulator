package board;

/**
 * Custom connection type
 * @author ZYL
 *
 */
public class CustomNeighborConnection extends NeighborConnection {

	private boolean[] connect;
	

	public CustomNeighborConnection(boolean[] c, boolean wrap) {
		super(NeighborConnectionType.CUSTOM, wrap);
		connect = c;
	}

	public boolean[] getConnect() {
		return connect;
	}
	
}
