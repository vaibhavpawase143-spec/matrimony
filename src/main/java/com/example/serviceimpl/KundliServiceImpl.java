package com.example.serviceimpl;

import com.example.client.ProkeralaClient;
import com.example.dto.request.KundliMatchRequestDTO;
import com.example.dto.response.KundliMatchResponseDTO;
import com.example.service.KundliService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KundliServiceImpl implements KundliService {

    private final ProkeralaClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KundliMatchResponseDTO match(KundliMatchRequestDTO request) {

        // ✅ 1. Call API
        System.out.println("🔥 Calling Kundli Matching Service");
        String apiResponse = client.matchKundli(request);

        try {
            System.out.println("📦 API Response: " + apiResponse);

            JsonNode root = objectMapper.readTree(apiResponse);

            // ✅ 2. Validate "data"
            JsonNode data = root.path("data");
            if (data.isMissingNode() || data.isNull()) {
                throw new RuntimeException("❌ Invalid API response: 'data' not found");
            }

            // ✅ 3. Validate "guna_milan"
            JsonNode gunaMilan = data.path("guna_milan");
            if (gunaMilan.isMissingNode() || gunaMilan.isNull()) {
                throw new RuntimeException("❌ Invalid API response: 'guna_milan' not found");
            }

            // ✅ 4. Extract score safely
            int score = gunaMilan.path("total_points").asInt(0);

            // ✅ 5. Business logic
            boolean compatible = score >= 18;

            // ✅ 6. Build response
            KundliMatchResponseDTO response = new KundliMatchResponseDTO();
            response.setGunaMilanScore(score);
            response.setCompatible(compatible);
            response.setMessage(compatible ? "Good match" : "Not compatible");

            System.out.println("✅ Kundli Matching Success | Score: " + score);

            return response;

        } catch (Exception e) {
            System.out.println("❌ Error parsing API response: " + e.getMessage());
            throw new RuntimeException("Error parsing Prokerala response", e);
        }
    }
}