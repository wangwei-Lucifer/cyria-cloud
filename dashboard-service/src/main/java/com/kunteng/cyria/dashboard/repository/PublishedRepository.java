package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.Published;

@Repository
public interface PublishedRepository extends MongoRepository<Published, String> {
	Published findByHash(String id);
	String deleteByHash(String hash);
}
