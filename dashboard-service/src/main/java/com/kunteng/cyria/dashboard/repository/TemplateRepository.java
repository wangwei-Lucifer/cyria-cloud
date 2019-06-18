package com.kunteng.cyria.dashboard.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
	Template findByHash(String id);
	void deleteByHash(String id);
}
