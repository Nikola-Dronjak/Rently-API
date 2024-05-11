package com.nikoladronjak.rently.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "Properties")
@Inheritance(strategy = InheritanceType.JOINED)
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int propertyId;

	private String name;

	@Column(unique = true)
	private String address;

	private String description;

	private double rentalRate;

	private int size;

	private boolean isAvailable;

	private int numberOfParkingSpots;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> photos;

	@ManyToOne
	@JoinColumn(name = "ownerId")
	private Owner owner;

	@OneToMany(mappedBy = "property")
	private List<Lease> leases;

	public Property() {

	}

	public Property(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, List<String> photos, Owner owner, List<Lease> leases) {
		this.propertyId = propertyId;
		this.name = name;
		this.address = address;
		this.description = description;
		this.rentalRate = rentalRate;
		this.size = size;
		this.isAvailable = isAvailable;
		this.numberOfParkingSpots = numberOfParkingSpots;
		this.photos = photos;
		this.owner = owner;
		this.leases = leases;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(double rentalRate) {
		this.rentalRate = rentalRate;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public int getNumberOfParkingSpots() {
		return numberOfParkingSpots;
	}

	public void setNumberOfParkingSpots(int numberOfParkingSpots) {
		this.numberOfParkingSpots = numberOfParkingSpots;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public List<Lease> getLeases() {
		return leases;
	}

	public void setLeases(List<Lease> leases) {
		this.leases = leases;
	}

	@Override
	public String toString() {
		return "Property [propertyId=" + propertyId + ", name=" + name + ", address=" + address + ", description="
				+ description + ", rentalRate=" + rentalRate + ", size=" + size + ", isAvailable=" + isAvailable
				+ ", numberOfParkingSpots=" + numberOfParkingSpots + ", photos=" + String.join(", ", photos)
				+ ", owner=" + owner + ", leases="
				+ leases.stream().map(Lease::toString).collect(Collectors.joining(", ")) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, description, isAvailable, leases, name, numberOfParkingSpots, owner, photos,
				propertyId, rentalRate, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		return Objects.equals(address, other.address) && Objects.equals(description, other.description)
				&& isAvailable == other.isAvailable && Objects.equals(leases, other.leases)
				&& Objects.equals(name, other.name) && numberOfParkingSpots == other.numberOfParkingSpots
				&& Objects.equals(owner, other.owner) && Objects.equals(photos, other.photos)
				&& propertyId == other.propertyId
				&& Double.doubleToLongBits(rentalRate) == Double.doubleToLongBits(other.rentalRate)
				&& size == other.size;
	}
}
