package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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
	 * Represents the number of bedrooms in a residence (Integer).
	 * 
	 * The number of bedrooms cannot be null and there has to be at least one
	 * bedroom in the residence.
	 */
	@NotNull(message = "The number of bedrooms for the residence is required.")
	@Min(value = 1, message = "The residence has to have at least 1 bedroom.")
	private Integer numberOfBedrooms;

	/**
	 * Represents the number of bathrooms in a residence (Integer).
	 * 
	 * The number of bathrooms in the residence cannot be null and there has to be
	 * at least one bathroom in the residence.
	 */
	@NotNull(message = "The number of bathrooms for the residence is required.")
	@Min(value = 1, message = "The residence has to have at least 1 bathroom.")
	private Integer numberOfBathrooms;

	/**
	 * Represents the heating type of the residence (HeatingType). There are 4
	 * different types of heating:
	 * <ul>
	 * <li>Central heating</li>
	 * <li>Gas heating</li>
	 * <li>Electrical heating</li>
	 * <li>Wood heating</li>
	 * </ul>
	 * 
	 * The heating type cannot be null.
	 */
	@NotNull(message = "The heating type of the residence is required.")
	private HeatingType heatingType;

	/**
	 * Indicates whether the residence is pet-friendly (Boolean).
	 * <ul>
	 * <li>True - The residence is pet-friendly.</li>
	 * <li>False - The residence is not pet-friendly.</li>
	 * </ul>
	 * 
	 * The isPetFriendly flag cannot be null.
	 */
	@NotNull(message = "You have to specify whether the property is pet friendly or not.")
	private Boolean isPetFriendly;

	/**
	 * Indicates whether the residence is furnished (Boolean).
	 * <ul>
	 * <li>True - The residence is furnished.</li>
	 * <li>False - The residence is not furnished.</li>
	 * </ul>
	 * 
	 * The isFurnished flag cannot be null.
	 */
	@NotNull(message = "You have to specify whether the property is furnished or not.")
	private Boolean isFurnished;

	public Residence() {

	}

	public Residence(int propertyId, String name, String address, String description, Double rentalRate, Integer size,
			Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases,
			Integer numberOfBedrooms, Integer numberOfBathrooms, HeatingType heatingType, Boolean isPetFriendly,
			Boolean isFurnished) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos,
				owner, leases);
		this.numberOfBedrooms = numberOfBedrooms;
		this.numberOfBathrooms = numberOfBathrooms;
		this.heatingType = heatingType;
		this.isPetFriendly = isPetFriendly;
		this.isFurnished = isFurnished;
	}

	public Integer getNumberOfBedrooms() {
		return numberOfBedrooms;
	}

	public void setNumberOfBedrooms(Integer numberOfBedrooms) {
		this.numberOfBedrooms = numberOfBedrooms;
	}

	public Integer getNumberOfBathrooms() {
		return numberOfBathrooms;
	}

	public void setNumberOfBathrooms(Integer numberOfBathrooms) {
		this.numberOfBathrooms = numberOfBathrooms;
	}

	public HeatingType getHeatingType() {
		return heatingType;
	}

	public void setHeatingType(HeatingType heatingType) {
		this.heatingType = heatingType;
	}

	public Boolean isPetFriendly() {
		return isPetFriendly;
	}

	public void setPetFriendly(Boolean isPetFriendly) {
		this.isPetFriendly = isPetFriendly;
	}

	public Boolean isFurnished() {
		return isFurnished;
	}

	public void setFurnished(Boolean isFurnished) {
		this.isFurnished = isFurnished;
	}

	@Override
	public String toString() {
		return "Residence [numberOfBedrooms=" + numberOfBedrooms + ", numberOfBathrooms=" + numberOfBathrooms
				+ ", heatingType=" + heatingType + ", isPetFriendly=" + isPetFriendly + ", isFurnished=" + isFurnished
				+ "]";
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
		return super.equals(other) && heatingType == other.heatingType && Objects.equals(isFurnished, other.isFurnished)
				&& Objects.equals(isPetFriendly, other.isPetFriendly)
				&& Objects.equals(numberOfBathrooms, other.numberOfBathrooms)
				&& Objects.equals(numberOfBedrooms, other.numberOfBedrooms);
	}
}
