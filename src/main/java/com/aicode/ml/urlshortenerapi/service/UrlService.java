package com.aicode.ml.urlshortenerapi.service;

import com.aicode.ml.urlshortenerapi.dto.UrlLongRequest;
import com.aicode.ml.urlshortenerapi.entity.Url;
import com.aicode.ml.urlshortenerapi.repository.UrlRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

	private final UrlRepository urlRepository;
	private final BaseConversion conversion;

	@Value("${shortner.domain}")
	private String domain="http://fkt.in"; // Use this attribute to generate urls for a custom
	// domain name defaults to http://fkt.in
	private char myChars[]; // This array is used for character to number
	// mapping
	private Random myRand; // Random object used to generate random integers

	@Value("${shortner.keylength}")
	private int keyLength; // the key length in URL defaults to 8
	@Value("${affiliate.flipkart.id}")
	private Object affiliateId;

	public UrlService(UrlRepository urlRepository, BaseConversion baseConversion) {
		this.urlRepository = urlRepository;
		this.conversion = baseConversion;
		myRand = new Random();
		keyLength = 8;
		myChars = new char[62];
		for (int i = 0; i < 62; i++) {
			int j = 0;
			if (i < 10) {
				j = i + 48;
			} else if (i > 9 && i <= 35) {
				j = i + 55;
			} else {
				j = i + 61;
			}
			myChars[i] = (char) j;
		}
	}

	public String convertToShortUrl(UrlLongRequest request) {
		var url = new Url();
		url.setLongUrl(request.getLongUrl());
		url.setKey(generateKey());
		url.setExpiresDate(request.getExpiresDate());
		url.setCreatedDate(new Date());
		var entity = urlRepository.save(url);

		//return conversion.encode(entity.getId());
		return String.format("%s/%s",domain, url.getKey());
	}


	// generateKey
	private String generateKey() {
		String key = "";
		boolean flag = true;
		while (flag) {
			key = "";
			for (int i = 0; i <= keyLength; i++) {
				key += myChars[myRand.nextInt(62)];
			}
			// System.out.println("Iteration: "+ counter + "Key: "+ key);
			Optional<Url> optionalUrl = urlRepository.findByKey(key);
			if (optionalUrl.isEmpty() || optionalUrl.isPresent()) {
				flag = false;
			}
		}
		return key;
	}

	public String getOriginalUrl(String shortUrl) {
		//var id = conversion.decode(shortUrl);
		var entity = urlRepository.findByKey(shortUrl)
				.orElseThrow(() -> new EntityNotFoundException("There is no entity with " + shortUrl));

		if (entity.getExpiresDate() != null && entity.getExpiresDate().before(new Date())){
			urlRepository.delete(entity);
			throw new EntityNotFoundException("Link expired!");
		}

		return sanitizeURL(entity.getLongUrl());
	}

	// sanitizeURL
	// This method should take care various issues with a valid url
	// e.g. www.google.com,www.google.com/, http://www.google.com,
	// http://www.google.com/
	// all the above URL should point to same shortened URL
	// There could be several other cases like these.
	private String sanitizeURL(String url) {

		if(url.contains("flipkart")) {
			url = url.replace("www.flipkart.com","dl.flipkart.com/dl");
			if(url.contains("?"))
				url = String.format("%s&affid=%s",url,affiliateId);
			else
				url = String.format("%s?affid=%s",url,affiliateId);
		}

		/*
		 * if (url.substring(0, 7).equals("http://")) url = url.substring(7);
		 * 
		 * if (url.substring(0, 8).equals("https://")) url = url.substring(8);
		 * 
		 * if (url.charAt(url.length() - 1) == '/') url = url.substring(0, url.length()
		 * - 1);
		 */
		return url;
	}

	public List<Url> findLatestUrls() {
		return urlRepository.findAll();
	}



}
