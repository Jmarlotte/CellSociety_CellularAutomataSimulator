package io;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import board.BoardBuilder;
import board.CustomNeighborConnection;
import board.NeighborConnection;
import board.NeighborConnectionType;
import cell.Cell;
import rule.FireRule;
import rule.ReproductionRule;
import rule.Rule;
import rule.SegregationRule;
import rule.WaTorRule;

/**
 * Class for input parsing
 * @author ZYL
 *
 */
public class SpecificationFileParser {

	private Rule rule;
	private ArrayList<Cell> board; // adjacency list representation of board
	private SpecFileParserDelegate delegate;

	public SpecificationFileParser(SpecFileParserDelegate d) {
		delegate = d;
	}

	private String getUniqueKey(Document d, String key) {
		try {
			return d.getElementsByTagName(key).item(0).getTextContent();
		} catch (NullPointerException e) {
			return null;
		}
	}

	private Document getDocumentFromFile(String f) throws FileParsingException {
		Document d = null;
		try {
			DocumentBuilder db;
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			d = db.parse(f);
		} catch (Exception e) {
			throw new FileParsingException("Error reading file");
		}
		return d;
	}

	/**
	 * Read XML file. Fill rule and board. 
	 * @param name filename
	 */
	public void readFile(String name) throws FileParsingException {
		try {
			Document d = getDocumentFromFile(name);
			checkRequiredField(d);
			String ruleType = this.getUniqueKey(d, "RuleType");
			if(ruleType.equals("Reproduction")) {
				parseReproductionRule(d);
				setupBoard(ruleType, d);
			} else if(ruleType.equals("Fire")) {
				parseFireRule(d);
				setupBoard(ruleType, d);
			} else if(ruleType.equals("Segregation")) {
				parseSegregationRule(d);
				setupBoard(ruleType, d);
			} else if(ruleType.equals("WaTor")) {
				parseWaTorRule(d);
				setupBoard(ruleType, d);
			}
		} catch (FileParsingException e) {
			String errorMsg = String.format("File %s error: %s", name, e.getMessage());
			System.out.println(errorMsg);
			this.delegate.showErrorMsg(errorMsg);
			return;
		}
		System.out.println("Parsing done");
	}

	private boolean fieldPresent(Document d, String s) {
		return getUniqueKey(d, s)!=null;
	}

	private void checkRequiredField(Document d) throws FileParsingException {
		String[] requiredFields = {"Width", "Height", "RuleType"};
		for(String s : requiredFields) {
			if(!fieldPresent(d, s))
				throw new FileParsingException(String.format("Field \"%s\" does not exist", s));
		}
	}
	
	private NeighborConnection getNeighborConnection(Document d) throws FileParsingException {
		String neighborConnectionType = getUniqueKey(d, "NeighborConnectionType");
		if(neighborConnectionType==null) {
			String msg = "NeighborConnectionType field not found, default to 8.";
			System.out.println(msg);
			delegate.showWarningMessage(msg);
			return new NeighborConnection(NeighborConnectionType.EIGHT_NEIGHBOR);
		}
		if(neighborConnectionType.equals("4"))
			return new NeighborConnection(NeighborConnectionType.FOUR_NEIGHBOR);
		if(neighborConnectionType.equals("8"))
			return new NeighborConnection(NeighborConnectionType.EIGHT_NEIGHBOR);
		if(neighborConnectionType.equalsIgnoreCase("custom")) {
			String neighborConnectStr = getUniqueKey(d, "NeighborConnect");
			if(neighborConnectStr==null) {
				throw new FileParsingException("NeighborConnect field not found for custom neighbor connection");
			}
			boolean[] connect = csvStrToBooleanArray(neighborConnectStr);
			return new CustomNeighborConnection(connect);
		}
		return null;
	}

	private void setupBoard(String type, Document d) throws FileParsingException {
		int width = Integer.parseInt(getUniqueKey(d, "Width"));
		int height = Integer.parseInt(getUniqueKey(d, "Height"));
		String random = getUniqueKey(d, "Random");
		String fullBoardDesciptionFile = getUniqueKey(d, "FullBoardDescriptionFile");
		NeighborConnection connection = getNeighborConnection(d);
		if(random!=null && random.equalsIgnoreCase("true")) {
			String ratioStr = getUniqueKey(d, "RandomRatio");
			ArrayList<Double> ratio = csvStrToDoubleList(ratioStr);
			board = BoardBuilder.buildRandomBoard(
					type, width, height, rule, connection, ratio);
		} else if(fullBoardDesciptionFile!=null) {
			try {
				Document descFile = DocumentBuilderFactory.newInstance().newDocumentBuilder().
						parse(fullBoardDesciptionFile);
				String boardDescStr = getUniqueKey(descFile, "BoardEnumeration");
				String[] boardDescArr = boardDescStr.split(",");
				board = BoardBuilder.buildFullBoard(type, width, height, rule, connection, boardDescArr);
			} catch (Exception e) {
				throw new FileParsingException("Unsupported Board Description File Format");
			}
		} else {
			int defaultCellVal = Integer.parseInt(getUniqueKey(d, "DefaultCellValue"));
			String nonDefaultCellValStr = getUniqueKey(d, "CellValues").trim().replace("\n", "");
			board = BoardBuilder.buildDefaultNonDefaultBoard(
					type, width, height, rule, connection, defaultCellVal, nonDefaultCellValStr);
		}
	}

	private ArrayList<Integer> csvStrToIntList(String s) {
		String[] strArr = s.split(",");
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for(String c : strArr) {
			intList.add(Integer.parseInt(c));
		}
		return intList;
	}
	
	private boolean[] csvStrToBooleanArray(String s) {
		String[] strArr = s.split(",");
		boolean[] arr = new boolean[strArr.length];
		for(int i=0; i<strArr.length; i++) {
			arr[i] = Integer.parseInt(strArr[i])!=0;
		}
		return arr;
	}

	private ArrayList<Double> csvStrToDoubleList(String s) {
		String[] strArr = s.split(",");
		ArrayList<Double> doubleList = new ArrayList<Double>();
		for(String c : strArr) {
			doubleList.add(Double.parseDouble(c));
		}
		return doubleList;
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

	private void parseSegregationRule(Document d) {
		double threshold = Double.parseDouble(this.getUniqueKey(d, "SatisfactionThreshold"));
		rule = new SegregationRule(threshold);
	}


	public Rule getRule() {
		return rule;
	}

	public ArrayList<Cell> getBoard() {
		return board;
	}

}
