package com.kunteng.cyria.dashboard.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
	Template findByHash(String id);
	void deleteByHash(String id);
}
