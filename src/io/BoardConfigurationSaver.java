package io;

import java.util.*;
import cell.*;

/**
 * Save board configuration
 * @author ZYL
 */
public class BoardConfigurationSaver {

	/**
	 * Save a board to file
	 * @param board
	 */
	public static void saveBoard(ArrayList<Cell> board, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<BoardEnumeration>");
		for(Cell c : board) {
			sb.append(c.getSaveStr());
			sb.append(",\n");
		}
		sb.append("</BoardEnumeration>");
	}
	
}
