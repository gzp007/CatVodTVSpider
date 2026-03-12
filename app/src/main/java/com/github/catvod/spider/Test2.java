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

/**
 * Aidi 爬虫 - 写死测试数据版（适配影视仓）
 * 所有接口返回固定数据，无需依赖外部网站，仅用于测试接口调用和显示
 */
public class Test2 extends Spider {
    private static final String siteUrl = "clan://localhost/TVBox/live.txt";
    private static final String siteHost = "localhost/TVBox";
    private static final String TAG = "TestSpider";

    // 固定播放源配置（影视仓显示用）
    private JSONObject playerConfig;
    // 固定筛选配置（影视仓筛选面板显示用）
    private JSONObject filterConfig;
    private String liveTxt;
    // 外层分类数组（最终返回的JSON数组）
    JSONArray typeArray = new JSONArray();

//    [{
//        "type_id": "TID001",
//                "type_name": "央视",
//                "videos": [{
//            "vod_id": "VID001",
//                    "vod_name": "CCTV1",
//                    "vod_play_url": ["http://183.62.8.58:50085/tsfile/live/0001_1.m3u8?key=txiptv", "http://43.156.8.127:5050/ysp?id=2024078201&pid=600001859&q=fhd"]
//        }, {
//            "vod_id": "VID002",
//                    "vod_name": "CCTV2",
//                    "vod_play_url": ["http://183.62.8.58:50085/tsfile/live/0002_1.m3u8?key=txiptv", "http://43.156.8.127:5050/ysp?id=2024075401&pid=600001800&q=fhd"]
//        }]
//    }, {
//        "type_id": "TID002",
//                "type_name": "卫视",
//                "videos": [{
//            "vod_id": "安徽卫视",
//                    "vod_name": "安徽卫视",
//                    "vod_play_url": ["http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/ahws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=", "http://43.156.8.127:5050/ysp?id=2024171403&pid=600002532&q=fhd"]
//        }]
//    }]


    @Override
    public void init(Context context) {
        super.init(context);
        SpiderDebug.log(TAG + "===== 初始化 Test 测试爬虫 =====");
        try {
            // 固定播放源配置
            playerConfig = new JSONObject("{\"xg_app_player\":{\"sh\":\"app全局解析\",\"pu\":\"\",\"sn\":0,\"or\":999},\"ckm3u8\":{\"sh\":\"测试影视\",\"pu\":\"\",\"sn\":0,\"or\":999},\"xin\":{\"sh\":\"测试高速\",\"pu\":\"\",\"sn\":0,\"or\":999}}");
            // 固定筛选配置
            filterConfig = new JSONObject("{\"dianying\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"dianying\"},{\"n\":\"动作片\",\"v\":\"dongzuopian\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"大陆\"}]}],\"lianxuju\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"lianxuju\"},{\"n\":\"国产剧\",\"v\":\"guochanju\"}]}]}");

//            liveTxt = OkHttpUtil.string(siteUrl, getHeaders(siteUrl));
//            // 临时变量：当前分类JSON对象
//            JSONObject currentTypeObj = null;
//            // 临时变量：当前分类下的视频（key=视频名称，value=视频JSON对象）
//            Map<String, JSONObject> videoMap = new HashMap<>();
//            // ID计数器
//            int typeIdCounter = 1;   // 分类ID：TID001开始
//            int videoIdCounter = 1;  // 视频ID：每个分类内重置
//
//            // 分割行：兼容\r\n（Windows）和\n（Linux）换行符
//            String[] lines = liveTxt.split("\\r?\\n");
//            for (String line : lines) {
//                line = line.trim();
//                if (line.isEmpty()) {
//                    continue; // 跳过空行
//                }
//
//                // 1. 解析分类行：格式 分类名,#genre#
//                if (line.endsWith(",#genre#")) {
//                    // 先处理上一个分类（避免遗漏）
//                    if (currentTypeObj != null && !videoMap.isEmpty()) {
//                        try {
//                            // 构建videos数组并添加到分类对象
//                            JSONArray videosArray = new JSONArray();
//                            for (JSONObject videoObj : videoMap.values()) {
//                                videosArray.put(videoObj);
//                            }
//                            currentTypeObj.put("videos", videosArray);
//                            // 将分类对象添加到外层数组
//                            typeArray.put(currentTypeObj);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        // 重置视频Map和计数器
//                        videoMap.clear();
//                        videoIdCounter = 1;
//                    }
//
//                    // 构建新的分类JSON对象
//                    String typeName = line.split(",")[0];
//                    String typeId = String.format("TID%03d", typeIdCounter++);
//                    try {
//                        currentTypeObj = new JSONObject();
//                        currentTypeObj.put("type_id", typeId);
//                        currentTypeObj.put("type_name", typeName);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                // 2. 解析视频行：格式 视频名,播放地址
//                else if (currentTypeObj != null && line.contains(",")) {
//                    String[] parts = line.split(",", 2); // 按第一个逗号分割（地址含逗号不影响）
//                    if (parts.length != 2) {
//                        continue; // 格式错误，跳过
//                    }
//                    String vodName = parts[0].trim();
//                    String playUrl = parts[1].trim();
//
//                    // 检查是否已存在该视频，不存在则新建
//                    JSONObject videoObj = videoMap.get(vodName);
//                    if (videoObj == null) {
//                        try {
//                            videoObj = new JSONObject();
//                            // 生成视频ID：VID001/VID002...
//                            String vodId = String.format("VID%03d", videoIdCounter++);
//                            videoObj.put("vod_id", vodId);
//                            videoObj.put("vod_name", vodName);
//                            // 初始化播放地址数组
//                            videoObj.put("vod_play_url", new JSONArray());
//                            videoMap.put(vodName, videoObj);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    // 添加播放地址到数组（合并多地址）
//                    try {
//                        JSONArray playUrlArray = videoObj.getJSONArray("vod_play_url");
//                        playUrlArray.put(playUrl);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            // 处理最后一个分类（避免遗漏）
//            if (currentTypeObj != null && !videoMap.isEmpty()) {
//                try {
//                    JSONArray videosArray = new JSONArray();
//                    for (JSONObject videoObj : videoMap.values()) {
//                        videosArray.put(videoObj);
//                    }
//                    currentTypeObj.put("videos", videosArray);
//                    typeArray.put(currentTypeObj);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
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
            // 视频列表
            JSONArray videos = new JSONArray();

            for (int i = 0; i < typeArray.length(); i++) {
                try {
                    JSONObject typeObj = typeArray.getJSONObject(i);
                    // 在这里处理每个 JSONObject
                    classes.put(new JSONObject().put("type_id", typeObj.get("type_id")).put("type_name", typeObj.get("type_name")));
                    JSONArray videoArr = typeObj.getJSONArray("videos");
                    for (int j = 0; j < videoArr.length(); j++) {
                        try {
                            JSONObject videObj = videoArr.getJSONObject(j);
                            // 固定首页视频列表
                            videos.put(new JSONObject()
                                    .put("vod_id", videObj.get("vod_id"))
                                    .put("vod_name", videObj.get("vod_name"))
                                    .put("vod_pic", videObj.get("vod_name"))
                                    .put("vod_remarks", videObj.get("vod_name")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // 固定筛选配置（如果开启筛选）
            if (filter) {
                result.put("filters", filterConfig);
            }
            result.put("class", classes);//分类
            result.put("list", videos);//首页列表
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
            // 固定分类视频列表
            JSONArray videos = new JSONArray();
            for (int i = 0; i < typeArray.length(); i++) {
                try {
                    JSONObject typeObj = typeArray.getJSONObject(i);
                    if (typeObj.get("type_id").equals(tid)) {
                        JSONArray videoArr = typeObj.getJSONArray("videos");
                        for (int j = 0; j < videoArr.length(); j++) {
                            try {
                                JSONObject videoObj = videoArr.getJSONObject(j);
                                // 固定首页视频列表
                                videos.put(new JSONObject()
                                        .put("vod_id", videoObj.get("vod_id"))
                                        .put("vod_name", videoObj.get("vod_name"))
                                        .put("vod_pic", videoObj.get("vod_name"))
                                        .put("vod_remarks", videoObj.get("vod_name")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;//跳出当前循环
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (filter) {
                result.put("filters", filterConfig);
            }
            result.put("page", Integer.parseInt(TextUtils.isEmpty(pg) ? "1" : pg));
            result.put("pagecount", 1);
            result.put("limit", videos.length());
            result.put("total", videos.length());
            result.put("list", videos);

            return result.toString();
        } catch (JSONException e) {
            SpiderDebug.log(TAG + "分类数据构建失败：" + e.getMessage());
            return getDefaultEmptyResult();
        }
    }

    /**
     * 核心方法：List<String> 转 # 拼接的字符串
     * @param list 待转换的列表（可为空）
     * @return 拼接后的字符串，示例：["a","b"] → "a#b"；空列表返回 ""
     */
    public static String listToJoinStr(List<String> list) {
        // 空值/空列表处理
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            // 跳过 null 元素（避免拼接出 "null"）
            if (item == null) {
                continue;
            }
            sb.append(item);
            // 最后一个元素不加 #
            if (i != list.size() - 1) {
                sb.append("#");
            }
        }
        return sb.toString();
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
            List<String> lis = new ArrayList<>();
            for (int i = 0; i < typeArray.length(); i++) {
                try {
                    JSONObject typeObj = typeArray.getJSONObject(i);
                    if (typeObj.get("type_id").equals(vodId)) {
                        JSONArray videoArr = typeObj.getJSONArray("videos");
                        for (int j = 0; j < videoArr.length(); j++) {
                            try {
                                JSONObject videoObj = videoArr.getJSONObject(j);
                                lis.add(videoObj.getString("vod_name") + "$" + videoObj.getJSONArray("vod_play_url").get(0).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String vod_play_url = listToJoinStr(lis);
                        JSONObject vodInfo = new JSONObject();
                        vodInfo.put("vod_id", vodId)
                                .put("vod_name", typeObj.get("type_name"))
                                .put("vod_pic", "")
                                .put("type_name", typeObj.get("type_name"))
                                .put("vod_year", "")
                                .put("vod_area", "")
                                .put("vod_remarks", typeObj.get("type_name"))
                                .put("vod_actor", "")
                                .put("vod_director", "")
                                .put("vod_content", typeObj.get("type_name"))
                                // 播放源（影视仓核心字段）
                                .put("vod_play_from", "线路一")
                                //.put("vod_play_url", "第1集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240095359203.mp4#第2集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240368550193.mp4$$$第1集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4#第2集$https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4");
                                .put("vod_play_url", vod_play_url);
                        list.put(vodInfo);
                        break;//跳出当前循环
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
            for (int i = 0; i < typeArray.length(); i++) {
                try {
                    JSONObject typeObj = typeArray.getJSONObject(i);
                    if (typeObj.get("type_id").equals(key)) {
                        videos.put(new JSONObject()
                            .put("vod_id", typeObj.get("type_id"))
                            .put("vod_name", typeObj.get("type_name"))
                            .put("vod_pic", "")
                            .put("vod_remarks", typeObj.get("type_name")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
