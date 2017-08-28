package jamel.v170804.util;

/**
 * An {@code Amount} with public mutators.
 */
public class BasicAmount extends Amount{

	/**
	 * Cancels this amount.
	 * 
	 * Sets the amount to zero.
	 */
	@Override
	public void cancel() {
		super.cancel();
	}

	/**
	 * Removes the specified value from this amount.
	 * 
	 * @param subtrahend
	 *            the amount to be removed.
	 */
	@Override
	public void minus(long subtrahend) {
		super.minus(subtrahend);
	}

	/**
	 * Adds the specified value to this amount.
	 * 
	 * @param addend
	 *            the value to be added.
	 */
	@Override
	public void plus(long addend) {
		super.plus(addend);
	}

}
