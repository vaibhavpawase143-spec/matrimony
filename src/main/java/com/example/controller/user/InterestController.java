package com.example.controller.user; // user folder

import com.example.model.Interest;
import com.example.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    @Autowired
    private InterestService interestService;

    // Send interest
    @PostMapping("/send")
    public ResponseEntity<Interest> sendInterest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(interestService.sendInterest(senderId, receiverId));
    }

    // Get sent interests
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Interest>> getSent(@PathVariable Long senderId) {
        return ResponseEntity.ok(interestService.getSentInterests(senderId));
    }

    // Get received interests
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<Interest>> getReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(interestService.getReceivedInterests(receiverId));
    }

    // Get received pending interests
    @GetMapping("/received/{receiverId}/pending")
    public ResponseEntity<List<Interest>> getReceivedPending(@PathVariable Long receiverId) {
        return ResponseEntity.ok(interestService.getReceivedPending(receiverId));
    }

    // Accept interest
    @PostMapping("/accept")
    public ResponseEntity<Interest> accept(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(interestService.acceptInterest(senderId, receiverId));
    }

    // Reject interest
    @PostMapping("/reject")
    public ResponseEntity<Interest> reject(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(interestService.rejectInterest(senderId, receiverId));
    }

    // Cancel interest
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long senderId, @RequestParam Long receiverId) {
        interestService.cancelInterest(senderId, receiverId);
        return ResponseEntity.noContent().build();
    }
}