package com.aicode.ml.urlshortenerapi.controller;

import com.aicode.ml.urlshortenerapi.dto.UrlLongRequest;
import com.aicode.ml.urlshortenerapi.entity.Url;
import com.aicode.ml.urlshortenerapi.service.UrlService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UrlController {
	
	@Value("${shortner.domain}")
	private String domain;

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @ApiOperation(value = "Convert new url", notes = "Converts long url to short url")
    @PostMapping("create-short")
    public String convertToShortUrl(@RequestBody UrlLongRequest request) {
        return urlService.convertToShortUrl(request);
    }

    @ApiOperation(value = "Redirect", notes = "Finds original url from short url and redirects")
    @GetMapping(value = "{shortUrl}")
    @Cacheable(value = "urls", key = "#shortUrl", sync = true)
    public ResponseEntity<Void> getAndRedirect(@PathVariable String shortUrl) {
        var url = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))//.header("Referrer",String.format("%s/%s", domain,shortUrl))
                .build();
    }
    
    @ApiOperation(value = "Short Urls", notes = "List of short urls")
    @PostMapping("urls")
    public List<Url> getUrlList() {
        return urlService.findLatestUrls();
    }
}
