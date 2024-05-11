package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

import com.nikoladronjak.rently.domain.HeatingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ResidenceDTO extends PropertyDTO {

	@NotNull(message = "The number of bedrooms for the residence is required.")
	@Min(value = 1, message = "The residence has to have at least 1 bedroom.")
	private Integer numberOfBedrooms;

	@NotNull(message = "The number of bathrooms for the residence is required.")
	@Min(value = 1, message = "The residence has to have at least 1 bathroom.")
	private Integer numberOfBathrooms;

	@NotNull(message = "The heating type of the residence is required.")
	private HeatingType heatingType;

	@NotNull(message = "You have to specify whether the property is pet friendly or not.")
	private Boolean isPetFriendly;

	@NotNull(message = "You have to specify whether the property is furnished or not.")
	private Boolean isFurnished;

	@NotNull(message = "You have to specify the owner of the residence.")
	private Integer ownerId;

	public ResidenceDTO() {

	}

	public ResidenceDTO(Integer propertyId, String name, String address, String description, double rentalRate,
			int size, Boolean isAvailable, int numberOfParkingSpots, List<String> photos, Integer numberOfBedrooms,
			Integer numberOfBathrooms, HeatingType heatingType, Boolean isPetFriendly, Boolean isFurnished,
			Integer ownerId) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos);
		this.numberOfBedrooms = numberOfBedrooms;
		this.numberOfBathrooms = numberOfBathrooms;
		this.heatingType = heatingType;
		this.isPetFriendly = isPetFriendly;
		this.isFurnished = isFurnished;
		this.ownerId = ownerId;
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

	public Boolean getIsPetFriendly() {
		return isPetFriendly;
	}

	public void setIsPetFriendly(Boolean isPetFriendly) {
		this.isPetFriendly = isPetFriendly;
	}

	public Boolean getIsFurnished() {
		return isFurnished;
	}

	public void setIsFurnished(Boolean isFurnished) {
		this.isFurnished = isFurnished;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "ResidenceDTO [numberOfBedrooms=" + numberOfBedrooms + ", numberOfBathrooms=" + numberOfBathrooms
				+ ", heatingType=" + heatingType + ", isPetFriendly=" + isPetFriendly + ", isFurnished=" + isFurnished
				+ ", ownerId=" + ownerId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(heatingType, isFurnished, isPetFriendly, numberOfBathrooms, numberOfBedrooms, ownerId);
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
		ResidenceDTO other = (ResidenceDTO) obj;
		return heatingType == other.heatingType && isFurnished == other.isFurnished
				&& isPetFriendly == other.isPetFriendly && Objects.equals(numberOfBathrooms, other.numberOfBathrooms)
				&& Objects.equals(numberOfBedrooms, other.numberOfBedrooms) && Objects.equals(ownerId, other.ownerId);
	}
}
