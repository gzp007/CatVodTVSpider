package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

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
                    .put("vod_id", "test_001")
                    .put("vod_name", "测试电影-流浪地球2")
                    .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                    .put("vod_remarks", "2023"));
            videos.put(new JSONObject()
                    .put("vod_id", "test_002")
                    .put("vod_name", "测试电视剧-狂飙")
                    .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                    .put("vod_remarks", "完结"));
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
            if ("dianying".equals(tid)) {
                videos.put(new JSONObject()
                        .put("vod_id", "movie_001")
                        .put("vod_name", "分类测试-动作电影-战狼2")
                        .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                        .put("vod_remarks", "2017"));
            } else if ("lianxuju".equals(tid)) {
                videos.put(new JSONObject()
                        .put("vod_id", "tv_001")
                        .put("vod_name", "分类测试-国产剧-庆余年")
                        .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                        .put("vod_remarks", "第一季"));
            }
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
            if ("test_001".equals(vodId)) {
                vodInfo.put("vod_id", "test_001")
                        .put("vod_name", "测试电影-流浪地球2")
                        .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                        .put("type_name", "科幻/动作")
                        .put("vod_year", "2023")
                        .put("vod_area", "大陆")
                        .put("vod_remarks", "高清")
                        .put("vod_actor", "吴京,刘德华")
                        .put("vod_director", "郭帆")
                        .put("vod_content", "《流浪地球2》是《流浪地球》的前传，讲述了太阳即将毁灭，人类开启“流浪地球计划”，带着地球逃离太阳系的故事。")
                        // 播放源（影视仓核心字段）
                        .put("vod_play_from", "xg_app_player$$$ckm3u8")
                        .put("vod_play_url", "第1集$test_play_001#第2集$test_play_002$$$第1集$test_play_001#第2集$test_play_002");
            } else {
                vodInfo.put("vod_id", vodId)
                        .put("vod_name", "默认测试视频")
                        .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                        .put("type_name", "测试分类")
                        .put("vod_year", "2024")
                        .put("vod_area", "大陆")
                        .put("vod_remarks", "测试")
                        .put("vod_content", "这是一个测试视频的详情内容");
            }
            list.put(vodInfo);
            result.put("list", list);

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
            result.put("parse", 0) // 0=不解析，直接播放；1=需要解析
                    .put("playUrl", "https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240095359203.mp4")
                    .put("url", "https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240095359203.mp4")
                    .put("header", "");
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
            // 固定搜索结果
            videos.put(new JSONObject()
                    .put("vod_id", "search_001")
                    .put("vod_name", "搜索结果-" + key)
                    .put("vod_pic", "https://gzp007.github.io/TVBox/test.png")
                    .put("vod_remarks", "搜索测试"));
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
