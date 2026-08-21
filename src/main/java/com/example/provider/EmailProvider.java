package com.example.provider;

import java.util.List;

public interface EmailProvider {

    void sendCriticalEmail(String to, String subject, String bodyHtml);

    void sendBulkEmail(String to, String firstName, String title, String bodyHtml);

    BulkEmailBatchResponse sendBatch(List<BulkEmailRecipientRequest> recipients, String title, String bodyHtml);

    boolean isBulkApiEnabled();

    boolean isRealBulkProviderConfigured();

    String getProviderName();
}

