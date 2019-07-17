package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.TempCSV;

@Repository
public interface TempCSVRepository  extends MongoRepository<TempCSV, String>{

}
