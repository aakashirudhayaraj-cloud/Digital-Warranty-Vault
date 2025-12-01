package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Warranty;

public interface WarrantyRepository extends JpaRepository<Warranty, Long> {
	  List<Warranty> findByWarrantyExpiryDateBetween(LocalDate start, LocalDate end);
	  List<Warranty> findByReminderSentFalseAndWarrantyExpiryDateBefore(LocalDate date);
	}