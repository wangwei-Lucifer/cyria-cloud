package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.FinalCSV;

@Repository
public interface FinalCSVRepository extends MongoRepository<FinalCSV, String> {

	FinalCSV findByFileName(String fileName);

	long countByTitle(String project);

	FinalCSV findByHash(String hash);

	void deleteByHash(String hash);

//	boolean existsByFileName(String fileName);

}
