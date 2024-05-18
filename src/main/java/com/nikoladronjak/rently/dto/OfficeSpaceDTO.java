package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the OfficeSpace entity. This
 * class is used for transferring office space data between the different layers
 * of the application (OfficeSpaceRepository, OfficeSpaceService and
 * OfficeSpaceController). It plays a vital role in the creation of requests as
 * well as the displaying of responses.
 * 
 * The OfficeSpaceDTO class contains the capacity and ownerId.
 * 
 * @author Nikola Dronjak
 */
public class OfficeSpaceDTO extends PropertyDTO {

	/**
	 * Represents the number of people that the office space can hold (Integer).
	 */
	private Integer capacity;

	/**
	 * Represents the id of the office space's owner (Integer).
	 */
	private Integer ownerId;

	public OfficeSpaceDTO() {

	}

	public OfficeSpaceDTO(Integer propertyId, String name, String address, String description, Double rentalRate,
			Integer size, Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos, Integer capacity,
			Integer ownerId) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos);
		this.capacity = capacity;
		this.ownerId = ownerId;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "OfficeSpaceDTO [capacity=" + capacity + ", ownerId=" + ownerId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capacity, ownerId);
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
		OfficeSpaceDTO other = (OfficeSpaceDTO) obj;
		return Objects.equals(capacity, other.capacity) && Objects.equals(ownerId, other.ownerId);
	}
}
