/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.fhd.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.fhd.FileInfo;
import com.fhd.Message;
import com.fhd.service.FileService;
import com.fhd.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller - 文件处理
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Controller("adminFileController")
@RequestMapping("/admin/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileInfo.FileType fileType, MultipartFile file, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
		} else {
			String url = fileService.upload(fileType, file, false);
			if (url == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				data.put("message", SUCCESS_MESSAGE);
				data.put("url", url);
			}
		}
		try {
			response.setContentType("text/html; charset=UTF-8");
			JsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 浏览
	 */
	@RequestMapping(value = "/browser", method = RequestMethod.GET)
	public @ResponseBody
	List<FileInfo> browser(String path, FileInfo.FileType fileType, FileInfo.OrderType orderType) {
		return fileService.browser(path, fileType, orderType);
	}

}