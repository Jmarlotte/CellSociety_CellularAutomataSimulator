package io;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import cell.Cell;
import cell.CellFactory;
import rule.FireRule;
import rule.ReproductionRule;
import rule.Rule;
import rule.SegregationRule;
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
	public void readFile(String name) throws FileParsingException {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = db.parse(name);
			checkRequiredField(d);
			String ruleType = this.getUniqueKey(d, "RuleType");
			if(ruleType.equals("Reproduction")) {
				parseReproductionRule(d);
				setupBoard(ruleType, d, 8);
			} else if(ruleType.equals("Fire")) {
				parseFireRule(d);
				setupBoard(ruleType, d, 4);
			} else if(ruleType.equals("Segregation")) {
				parseSegregationRule(d);
				setupBoard(ruleType, d, 8);
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

	private void setupBoard(String cellClass, Document d, int connection) throws FileParsingException {
		int width = Integer.parseInt(getUniqueKey(d, "Width"));
		int height = Integer.parseInt(getUniqueKey(d, "Height"));
		String random = getUniqueKey(d, "Random");
		if(random!=null && random.equalsIgnoreCase("true")) {
			String ratioStr = getUniqueKey(d, "RandomRatio");
			ArrayList<Double> ratio = csvStrToDoubleList(ratioStr);
			board = BoardBuilder.buildRandomBoard(
					cellClass, width, height, rule, connection, ratio);
		} else {
			int defaultCellVal = Integer.parseInt(getUniqueKey(d, "DefaultCellValue"));
			String nonDefaultCellValStr = getUniqueKey(d, "CellValues").trim().replace("\n", "");
			board = BoardBuilder.buildDefaultNonDefaultBoard(
					cellClass, width, height, rule, connection, defaultCellVal, nonDefaultCellValStr);
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
