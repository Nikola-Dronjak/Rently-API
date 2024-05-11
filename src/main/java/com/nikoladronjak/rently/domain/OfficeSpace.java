package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "OfficeSpaces")
@PrimaryKeyJoinColumn(name = "propertyId")
public class OfficeSpace extends Property {

	private int capacity;

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
