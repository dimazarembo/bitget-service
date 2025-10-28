package com.dzarembo.bitgetservice.client;

import com.dzarembo.bitgetservice.model.FundingRate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class BitgetApiClient {

    private final WebClient webClient = WebClient.create("https://api.bitget.com");

    public Collection<FundingRate> fetchFundingRates() {
        try {
            BitgetResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v2/mix/market/current-fund-rate")
                            .queryParam("productType", "usdt-futures")
                            .build())
                    .retrieve()
                    .bodyToMono(BitgetResponse.class)
                    .block();

            if (response == null || response.getData() == null) {
                log.warn("Empty response from Bitget");
                return List.of();
            }

            return response.getData().stream()
                    .map(this::mapToFundingRate)
                    .filter(fr -> fr != null)
                    .toList();

        } catch (Exception e) {
            log.error("Failed to fetch funding rates from Bitget", e);
            return List.of();
        }
    }

    private FundingRate mapToFundingRate(BitgetResponse.Item item) {
        try {
            double rate = Double.parseDouble(item.getFundingRate());
            int intervalHours = Integer.parseInt(item.getFundingRateInterval());
            long nextFundingTimeUtc = Long.parseLong(item.getNextUpdate());

            log.debug("Bitget: {} rate={}, nextFundingTime(UTC)={}, interval={}h",
                    item.getSymbol(),
                    rate,
                    Instant.ofEpochMilli(nextFundingTimeUtc),
                    intervalHours
            );

            return new FundingRate(
                    item.getSymbol(),
                    rate,
                    nextFundingTimeUtc,
                    intervalHours
            );
        } catch (Exception e) {
            log.error("Failed to parse Bitget item: {}", item, e);
            return null;
        }
    }

    @Data
    public static class BitgetResponse {
        private String code;
        private String msg;
        private long requestTime;
        private List<Item> data;

        @Data
        public static class Item {
            private String symbol;               // "BTCUSDT"
            private String fundingRate;          // "0.000068"
            private String fundingRateInterval;  // "8"
            private String nextUpdate;           // "1743062400000"
        }
    }
}
