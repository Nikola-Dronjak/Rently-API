package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "Rents")
public class Rent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rentId;

	private double totalRent;

	@ManyToMany(mappedBy = "rents", fetch = FetchType.EAGER)
	private List<UtilityLease> utilityLeases;

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
