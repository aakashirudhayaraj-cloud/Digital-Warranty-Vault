package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Receipt {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String filename;
  private String filePath;
  private LocalDate uploadedAt;

  @ManyToOne
  @JoinColumn(name = "warranty_id")
  private Warranty warranty;

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getFilename() {
	return filename;
  }

  public void setFilename(String filename) {
	this.filename = filename;
  }

  public String getFilePath() {
	return filePath;
  }

  public void setFilePath(String filePath) {
	this.filePath = filePath;
  }

  public LocalDate getUploadedAt() {
	return uploadedAt;
  }

  public void setUploadedAt(LocalDate uploadedAt) {
	this.uploadedAt = uploadedAt;
  }

  public Warranty getWarranty() {
	return warranty;
  }

  public void setWarranty(Warranty warranty) {
	this.warranty = warranty;
  }
  
}
