package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.entity.Warranty;
import com.example.service.WarrantyService;

@Component
public class ReminderScheduler {
  private final WarrantyService warrantyService;
  @Value("${app.reminder.days-before:7}") private int daysBefore;

  @Autowired
  public ReminderScheduler(WarrantyService warrantyService) {
    this.warrantyService = warrantyService;
  }

  @Scheduled(cron = "0 0 9 * * ?") // every day at 9:00 AM
  public void sendDailyReminders() {
    LocalDate target = LocalDate.now().plusDays(daysBefore);
    List<Warranty> expiring = warrantyService.findWarrantiesExpiringBetween(LocalDate.now(), target);
    for (Warranty w : expiring) {
      if (!w.isReminderSent()) {
        warrantyService.sendReminder(w, daysBefore);
      }
    }
  }
}
