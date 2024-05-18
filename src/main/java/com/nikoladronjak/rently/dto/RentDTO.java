package com.nikoladronjak.rently.dto;

import java.util.List;
import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the Rent entity. This class is
 * used for transferring rent data between the different layers of the
 * application (RentRepository, RentService and RentController). It plays a
 * vital role in the creation of requests as well as the displaying of
 * responses.
 * 
 * The RentDTO class contains the leaseId, total monthly rent and list of the
 * utility lease ids.
 * 
 * @author Nikola Dronjak
 */
public class RentDTO {

	/**
	 * Represents the id of the lease for which the rent is being calculated
	 * (Integer).
	 */
	private Integer leaseId;

	/**
	 * Represents the total monthly rent (Double). This rent is calculated by adding
	 * up monthly rental rates for all the utility leases and the monthly rental
	 * rate for the lease.
	 */
	private Double totalRent;

	/**
	 * Represents the list of all the utility leases that are being leased
	 * (List&lt;Integer&gt;).
	 */
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
