package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

public class RentDTO {

	@NotNull(message = "You have to specify the lease from which the rent is derived.")
	private Integer leaseId;

	private Double totalRent;

	@NotNull(message = "You have to specify the utility leases which are part of the rent.")
	private List<Integer> utilityLeaseIds;

	public RentDTO() {

	}

	public RentDTO(Integer leaseId, Double totalRent, List<Integer> utilityLeaseIds) {
		this.leaseId = leaseId;
		this.totalRent = totalRent;
		this.utilityLeaseIds = utilityLeaseIds;
	}

	public Integer getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(Integer leaseId) {
		this.leaseId = leaseId;
	}

	public Double getTotalRent() {
		return totalRent;
	}

	public void setTotalRent(Double totalRent) {
		this.totalRent = totalRent;
	}

	public List<Integer> getUtilityLeaseIds() {
		return utilityLeaseIds;
	}

	public void setUtilityLeaseIds(List<Integer> utilityLeaseIds) {
		this.utilityLeaseIds = utilityLeaseIds;
	}

	@Override
	public String toString() {
		return "RentDTO [leaseId=" + leaseId + ", totalRent=" + totalRent + ", utilityLeaseIds=" + utilityLeaseIds
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(leaseId, totalRent, utilityLeaseIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RentDTO other = (RentDTO) obj;
		return Objects.equals(leaseId, other.leaseId) && Objects.equals(totalRent, other.totalRent)
				&& Objects.equals(utilityLeaseIds, other.utilityLeaseIds);
	}
}
