package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Represents a domain class for storing information about a Rent entity. This
 * class is mapped to the "Rents" table in the database using JPA annotations.
 * The primary key of this table is "rentId". This table also contains a foreign
 * key, "leaseId", which references the "Leases" table.
 * 
 * The Rent entity contains a rentId, a total (monthly) rental rate and a list
 * of utility leases which are associated with the Rent entity.
 * 
 * @author Nikola Dronjak
 */
@Entity
@Table(name = "Rents")
public class Rent {

	/**
	 * Represents a unique identifier for the rent (int). This identifier is
	 * generated automatically by JPA.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rentId;

	/**
	 * Represents the total monthly rent (double). This rent is calculated by adding
	 * up monthly rental rates for all the utility leases and the monthly rental
	 * rate for the lease.
	 */
	private double totalRent;

	/**
	 * Represents the list of utility leases with which the rents are associated
	 * (List&lt;UtilityLease&gt;).
	 * 
	 * This annotation creates an association table called "UtilityLeasesRents".
	 * This table contains two columns:
	 * <ul>
	 * <li>rentId - The id of the rent associated with the utility lease.</li>
	 * <li>utilityLeaseId - The id of the utility lease associated with the
	 * rent.</li>
	 * </ul>
	 */
	@ManyToMany(mappedBy = "rents", fetch = FetchType.EAGER)
	private List<UtilityLease> utilityLeases;

	/**
	 * Represents the lease which is associated with the rent (Lease).
	 */
	@ManyToOne
	@JoinColumn(name = "leaseId")
	private Lease lease;

	public Rent() {

	}

	public Rent(int rentId, double totalRent, List<UtilityLease> utilityLeases, Lease lease) {
		this.rentId = rentId;
		this.totalRent = totalRent;
		this.utilityLeases = utilityLeases;
		this.lease = lease;
	}

	public int getRentId() {
		return rentId;
	}

	public void setRentId(int rentId) {
		this.rentId = rentId;
	}

	public double getTotalRent() {
		return totalRent;
	}

	public void setTotalRent(double totalRent) {
		this.totalRent = totalRent;
	}

	public List<UtilityLease> getUtilityLeases() {
		return utilityLeases;
	}

	public void setUtilityLeases(List<UtilityLease> utilityLeases) {
		this.utilityLeases = utilityLeases;
	}

	public Lease getLease() {
		return lease;
	}

	public void setLease(Lease lease) {
		this.lease = lease;
	}

	@Override
	public String toString() {
		return "Rent [rentId=" + rentId + ", totalRent=" + totalRent + ", utilityLeases=" + utilityLeases + ", lease="
				+ lease + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(lease, rentId, totalRent, utilityLeases);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rent other = (Rent) obj;
		return Objects.equals(lease, other.lease) && rentId == other.rentId
				&& Double.doubleToLongBits(totalRent) == Double.doubleToLongBits(other.totalRent)
				&& Objects.equals(utilityLeases, other.utilityLeases);
	}
}
