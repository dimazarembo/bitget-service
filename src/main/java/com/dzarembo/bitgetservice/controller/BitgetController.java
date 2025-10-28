package com.dzarembo.bitgetservice.controller;

import com.dzarembo.bitgetservice.cache.FundingCache;
import com.dzarembo.bitgetservice.model.FundingRate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/bitget")
@RequiredArgsConstructor
public class BitgetController {
    private final FundingCache cache;

    @GetMapping("/funding")
    public Collection<FundingRate> getFundingRates() {
        return cache.getAll();
    }
}
