package com.yango.common.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yango.common.service.FtpService;
import com.yango.common.web.vo.ResultVo;

import cn.hutool.core.util.IdUtil;

@RestController
@RequestMapping("/rest/common")
public class CommonRestController {

	@Autowired
	private FtpService ftpService;
	
	// 上传文件
	@PostMapping("/upload")
	public ResultVo upload(String type, @RequestParam("file") MultipartFile file) throws IOException {
		String uuid = IdUtil.fastSimpleUUID();
		String fileName = file.getOriginalFilename();

		String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		String newFileName = uuid + suffix;

		String path = "/static/" + type;

		ftpService.upload(path, newFileName, file.getInputStream());

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("serviceUrl", ftpService.getServiceUrl()+"/"+path);
		resultMap.put("filePath", fileName);
		return ResultVo.ok(resultMap);
	}

}
