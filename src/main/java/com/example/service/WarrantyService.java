package com.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Product;
import com.example.entity.Receipt;
import com.example.entity.User;
import com.example.entity.Warranty;
import com.example.repository.ProductRepository;
import com.example.repository.ReceiptRepository;
import com.example.repository.UserRepository;
import com.example.repository.WarrantyRepository;

@Service
public class WarrantyService {
  private final WarrantyRepository warrantyRepo;
  private final UserRepository userRepo;
  private final ProductRepository productRepo;
  private final ReceiptRepository receiptRepo;
  private final FileStorageService fileStorage;
  private final EmailService emailService;

  @Autowired
  public WarrantyService(WarrantyRepository warrantyRepo, UserRepository userRepo,
                         ProductRepository productRepo, ReceiptRepository receiptRepo,
                         FileStorageService fileStorage, EmailService emailService) {
    this.warrantyRepo = warrantyRepo;
    this.userRepo = userRepo;
    this.productRepo = productRepo;
    this.receiptRepo = receiptRepo;
    this.fileStorage = fileStorage;
    this.emailService = emailService;
  }

  public Warranty createWarranty(Long userId, Long productId, LocalDate purchaseDate,
                                 LocalDate expiryDate, String vendor, String serial) {
    User user = userRepo.findById(userId).orElseThrow();
    Product product = productRepo.findById(productId).orElseThrow();
    Warranty w = new Warranty();
    w.setUser(user);
    w.setProduct(product);
    w.setPurchaseDate(purchaseDate);
    w.setWarrantyExpiryDate(expiryDate);
    w.setVendorName(vendor);
    w.setSerialNumber(serial);
    return warrantyRepo.save(w);
  }

  public Receipt attachReceipt(Long warrantyId, MultipartFile file) {
    Warranty w = warrantyRepo.findById(warrantyId).orElseThrow();
    String path = fileStorage.storeFile(file);
    Receipt r = new Receipt();
    r.setFilename(file.getOriginalFilename());
    r.setFilePath(path);
    r.setUploadedAt(LocalDate.now());
    r.setWarranty(w);
    receiptRepo.save(r);
    w.getReceipts().add(r);
    warrantyRepo.save(w);
    return r;
  }

  
  public List<Warranty> findWarrantiesExpiringBetween(LocalDate from, LocalDate to) {
    return warrantyRepo.findByWarrantyExpiryDateBetween(from, to);
  }

  public List<Warranty> findPendingReminders(LocalDate date) {
    return warrantyRepo.findByReminderSentFalseAndWarrantyExpiryDateBefore(date);
  }

  public void markReminderSent(Warranty w) {
    w.setReminderSent(true);
    warrantyRepo.save(w);
  }

  public void sendReminder(Warranty w, int daysBefore) {
    String to = w.getUser().getEmail();
    String subject = "Warranty expiring in " + daysBefore + " days: " + w.getProduct().getName();
    String body = String.format("Hi %s,\n\nYour warranty for product '%s' (serial: %s) will expire on %s.\n\nRegards,\nWarranty Vault",
                 w.getUser().getName(), w.getProduct().getName(), w.getSerialNumber(), w.getWarrantyExpiryDate());
    emailService.sendSimpleMessage(to, subject, body);
    markReminderSent(w);
  }
}

