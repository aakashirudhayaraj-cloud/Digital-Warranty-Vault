package com.example.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class CreateWarrantyRequest {
	  @NotNull private Long userId;
	  @NotNull private Long productId;
	  @NotNull private LocalDate purchaseDate;
	  @NotNull private LocalDate expiryDate;
	  private String vendorName;
	  private String serialNumber;
	  public Long getUserId() {
		  return userId;
	  }
	  public void setUserId(Long userId) {
		  this.userId = userId;
	  }
	  public Long getProductId() {
		  return productId;
	  }
	  public void setProductId(Long productId) {
		  this.productId = productId;
	  }
	  public LocalDate getPurchaseDate() {
		  return purchaseDate;
	  }
	  public void setPurchaseDate(LocalDate purchaseDate) {
		  this.purchaseDate = purchaseDate;
	  }
	  public LocalDate getExpiryDate() {
		  return expiryDate;
	  }
	  public void setExpiryDate(LocalDate expiryDate) {
		  this.expiryDate = expiryDate;
	  }
	  public String getVendorName() {
		  return vendorName;
	  }
	  public void setVendorName(String vendorName) {
		  this.vendorName = vendorName;
	  }
	  public String getSerialNumber() {
		  return serialNumber;
	  }
	  public void setSerialNumber(String serialNumber) {
		  this.serialNumber = serialNumber;
	  }
	  
	}

