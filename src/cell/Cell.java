// This entire file is part of my masterpiece.
// Yilun Zhou

/**
 * This is the abstract superclass for all Cells. It sets up basic 
 * structures of a cell, such as CellNeighbor, Rule, and CellValue. 
 * 
 * In addition, it is also capable for local stepping, a type of simulation 
 * in which each cell has complete information as to how to behave according 
 * to its neighbor. This is the case for Game of Life and Fire. 
 * 
 * The discussion for Rule and CellNeighbor is in the analysis file, which talks 
 * about the separability of cell, rule, and board geometry. 
 */

package cell;

import rule.Rule;

/**
 * Cell super class
 * @author ZYL
 *
 */
public abstract class Cell {

	// protected ArrayList<Cell> neighbors;
	protected CellNeighbors neighbors;
	protected CellValue value;

	protected CellValue nextValue;
	protected Rule rule;

	// The x and y index of the cell. Mainly used for rendering on GUI. 
	private int x;
	private int y;

	public Cell(int value, Rule rule) {
		neighbors = new CellNeighbors();
		this.value = new CellValue(value);
		this.nextValue = new CellValue(value);
		this.rule = rule;
	}
	
	public void setAllValue(int value) {
		this.value.setVal(value);
		this.nextValue.setVal(value);
	}
	
	
	/**
	 * Prepare for update. Change nextValue based on neighbors.
	 * Needs to consider rule. 
	 */
	public abstract void prepareForUpdate();

	/**
	 * update. Replace value with nextValue.
	 */
	public boolean update() {
		boolean valueChanged = value.getVal() != nextValue.getVal();
		value.setVal(nextValue.getVal());
		return valueChanged;
	}

	public CellNeighbors getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(CellNeighbors neighbors) {
		this.neighbors = neighbors;
	}

	public CellValue getValue() {
		return value;
	}

	public void setValue(CellValue value) {
		this.value = value;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	public String getSaveStr() {
		return String.format("%d %d %d", this.getX(), this.getY(), this.getValue().getVal());
	}

	@Override
	public String toString() {
		return "Cell [value=" + value.getVal() + ", x=" + x + ", y=" + y + "]";
	}

}
