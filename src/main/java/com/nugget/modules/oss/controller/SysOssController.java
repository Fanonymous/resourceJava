

package com.nugget.modules.oss.controller;


import com.nugget.common.fastdfs.utils.FastDFSUtils;
import com.nugget.common.utils.*;

import com.nugget.modules.oss.entity.SysOssEntity;
import com.nugget.modules.oss.service.SysOssService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
@Controller
@RequestMapping("sys/oss")
public class SysOssController {
    @Autowired
    private SysOssService sysOssService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

    /**
     * 列表
     */
    @RequestMapping("/list")
//	@RequiresPermissions("sys:oss:all")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysOssEntity> sysOssList = sysOssService.queryList(query);
        int total = sysOssService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysOssList, total, query.getLimit(), query.getCurrPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 上传图片
     */
    @PostMapping("/picUpload")
    public void picUpload(@RequestParam("file") MultipartFile file, String type, HttpServletResponse response
    ) throws Exception {
        String path = FastDFSUtils.saveFile(file);
        newProFile(path, response);
    }


    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public void upload(@RequestParam MultipartFile file, String type, HttpServletResponse response
    ) throws Exception {
        String path = FastDFSUtils.saveFile(file);
        newProFile(path, response);
    }

    /**
     * 客户端上传文件
     */
    @PostMapping("/uploadClient")
    public void uploadClient(@RequestParam MultipartFile file,
                             HttpServletResponse response) throws Exception {
        String path = FastDFSUtils.saveFile(file);
        newProFile(path, response);

    }

    /**
     * 上传文件
     */
    @PostMapping("/uploadMenuFile")
    public void uploadMenuFile(@RequestParam MultipartFile file, String type,
                               HttpServletResponse response) throws Exception {

        String path = FastDFSUtils.saveFile(file);
        newProFile(path, response);

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//	@RequiresPermissions("sys:oss:all")
    public R delete(@RequestBody Long[] ids) {
        sysOssService.deleteBatch(ids);

        return R.ok();
    }

    private String getFileName(String fileType) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid + fileType;
    }

//	private void proFile(MultipartFile file,String type,String fPath,HttpServletResponse response)throws Exception{
//
//		if (file.isEmpty()) {
//			throw new RRException("上传文件不能为空");
//		}
//
////
//
//		String fileName=getFileName(type);
//		StringBuffer pathOne=new StringBuffer();
//		pathOne.append(fPath);
//		pathOne.append(DateUtils.format(new Date(), "yyyyMMdd")+"/");
//
//		StringBuffer pathTwo=new StringBuffer();
//		pathTwo.append(pathOne);
//		pathTwo.append(fileName);
//
//		StringBuffer pathThr=new StringBuffer();
//		pathThr.append(DateUtils.format(new Date(), "yyyyMMdd")+"/");
//		pathThr.append(fileName);
//
//		CreateFileUtil.createDir(pathOne.toString());
//
//		//缺少有效性处理
//		File newFile=new File(pathTwo.toString());
//		//通过CommonsMultipartFile的方法直接写文件（注意这个时候）
//		try {
//			file.transferTo(newFile);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
////		String url = OSSFactory.build().upload(file.getBytes());
//
//		//保存文件信息
//		SysOssEntity ossEntity = new SysOssEntity();
//		ossEntity.setUrl(pathThr.toString());
//		ossEntity.setCreateDate(new Date());
//		sysOssService.save(ossEntity);
//		JSONObject responseJSONObject = JSONObject.fromObject(R.ok().put("url", pathThr.toString()));
//
//
//
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("application/json; charset=utf-8");
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			out.append(responseJSONObject.toString());
//			logger.debug("返回是\n");
//			logger.debug(responseJSONObject.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null) {
//				out.flush();
//				out.close();
//			}
//		}
//		response.setHeader("Access-Control-Allow-Origin", "*");
//
//	}

    private void newProFile(String path, HttpServletResponse response) {

        JSONObject responseJSONObject = JSONObject.fromObject(R.ok().put("url", path));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    /**
     * 下载文件
     */
    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam String filePath, @RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {

        sysOssService.downloadFile2(filePath, fileName, response, 3);
    }


}
