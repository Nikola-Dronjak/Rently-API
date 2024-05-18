package com.nikoladronjak.rently.dto;

import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the Lease entity. This class is
 * used for transferring lease data between the different layers of the
 * application (LeaseRepository, LeaseService and LeaseController). It plays a
 * vital role in the creation of requests as well as the displaying of
 * responses.
 * 
 * The LeaseDTO class contains the propertyId, customerId, monthly rental rate,
 * start date of the lease and end date of the lease.
 * 
 * @author Nikola Dronjak
 */
public class LeaseDTO {

	/**
	 * Represents the id of the property that is being leased (Integer).
	 */
	private Integer propertyId;

	/**
	 * Represents the id of the customer who is leasing the property as a tenant
	 */
	private Integer customerId;

	/**
	 * Represents the monthly rental rate of the lease (Double).
	 * 
	 * The value of this field is related to the rentalRate field in the PropertyDTO
	 * class.
	 */
	private Double rentalRate;

	/**
	 * Represents the start date of the lease (GregorianCalendar).
	 */
	private GregorianCalendar startDate;

	/**
	 * Represents the end date of the lease (GregorianCalendar).
	 */
	private GregorianCalendar endDate;

	public LeaseDTO() {

	}

	public LeaseDTO(Integer propertyId, Integer customerId, Double rentalRate, GregorianCalendar startDate,
			GregorianCalendar endDate) {
		this.propertyId = propertyId;
		this.customerId = customerId;
		this.rentalRate = rentalRate;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(Double rentalRate) {
		this.rentalRate = rentalRate;
	}

	public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "LeaseDTO [propertyId=" + propertyId + ", customerId=" + customerId + ", rentalRate=" + rentalRate
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, endDate, propertyId, rentalRate, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaseDTO other = (LeaseDTO) obj;
		return Objects.equals(customerId, other.customerId) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(propertyId, other.propertyId) && Objects.equals(rentalRate, other.rentalRate)
				&& Objects.equals(startDate, other.startDate);
	}
}
