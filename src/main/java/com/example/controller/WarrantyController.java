package com.example.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.CreateWarrantyRequest;
import com.example.entity.Product;
import com.example.entity.Receipt;
import com.example.entity.User;
import com.example.entity.Warranty;
import com.example.repository.ProductRepository;
import com.example.repository.ReceiptRepository;
import com.example.repository.UserRepository;
import com.example.service.WarrantyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warranties")
public class WarrantyController {

    private final WarrantyService warrantyService;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final ReceiptRepository receiptRepo;

    @Autowired
    public WarrantyController(
            WarrantyService warrantyService,
            ProductRepository productRepo,
            UserRepository userRepo,
            ReceiptRepository receiptRepo) {

        this.warrantyService = warrantyService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.receiptRepo = receiptRepo;
    }

    // -------------------------------------------------------------
    // 1. CREATE WARRANTY
    // -------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Warranty> createWarranty(
            @Valid @RequestBody CreateWarrantyRequest req) {

        Warranty w = warrantyService.createWarranty(
                req.getUserId(),
                req.getProductId(),
                req.getPurchaseDate(),
                req.getExpiryDate(),
                req.getVendorName(),
                req.getSerialNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(w);
    }

    // -------------------------------------------------------------
    // 2. UPLOAD RECEIPT
    // -------------------------------------------------------------
    @PostMapping("/{id}/receipts")
    public ResponseEntity<Receipt> uploadReceipt(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Receipt receipt = warrantyService.attachReceipt(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
    }

    // -------------------------------------------------------------
    // 3. DOWNLOAD RECEIPT (Step 11)
    // -------------------------------------------------------------
    @GetMapping("/receipts/{id}/download")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable Long id) {

        Receipt r = receiptRepo.findById(id).orElseThrow();
        Path path = Paths.get(r.getFilePath());

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found", e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + r.getFilename() + "\"")
                .body(resource);
    }

    // -------------------------------------------------------------
    // 4. GET EXPIRING WARRANTIES
    // -------------------------------------------------------------
    @GetMapping("/expiring")
    public ResponseEntity<List<Warranty>> getExpiring(@RequestParam int days) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(days);
        List<Warranty> list = warrantyService.findWarrantiesExpiringBetween(from, to);
        return ResponseEntity.ok(list);
    }

    // -------------------------------------------------------------
    // 5. CREATE USER
    // -------------------------------------------------------------
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User u) {
        return ResponseEntity.ok(userRepo.save(u));
    }

    // -------------------------------------------------------------
    // 6. CREATE PRODUCT
    // -------------------------------------------------------------
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product p) {
        return ResponseEntity.ok(productRepo.save(p));
    }
}
