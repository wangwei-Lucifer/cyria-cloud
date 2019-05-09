package com.kunteng.cyria.dashboard.controller;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

//	@PreAuthorize("#oauth2.hasScope('server') or #name.equals('dashboard')")
	@RequestMapping(path = "/user/{id}/dashboards", method = RequestMethod.GET)
	public Page<Dashboard> getAllDashboard(@PathVariable String id, @Valid @RequestBody Integer page, @Valid @RequestBody Integer size ){
		return dashboardService.getAllDashboard(id, page, size);
	}

	@RequestMapping(path = "/user/{id}/dashboards", method = RequestMethod.POST)
	public Dashboard createNewDashboard(@PathVariable String id, @Valid @RequestBody Translation ts) {
		return dashboardService.createNewDashboard(id, ts);
	}

	@RequestMapping(path = "/dashboards/{id}", method = RequestMethod.GET)
	public Dashboard getDashboardById(@PathVariable String id){
		return dashboardService.getDashboardById(id);
	}
	
	@RequestMapping(path = "/dashboards/{id}", method = RequestMethod.DELETE)
	public String deleteDashboardByHash(@PathVariable String id) {
		return dashboardService.deleteDashboardByHash(id);
	}
	
	@RequestMapping(path = "/dashboards/{id}", method = RequestMethod.PUT)
	public void updateDashboardById(@PathVariable String id, @RequestBody String db) throws Exception {
		dashboardService.updateDashboardById(id, db);
	}
	
	@RequestMapping(path = "/publish/{id}", method = RequestMethod.POST)
	public void publishDashboardById(@PathVariable String id, @RequestBody String option){
		dashboardService.publishDashboardById(id, option);
	}
	
	@RequestMapping(path= "/publish/{id}", method = RequestMethod.GET)
	public Published getPublishedById(@PathVariable String id) {
		return dashboardService.getPublishedById(id);
	}
	
	@RequestMapping(path= "/upload/image/{id}", method = RequestMethod.POST)
	public Object uploadImage(@PathVariable String id, @RequestBody MultipartFile files) throws IllegalStateException, IOException {
		return dashboardService.uploadImage(id, files);
	}
}
