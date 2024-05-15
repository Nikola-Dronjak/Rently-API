package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Represents a data transfer object (DTO) for the Property entity. This class
 * is the parent class of the following classes:
 * <ul>
 * <li>ResidenceDTO</li>
 * <li>EventSpaceDTO</li>
 * <li>OfficeSpaceDTO</li>
 * </ul>
 * 
 * This means that all the fields of this class are inherited by them.
 * 
 * The PropertyDTO class contains the name, street address, description, monthly
 * rental rate, size, isAvailable flag, number of parking spaces and list of
 * photos of the property.
 * 
 * @author Nikola Dronjak
 */
public class PropertyDTO {
	private Integer propertyId;

	/**
	 * Represents the name of the property (String).
	 * 
	 * The name cannot be null and it has to have at least 5 characters.
	 */
	@NotBlank(message = "The name of the property is required.")
	@Size(min = 5, message = "The name of the property has to have at least 5 characters.")
	private String name;

	/**
	 * Represents the street address of the property (String).
	 * 
	 * The street address cannot be null and it has to have at least 5 characters.
	 */
	@NotBlank(message = "The address of the property is required.")
	@Size(min = 5, message = "The address of the property has to have at least 5 characters.")
	private String address;

	/**
	 * Represents the description of the property (String).
	 * 
	 * The description cannot be null (but it can be empty).
	 */
	@NotNull(message = "The description of the property is required.")
	private String description;

	/**
	 * Represents the monthly rental rate of the property (Double). This value will
	 * be assigned to the lease as well.
	 * 
	 * The rental rate cannot be null and it has to be a positive value (greater
	 * than 0).
	 */
	@NotNull(message = "The rental rate of the property is required.")
	@Positive(message = "The rental rate of the property has to be a positive value.")
	private Double rentalRate;

	/**
	 * Represents the size of the property is square meters (Integer).
	 * 
	 * The size cannot be null and it has to be a positive value (greater than 0).
	 */
	@NotNull(message = "The size of the property is required.")
	@Positive(message = "The size of the property has to be a positive value.")
	private Integer size;

	/**
	 * Indicates whether the property is available (Boolean).
	 * <ul>
	 * <li>True - The property is available.</li>
	 * <li>False - The property is not available.</li>
	 * </ul>
	 * 
	 * The isAvailable flag cannot be null.
	 */
	@NotNull(message = "You have to specify wether the property is available or not.")
	private Boolean isAvailable;

	/**
	 * Represents the number of parking spaces that come with the property
	 * (Integer).
	 * 
	 * The number of parking spaces cannot be null and it has to be a positive value
	 * (greater than 0).
	 */
	@NotNull(message = "The number of parking spots for the property is required.")
	@Positive(message = "The number of parking spots for the property has to be a positive value.")
	private Integer numberOfParkingSpots;

	/**
	 * Represents the list of photos of the property (List&lt;String&gt;).
	 * 
	 * The list of photos cannot be null. There must be at least 1 photo of the
	 * property and there can't be more than 15 photos of the property.
	 */
	@NotNull(message = "The photos of the property are required.")
	@Size(min = 1, max = 15, message = "There has to be atleast 1 photo of the property and there cant be more than 15 photos of the property.")
	private List<String> photos;

	public PropertyDTO() {

	}

	public PropertyDTO(Integer propertyId, String name, String address, String description, Double rentalRate,
			Integer size, Boolean isAvailable, Integer numberOfParkingSpots, List<String> photos) {
		this.propertyId = propertyId;
		this.name = name;
		this.address = address;
		this.description = description;
		this.rentalRate = rentalRate;
		this.size = size;
		this.isAvailable = isAvailable;
		this.numberOfParkingSpots = numberOfParkingSpots;
		this.photos = photos;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(Double rentalRate) {
		this.rentalRate = rentalRate;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getNumberOfParkingSpots() {
		return numberOfParkingSpots;
	}

	public void setNumberOfParkingSpots(Integer numberOfParkingSpots) {
		this.numberOfParkingSpots = numberOfParkingSpots;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "PropertyDTO [propertyId=" + propertyId + ", name=" + name + ", address=" + address + ", description="
				+ description + ", rentalRate=" + rentalRate + ", size=" + size + ", isAvailable=" + isAvailable
				+ ", numberOfParkingSpots=" + numberOfParkingSpots + ", photos=" + photos + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, description, isAvailable, name, numberOfParkingSpots, photos, propertyId,
				rentalRate, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyDTO other = (PropertyDTO) obj;
		return Objects.equals(address, other.address) && Objects.equals(description, other.description)
				&& Objects.equals(isAvailable, other.isAvailable) && Objects.equals(name, other.name)
				&& Objects.equals(numberOfParkingSpots, other.numberOfParkingSpots)
				&& Objects.equals(photos, other.photos) && Objects.equals(propertyId, other.propertyId)
				&& Objects.equals(rentalRate, other.rentalRate) && Objects.equals(size, other.size);
	}
}
