package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OfficeSpaceDTO extends PropertyDTO {

	@NotNull(message = "The size of the office space is required.")
	@Positive(message = "The size of the office space has to be a positive value.")
	private Integer capacity;

	@NotNull(message = "You have to specify the owner of the office space.")
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
