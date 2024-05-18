package com.nikoladronjak.rently.dto;

import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the UtilityLease entity. This
 * class is used for transferring utility lease data between the different
 * layers of the application (UtilityLeaseRepository, UtilityLeaseService and
 * UtilityLeaseController). It plays a vital role in the creation of requests as
 * well as the displaying of responses.
 * 
 * The UtilityLeaseDTO class contains the utilityId, propertyId and monthly
 * rental rate of the utility lease.
 * 
 * @author Nikola Dronjak
 */
public class UtilityLeaseDTO {

	/**
	 * Represents the id of the utility that is being leased (Integer).
	 */
	private Integer utilityId;

	/**
	 * Represents the id of the property for which the utility is being leased
	 */
	private Integer propertyId;

	/**
	 * Represents the monthly rental rate for the utility lease (Double).
	 */
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