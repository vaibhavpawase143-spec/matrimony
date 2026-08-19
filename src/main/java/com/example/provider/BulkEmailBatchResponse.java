package com.example.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkEmailBatchResponse {
    private String providerBatchId;
    private boolean success;
    private int acceptedCount;
    private int rejectedCount;
    private String providerMessage;
    private List<Long> failedUserIds;
}
