package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Represents a domain class for storing information about a OfficeSpace entity.
 * This class is mapped to the "OfficeSpaces" table in the database using JPA
 * annotations. The primary key of this table is "propertyId". Since
 * "propertyId" is inherited from the Property entity it also represents a
 * foreign key that references the "Properties" table.
 * 
 * The OfficeSpace entity contains its capacity and a list of the utility leases
 * associated with the OfficeSpace entity.
 * 
 * This entity is a child class of the Property class. This means that it also
 * contains all the fields of the Property class.
 * 
 * @author Nikola Dronjak
 */
@Entity
@Table(name = "OfficeSpaces")
@PrimaryKeyJoinColumn(name = "propertyId")
public class OfficeSpace extends Property {

	/**
	 * Represents the number of people that the office space can hold (Integer).
	 * 
	 * The capacity cannot be null and it has to be a positive value (greater than
	 * 0).
	 */
	@NotNull(message = "The size of the office space is required.")
	@Positive(message = "The size of the office space has to be a positive value.")
	private Integer capacity;

	/**
	 * Represents the list of utility leases with which the office space is
	 * associated (List&lt;UtilityLease&gt;).
	 */
	@OneToMany(mappedBy = "property")
	private List<UtilityLease> utilityLeases;

	public OfficeSpace() {

	}

	public OfficeSpace(int propertyId, String name, String address, String description, Double rentalRate, Integer size,
			Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			Integer capacity, List<UtilityLease> utilityLeases) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.capacity = capacity;
		this.utilityLeases = utilityLeases;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public List<UtilityLease> getUtilityLeases() {
		return utilityLeases;
	}

	public void setUtilityLeases(List<UtilityLease> utilityLeases) {
		this.utilityLeases = utilityLeases;
	}

	@Override
	public String toString() {
		return "OfficeSpace [capacity=" + capacity + ", utilityLeases=" + utilityLeases + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capacity, utilityLeases);
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
		OfficeSpace other = (OfficeSpace) obj;
		return super.equals(other) && Objects.equals(capacity, other.capacity)
				&& Objects.equals(utilityLeases, other.utilityLeases);
	}
}
