package ui;
//@author James Marlotte

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import cell.Cell;
import cell.FireCell;
import cell.ReproductionCell;
import cell.WaTorCell;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import sun.security.util.Resources;

public class LineChartManager {
	
	public LineChartManager(){
		
	}
	
	private int updateCount = 1;
	private LineChart<Number, Number> myChart;
	private NumberAxis myXAxis;
	private NumberAxis myYAxis;
	private ResourceBundle myChartResources = Resources.getBundle("resources/LineChart");
	private Series<Number,Number> series0;
	private Series<Number,Number> series1;
	private Series<Number,Number> series2;	
	
	
	
	
	
	
	
	public void chartCreator(List<Cell> board){
		
		HashMap<Integer, Integer> possValues = new HashMap<Integer, Integer>();
		
		for(Cell c : board){
			if (possValues.keySet().size() > 2){
				break;
			}
			Integer myVal = possValues.get(c.getValue().getVal());
			if (myVal != null){
					myVal +=1;
					possValues.put(possValues.get(c.getValue().getVal()),myVal);
			}else{
				possValues.put(c.getValue().getVal(),1);
			}
		}
		series0 = new Series<Number,Number>();
		series1 = new Series<Number,Number>();
		series2 = new Series<Number,Number>();
		
		if(board.get(0) instanceof WaTorCell ){
				series0.setName("EMPTY");
				series1.setName("FISH");
				series2.setName("SHARKS");
				series2.getData().add(new XYChart.Data<>(0,possValues.get(2)));
			}else if(board.get(0) instanceof FireCell){
				if(possValues.get(0) == null){
					possValues.put(0,0);
				}
				series0.setName("BURNT TREES");
				series1.setName("UNTOUCHED TREES");
				series2.setName("BURNING TREES");
				series2.getData().add(new XYChart.Data<>(0,possValues.get(2)));
			}else if(board.get(0) instanceof ReproductionCell){
				series0.setName("EMPTY");
				series1.setName("LIVE CELLS");
			}
		
		series0.getData().add(new XYChart.Data<>(0,possValues.get(0) ));
		series1.getData().add(new XYChart.Data<>(0,possValues.get(1)));		
		myXAxis = new NumberAxis(myChartResources.getString("xaxis"), 0 , 50, 10);
		myXAxis.setForceZeroInRange(false);
		myYAxis = new NumberAxis(0,100, 10);
		myYAxis.setLabel(myChartResources.getString("yaxis"));
		myYAxis.setForceZeroInRange(true);
		
		myChart = new LineChart<Number,Number>(myXAxis,myYAxis);
		myChart.getData().add(series0);
		myChart.getData().add(series1);
		if(series2.getData() != null) myChart.getData().add(series2);
		myYAxis.setAutoRanging(true);
		
		
	}
	
	public void updateChart(List<Cell> board){
		
		HashMap<Integer, Integer> cellValues = new HashMap<Integer, Integer>();
		
		for(Cell c : board){
			if (cellValues.keySet().size() > 2){
				break;
			}
			Integer myVal = cellValues.get(c.getValue().getVal());
			if (myVal != null){
					myVal +=1;
					cellValues.put(c.getValue().getVal(),myVal);
			}else{
				cellValues.put(c.getValue().getVal(),1);
			}
		}
		
		
		Series<Number, Number> serTemp0 = (Series) myChart.getData().get(0);
		Series<Number, Number> serTemp1 = (Series) myChart.getData().get(1);
		Series<Number, Number> serTemp2 = null;
		
		if(cellValues.get(2) != null){
			serTemp2 = (Series) myChart.getData().get(2);
			serTemp2.getData().add(new Data<Number, Number>(updateCount, cellValues.get(2)));
		}
		
		serTemp0.getData().add(new Data<Number, Number>(updateCount, cellValues.get(0)));
		serTemp1.getData().add(new Data<Number, Number>(updateCount, cellValues.get(1)));
		
		if (serTemp0.getData().size() >= 50){
		myXAxis.setLowerBound(myXAxis.getLowerBound()+1);
		myXAxis.setUpperBound(myXAxis.getUpperBound()+1);
		serTemp0.getData().remove(0);
		serTemp1.getData().remove(0);
		
		if(serTemp2 != null){
			serTemp2.getData().remove(0);
		}}
		
		updateCount++;	
	}
	
	public LineChart<Number, Number> getMyChart(){
		return myChart;
	}

	

}
