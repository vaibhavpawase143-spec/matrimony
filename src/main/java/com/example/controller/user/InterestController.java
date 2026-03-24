package com.example.controller.user;

import com.example.model.Interest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;
    private final UserRepository userRepository;

    public InterestController(InterestService interestService,
                              UserRepository userRepository) {
        this.interestService = interestService;
        this.userRepository = userRepository;
    }

    // ✅ Send interest
    @PostMapping("/send")
    public ResponseEntity<Interest> sendInterest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Interest interest = new Interest();
        interest.setSender(sender);
        interest.setReceiver(receiver);

        Interest saved = interestService.sendInterest(interest);

        return ResponseEntity.ok(saved);
    }

    // ✅ Get sent interests
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Interest>> getSent(@PathVariable Long senderId) {
        return ResponseEntity.ok(interestService.getBySender(senderId));
    }

    // ✅ Get received interests
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<Interest>> getReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(interestService.getByReceiver(receiverId));
    }

    // ✅ Get received pending interests
    @GetMapping("/received/{receiverId}/pending")
    public ResponseEntity<List<Interest>> getReceivedPending(@PathVariable Long receiverId) {
        return ResponseEntity.ok(
                interestService.getByReceiverAndStatus(receiverId, "PENDING")
        );
    }

    // ✅ Accept interest
    @PostMapping("/accept/{id}")
    public ResponseEntity<Interest> accept(@PathVariable Long id) {
        return ResponseEntity.ok(
                interestService.updateStatus(id, "ACCEPTED")
        );
    }

    // ✅ Reject interest
    @PostMapping("/reject/{id}")
    public ResponseEntity<Interest> reject(@PathVariable Long id) {
        return ResponseEntity.ok(
                interestService.updateStatus(id, "REJECTED")
        );
    }

    // ✅ Delete / Cancel interest
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}