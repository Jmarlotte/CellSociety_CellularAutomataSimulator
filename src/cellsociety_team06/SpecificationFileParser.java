package cellsociety_team06;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class SpecificationFileParser {

	private Rule rule;
	private HashMap<Cell, ArrayList<Cell>> board; // adjacency list representation of board
	
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
				String[] neighborCounts = this.getUniqueKey(d, "RequiredNeighborCounts").split(",");
				System.out.println(Arrays.toString(neighborCounts));
				ArrayList<Integer> countArrayList = new ArrayList<Integer>();
				for(String c : neighborCounts) {
					countArrayList.add(Integer.parseInt(c));
				}
				rule = new ReproductionRule(countArrayList);
			}
			int width = Integer.parseInt(getUniqueKey(d, "Width"));
			int height = Integer.parseInt(getUniqueKey(d, "Height"));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public Rule getRule() {
		return rule;
	}
	
}
