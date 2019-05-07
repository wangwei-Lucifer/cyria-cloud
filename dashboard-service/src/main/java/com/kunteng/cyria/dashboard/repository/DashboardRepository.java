package com.kunteng.cyria.dashboard.repository;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface DashboardRepository extends MongoRepository<Dashboard, String> {
	Page<Dashboard> findByUser(String user, Pageable pageable);
	String deleteByHash(String hash);
	Dashboard findByHash(String id);
}
