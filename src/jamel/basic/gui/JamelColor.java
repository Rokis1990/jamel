package jamel.basic.gui;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.ChartColor;

/**
 * Enumerates the colors used by the charts.
 */
public enum JamelColor {

	/** black */
	black(Color.black),

	/** blue */
	blue(Color.blue),

	/** cyan */
	cyan(Color.cyan),

	/** gray */
	gray(Color.gray),

	/** green */
	green(Color.green),

	/** magenta */
	magenta(Color.magenta),

	/** orange */
	orange(Color.orange),

	/** red */
	red(Color.red),

	/** transparent blue */
	transparentBlue(new Color(0x80, 0x80, 0xFF, 100)),

	/** transparent red */
	transparentRed(new Color(0xFF, 0x80, 0x80, 100)),

	/** very light blue */
	veryLightBlue(ChartColor.VERY_LIGHT_BLUE),

	/** very light green */
	veryLightGreen(ChartColor.VERY_LIGHT_GREEN),

	/** very light red */
	veryLightRed(ChartColor.VERY_LIGHT_RED),

	/** white */
	white(Color.white),

	/** yellow */
	yellow(Color.yellow);

	/**
	 * Returns the specified color.
	 * @param name the name of the color to return.
	 * @return a color.
	 */
	public static Color getColor(String name) {
		return valueOf(name).color;
	}

	/**
	 * Returns an array of paints with the specified colors.
	 * @param colorKeys the keys of the paints to return.
	 * @return an array of paints.
	 */
	public static Paint[] getColors(String... colorKeys) {
		final Paint[] colors;
		if (colorKeys.length>0){
			colors = new Paint[colorKeys.length];
			for (int count = 0; count<colors.length ; count++){
				colors[count] = JamelColor.getColor(colorKeys[count]);
			}
		}
		else {
			colors = null;
		}
		return colors;
	}

	/** The color. */
	private final Color color;

	/**
	 * Creates a color.
	 * @param color the color to create.
	 */
	private JamelColor(Color color){
		this.color=color;
	}

	/**
	 * Returns the color.
	 * @return the color.
	 */
	@SuppressWarnings("unused")
	private Color get() {
		return this.color;
	}

}