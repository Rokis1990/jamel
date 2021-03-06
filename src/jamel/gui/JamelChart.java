package jamel.gui;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.ValueMarker;

import jamel.util.Simulation;

/**
 * A convenient extension of JFreeChart.
 */
public abstract class JamelChart extends JFreeChart {

	/** The font for displaying the chart titles. */
	private static final Font titleFont = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * The simulation.
	 */
	private Simulation simulation;

	/**
	 * Creates a new chart based on the supplied plot.
	 * 
	 * @param title
	 *            the chart title (<code>null</code> permitted).
	 * @param plot
	 *            the plot (null not permitted).
	 * @param simulation
	 *            the simulation.
	 */
	public JamelChart(String title, Plot plot, Simulation simulation) {
		super(title, titleFont, plot, true);
		this.simulation = simulation;
		this.setBackgroundPaint(null);
		this.getLegend().setFrame(new BlockBorder(0.5, 0.5, 0.5, 0.5, Color.gray));
		// this.getLegend().setItemFont(legendItemFont);
		this.setNotify(this.simulation.isPaused());
	}

	/**
	 * Adds a marker to the chart.
	 * 
	 * @param marker
	 *            the marker to be added (<code>null</code> not permitted).
	 */
	public abstract void addTimeMarker(ValueMarker marker);

	/**
	 * Updates the chart.
	 */
	public void update() {
		this.setNotify(true);
		if (!this.simulation.isPaused()) {
			this.setNotify(false);
		}

	}

}
