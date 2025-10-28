package com.dzarembo.bitgetservice.updater;

import com.dzarembo.bitgetservice.cache.FundingCache;
import com.dzarembo.bitgetservice.client.BitgetApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BitgetUpdater {
    private final FundingCache cache;
    private final BitgetApiClient apiClient;

    @Scheduled(fixedRate = 1 * 60 * 1000) // обновление каждые 1 минут
    public void updateFundingRates() {
        log.info("Updating Bitget funding cache...");
        cache.putAll(apiClient.fetchFundingRates());
        log.info("Bitget funding cache updated: {} entries", cache.getAll().size());
    }
}
