package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

import com.nikoladronjak.rently.domain.HeatingType;

/**
 * Represents a data transfer object (DTO) for the Residence entity. This class
 * is used for transferring residence data between the different layers of the
 * application (ResidenceRepository, ResidenceService and ResidenceController).
 * It plays a vital role in the creation of requests as well as the displaying
 * of responses.
 * 
 * The ResidenceDTO class contains the number of bedrooms, number of bathrooms,
 * heating type, isPetFriendly flag, isFurnished flag and ownerId.
 * 
 * @author Nikola Dronjak
 */
public class ResidenceDTO extends PropertyDTO {

	/**
	 * Represents the number of bedrooms in the residence (Integer).
	 */
	private Integer numberOfBedrooms;

	/**
	 * Represents the number of bathrooms in the residence (Integer).
	 */
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
	 */
	private HeatingType heatingType;

	/**
	 * Indicates whether the residence is pet-friendly (Boolean).
	 * <ul>
	 * <li>True - The residence is pet-friendly.</li>
	 * <li>False - The residence is not pet-friendly.</li>
	 * </ul>
	 */
	private Boolean isPetFriendly;

	/**
	 * Indicates whether the residence is furnished (Boolean).
	 * <ul>
	 * <li>True - The residence is furnished.</li>
	 * <li>False - The residence is not furnished.</li>
	 * </ul>
	 */
	private Boolean isFurnished;

	/**
	 * Represents the id of the residence's owner (Integer).
	 */
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
