package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

/**
 * Represents a domain class for storing information about a EventSpace entity.
 * This class is mapped to the "EventSpaces" table in the database using JPA
 * annotations. The primary key of this table is "propertyId". Since
 * "propertyId" is inherited from the Property entity it also represents a
 * foreign key that references the "Properties" table.
 * 
 * The EventSpace entity contains its capacity, a hasKitchen flag, a hasBar flag
 * and a list of utility leases associated with the EventSpace entity.
 * 
 * This entity is a child class of the Property class. This means that it also
 * contains all the fields of the Property class.
 * 
 * @author Nikola Dronjak
 */
@Entity
@Table(name = "EventSpaces")
@PrimaryKeyJoinColumn(name = "propertyId")
public class EventSpace extends Property {

	/**
	 * Represents the number of people that the event space can hold (int).
	 */
	private int capacity;

	/**
	 * Indicates whether the event space has a kitchen (boolean).
	 * <ul>
	 * <li>True - The event space has a kitchen.</li>
	 * <li>False - The event space doesn't have a kitchen.</li>
	 * </ul>
	 */
	private boolean hasKitchen;

	/**
	 * Indicates whether the event space has a bar (boolean).
	 * <ul>
	 * <li>True - The event space has a bar.</li>
	 * <li>False - The event space doesn't have a bar.</li>
	 * </ul>
	 */
	private boolean hasBar;

	/**
	 * Represents the list of utility leases with which the event space is
	 * associated (List&lt;UtilityLease&gt;).
	 */
	@OneToMany(mappedBy = "property")
	private List<UtilityLease> utilityLeases;

	public EventSpace() {

	}

	public EventSpace(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			int capacity, boolean hasKitchen, boolean hasBar, List<UtilityLease> utilityLeases) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.capacity = capacity;
		this.hasKitchen = hasKitchen;
		this.hasBar = hasBar;
		this.utilityLeases = utilityLeases;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isHasKitchen() {
		return hasKitchen;
	}

	public void setHasKitchen(boolean hasKitchen) {
		this.hasKitchen = hasKitchen;
	}

	public boolean isHasBar() {
		return hasBar;
	}

	public void setHasBar(boolean hasBar) {
		this.hasBar = hasBar;
	}

	public List<UtilityLease> getUtilityLeases() {
		return utilityLeases;
	}

	public void setUtilityLeases(List<UtilityLease> utilityLeases) {
		this.utilityLeases = utilityLeases;
	}

	@Override
	public String toString() {
		return "EventSpace [propertyId=" + getPropertyId() + ", name=" + getName() + ", address=" + getAddress()
				+ ", description=" + getDescription() + ", rentalRate=" + getRentalRate() + ", size=" + getSize()
				+ ", isAvailable=" + isAvailable() + ", numberOfParkingSpots=" + getNumberOfParkingSpots() + ", photos="
				+ String.join(", ", getPhotos()) + ", owner=" + getOwner() + ", leases="
				+ getLeases().stream().map(Lease::toString).collect(Collectors.joining(", ")) + "capacity=" + capacity
				+ ", hasKitchen=" + hasKitchen + ", hasBar=" + hasBar + ", utilityLeases="
				+ utilityLeases.stream().map(UtilityLease::toString).collect(Collectors.joining(", ")) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capacity, hasBar, hasKitchen);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventSpace other = (EventSpace) obj;
		return capacity == other.capacity && hasBar == other.hasBar && hasKitchen == other.hasKitchen;
	}
}
