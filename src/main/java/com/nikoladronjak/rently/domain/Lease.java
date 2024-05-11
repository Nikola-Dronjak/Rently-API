package com.nikoladronjak.rently.domain;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "Leases", uniqueConstraints = { @UniqueConstraint(columnNames = { "propertyId", "customerId" }) })
public class Lease {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaseId;

	private double rentalRate;

	private GregorianCalendar startDate;

	private GregorianCalendar endDate;

	@ManyToOne
	@JoinColumn(name = "propertyId")
	private Property property;

	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;

	@OneToMany(mappedBy = "lease")
	private List<Rent> rents;

	public Lease() {

	}

	public Lease(int leaseId, double rentalRate, GregorianCalendar startDate, GregorianCalendar endDate,
			Property property, Customer customer, List<Rent> rents) {
		this.leaseId = leaseId;
		this.rentalRate = rentalRate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.property = property;
		this.customer = customer;
		this.rents = rents;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(double rentalRate) {
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

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Rent> getRents() {
		return rents;
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	@Override
	public String toString() {
		return "Lease [leaseId=" + leaseId + ", rentalRate=" + rentalRate + ", startDate=" + startDate + ", endDate="
				+ endDate + ", property=" + property + ", customer=" + customer + ", rents="
				+ rents.stream().map(Rent::toString).collect(Collectors.joining(", ")) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, endDate, leaseId, property, rentalRate, rents, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lease other = (Lease) obj;
		return Objects.equals(customer, other.customer) && Objects.equals(endDate, other.endDate)
				&& leaseId == other.leaseId && Objects.equals(property, other.property)
				&& Double.doubleToLongBits(rentalRate) == Double.doubleToLongBits(other.rentalRate)
				&& Objects.equals(rents, other.rents) && Objects.equals(startDate, other.startDate);
	}
}
