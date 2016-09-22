package cellsociety_team06;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class SpecificationFileParser {

	private Rule rule;
	private ArrayList<Cell> board; // adjacency list representation of board

	private String getUniqueKey(Document d, String key) {
		return d.getElementsByTagName(key).item(0).getTextContent();
	}

	/**
	 * Read XML file. Fill rule and board. 
	 * @param name filename
	 */
	public void readFile(String name) {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = db.parse(name);
			String ruleType = this.getUniqueKey(d, "RuleType");
			assert ruleType.equals("Reproduction"); // the only currently supported rule type
			if(ruleType.equals("Reproduction")) {
				parseReproductionRule(d);
			}
			int width = Integer.parseInt(getUniqueKey(d, "Width"));
			int height = Integer.parseInt(getUniqueKey(d, "Height"));
			int defaultCellVal = Integer.parseInt(getUniqueKey(d, "DefaultCellValue"));
			String nonDefaultCellValStr = getUniqueKey(d, "CellValues").trim().replace("\n", "");
			board = buildBoard(width, height, defaultCellVal, nonDefaultCellValStr, rule);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Parsing done");
	}

	private void parseReproductionRule(Document d) {
		String[] liveCounts = this.getUniqueKey(d, "RequiredNeighborCountsToLive").split(",");
		ArrayList<Integer> liveCountList = new ArrayList<Integer>();
		for(String c : liveCounts) {
			liveCountList.add(Integer.parseInt(c));
		}
		String[] emergeCounts = this.getUniqueKey(d, "RequiredNeighborCountsToEmerge").split(",");
		ArrayList<Integer> emergeCountList = new ArrayList<Integer>();
		for(String c : emergeCounts) {
			emergeCountList.add(Integer.parseInt(c));
		}
		rule = new ReproductionRule(liveCountList, emergeCountList);
	}

	
	
	/**
	 * Build board adjacency list representation
	 * @param width
	 * @param height
	 * @param defaultCellVal
	 * @param nonDefaultCellValStr
	 * @param rule
	 * @return
	 */
	private ArrayList<Cell> buildBoard(int width, int height, int defaultCellVal,
			String nonDefaultCellValStr, Rule rule) {
		Cell[][] board = new Cell[height][width];
		// add cell and append rule
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				board[h][w] = new Cell();
				board[h][w].setRule(rule);
				board[h][w].setX(h);
				board[h][w].setY(w);
				board[h][w].setValue(new CellValue(defaultCellVal));
			}
		}
		String[] nonDefaultCellValArr = nonDefaultCellValStr.split(";");
		// Overwrite non-default cell values
		// NOTE: Assuming Int Type
		for(String s : nonDefaultCellValArr) {
			String[] info = s.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			int v = Integer.parseInt(info[2]);
			board[x][y].setValue(new CellValue(v));
		}
		// Add neighbors
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				ArrayList<Cell> neighbors = new ArrayList<Cell>();
				if(h!=0)
					neighbors.add(board[h-1][w]);
				if(w!=0)
					neighbors.add(board[h][w-1]);
				if(h!=height-1)
					neighbors.add(board[h+1][w]);
				if(w!=width-1)
					neighbors.add(board[h][w+1]);
				board[h][w].setNeighbors(neighbors);
			}
		}
		// Build ArrayList
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				cellList.add(board[h][w]);
			}
		}
		return cellList;
	}

	public Rule getRule() {
		return rule;
	}
	
	public ArrayList<Cell> getBoard() {
		return board;
	}

}
