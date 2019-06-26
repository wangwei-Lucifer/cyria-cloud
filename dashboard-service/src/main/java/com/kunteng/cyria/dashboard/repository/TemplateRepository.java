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
	
	@Query(value="{'hash':{'$ne':null}}",fields="{'hash':1, 'config.title':1, 'imgUrl':1, 'config.height':1, 'config.width':1, 'timestamp':1}")
	Page<Template> findByHashNotNull(Pageable pageable);
}
