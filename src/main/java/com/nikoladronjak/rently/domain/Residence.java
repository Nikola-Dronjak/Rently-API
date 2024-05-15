package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

/**
 * Represents a domain class for storing information about a Residence entity.
 * This class is mapped to the "Residences" table in the database using JPA
 * annotations. The primary key of this table is "propertyId". Since
 * "propertyId" is inherited from the Property entity it also represents a
 * foreign key that references the "Properties" table.
 * 
 * The Residence entity contains the number of bedrooms, the number of
 * bathrooms, an isPetFriendly flag and an isFurnished flag.
 * 
 * This entity is a child class of the Property class. This means that it also
 * contains all the fields of the Property class.
 * 
 * @author Nikola Dronjak
 */
@Entity
@Table(name = "Residences")
@PrimaryKeyJoinColumn(name = "propertyId")
public class Residence extends Property {

	/**
	 * Represents the number of bedrooms in a residence (int).
	 */
	private int numberOfBedrooms;

	/**
	 * Represents the number of bathrooms in a residence (int).
	 */
	private int numberOfBathrooms;

	/**
	 * Represents the heating type of the residence (HeatingType). There are 4
	 * different types of heating:
	 * <ul>
	 * <li>Central heating</li>
	 * <li>Gas heating</li>
	 * <li>Electrical heating</li>
	 * <li>Wood heating</li>
	 * </ul>
	 */
	private HeatingType heatingType;

	/**
	 * Indicates whether the residence is pet-friendly (boolean).
	 * <ul>
	 * <li>True - The residence is pet-friendly.</li>
	 * <li>False - The residence is not pet-friendly.</li>
	 * </ul>
	 */
	private boolean isPetFriendly;

	/**
	 * Indicates whether the residence is furnished (boolean).
	 * <ul>
	 * <li>True - The residence is furnished.</li>
	 * <li>False - The residence is not furnished.</li>
	 * </ul>
	 */
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
