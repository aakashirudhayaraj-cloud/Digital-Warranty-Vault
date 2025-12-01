package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
