package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

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
	 * Represents the number of people that the office space can hold (int).
	 */
	private int capacity;

	/**
	 * Represents the list of utility leases with which the office space is
	 * associated (List&lt;UtilityLease&gt;).
	 */
	@OneToMany(mappedBy = "property")
	private List<UtilityLease> utilityLeases;

	public OfficeSpace() {

	}

	public OfficeSpace(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			int capacity, List<UtilityLease> utilityLeases) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.capacity = capacity;
		this.utilityLeases = utilityLeases;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
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
		return "OfficeSpace [propertyId=" + getPropertyId() + ", name=" + getName() + ", address=" + getAddress()
				+ ", description=" + getDescription() + ", rentalRate=" + getRentalRate() + ", size=" + getSize()
				+ ", isAvailable=" + isAvailable() + ", numberOfParkingSpots=" + getNumberOfParkingSpots() + ", photos="
				+ String.join(", ", getPhotos()) + ", owner=" + getOwner() + ", leases="
				+ getLeases().stream().map(Lease::toString).collect(Collectors.joining(", ")) + "capacity=" + capacity
				+ ", utilityLeases="
				+ utilityLeases.stream().map(UtilityLease::toString).collect(Collectors.joining(", ")) + "]";
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
		return capacity == other.capacity && Objects.equals(utilityLeases, other.utilityLeases);
	}
}
