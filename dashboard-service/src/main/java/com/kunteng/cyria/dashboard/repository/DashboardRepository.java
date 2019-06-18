package com.kunteng.cyria.dashboard.repository;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

@Repository
public interface DashboardRepository extends MongoRepository<Dashboard, String> {
	@Query(value= "{'user':?0}",fields="{'config.title':1, 'user':1, 'hash':1, 'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUser(String user, Pageable pageable);
	
	@Query(value= "{'publish.status':?1}",fields="{'config.title':1, 'user':1, 'hash':1, 'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUserAndStatus(String user, String status,Pageable pageable);
	
	@Query(value= "{'config.title':?1}",fields="{'config.title':1, 'user':1, 'hash':1, 'publish.status':1,'publish.hash':1}")
	List<Dashboard> findByUserAndTitle(String user, String title,Pageable pageable);
	
	@Query(value= "{'config.title':?1, 'publish.status':?2}",fields="{'config.title':1, 'user':1,'hash':1, 'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUserAndTitleAndStatus(String user, String title, String status, Pageable pageable);
	
	@Query(value= "{'user':?0, 'project':?1}",fields="{'config.title':1, 'user':1, 'hash':1, 'project':1 ,'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUserAndProject(String user, String project, Pageable pageable);
	
	@Query(value= "{'publish.status':?1, 'project':?2}",fields="{'config.title':1, 'user':1, 'hash':1, 'project':1 ,'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUserAndStatusAndProject(String user, String status, String project,Pageable pageable);
	
	@Query(value= "{'config.title':?1, 'project':?2}",fields="{'config.title':1, 'user':1, 'hash':1, 'project':1 ,'publish.status':1,'publish.hash':1}")
	List<Dashboard> findByUserAndTitleAndProject(String user, String title, String project, Pageable pageable);
	
	@Query(value= "{'config.title':?1, 'publish.status':?2, 'project':?3}",fields="{'config.title':1, 'user':1,'hash':1,'project':1 , 'publish.status':1, 'publish.hash':1}")
	List<Dashboard> findByUserAndTitleAndStatusAndProject(String user, String title, String status, String project ,Pageable pageable);
	
	//@Query(value= "{'user':?0}",count=true)
	Long countByUser(String user);
	
	@Query(value= "{'publish.status':?1}",count=true)
	Long countByUserAndStatus(String user, String status);
	
	@Query(value= "{'config.title':?1}",count=true)
	Long countByUserAndTitle(String user, String title);
	
	@Query(value= "{'config.title':?1, 'publish.status':?2}",count=true)
	Long countByUserAndTitleAndStatus(String user, String title,String status);
	
	@Query(value= "{'user':?0, 'project':?1}",count=true)
	Long countByUserAndProject(String user, String project);
	
	@Query(value= "{'publish.status':?1, 'project':?2}",count=true)
	Long countByUserAndStatusAndProject(String user, String status, String project);
	
	@Query(value= "{'config.title':?1, 'project':?2}",count=true)
	Long countByUserAndTitleAndProject(String user, String title, String project);
	
	@Query(value= "{'config.title':?1, 'publish.status':?2, 'project':?3}",count=true)
	Long countByUserAndTitleAndStatusAndProject(String user, String title, String status, String project);
	
	String deleteByHash(String hash);
	Dashboard findByHash(String id);

	Dashboard findByProject(String key);
}
