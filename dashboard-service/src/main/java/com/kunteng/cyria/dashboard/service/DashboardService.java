package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DashboardService {

	CommonResult getAllDashboard(String id, Map<String, Object> map);
	CommonResult getDashboardById(String id);
	CommonResult createNewDashboard(String id, Translation ts);
	CommonResult deleteDashboardByHash(String id);
	CommonResult publishDashboardById(String id, Map<String,String> map);
	CommonResult getPublishedById(String id);
	CommonResult updateDashboardById(String id, String db) throws IOException, Exception;
	CommonResult uploadImage(String id, MultipartFile files) throws IllegalStateException, IOException;
}
