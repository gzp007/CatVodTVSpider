package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

import com.github.catvod.utils.okhttp.OkHttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aidi 爬虫 - 写死测试数据版（适配影视仓）
 * 所有接口返回固定数据，无需依赖外部网站，仅用于测试接口调用和显示
 */
public class Test extends Spider {
    private static final String siteUrl = "clan://localhost/TVBox/live.txt";
    private static final String siteHost = "localhost/TVBox";
    private static final String TAG = "TestSpider";

    // 固定播放源配置（影视仓显示用）
    private JSONObject playerConfig;
    // 固定筛选配置（影视仓筛选面板显示用）
    private JSONObject filterConfig;

    @Override
    public void init(Context context) {
        super.init(context);
        SpiderDebug.log(TAG + "===== 初始化 Test 测试爬虫 =====");
        try {
            // 固定播放源配置
            playerConfig = new JSONObject("{\"xg_app_player\":{\"sh\":\"app全局解析\",\"pu\":\"\",\"sn\":0,\"or\":999},\"ckm3u8\":{\"sh\":\"测试影视\",\"pu\":\"\",\"sn\":0,\"or\":999},\"xin\":{\"sh\":\"测试高速\",\"pu\":\"\",\"sn\":0,\"or\":999}}");
            // 固定筛选配置
            filterConfig = new JSONObject("{\"dianying\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"dianying\"},{\"n\":\"动作片\",\"v\":\"dongzuopian\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"大陆\"}]}],\"lianxuju\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"lianxuju\"},{\"n\":\"国产剧\",\"v\":\"guochanju\"}]}]}");
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "初始化配置失败：" + e.getMessage());
        }
    }

    /**
     * 爬虫headers
     *
     * @param url
     * @return
     */
    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("Host", siteHost);
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("DNT", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return headers;
    }

    /**
     * 首页内容 - 写死固定数据
     */
    @Override
    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            // 固定分类列表
            JSONArray classes = new JSONArray();
            classes.put(new JSONObject().put("type_id", "dianying").put("type_name", "电影"));
            classes.put(new JSONObject().put("type_id", "lianxuju").put("type_name", "连续剧"));
            classes.put(new JSONObject().put("type_id", "dongman").put("type_name", "动漫"));
            result.put("class", classes);

            // 固定筛选配置（如果开启筛选）
            if (filter) {
                result.put("filters", filterConfig);
            }

            // 固定首页视频列表
            JSONArray videos = new JSONArray();
            videos.put(new JSONObject()
                    .put("vod_id", "HC-vod_id")
                    .put("vod_name", "HC-vod_name")
                    .put("vod_pic", "")
                    .put("vod_remarks", "HC-vod_remarks"));
            result.put("list", videos);

            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "首页数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 分类内容 - 写死固定数据
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            JSONObject result = new JSONObject();
            // 固定分页信息
            result.put("page", Integer.parseInt(TextUtils.isEmpty(pg) ? "1" : pg));
            result.put("pagecount", 1);
            result.put("limit", 60);
            result.put("total", 2);

            // 固定分类视频列表
            JSONArray videos = new JSONArray();
            //tid 代表 class中的type_id
            videos.put(new JSONObject()
                    .put("vod_id", "CC-vod_id-tid" + tid)
                    .put("vod_name", "CC-vod_name-tid" + tid)
                    .put("vod_pic", "")
                    .put("vod_remarks", "CC-vod_remarks-tid" + tid));
            result.put("list", videos);

            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "分类数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 视频详情 - 写死固定数据
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();

            // 固定详情数据（根据id返回不同测试数据）
            String vodId = ids.get(0);
            JSONObject vodInfo = new JSONObject();
            vodInfo.put("vod_id", vodId)
                    .put("vod_name", "DC-vod_name-ids" + ids)
                    .put("vod_pic", "")
                    .put("type_name", "DC-type_name")
                    .put("vod_year", "DC-vod_year")
                    .put("vod_area", "DC-vod_area")
                    .put("vod_remarks", "DC-vod_remarks")
                    .put("vod_actor", "DC-vod_actor")
                    .put("vod_director", "DC-vod_director")
                    .put("vod_content", "DC-vod_content")
                    // 播放源（影视仓核心字段）
                    .put("vod_play_from", "线路一$$$线路二")
                    .put("vod_play_url", "第1集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240095359203.mp4#第2集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240368550193.mp4$$$第1集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4#第2集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4");

            list.put(vodInfo);
            result.put("code", 200)
                    .put("msg", "success")
                    .put("list", list);

            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "详情数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 播放地址 - 写死固定数据
     */
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject result = new JSONObject();
            // 固定播放地址（测试用的有效m3u8地址，可直接播放）
            result.put("code", 200) // 新增：前端常用的成功状态码
                    .put("msg", "success") // 新增：提示信息
                    .put("parse", 0) // 0=不解析，直接播放；1=需要解析
                    .put("playUrl", "")//有值的话会报”播放错误“
                    .put("url", id)//无值的话会报”播放错误“ 最终要播放的URL https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4
                    .put("header", "")//可为空
                    .put("flag", flag)
                    .put("videoId", id);

            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "播放数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 搜索内容 - 写死固定数据
     */
    @Override
    public String searchContent(String key, boolean quick) {
        try {
            JSONObject result = new JSONObject();
            JSONArray videos = new JSONArray();
            if ("SC-vod_id".equals(key)) {
                // 固定搜索结果
                videos.put(new JSONObject()
                        .put("vod_id", "SC-vod_id")
                        .put("vod_name", "SC-vod_name")
                        .put("vod_pic", "")
                        .put("vod_remarks", "SC-vod_remarks"));
            }
            result.put("list", videos);
            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "搜索数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 通用空结果（避免返回空字符串导致影视仓崩溃）
     */
    private String getDefaultEmptyResult() {
        try {
            return new JSONObject().put("list", new JSONArray()).toString();
        } catch (JSONException e) {
            return "{\"list\":[]}";
        }
    }
}
