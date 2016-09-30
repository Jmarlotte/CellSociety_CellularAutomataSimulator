package board;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<BoardEnumeration>");
		for(int i=0; i<board.size(); i++) {
			Cell c = board.get(i);
			sb.append(c.getSaveStr());
			if(i!=board.size()-1)
				sb.append(",\n");
		}
		sb.append("</BoardEnumeration>");
		try(  PrintWriter out = new PrintWriter( fileName )  ){
		    out.println( sb.toString() );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
