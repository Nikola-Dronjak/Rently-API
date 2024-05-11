package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "Customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerId;

	private String firstName;

	private String lastName;

	@Column(unique = true)
	private String email;

	private String password;

	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private List<Lease> leases;

	public Customer() {

	}

	public Customer(int customerId, String firstName, String lastName, String email, String password,
			List<Lease> leases) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.leases = leases;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Lease> getLeases() {
		return leases;
	}

	public void setLeases(List<Lease> leases) {
		this.leases = leases;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", password=" + password + ", leases="
				+ leases.stream().map(Lease::toString).collect(Collectors.joining(", ")) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, email, firstName, lastName, leases, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return customerId == other.customerId && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(leases, other.leases) && Objects.equals(password, other.password);
	}
}
