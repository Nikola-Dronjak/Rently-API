package com.nikoladronjak.rently.domain;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Represents a domain class for storing information about a Owner entity. This
 * class is mapped to the "Owners" table in the database using JPA annotations.
 * The primary key of this table is "ownerId".
 * 
 * The Owner entity contains an ownerId, a first name, a last name, an email
 * address, a password and a phone number.
 * 
 * @author Nikola Dronjak
 */
@Entity
@Table(name = "Owners")
public class Owner {

	/**
	 * Represents a unique identifier for the owner (int). This identifier is
	 * automatically generated by JPA.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ownerId;

	/**
	 * Represents the first name of the owner (String).
	 * 
	 * The first name cannot be null and it has to have at least 2 characters.
	 */
	@NotBlank(message = "The first name of the owner is required.")
	@Size(min = 2, message = "The first name of the owner has to have at least 2 characters.")
	private String firstName;

	/**
	 * Represents the last name of the owner (String).
	 * 
	 * The last name cannot be null and it has to have at least 2 characters.
	 */
	@NotBlank(message = "The last name of the owner is required.")
	@Size(min = 2, message = "The last name of the owner has to have at least 2 characters.")
	private String lastName;

	/**
	 * Represents the email address of the owner (String). The email address has to
	 * be unique.
	 * 
	 * The email address cannot be null and it has to be a valid email address.
	 */
	@NotBlank(message = "The email address of the owner is required.")
	@Email(message = "The email address of the owner must be valid.")
	@Column(unique = true)
	private String email;

	/**
	 * Represents the password of the owner (String).
	 * 
	 * The password cannot be null and it has to have at least 5 characters.
	 */
	@NotBlank(message = "The password of the owner is required.")
	@Size(min = 5, message = "The password of the owner has to have at least 5 characters.")
	private String password;

	/**
	 * Represents the phone number of the owner (String).
	 * 
	 * The phone number cannot be null and it has to have at least 10 characters.
	 */
	@NotBlank(message = "The phone number of the owner is required.")
	@Size(min = 10, message = "The phone number of the owner has to have at least 10 characters.")
	private String phoneNumber;

	public Owner() {

	}

	public Owner(int ownerId, String firstName, String lastName, String email, String password, String phoneNumber) {
		this.ownerId = ownerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Owner [ownerId=" + ownerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", phoneNumber=" + phoneNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, lastName, ownerId, password, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Owner other = (Owner) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && ownerId == other.ownerId
				&& Objects.equals(password, other.password) && Objects.equals(phoneNumber, other.phoneNumber);
	}
}
