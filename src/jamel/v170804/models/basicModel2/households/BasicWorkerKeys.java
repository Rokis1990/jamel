package jamel.v170804.models.basicModel2.households;

import jamel.v170804.data.AbstractDataKeys;

/**
 * Data keys for the {@code BasicWorker}.
 */
public class BasicWorkerKeys extends AbstractDataKeys {
	
	@SuppressWarnings("javadoc")
	final public int consumptionBudget = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int consumptionValue = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int consumptionVolume = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int count = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int employed = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int employmentDuration = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int hiring = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int isInvalid = getNextIndex();// TODO remove me

	@SuppressWarnings("javadoc")
	final public int isNull = getNextIndex();// TODO remove me

	@SuppressWarnings("javadoc")
	final public int isValid = getNextIndex();// TODO remove me

	@SuppressWarnings("javadoc")
	final public int money = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int reservationWage = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int sinceStart = getNextIndex();// TODO remove me

	@SuppressWarnings("javadoc")
	final public int unempDuration = getNextIndex();

	@SuppressWarnings("javadoc")
	final public int wage = getNextIndex();

	/**
	 * Creates a new set of data keys.
	 */
	BasicWorkerKeys() {
		this.init(this.getClass().getFields());
	}

}