

package com.nugget.modules.oss.service;

import com.nugget.common.utils.R;
import com.nugget.modules.oss.entity.SysOssEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
public interface SysOssService{

	SysOssEntity queryObject(Long id);

	List<SysOssEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(SysOssEntity sysOss);

	void update(SysOssEntity sysOss);

	void delete(Long id);

	void deleteFile();

	void deleteBatch(Long[] ids);

	R downloadFile2(String urlPath, String excelName, HttpServletResponse response, int i) throws Exception;

	String fileUpload(MultipartFile file, String type, String sysFileUrl, HttpServletResponse response, HttpServletRequest request)throws Exception;
}
