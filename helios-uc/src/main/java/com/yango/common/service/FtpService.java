package com.yango.common.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yango.common.exception.ServiceException;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.ftp.Ftp;

@Component
public class FtpService {

	@Value("${ftp.host}")
	private String host;

	@Value("${ftp.port}")
	private String port;

	@Value("${ftp.username}")
	private String username;

	@Value("${ftp.password}")
	private String password;

	@Value("${ftp.serviceUrl}")
	private String serviceUrl;

	public void upload(String path, String filename, InputStream is) {
		Ftp ftp = new Ftp(host, Convert.toInt(port), username, password);
		try {
			ftp.mkDirs(path);
			boolean result = ftp.upload(path, filename, is);
			ftp.close();

			if (!result) {
				throw new ServiceException("上传未成功");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

}
