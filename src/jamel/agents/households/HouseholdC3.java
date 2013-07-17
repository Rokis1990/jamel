/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Laboratory.
 * =========================================================
 *
 * (C) Copyright 2007-2013, Pascal Seppecher.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */


/*
 * Un m�nage dont la mobilit� sectorielle augmente avec la dur�e pass�e au ch�mage.
 * Creation le 14-07-13.
 * La stabilit� du mod�le appara�t tr�s sensible � la r�sistance � la mobilit� des agents:
 * une cyclicit� importante qui conduit rapidement � la crise.
 */
package jamel.agents.households;

import jamel.Circuit;
import jamel.agents.roles.Employer;

import java.util.Collections;
import java.util.LinkedList;

/**
 * A household that waits a certain time of unemployment before considering job offers from the other sector.
 */
public class HouseholdC3 extends HouseholdTypeB {
	
	/** 
	 * The number of periods before considering jog offers from the other sector. 
	 */
	protected int mobilityResistance;
	
	/**
	 * Creates a new household.
	 * @param aName  the name.
	 */
	public HouseholdC3(String aName) {
		super(aName);
		this.mobilityResistance = 3; // TODO should be a parameter.
	}

	
	/**
	 * Updates the list of employers.
	 */
	@Override
	protected void updateEmployersList() {
		final LinkedList<Employer> newListOfEmployers = new LinkedList<Employer>();
		for (Employer employer: this.employers){
			if (!employer.isBankrupt())
				newListOfEmployers.add(employer);
		}
		this.employers.clear();
		if (mobilityResistance*getRandom().nextFloat()>this.unemploymentDuration) {
			this.employers.addAll(Circuit.getEmployerCollection(maxEmployers,this.sector));
		}
		else {
			this.employers.addAll(Circuit.getEmployerCollection(maxEmployers,null));
		}
		Collections.sort(this.employers, EMPLOYER_COMPARATOR);
	}
	
}
