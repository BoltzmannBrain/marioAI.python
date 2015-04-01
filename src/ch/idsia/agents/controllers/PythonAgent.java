package ch.idsia.agents.controllers;

public interface PythonAgent {

	public void reset();
	public boolean _dangerOfGap();
	public String _a2();
	public Integer getAction();
	public void integrateObservation();
	public void printLevelScene();
	public String mapELToStr();
	public void printObs();
}
