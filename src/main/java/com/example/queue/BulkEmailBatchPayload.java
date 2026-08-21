package com.example.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkEmailBatchPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchId;
    private Long broadcastJobId;
    private String title;
    private String message;
    private List<RecipientItem> recipients;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long userId;
        private String email;
        private String firstName;
    }
}
