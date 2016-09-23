package cell;

import java.util.*;

import rule.Rule;

public abstract class Cell {

	protected ArrayList<Cell> neighbors;
	protected CellValue value;

	protected CellValue nextValue;
	protected Rule rule;

	// The x and y index of the cell. Mainly used for rendering on GUI. 
	private int x;
	private int y;

	/**
	 * Prepare for update. Change nextValue based on neighbors.
	 * Needs to consider rule. 
	 */
	public abstract void prepareForUpdate();

	/**
	 * update. Replace value with nextValue.
	 */
	public void update() {
		value.setVal(nextValue.getVal());
	}

	public ArrayList<Cell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}

	public CellValue getValue() {
		return value;
	}

	public void setValue(CellValue value) {
		this.value = value;
	}

	public CellValue getNextValue() {
		return nextValue;
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


}
