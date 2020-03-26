package com.nugget.common.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 描述： 汉字转拼音工具类
 * @author Administrator
 * @date 2018/12/11
 */
@RestController
@RequestMapping("/commom/pinyinUtil")
public class PinyinUtil {

    @RequestMapping(value = "/chineseToPinyin",method = RequestMethod.GET)
    @ResponseBody
    public R equipUsageTime(@RequestParam String chineseStr, HttpServletRequest request){
        String chinese= null;
        try {
            chinese=URLDecoder.decode(chineseStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return R.ok().put("pinyin",chineseToPinyin(chinese));
    }

    public static String chineseToPinyin(String chineseStr) {
        String pinyin = "";
        try {
            if(chineseStr == null || "".equals(chineseStr)){
                return pinyin;
            }
            pinyin = PinyinHelper.convertToPinyinString(chineseStr, "", PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return pinyin;
    }

}
