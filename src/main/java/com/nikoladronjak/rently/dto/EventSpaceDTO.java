package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the EventSpace entity. This class
 * is used for transferring event space data between the different layers of the
 * application (EventSpaceRepository, EventSpaceService and
 * EventSpaceController). It plays a vital role in the creation of requests as
 * well as the displaying of responses.
 * 
 * The EventSpaceDTO class contains the capacity, hasKitchen flag, hasBar flag
 * and ownerId.
 * 
 * @author Nikola Dronjak
 */
public class EventSpaceDTO extends PropertyDTO {

	/**
	 * Represents the number of people that the event space can hold (Integer).
	 */
	private Integer capacity;

	/**
	 * Indicates whether the event space has a kitchen (Boolean).
	 * <ul>
	 * <li>True - The event space has a kitchen.</li>
	 * <li>False - The event space doesn't have a kitchen.</li>
	 * </ul>
	 */
	private Boolean hasKitchen;

	/**
	 * Indicates whether the event space has a bar (Boolean).
	 * <ul>
	 * <li>True - The event space has a bar.</li>
	 * <li>False - The event space doesn't have a bar.</li>
	 * </ul>
	 */
	private Boolean hasBar;

	/**
	 * Represents the id of the event space's owner (Integer).
	 */
	private Integer ownerId;

	public EventSpaceDTO() {

	}

	public EventSpaceDTO(Integer propertyId, String name, String address, String description, Double rentalRate,
			Integer size, Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos, Integer capacity,
			Boolean hasKitchen, Boolean hasBar, Integer ownerId) {
		super(propertyId, name, address, description, rentalRate, size, isAvailable, numberOfParkingSpots, photos);
		this.capacity = capacity;
		this.hasKitchen = hasKitchen;
		this.hasBar = hasBar;
		this.ownerId = ownerId;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Boolean getHasKitchen() {
		return hasKitchen;
	}

	public void setHasKitchen(Boolean hasKitchen) {
		this.hasKitchen = hasKitchen;
	}

	public Boolean getHasBar() {
		return hasBar;
	}

	public void setHasBar(Boolean hasBar) {
		this.hasBar = hasBar;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "EventSpaceDTO [capacity=" + capacity + ", hasKitchen=" + hasKitchen + ", hasBar=" + hasBar
				+ ", ownerId=" + ownerId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capacity, hasBar, hasKitchen, ownerId);
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
		EventSpaceDTO other = (EventSpaceDTO) obj;
		return Objects.equals(capacity, other.capacity) && Objects.equals(hasBar, other.hasBar)
				&& Objects.equals(hasKitchen, other.hasKitchen) && Objects.equals(ownerId, other.ownerId);
	}
}
