package com.nikoladronjak.rently.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UtilityDTO {

	@NotBlank(message = "The name of the utility is required.")
	@Size(min = 5, message = "The name of the utility has to have at least 5 characters.")
	private String name;

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
