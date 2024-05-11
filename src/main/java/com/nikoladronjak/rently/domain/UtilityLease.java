package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "UtilityLeases", uniqueConstraints = { @UniqueConstraint(columnNames = { "utilityId", "propertyId" }) })
public class UtilityLease {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int utilityLeaseId;

	private double rentalRate;

	@ManyToOne
	@JoinColumn(name = "utilityId")
	private Utility utility;

	@ManyToOne
	@JoinColumn(name = "propertyId")
	private Property property;

	@ManyToMany
	private List<Rent> rents;

	public UtilityLease() {

	}

	public UtilityLease(int utilityLeaseId, double rentalRate, Utility utility, Property property, List<Rent> rents) {
		this.utilityLeaseId = utilityLeaseId;
		this.rentalRate = rentalRate;
		this.utility = utility;
		this.property = property;
		this.rents = rents;
	}

	public int getUtilityLeaseId() {
		return utilityLeaseId;
	}

	public void setUtilityLeaseId(int utilityLeaseId) {
		this.utilityLeaseId = utilityLeaseId;
	}

	public double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(double rentalRate) {
		this.rentalRate = rentalRate;
	}

	public Utility getUtility() {
		return utility;
	}

	public void setUtility(Utility utility) {
		this.utility = utility;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public List<Rent> getRents() {
		return rents;
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	@Override
	public String toString() {
		return "UtilityLease [utilityLeaseId=" + utilityLeaseId + ", rentalRate=" + rentalRate + ", utility=" + utility
				+ ", property=" + property + ", rents=" + rents + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(property, rentalRate, rents, utility, utilityLeaseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtilityLease other = (UtilityLease) obj;
		return Objects.equals(property, other.property)
				&& Double.doubleToLongBits(rentalRate) == Double.doubleToLongBits(other.rentalRate)
				&& Objects.equals(rents, other.rents) && Objects.equals(utility, other.utility)
				&& utilityLeaseId == other.utilityLeaseId;
	}
}
