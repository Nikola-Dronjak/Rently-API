package com.nikoladronjak.rently.dto;

import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for the Customer entity. This class
 * is used for transferring customer data between the different layers of the
 * application (CustomerRepository, CustomerService and CustomerController). It
 * plays a vital role in the creation of requests as well as the displaying of
 * responses.
 * 
 * The CustomerDTO class contains the first name, last name, email address and
 * password of the customer.
 * 
 * @author Nikola Dronjak
 */
public class CustomerDTO {

	/**
	 * Represents the first name of the customer (String).
	 */
	private String firstName;

	/**
	 * Represents the last name of the customer (String).
	 */
	private String lastName;

	/**
	 * Represents the email address of the customer (String).
	 */
	private String email;

	/**
	 * Represents the password of the customer (String).
	 */
	private String password;

	public CustomerDTO() {

	}

	public CustomerDTO(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
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

	@Override
	public String toString() {
		return "CustomerDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, lastName, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDTO other = (CustomerDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password);
	}
}
