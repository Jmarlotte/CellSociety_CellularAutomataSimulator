package cellsociety_team06;

public interface SimulationDisplayDelegate {
	public void stepSimulation();
	public void resetSimulation();
	public void changeSimulationSpeed(double newRate);
	public void resumeSimulation();
	public void pauseSimulation();
	public void setSimulationFileName(String newSim);
}
