package com.nikoladronjak.rently.dto;

import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the Utility entity. This class is
 * used for transferring utility data between the different layers of the
 * application (UtilityRepository, UtilityService and UtilityController). It
 * plays a vital role in the creation of requests as well as the displaying of
 * responses.
 * 
 * The UtilityDTO class contains the name and description of the utility.
 * 
 * @author Nikola Dronjak
 */
public class UtilityDTO {

	/**
	 * Represents the name of the utility (String).
	 */
	private String name;

	/**
	 * Represents the description of the utility (String).
	 */
	private String description;

	public UtilityDTO() {

	}

	public UtilityDTO(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UtilityDTO [name=" + name + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtilityDTO other = (UtilityDTO) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}
}
