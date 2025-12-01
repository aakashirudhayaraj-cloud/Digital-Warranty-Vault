package com.example.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Warranty {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(nullable = false)
  private LocalDate purchaseDate;

  @Column(nullable = false)
  private LocalDate warrantyExpiryDate;

  private String vendorName;
  private String serialNumber;

  @OneToMany(mappedBy = "warranty", cascade = CascadeType.ALL)
  private List<Receipt> receipts = new ArrayList<>();

  private boolean reminderSent = false;

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public User getUser() {
	return user;
  }

  public void setUser(User user) {
	this.user = user;
  }

  public Product getProduct() {
	return product;
  }

  public void setProduct(Product product) {
	this.product = product;
  }

  public LocalDate getPurchaseDate() {
	return purchaseDate;
  }

  public void setPurchaseDate(LocalDate purchaseDate) {
	this.purchaseDate = purchaseDate;
  }

  public LocalDate getWarrantyExpiryDate() {
	return warrantyExpiryDate;
  }

  public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
	this.warrantyExpiryDate = warrantyExpiryDate;
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

  public List<Receipt> getReceipts() {
	return receipts;
  }

  public void setReceipts(List<Receipt> receipts) {
	this.receipts = receipts;
  }

  public boolean isReminderSent() {
	return reminderSent;
  }

  public void setReminderSent(boolean reminderSent) {
	this.reminderSent = reminderSent;
  }

  
}

