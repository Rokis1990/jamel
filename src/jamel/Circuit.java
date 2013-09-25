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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.jfree.date.MonthConstants;

import jamel.agents.firms.Firm;
import jamel.agents.firms.FirmsSector;
import jamel.agents.firms.ProductionType;
import jamel.agents.households.HouseholdsSector;
import jamel.agents.roles.AccountHolder;
import jamel.agents.roles.CapitalOwner;
import jamel.agents.roles.Employer;
import jamel.agents.roles.Provider;
import jamel.spheres.monetarySphere.Account;
import jamel.spheres.monetarySphere.Bank;
import jamel.util.BalanceSheetMatrix;
import jamel.util.data.CrossSectionSeries;
import jamel.util.data.PeriodDataset;
import jamel.util.data.TimeseriesCollection;
import jamel.util.data.YearDataset;

/**
 * Represents the macroeconomic circuit.
 */
public class Circuit extends JamelObject {

	/** The circuit. */
	private static Circuit circuit;

	/**
	 * Returns the circuit.
	 * @return the circuit.
	 */
	public static Circuit getCircuit() {
		return circuit;
	}

	/**
	 * Returns a list of employers selected at random.
	 * @param size  the number of employers to select.
	 * @return a collection of employers.
	 */
	public static Collection<? extends Employer> getEmployerCollection(int size) {
		return circuit.firms.getRandomFirms(size);
	}

	/**
	 * Returns a list of employers selected at random in the specified sector.
	 * @param size  the number of employers to select.
	 * @param sector  the sector of the employers.
	 * @return a collection of employers.
	 */
	public static Collection<? extends Employer> getEmployerCollection(int size, ProductionType sector) {
		return circuit.firms.getRandomFirms(size,sector);
	}

	/**
	 * Returns a new bank account for the given account holder.
	 * @param holder - the new account holder.
	 * @return the new account.
	 */
	public static Account getNewAccount(AccountHolder holder) {
		return circuit.bank.getNewAccount(holder);
	}

	/**
	 * Returns the parameter to which the specified key is mapped, or null if the circuit contains no mapping for the key.
	 * @param key the key whose associated parameter is to be returned.
	 * @return the parameter to which the specified key is mapped, or null if the circuit contains no mapping for the key.
	 */
	public static String getParameter(String key) {
		final String value = circuit.parameters.get(key);
		if (value==null) {
			final String message = "Missing parameter: \""+key+"\"";
			JOptionPane.showMessageDialog(null,
					message,
					"Scenario error",
					JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException(message);
		}
		return value;
	}

	/**
	 * Mmmm... 
	 * @param aParameters - a list of strings representing parameters.
	 * @param key - the key.
	 * @param separator - the separator.
	 * @return a list of string.
	 * DELETE cette methode quand elle sera inutile.
	 */
	public static LinkedList<String> getParametersList(LinkedList<String> aParameters, String key, String separator) {
		final LinkedList<String> pList = new LinkedList<String>();
		for (String line: aParameters) {
			final String[] temp = line.split(separator, 2);
			if (temp.length==2) {
				if (temp[0].equals(key)) {
					final String parameters = temp[1];
					pList.add(parameters);
				}
			}
		}
		return pList;
	}

	/**
	 * Returns an agent of type <code>CapitalOwner</code> selected at random.
	 * @return an agent of type <code>CapitalOwner</code>.
	 */
	public static CapitalOwner getRandomCapitalOwner() {
		return circuit.households.selectRandomCapitalOwner();
	}

	/**
	 * Returns an agent of type <code>Employer</code> selected at random. 
	 * @return an agent of type <code>Employer</code>.
	 */
	public static Employer getRandomEmployer() {
		return circuit.firms.getRandomEmployer();
	}

	/**
	 * Returns a firm selected at random.
	 * @return a Firm.
	 */
	public static Firm getRandomFirm() {
		return circuit.firms.getRandomFirm();
	}

	/**
	 * Returns a list of firms selected at random.
	 * @param size the number of firms to select.
	 * @return a list of firms.
	 */
	public static Collection<? extends Firm> getRandomFirms(int size) {
		return circuit.firms.getRandomFirms(size);
	}

	/**
	 * Returns a list of firms selected at random.
	 * @param i the number of firms to select.
	 * @param string the type of production of the firm to select ("intermediate" or "final").
	 * @return a collection of firms.
	 */
	public static List<Firm> getRandomFirms(int i, String string) {
		ProductionType sector ;
		if (string.equals("final"))
			sector = ProductionType.finalProduction;			
		else if (string.equals("integrated"))
			sector = ProductionType.integratedProduction;
		else if (string.equals("intermediate"))
			sector = ProductionType.intermediateProduction;
		else
			throw new RuntimeException("Error while searching for firms. Unknown type of production: "+string);

		return new LinkedList<Firm>(circuit.firms.getRandomFirms(i,sector));
	}

	/**
	 * Returns a provider of final goods selected at random. 
	 * @return a provider of final goods.
	 */
	public static Provider getRandomProviderOfFinalGoods() {
		return circuit.firms.getRandomProviderOfFinalGoods();
	}

	/**
	 * Returns a provider of raw materials selected at random. 
	 * @return a provider of raw materials.
	 */
	public static Provider getRandomProviderOfRawMaterials() {
		return circuit.firms.getRandomProviderOfRawMaterials();
	}

	/**
	 * Adds a new event to the scenario.
	 * @param event  a string that contains the description of the event.
	 */
	public static void newEvent(String event) {
		circuit.scenario.add(event);
	}

	/**
	 * Sets the sate of the simulation (paused or running).
	 * @param b a flag that indicates whether or not the simulation must be paused.
	 */
	public static void pause(boolean b) {
		circuit.simulator.pause(b);
	}

	/**
	 * Prints a String in the console panel.
	 * @param message the String to be printed.
	 */
	public static void println(String message) {
		circuit.simulator.println(message);
	}

	/** The bank. */
	private final Bank bank ;

	/** The cross-section series. */
	private final CrossSectionSeries crossSectionSeries;

	/** The collection of the data of the circuit, in a chronological order. */
	private final LinkedList<PeriodDataset> data = new LinkedList<PeriodDataset>();

	/** The firms sector. */
	private final FirmsSector firms ;

	/** The households sector. */
	private final HouseholdsSector households ;

	/** The balance sheet matrix. */
	private final BalanceSheetMatrix matrix = new BalanceSheetMatrix();

	/** The output file. */
	private File outputFile;

	/** The parameters of the simulation. */
	private final HashMap<String,String> parameters = new HashMap<String,String>();

	/** The random seed. */
	private int randomSeed;

	/** The list of parameters and events for the circuit. */
	final private LinkedList<String> scenario;

	/** The simulator. */
	private final AbstractSimulator simulator;

	/** The time series collection. */
	private final TimeseriesCollection timesSeriesCollection;

	/** A dataset that contains aggregate data of the last year. */
	private YearDataset yearDataset;

	/**
	 * Creates a new circuit.
	 * @param aSimulator  the simulator.
	 * @param aScenario  a list of strings representing parameters and events.
	 */
	public Circuit(AbstractSimulator aSimulator, LinkedList<String> aScenario) {
		circuit = this;
		simulator = aSimulator;
		this.scenario = getParametersList(aScenario,"Circuit","\\.");
		this.crossSectionSeries = new CrossSectionSeries();
		this.timesSeriesCollection = new TimeseriesCollection();
		this.bank = new Bank();
		this.households = new HouseholdsSector();
		this.firms = new FirmsSector();
	}

	/**
	 * Closes the circuit.<br>
	 * Each sector composing the circuit is closed and data are updated.
	 */
	private void close() {
		final PeriodDataset periodDataset = new PeriodDataset();
		periodDataset.randomSeed = this.randomSeed;
		this.data.add(periodDataset);
		if (this.data.size()>12) {
			this.data.removeFirst();
			if (this.data.size()>12) 
				throw new RuntimeException("Too many dataset.");
		}
		households.close(periodDataset) ;
		firms.close(periodDataset) ;
		bank.close(periodDataset) ;	
		periodDataset.updateRatios() ;
		this.timesSeriesCollection.updateSeries(periodDataset);
		if (getCurrentPeriod().isMonth(MonthConstants.DECEMBER)){
			this.yearDataset = new YearDataset(yearDataset);
			this.yearDataset.update(this.data);
			this.timesSeriesCollection.updateSeries(yearDataset);
		}
		matrix.clear();
		matrix.setDate(getCurrentPeriod().toString());
		matrix.setFirmsInventories(periodDataset.INVENTORY_VALUE+periodDataset.INVENTORY_UNF_VALUE);
		matrix.setFirmsDeposits(periodDataset.FIRMS_DEPOSITS);
		matrix.setHouseholdsDeposits(periodDataset.HOUSEHOLDS_DEPOSITS);
		matrix.setBankDeposits(periodDataset.DEPOSITS);
		matrix.setFirmsLoans(periodDataset.LOANS);
		matrix.setBankLoans(periodDataset.LOANS);
		matrix.update();
		if (bank.isBankrupt())
			simulator.failure();
	}

	/**
	 * Opens the circuit.
	 */
	private void open() {				
		final LinkedList<String> eList = getParametersList(this.scenario, getCurrentPeriod().toString(), "\\.");
		if (!eList.isEmpty()) {
			for (String string: eList){
				if (string.equals("pause()")) 
					simulator.pause(true);
				else {
					try {
						String[] word = string.split("\\)",2);
						String[] event = word[0].split("\\(",2);
						if (event[0].equals("setRandomSeed")) {
							final int seed = Integer.parseInt(event[1]);
							setRandom(new Random(seed));
							//getRandom().setSeed(seed);
							this.randomSeed = seed;						
						}
						else if (event[0].equals("set"))
							setParameters(event[1]);
						else if (event[0].equals("newHouseholds")) {
							this.households.newHouseholds(event[1].split(","));
						}
						else if (event[0].equals("newFirms")) {
							this.firms.newFirms(event[1]);				
						}
						else if (event[0].equals("setWindowRange")) {
							simulator.zoom(Integer.parseInt(event[1]));
						}
						else if (event[0].equals("exportAnnualData")) {
							simulator.export(this.yearDataset,event[1]);
						}
						else if (event[0].equals("exportMonthlyData")) {
							simulator.export(this.data.getLast(),event[1]);
						}
						else if (event[0].equals("exportYearDataEachYear")) {
							simulator.export(event[1]);
							simulator.export(this.yearDataset,event[1]);
							final String s=getCurrentPeriod().getNewPeriod(12).toString()+".exportYearDataEachYear2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("exportYearDataEachYear2")) {
							simulator.export(this.yearDataset,event[1]);
							final String s=getCurrentPeriod().getNewPeriod(12).toString()+".exportYearDataEachYear2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("exportMonthlyDataEachPeriod")) {
							simulator.export(event[1]);
							simulator.export(this.data.getLast(),event[1]);
							final String s=getCurrentPeriod().next().toString()+".exportMonthlyDataEachPeriod2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("exportMonthlyDataEachPeriod2")) {
							simulator.export(this.data.getLast(),event[1]);
							final String s=getCurrentPeriod().next().toString()+".exportMonthlyDataEachPeriod2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("print")) {
							throw new RuntimeException("Invalid instruction: \"print\".");// DELETE ??? to verify
							//this.write(event[1].split(","));
						}
						else if (event[0].equals("printEachFirm")) {
							String context = "randomSeed,period,date";
							this.writeString(context);
							this.write(context.split(","));
							this.writeString(event[1]);
							this.firms.printEach(outputFile,event[1].split(","));
						}
						else if (event[0].equals("printEachPeriod")) {
							this.writeString(event[1]);
							this.write(event[1].split(","));
							final String s=getCurrentPeriod().next().toString()+".printEachPeriod2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("printEachPeriod2")) {
							this.write(event[1].split(","));
							final String s=getCurrentPeriod().next().toString()+".printEachPeriod2("+event[1]+")";
							this.scenario.addLast(s);
						}
						else if (event[0].equals("marker"))
							simulator.marker(event[1],getCurrentPeriod().getMonth());
						else if (event[0].equals("setChart")) {
							String[] parameters = event[1].split(",", 3);
							simulator.setChart(Integer.parseInt(parameters[0]),Integer.parseInt(parameters[1]),parameters[2]);
						}
						else if (event[0].equals("setPanel")) {
							simulator.setVisiblePanel(Integer.parseInt(event[1]));
						}
						else 
							throw new RuntimeException("Unknown event \""+event[0]+"\".");
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (NumberFormatException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (SecurityException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (InstantiationException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						warningDialog("Circuit event: Error in the instruction \""+string+"\".<br>Date: "+getCurrentPeriod().toString()+".<br>",e.toString());
					}
				}
			}
		}
		firms.open() ;					
		bank.open() ;					
		households.open() ;				
	}

	/**
	 * Displays a warning message.
	 * @param string the message.
	 * @param string2 the cause.
	 */
	static private void warningDialog(String string, String string2) {
		JOptionPane.showMessageDialog(null,
				"<html>" +
						string+
						"Cause: "+string2+"<br>"+
						"Please see server.log for more details.</html>",
						"Warning",
						JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Sets the parameters of the circuit.
	 * @param parameters  an array of strings that contain parameters.
	 */
	private void setParameters(String parameters) {
		final String[] line = parameters.split(":",2);
		final String radical = line[0];
		final String[] values = line[1].split(",");
		for(final String param:values) {
			final String[] parameter = param.split("=",2);
			this.parameters.put(radical+"."+parameter[0], parameter[1]);
		}		
	}

	/**
	 * Imprime dans le fichier de sortie les variables demand�es.
	 * Utilis� pour les analyses de sensibilit�.
	 * @param keyArray  le tableau qui contient le nom des variables � imprimer.
	 * TODO v�rifier l'utilit�.
	 */
	@SuppressWarnings("static-method")
	private void write(String[] keyArray) {		
		throw new RuntimeException("This method should not be called.");
		/*try {
			final FileWriter writer = new FileWriter(outputFile,true);
			for (String key:keyArray) {
				String value = null;
				try {
					value=this.data.getLast().getFieldValue(key).toString();
				} catch (NoSuchFieldException e) {
					value="No Such Field: "+key;
				}
				writer.write(value+",");
			}
			writer.write(rc);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error while writing data in the output file.");
		}*/
	}

	/**
	 * Prints the given string in the output file.
	 * Used to print the header of the data in csv files. 
	 * @param s  the string to print.
	 */
	private void writeString(String s) {
		try {
			final FileWriter writer = new FileWriter(outputFile,true);
			writer.write(s);
			writer.write(System.getProperty("line.separator"));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error while writing data in the output file.");
		}
	}

	/**
	 * Executes a period a the circuit.<br>
	 * A period is defined as the time between two consecutive payments of income. 
	 */
	public void doPeriod() {
		this.nextPeriod();
		this.open() ;
		this.bank.payDividend() ;
		this.firms.payDividend() ;
		this.firms.planProduction() ;
		this.households.jobSearch() ;	
		this.firms.production() ;
		this.firms.buyRawMaterials();
		this.households.consume() ;
		this.bank.debtRecovery() ;// essayer de placer �a en tout d�but de p�riode.
		this.close() ;
	}

	/**
	 * Returns the cross-section series dataset.
	 * @return the cross-section series dataset.
	 */
	public CrossSectionSeries getCrossSectionSeries() {
		return this.crossSectionSeries;
	}

	/**
	 * Returns the html representation of the balance sheet matrix for the current period.
	 * @return a string that contains the html matrix. 
	 */
	public String getHtmlMatrix() {
		return matrix.toHtml();
	}

	/**
	 * Returns a wage at random.
	 * @return a double that represents the wage.
	 */
	public Double getRandomWage() {
		if (data.size()==0) return null;
		final PeriodDataset lastDatatSet = data.getLast();
		if (lastDatatSet==null) return null;
		final LinkedList<Long> wagesList = lastDatatSet.wagesList;
		if (wagesList.size()==0) return null; 
		return lastDatatSet.wagesList.get(getRandom().nextInt(lastDatatSet.wagesList.size())).doubleValue();
	}

	/**
	 * @return the series.
	 */
	public TimeseriesCollection getTimeSeries() {
		return this.timesSeriesCollection ;
	}

	/**
	 * Destroys the components of the circuit.
	 */
	public void kill() {
		//this.bank.kill();
		this.firms.kill();
		this.timesSeriesCollection.clear();
		this.crossSectionSeries.clear();
	}

	/**
	 * Sets the output file.
	 * @param outputFile the output file.
	 */
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

}