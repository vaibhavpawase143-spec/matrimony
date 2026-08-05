package com.example.controller.user;

import com.example.dto.request.InterestRequestDTO;
import com.example.dto.response.InterestResponseDTO;
import com.example.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    // ✅ Send interest
    @PostMapping("/send")
    public ResponseEntity<InterestResponseDTO> sendInterest(
            @RequestBody InterestRequestDTO request) {

        return ResponseEntity.ok(interestService.sendInterest(request));
    }

    // ✅ Get sent interests
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<InterestResponseDTO>> getSent(@PathVariable Long senderId) {
        return ResponseEntity.ok(interestService.getBySender(senderId));
    }

    // ✅ Get received interests
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<InterestResponseDTO>> getReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(interestService.getByReceiver(receiverId));
    }

    // ✅ Get received pending
    @GetMapping("/received/{receiverId}/pending")
    public ResponseEntity<List<InterestResponseDTO>> getReceivedPending(@PathVariable Long receiverId) {
        return ResponseEntity.ok(
                interestService.getByReceiverAndStatus(receiverId, "PENDING")
        );
    }

    // ✅ Accept
    @PutMapping("/accept/{id}")
    public ResponseEntity<InterestResponseDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(
                interestService.updateStatus(id, "ACCEPTED")
        );
    }

    // ✅ Reject
    @PutMapping("/reject/{id}")
    public ResponseEntity<InterestResponseDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(
                interestService.updateStatus(id, "REJECTED")
        );
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interestService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // =====================================================
// PAGINATED SENT INTERESTS
// =====================================================

    @GetMapping("/sent/{senderId}/page")
    public ResponseEntity<Page<InterestResponseDTO>> getSentPage(
            @PathVariable Long senderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                interestService.getBySender(
                        senderId,
                        pageable
                )
        );
    }

// =====================================================
// PAGINATED RECEIVED INTERESTS
// =====================================================

    @GetMapping("/received/{receiverId}/page")
    public ResponseEntity<Page<InterestResponseDTO>> getReceivedPage(
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                interestService.getByReceiver(
                        receiverId,
                        pageable
                )
        );
    }

// =====================================================
// PAGINATED RECEIVED PENDING
// =====================================================

    @GetMapping("/received/{receiverId}/pending/page")
    public ResponseEntity<Page<InterestResponseDTO>> getReceivedPendingPage(
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                interestService.getByReceiverAndStatus(
                        receiverId,
                        "PENDING",
                        pageable
                )
        );
    }
}