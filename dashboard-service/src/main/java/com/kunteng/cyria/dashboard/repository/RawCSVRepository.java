package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kunteng.cyria.dashboard.domain.RawCSV;

public interface RawCSVRepository extends MongoRepository<RawCSV, String>{
	RawCSV findByHash(String hash);

}
