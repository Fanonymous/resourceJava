package com.nugget.modules.rs.controller;

import com.nugget.common.fastdfs.utils.FastDFSUtils;
import com.nugget.common.utils.R;
import com.nugget.common.utils.VideoUtils;
import com.nugget.modules.rs.dao.RsFileDetailsDAO;
import com.nugget.modules.rs.service.ResourcesService;
import net.sf.json.JSONObject;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LIUHAO
 * Date:2020/2/7
 * 资源详情
 */
@RestController
@RequestMapping("/resourcesInfo")
public class ResourcesInfoController {

    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private RsFileDetailsDAO fileDetailsDAO;

    /**
     * 资源搜索
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public R search(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return resourcesService.search(param);

    }

    /**
     * 资源列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/resourcesList", method = RequestMethod.POST)
    @ResponseBody
    public R resourcesList(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return resourcesService.resourcesList(param);

    }


    /**
     * 资源详情
     *
     * @param resourcesId
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public R info(@RequestParam String resourcesId, @RequestParam String rsUserId
    ) {
        if (resourcesId == null && rsUserId == null) {
            return R.error("参数不能为空");
        }
        return resourcesService.info(resourcesId, rsUserId);

    }

    /**
     * 相关资源推荐
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/relationResources", method = RequestMethod.POST)
    @ResponseBody
    public R relationResources(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return resourcesService.relationResources(param);

    }

    /**
     * 名师资源
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bestTeacherResources", method = RequestMethod.POST)
    @ResponseBody
    public R bestTeacherResources(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return resourcesService.bestTeacherResources(param);

    }

    /**
     * 猜您喜欢
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/mayLikeResources", method = RequestMethod.POST)
    @ResponseBody
    public R mayLikeResources(@RequestBody Map<String, Object> param
    ) {
        if (param.isEmpty()) {
            return R.error("参数不能为空");
        }
        return resourcesService.mayLikeResources(param);

    }

    /**
     * 添加微课资源
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources(new HashMap<>());

    }


    /**
     * 添加flash资源
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources1", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources1(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources1(new HashMap<>());

    }


    /**
     * 添加德育资源
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources2", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources2(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources2(new HashMap<>());

    }

    /**
     * 添加安全教育
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources3", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources3(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources3(new HashMap<>());

    }


    /**
     * 添加文库
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources4", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources4(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources4(new HashMap<>());

    }

    /**
     * 添加精品课程
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveResources5", method = RequestMethod.GET)
    @ResponseBody
    public R saveResources5(
    ) {
//		if(param.isEmpty()){
//			return R.error("参数不能为空");
//		}
        return resourcesService.saveResources5(new HashMap<>());

    }

    /**
     * 上传视频
     */
    @PostMapping("/viedoUpload")
    public void viedoUpload(@RequestParam("file") String filepath, HttpServletResponse response) throws Exception {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        File file = new File(filepath);//File类型可以是文件也可以是文件夹


        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File pdf = file;

                    FileInputStream fileInputStream = null;
                    fileInputStream = new FileInputStream(pdf);

                    MultipartFile multipartFile = new MockMultipartFile(pdf.getName(), pdf.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                    //创建临时文件
                    File jpgFile = File.createTempFile("temp", ".jpg");
                    //将视频第一帧转化为图片file文件
                    VideoUtils.fetchFrame(multipartFile, jpgFile);
                    String jpgpath = FastDFSUtils.saveFile(jpgFile);

                    String path = FastDFSUtils.saveFile(multipartFile);
                    newViedoFile(path, jpgpath, multipartFile.getOriginalFilename(), response);
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }
            }
        });


    }


    private void newViedoFile(String path, String viedoImage, String name, HttpServletResponse response) {
        JSONObject responseJSONObject = JSONObject.fromObject(R.ok().put("url", path).put("viedoImage", viedoImage));
        Map<String, Object> map = new HashMap<>();
        String url = "http://192.168.1.206:9989/group1/";
        map.put("title", name);
        map.put("introduction", name);
        map.put("resourcesAddress", url + path);
        map.put("searchFiled", name);
        map.put("thumbAddress", url + viedoImage);
        map.put("headerImageAddress", url + viedoImage);
        map.put("resourcesType", 13);
        map.put("channelId", 12);
//		map.put("tags",tagId);
        fileDetailsDAO.saveMap(map);


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

        resourcesService.downloadFile2(filePath, fileName, response, 3);
    }

}
