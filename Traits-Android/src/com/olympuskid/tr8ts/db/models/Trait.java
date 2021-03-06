package com.olympuskid.tr8ts.db.models;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table trait.
 */
public class Trait {

	private Long id;
	/** Not-null value. */
	private String name;
	private int polarity;
	private Integer gender;
	private Boolean isCustom;
	private java.util.Date createdAt;
	private java.util.Date updatedAt;

	// KEEP FIELDS - put your custom fields here
	// KEEP FIELDS END

	public Trait() {
	}

	public Trait(Long id) {
		this.id = id;
	}

	public Trait(Long id, String name, int polarity, Integer gender, Boolean isCustom, java.util.Date createdAt, java.util.Date updatedAt) {
		this.id = id;
		this.name = name;
		this.polarity = polarity;
		this.gender = gender;
		this.isCustom = isCustom;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** Not-null value. */
	public String getName() {
		return name;
	}

	/** Not-null value; ensure this value is available before it is saved to the database. */
	public void setName(String name) {
		this.name = name;
	}

	public int getPolarity() {
		return polarity;
	}

	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Boolean getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}

	public java.util.Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.util.Date createdAt) {
		this.createdAt = createdAt;
	}

	public java.util.Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(java.util.Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	// KEEP METHODS - put your custom methods here
	// KEEP METHODS END

}
