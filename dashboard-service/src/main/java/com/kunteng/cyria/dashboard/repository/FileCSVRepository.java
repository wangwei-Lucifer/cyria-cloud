package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.FinalCSV;

@Repository
public interface FileCSVRepository extends MongoRepository<FinalCSV, String> {

}
