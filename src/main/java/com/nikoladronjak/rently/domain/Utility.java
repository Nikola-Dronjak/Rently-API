package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "Utilities")
public class Utility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int utilityId;

	@Column(unique = true)
	private String name;

	private String description;

	@OneToMany(mappedBy = "utility")
	private List<UtilityLease> utilityLeases;

	public Utility() {

	}

	public Utility(int utilityId, String name, String description, List<UtilityLease> utilityLeases) {
		this.utilityId = utilityId;
		this.name = name;
		this.description = description;
		this.utilityLeases = utilityLeases;
	}

	public int getUtilityId() {
		return utilityId;
	}

	public void setUtilityId(int utilityId) {
		this.utilityId = utilityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UtilityLease> getUtilityLeases() {
		return utilityLeases;
	}

	public void setUtilityLeases(List<UtilityLease> utilityLeases) {
		this.utilityLeases = utilityLeases;
	}

	@Override
	public String toString() {
		return "Utility [utilityId=" + utilityId + ", name=" + name + ", description=" + description
				+ ", utilityLeases="
				+ utilityLeases.stream().map(UtilityLease::toString).collect(Collectors.joining(", ")) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, utilityId, utilityLeases);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utility other = (Utility) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& utilityId == other.utilityId && Objects.equals(utilityLeases, other.utilityLeases);
	}
}
