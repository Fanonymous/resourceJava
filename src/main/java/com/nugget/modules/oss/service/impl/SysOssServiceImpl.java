

package com.nugget.modules.oss.service.impl;

import com.nugget.common.utils.Constant;
import com.nugget.common.utils.ParseXmlUtil;
import com.nugget.common.utils.R;
import com.nugget.modules.oss.dao.SysOssDao;
import com.nugget.modules.oss.entity.SysOssEntity;
import com.nugget.modules.oss.service.SysOssService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("sysOssService")
public class SysOssServiceImpl implements SysOssService {

	@Autowired
	private SysOssDao sysOssDao;

	@Override
	public SysOssEntity queryObject(Long id){
		return sysOssDao.queryObject(id);
	}

	@Override
	public List<SysOssEntity> queryList(Map<String, Object> map){
		return sysOssDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return sysOssDao.queryTotal(map);
	}

	@Override
	public void save(SysOssEntity sysOss){
		sysOssDao.save(sysOss);
	}

	@Override
	public void update(SysOssEntity sysOss){



		sysOssDao.update(sysOss);
	}

	@Override
	public void delete(Long id){
		sysOssDao.delete(id);
	}


	/**
	 * 查询并删除3个月前生成的文件
	 * @return
	 */
	@Override
	public void deleteFile() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date=sdf.format(new Date());
		Date nowDate;
		try {
			nowDate=sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(nowDate);
			calendar.add(Calendar.MONTH, -3);
			File file=new File(Constant.SYS_FILE_URL);
			String[]  tempList  =  file.list();
			File  temp  =  null;
			for  (int  i  =  0;  i  <  tempList.length;  i++)  {
				String path=Constant.SYS_FILE_URL+tempList[i];
				if(tempList[i].equals("errorFile")){
					continue;
				}
				temp  =  new  File(path);
				Date fileDate = sdf.parse(tempList[i]);
				if(sdf.parse(sdf.format(calendar.getTime())).getTime()>fileDate.getTime()){//如果存在这个文件
//					if (temp.exists()) {
					if (temp.isFile()) {//判断是否是文件
						file.delete();//删除文件
					}else if(temp.isDirectory()){
						File[] fi = temp.listFiles();
						for (int s = 0;s < fi.length;s ++) {//遍历目录下所有的文件
							File t  =  new  File(String.valueOf(fi[s]));
							Boolean fl=t.delete();
						}
						temp.delete();//删除文件夹
					}
//					}
				}
			}
			File files=new File(Constant.SYS_FILE_URL+Constant.EXPORT_ERROR_FILE_URL);
			String[]  tempLists  =  files.list();
			File  temps  =  null;
			for  (int  i  =  0;  i  <  tempLists.length;  i++)  {
				String path=Constant.SYS_FILE_URL+Constant.EXPORT_ERROR_FILE_URL+tempLists[i];
				temps  =  new  File(path);
				Date fileDate = sdf.parse(tempLists[i]);
				if(sdf.parse(sdf.format(calendar.getTime())).getTime()>fileDate.getTime()){//如果存在这个文件
					if (temps.exists()) {
						if (temps.isFile()) {//判断是否是文件
							files.delete();//删除文件
						}else if(temps.isDirectory()){
							File[] fi = temps.listFiles();
							for (int s = 0;s < fi.length;s ++) {//遍历目录下所有的文件
								File t  =  new  File(String.valueOf(fi[s]));
								Boolean fl=t.delete();
							}
							temps.delete();//删除文件夹
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String fileUpload(MultipartFile file, String type, String fileUrl, HttpServletResponse response, HttpServletRequest request)throws Exception
	{
		//允许上传的文件类型
		String fileType = "mp3,mp4,video,rmvb,pdf,txt,xml,doc,gif,png,bmp,jpeg";
		//允许上传的文件最大大小(100M,单位为byte)
		int maxSize = 1024*1024*100;
		response.addHeader("Access-Control-Allow-Origin", "*");
		//文件要保存的路径
		response.setContentType("text/html; charset=UTF-8");
		//检查目录
		File uploadDir = new File(fileUrl);
		if ( !uploadDir.exists())
		{
			uploadDir.mkdirs();
		}
		if ( !uploadDir.canWrite())
		{
			return "上传目录没有写权限！";
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024); //设置缓冲区大小，这里是1M
		factory.setRepository(uploadDir); //设置缓冲区目录

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");

		List items = upload.parseRequest(request);
		Iterator it = items.iterator();
		FileItem item = null;
		String fileName = "";
		String name = "";
		String extName = "";
		String newFileName = "";
		while (it.hasNext())
		{
			item = (FileItem)it.next();

			fileName = item.getName();
			if (null == fileName || "".equals(fileName))
			{
				continue;
			}

			//判断文件大小是否超限
			if (item.getSize() > maxSize)
			{
				item.delete();
				JOptionPane.showMessageDialog(null, "文件大小超过限制！应小于" + maxSize
						/ 1024 / 1024 + "M");
				return "文件大小超过限制！应小于" + maxSize;
			}

			//判断文件类型是否匹配
			//            System.getProperties().getProperty("file.separator"))
			//获取文件名称
			name = fileName.substring(fileName.lastIndexOf("\\") + 1,
					fileName.lastIndexOf("."));
			//获取文件后缀名
			extName = fileName.substring(fileName.indexOf(".") + 1).toLowerCase().trim();

			//判断是否为允许上传的文件类型
			if ( !Arrays.<String> asList(fileType.split(",")).contains(extName))
			{
				item.delete();
				JOptionPane.showMessageDialog(null, "文件类型不正确，必须为" + fileType
						+ "的文件！");
				return "文件类型不正确，必须为" + fileType
						+ "的文件！";
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			newFileName = name + df.format(new Date()) + "." + extName;
			File uploadedFile = new File(fileUrl, newFileName);
			item.write(uploadedFile);
		}

		return "success";
	}


	@Override
	public void deleteBatch(Long[] ids){
		sysOssDao.deleteBatch(ids);
	}

	@Override
	public R downloadFile2(String urlPath, String fileName, HttpServletResponse response,int type) throws Exception {

		BufferedOutputStream out = null;
		BufferedInputStream input = null;
		File file=null;
		if(type==1) {
			//通过文件路径获得File对象(假如此路径中有一个download.pdf文件)

			file = new File(Constant.RE_FILE_URL + urlPath);
		}else if(type==2){
			file = new File(Constant.PDF_FILE_URL + urlPath);
		}else if (type==3){
			file = new File(Constant.SYS_FILE_URL + urlPath);
		}

		fileName =  URLDecoder.decode(fileName, "UTF-8");
		fileName= new String(fileName.getBytes(),"ISO-8859-1");
//在下载菜单文件模板时生成菜单文件模板,应用发布使用
		ParseXmlUtil.createXml();

		if(!file.exists()){
//			throw new RRException("文件未找到,请联系超级管理员！");
			return R.error("文件未找到,请联系超级管理员！");
		}

		try {

			response.reset();
			//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
//			response.setContentType("application/octet-streem");

			//2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Content-Disposition", "attachment; filename =\"" + fileName+".xml"+"\"");
			response.setContentLength((int) file.length());
			input = new BufferedInputStream(new FileInputStream(file));
			//3.通过response获取ServletOutputStream对象(out)
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[4096];// 缓冲区
			int n = -1;
			//遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				out.write(buffer, 0, n);
			}
			out.flush();   //不可少
			response.flushBuffer();//不可少
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(input !=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return R.ok();
	}

}
