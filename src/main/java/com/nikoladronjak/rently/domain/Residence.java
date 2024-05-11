package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "Residences")
@PrimaryKeyJoinColumn(name = "propertyId")
public class Residence extends Property {

	private int numberOfBedrooms;

	private int numberOfBathrooms;

	private HeatingType heatingType;

	private boolean isPetFriendly;

	private boolean isFurnished;

	public Residence() {

	}

	public Residence(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			int numberOfBedrooms, int numberOfBathrooms, HeatingType heatingType, boolean isPetFriendly,
			boolean isFurnished) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.numberOfBedrooms = numberOfBedrooms;
		this.numberOfBathrooms = numberOfBathrooms;
		this.heatingType = heatingType;
		this.isPetFriendly = isPetFriendly;
		this.isFurnished = isFurnished;
	}

	public int getNumberOfBedrooms() {
		return numberOfBedrooms;
	}

	public void setNumberOfBedrooms(int numberOfBedrooms) {
		this.numberOfBedrooms = numberOfBedrooms;
	}

	public int getNumberOfBathrooms() {
		return numberOfBathrooms;
	}

	public void setNumberOfBathrooms(int numberOfBathrooms) {
		this.numberOfBathrooms = numberOfBathrooms;
	}

	public HeatingType getHeatingType() {
		return heatingType;
	}

	public void setHeatingType(HeatingType heatingType) {
		this.heatingType = heatingType;
	}

	public boolean isPetFriendly() {
		return isPetFriendly;
	}

	public void setPetFriendly(boolean isPetFriendly) {
		this.isPetFriendly = isPetFriendly;
	}

	public boolean isFurnished() {
		return isFurnished;
	}

	public void setFurnished(boolean isFurnished) {
		this.isFurnished = isFurnished;
	}

	@Override
	public String toString() {
		return "Residence [propertyId=" + getPropertyId() + ", name=" + getName() + ", address=" + getAddress()
				+ ", description=" + getDescription() + ", rentalRate=" + getRentalRate() + ", size=" + getSize()
				+ ", isAvailable=" + isAvailable() + ", numberOfParkingSpots=" + getNumberOfParkingSpots() + ", photos="
				+ String.join(", ", getPhotos()) + ", owner=" + getOwner() + ", leases="
				+ getLeases().stream().map(Lease::toString).collect(Collectors.joining(", ")) + "numberOfBedrooms="
				+ numberOfBedrooms + ", numberOfBathrooms=" + numberOfBathrooms + ", heatingType=" + heatingType
				+ ", isPetFriendly=" + isPetFriendly + ", isFurnished=" + isFurnished + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(heatingType, isFurnished, isPetFriendly, numberOfBathrooms, numberOfBedrooms);
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
		Residence other = (Residence) obj;
		return heatingType == other.heatingType && isFurnished == other.isFurnished
				&& isPetFriendly == other.isPetFriendly && numberOfBathrooms == other.numberOfBathrooms
				&& numberOfBedrooms == other.numberOfBedrooms;
	}
}
