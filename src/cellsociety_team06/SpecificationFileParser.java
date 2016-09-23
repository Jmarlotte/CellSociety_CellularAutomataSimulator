package cellsociety_team06;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import cell.Cell;
import cell.CellFactory;
import cell.CellValue;
import rule.FireRule;
import rule.ReproductionRule;
import rule.Rule;
import rule.WaTorRule;


public class SpecificationFileParser {

	private Rule rule;
	private ArrayList<Cell> board; // adjacency list representation of board

	private String getUniqueKey(Document d, String key) {
		try {
			return d.getElementsByTagName(key).item(0).getTextContent();
		} catch (NullPointerException e) {
			return null;
		}

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
			if(ruleType.equals("Reproduction")) {
				parseReproductionRule(d);
				setupBoard(ruleType, d, 8);
			} else if(ruleType.equals("Fire")) {
				parseFireRule(d);
				setupBoard(ruleType, d, 4);
			} else if(ruleType.equals("WaTor")) {
				parseWaTorRule(d);
				setupBoard(ruleType, d, 4);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Parsing done");
	}

	private void setupBoard(String cellClass, Document d, int neighbors) {
		int width = Integer.parseInt(getUniqueKey(d, "Width"));
		int height = Integer.parseInt(getUniqueKey(d, "Height"));
		int defaultCellVal = Integer.parseInt(getUniqueKey(d, "DefaultCellValue"));
		String nonDefaultCellValStr = getUniqueKey(d, "CellValues").trim().replace("\n", "");
		board = buildBoard(cellClass, width, height, defaultCellVal, nonDefaultCellValStr, rule, neighbors);
	}

	private ArrayList<Integer> csvStrToIntList(String s) {
		String[] strArr = s.split(",");
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for(String c : strArr) {
			intList.add(Integer.parseInt(c));
		}
		return intList;
	}

	private void parseReproductionRule(Document d) {
		ArrayList<Integer> liveCountList = 
				csvStrToIntList(this.getUniqueKey(d, "RequiredNeighborCountsToLive"));
		ArrayList<Integer> emergeCountList = 
				csvStrToIntList(this.getUniqueKey(d, "RequiredNeighborCountsToEmerge"));
		rule = new ReproductionRule(liveCountList, emergeCountList);
	}

	private void parseWaTorRule(Document d) {
		WaTorRule watorRule = new WaTorRule();
		String initHealth = this.getUniqueKey(d, "InitialHealth");
		if(initHealth != null) {
			watorRule.setInitialHealth(Integer.parseInt(initHealth));
		}
		String minReprHealth = this.getUniqueKey(d, "MinReproductionHealth");
		if(minReprHealth != null) {
			watorRule.setMinReproductionHealth(Integer.parseInt(minReprHealth));
		}
		String sharkReprInterval = this.getUniqueKey(d, "SharkReproductionInterval");
		if(sharkReprInterval != null) {
			watorRule.setSharkReproductionInterval(Integer.parseInt(sharkReprInterval));
		}
		String fishReprInterval = this.getUniqueKey(d, "FishReproductionInterval");
		if(fishReprInterval != null) {
			watorRule.setFishReproductionInterval(Integer.parseInt(fishReprInterval));
		}
		String fishEnergy = this.getUniqueKey(d, "FishEnergy");
		if(fishEnergy != null) {
			watorRule.setFishEnergy(Integer.parseInt(fishEnergy));
		}
		rule = watorRule;
	}

	private void parseFireRule(Document d) {
		double probCatch = Double.parseDouble(this.getUniqueKey(d, "ProbCatch"));
		rule = new FireRule(probCatch);
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
	private ArrayList<Cell> buildBoard(String cellClass, int width, int height, int defaultCellVal,
			String nonDefaultCellValStr, Rule rule, int connection) {
		Cell[][] board = new Cell[height][width];
		// add cell and append rule
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				board[h][w] = CellFactory.newCell(cellClass, defaultCellVal, rule);
				board[h][w].setX(h);
				board[h][w].setY(w);
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
			board[x][y].setAllValue(v);
		}
		// Add neighbors
		setNeighborConnection(width, height, connection, board);
		// Build ArrayList
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				cellList.add(board[h][w]);
			}
		}
		return cellList;
	}

	private void setNeighborConnection(int width, int height, int connection, Cell[][] board) {
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
				if(connection==8) {
					if(h!=0 && w!=0)
						neighbors.add(board[h-1][w-1]);
					if(h!=0 && w!=width-1)
						neighbors.add(board[h-1][w+1]);
					if(h!=height-1 && w!=0)
						neighbors.add(board[h+1][w-1]);
					if(h!=height-1 && w!=width-1)
						neighbors.add(board[h+1][w+1]);
				}
				board[h][w].setNeighbors(neighbors);
			}
		}
	}

	public Rule getRule() {
		return rule;
	}

	public ArrayList<Cell> getBoard() {
		return board;
	}

}
