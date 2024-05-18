package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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
	 * Represents the number of people that the event space can hold (Integer).
	 * 
	 * The capacity cannot be null and it has to be a positive value (greater than
	 * 0).
	 */
	@NotNull(message = "The size of the event space is required.")
	@Positive(message = "The size of the event space has to be a positive value.")
	private Integer capacity;

	/**
	 * Indicates whether the event space has a kitchen (Boolean).
	 * <ul>
	 * <li>True - The event space has a kitchen.</li>
	 * <li>False - The event space doesn't have a kitchen.</li>
	 * </ul>
	 * 
	 * The hasKitchen flag cannot be null.
	 */
	@NotNull(message = "You have to specify whether the event space has a kitchen or not.")
	private Boolean hasKitchen;

	/**
	 * Indicates whether the event space has a bar (Boolean).
	 * <ul>
	 * <li>True - The event space has a bar.</li>
	 * <li>False - The event space doesn't have a bar.</li>
	 * </ul>
	 * 
	 * The hasBar flag cannot be null.
	 */
	@NotNull(message = "You have to specify whether the event space has a bar or not.")
	private Boolean hasBar;

	/**
	 * Represents the list of utility leases with which the event space is
	 * associated (List&lt;UtilityLease&gt;).
	 */
	@OneToMany(mappedBy = "property")
	private List<UtilityLease> utilityLeases;

	public EventSpace() {

	}

	public EventSpace(int propertyId, String name, String address, String description, Double rentalRate, Integer size,
			Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			Integer capacity, Boolean hasKitchen, Boolean hasBar, List<UtilityLease> utilityLeases) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.capacity = capacity;
		this.hasKitchen = hasKitchen;
		this.hasBar = hasBar;
		this.utilityLeases = utilityLeases;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Boolean isHasKitchen() {
		return hasKitchen;
	}

	public void setHasKitchen(Boolean hasKitchen) {
		this.hasKitchen = hasKitchen;
	}

	public Boolean isHasBar() {
		return hasBar;
	}

	public void setHasBar(Boolean hasBar) {
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
		return "EventSpace [capacity=" + capacity + ", hasKitchen=" + hasKitchen + ", hasBar=" + hasBar
				+ ", utilityLeases=" + utilityLeases + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capacity, hasBar, hasKitchen, utilityLeases);
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
		return super.equals(other) && Objects.equals(capacity, other.capacity) && hasBar == other.hasBar
				&& Objects.equals(hasKitchen, other.hasKitchen) && Objects.equals(utilityLeases, other.utilityLeases);
	}
}
