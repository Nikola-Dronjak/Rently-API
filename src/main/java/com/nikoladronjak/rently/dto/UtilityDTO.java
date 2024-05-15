package com.nikoladronjak.rently.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	 * 
	 * The name cannot be null and it has to have at least 5 characters.
	 */
	@NotBlank(message = "The name of the utility is required.")
	@Size(min = 5, message = "The name of the utility has to have at least 5 characters.")
	private String name;

	/**
	 * Represents the description of the utility (String).
	 * 
	 * The description cannot be null (but it can be empty).
	 */
	@NotNull(message = "The description of the utility is required.")
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
