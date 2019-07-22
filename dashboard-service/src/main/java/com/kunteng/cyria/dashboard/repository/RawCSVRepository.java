package com.kunteng.cyria.dashboard.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;

public interface RawCSVRepository extends MongoRepository<RawCSV, String>{
	RawCSV findByHash(String hash);

	void deleteByHash(String hash);

}
