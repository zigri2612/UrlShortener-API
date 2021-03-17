package com.aicode.ml.urlshortenerapi.repository;

import com.aicode.ml.urlshortenerapi.entity.Url;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

	//@Query("select 1 from URL where key=:key")
	Optional<Url> findByKey(String key);
}
