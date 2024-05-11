package com.nikoladronjak.rently.dto;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OwnerDTO {

	@NotBlank(message = "The first name of the owner is required.")
	@Size(min = 2, message = "The first name of the owner has to have at least 2 characters.")
	private String firstName;

	@NotBlank(message = "The last name of the owner is required.")
	@Size(min = 2, message = "The last name of the owner has to have at least 2 characters.")
	private String lastName;

	@NotBlank(message = "The email address of the owner is required.")
	@Email(message = "The email address of the owner must be valid.")
	private String email;

	@NotBlank(message = "The password of the owner is required.")
	@Size(min = 5, message = "The password of the owner has to have at least 5 characters.")
	private String password;

	@NotBlank(message = "The phone number of the owner is required.")
	@Size(min = 10, message = "The phone number of the owner has to have at least 10 characters.")
	private String phoneNumber;

	public OwnerDTO() {

	}

	public OwnerDTO(String firstName, String lastName, String email, String password, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
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
		return "OwnerDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + ", phoneNumber=" + phoneNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, lastName, password, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwnerDTO other = (OwnerDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}
}
