/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Laboratory.
 * =========================================================
 *
 * (C) Copyright 2007-2013, Pascal Seppecher and contributors.
 * 
 * Project Info <http://p.seppecher.free.fr/jamel/javadoc/index.html>. 
 *
 * This file is a part of JAMEL (Java Agent-based MacroEconomic Laboratory).
 * 
 * JAMEL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JAMEL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JAMEL. If not, see <http://www.gnu.org/licenses/>.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates.]
 */

package jamel;

import jamel.util.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ProgressMonitor;

import org.jfree.data.time.Month;

/**
 * A simulator for the sensitivity analyses.
 */
public class Analyst extends AbstractSimulator {

	/**
	 * The main method.
	 * @param args  the arguments.
	 */
	public static void main(String[] args) {
		final File file = selectScenario();
		if (file!=null) {
			LinkedList<String> parameters = new LinkedList<String>();
			try {
				Scanner scanner=new Scanner(file);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					parameters.add(line);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("File not found.");
			}
			new Analyst(file.getName(),parameters);
		}
	}

	/** The basic scenario: a list of strings representing an instruction of the scenario. */
	final private LinkedList<String> basicScenario;

	/** The max num of simulation. */
	private int simMax;

	/** The num of the current sim. */
	private int simulationID;

	/** The variables of the analysis. */
	final private HashMap<String,String> variables;

	/**
	 * Creates a new simulator.
	 * @param name  the name of the simulation.
	 * @param aParameters  the parameters of the simulation.
	 */

	public Analyst(String name, LinkedList<String> aParameters) {
		this.name=name;
		setNewOutputFile();
		this.basicScenario = new LinkedList<String>(); // Creates a new empty scenario.
		this.variables = new HashMap<String,String>(); // Creates a new empty map that contains variables.
		initAnalysis(aParameters); // Extract analysis parameters and fills the scenario with the instructions. 
		final long start = (new Date()).getTime();

		final ProgressMonitor progressMonitor = new ProgressMonitor(null,
				"Running "+name,
				"", 0,simMax);
		progressMonitor.setMillisToDecideToPopup(0);

		JamelObject.setRandom(new Random(0));
		for(this.simulationID=0; this.simulationID<=simMax; this.simulationID++) {
			progressMonitor.setProgress(simulationID);
			progressMonitor.setNote("Simulation "+simulationID+" on "+simMax);
			newSimulation(name,simulationID);
		}
		final String msg = "Duration: "+((new Date()).getTime()-start)/1000.+" s.";
		export(msg);
		progressMonitor.close();
	}

	/**
	 * Returns a scenario.
	 * @return  a list of strings.
	 */
	private LinkedList<String> getScenario() {
		final LinkedList<String> scenario = new LinkedList<String>() ;
		final HashMap<String,Number> mVariables = new HashMap<String,Number>();
		for(Map.Entry<String, String> entry : this.variables.entrySet()) {
			final String key = entry.getKey();
			final String[] value = entry.getValue().split("-",3);
			if (value[0].equals("integer")) {
				final int min = Integer.parseInt(value[1]);
				final int max = Integer.parseInt(value[2]);
				final int val=min+JamelObject.getRandom().nextInt(max-min+1);
				mVariables.put(key, val);
			}
			else if (value[0].equals("float")) {
				final float min = Float.parseFloat(value[1]);
				final float max = Float.parseFloat(value[2]);
				final float val=min+JamelObject.getRandom().nextFloat()*(max-min);
				mVariables.put(key, val);				
			}
			else
				throw new RuntimeException("Unexpected number format: "+value[0]);
		}		
		for (String string: this.basicScenario) {
			for(Map.Entry<String, Number> entry : mVariables.entrySet()) {
				final String key = entry.getKey();
				final Number value = entry.getValue();
				if (value.equals(value.intValue()))
					string = string.replace(key, ""+value.intValue());
				else
					string = string.replace(key, ""+value.floatValue());
			}
			scenario.add(string);
			System.out.println(string);//DELETE
		}
		System.out.println();//DELETE
		return scenario;
	}

	/**
	 * Etablit le sc�nario de base des simulations.
	 * Les donn�es variables sont extraites et enregistr�es dans les champs correspondants.
	 * @param aParameters  une liste de chaines contenant des instructions.
	 */
	private void initAnalysis(LinkedList<String> aParameters) {
		for (String line: aParameters) {
			final String[] temp1 = line.split("\\(", 2);
			if (temp1.length==2) {
				if (temp1[0].trim().equals("Analyst.set")) {
					final String[] temp2 = temp1[1].trim().split("\\)", 2);
					final String[] temp3 = temp2[0].trim().split(":", 2);
					final String key = temp3[0].trim(); 
					if (key.equals("simMax")) {
						this.simMax= Integer.parseInt(temp3[1]);						
					}
					else {
						this.variables.put(key, temp3[1]);
					}
				}
				else if (temp1[0].trim().equals("Analyst.export")) {
					final String[] temp2 = temp1[1].trim().split("\\)", 2);
					export(temp2[0]);
				}
				else {
					this.basicScenario.add(line);
				}
			}		
		}
	}

	/**
	 * Runs a new simulation.
	 * @param name  the name of the scenario.
	 * @param randomSeed  the randomSeed.
	 */
	private void newSimulation(String name,int randomSeed) {
		JamelObject.setTimer(new Timer());
		JamelObject.setScenarioFileName(name+" "+randomSeed);
		final Circuit circuit = new Circuit(this, getScenario());
		circuit.setOutputFile(outputFile);// DELETE le cicuit ne devrait pas avoir acces a outputfile
		this.run=true;
		while (this.run) {
			circuit.doPeriod();
		}
	}

	@Override
	int getSimulationId() {
		return this.simulationID;
	}

	/**
	 * Stops the simulation before the normal ending.
	 */
	@Override
	public void failure() {
		run = false;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void marker(String label, Month month) {
	}

	/**
	 * Stops the simulation.
	 */
	@Override
	public void pause(boolean b) {
		run = false;// TODO � revoir, c'est moche
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void println(String message) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void setChart(int tabIndex, int panelIndex, String chartPanelName) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void setVisiblePanel(int index) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void zoom(int aZoom) {
	}	

}
