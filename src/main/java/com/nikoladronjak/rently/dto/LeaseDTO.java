package com.nikoladronjak.rently.dto;

import java.util.GregorianCalendar;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class LeaseDTO {

	@NotNull(message = "You have to specify the property which is being leased.")
	private Integer propertyId;

	@NotNull(message = "You have to specify the customer who is leasing the property.")
	private Integer customerId;

	private Double rentalRate;

	@NotNull(message = "The start date of the lease is required.")
	@FutureOrPresent(message = "The start date of the lease has to be in the present or in the future.")
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private GregorianCalendar startDate;

	@NotNull(message = "The end date of the lease is required.")
	@FutureOrPresent(message = "The end date of the lease has to be in the present or in the future.")
	@DateTimeFormat(pattern = "yyyy-mm-dd")
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
