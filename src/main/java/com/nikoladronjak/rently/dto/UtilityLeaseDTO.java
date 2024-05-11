package com.nikoladronjak.rently.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UtilityLeaseDTO {

	@NotNull(message = "You have to specify the utility which is being leased.")
	private Integer utilityId;

	@NotNull(message = "You have to specify the property for which the utility is being leased.")
	private Integer propertyId;

	@NotNull(message = "The rental rate of the utility being leased is required.")
	@Positive(message = "The rental rate of the utility being leased has to be a positive value.")
	private Double rentalRate;

	public UtilityLeaseDTO() {

	}

	public UtilityLeaseDTO(Integer utilityId, Integer propertyId, Double rentalRate) {
		this.utilityId = utilityId;
		this.propertyId = propertyId;
		this.rentalRate = rentalRate;
	}

	public Integer getUtilityId() {
		return utilityId;
	}

	public void setUtilityId(Integer utilityId) {
		this.utilityId = utilityId;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public Double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(Double rentalRate) {
		this.rentalRate = rentalRate;
	}

	@Override
	public String toString() {
		return "UtilityLeaseDTO [utilityId=" + utilityId + ", propertyId=" + propertyId + ", rentalRate=" + rentalRate
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(propertyId, rentalRate, utilityId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtilityLeaseDTO other = (UtilityLeaseDTO) obj;
		return Objects.equals(propertyId, other.propertyId) && Objects.equals(rentalRate, other.rentalRate)
				&& Objects.equals(utilityId, other.utilityId);
	}
}