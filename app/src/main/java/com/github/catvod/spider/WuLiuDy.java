package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 56dy 影视爬虫
 * Author: CatVod
 */
public class WuLiuDy extends Spider {
    // 站点基础配置
    private static final String siteUrl = "http://56dy.com";
    private static final String siteHost = "56dy.com";

    /**
     * 播放源配置（适配56dy的播放线路命名）
     */
    private JSONObject playerConfig;
    /**
     * 筛选配置（适配56dy的分类筛选维度）
     */
    private JSONObject filterConfig;

    // 正则表达式配置
    private Pattern regexCategory = Pattern.compile("/(movie|ju|dm|zy)/list|/(\\w+)"); // 匹配分类ID
    private Pattern regexVid = Pattern.compile("/(movie|ju|dm|zy)/(\\d+)"); // 匹配视频ID
    private Pattern regexPlay = Pattern.compile("/play/(\\w+)/(\\d+)/(\\d+).html"); // 匹配播放页ID
    private Pattern regexPage = Pattern.compile("/list/(\\w+)-(\\d+).html"); // 匹配分页URL

    @Override
    public void init(Context context) {
        super.init(context);
        try {
            // 播放源配置（适配56dy的播放线路）
            playerConfig = new JSONObject("{\"xg_app_player\":{\"sh\":\"APP全局解析\",\"pu\":\"\",\"sn\":0,\"or\":999},\"dplayer\":{\"sh\":\"DPlayer播放器\",\"pu\":\"\",\"sn\":0,\"or\":999},\"videojs\":{\"sh\":\"VideoJS播放器\",\"pu\":\"\",\"sn\":0,\"or\":999},\"iframe\":{\"sh\":\"外链播放\",\"pu\":\"\",\"sn\":0,\"or\":999},\"link\":{\"sh\":\"直链播放\",\"pu\":\"\",\"sn\":0,\"or\":999},\"ckm3u8\":{\"sh\":\"高清线路\",\"pu\":\"\",\"sn\":0,\"or\":999},\"xin\":{\"sh\":\"极速线路\",\"pu\":\"\",\"sn\":0,\"or\":999},\"ppyun\":{\"sh\":\"云播线路\",\"pu\":\"\",\"sn\":0,\"or\":999},\"jisu\":{\"sh\":\"备用线路\",\"pu\":\"\",\"sn\":0,\"or\":999},\"dbm3u8\":{\"sh\":\"蓝光线路\",\"pu\":\"\",\"sn\":0,\"or\":999},\"yjm3u8\":{\"sh\":\"720P线路\",\"pu\":\"\",\"sn\":0,\"or\":999}}");

            // 筛选配置（适配56dy的分类维度）
            filterConfig = new JSONObject("{\"movie\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"movie\"},{\"n\":\"动作片\",\"v\":\"dongzuo\"},{\"n\":\"喜剧片\",\"v\":\"xiju\"},{\"n\":\"爱情片\",\"v\":\"aiqing\"},{\"n\":\"科幻片\",\"v\":\"kehuan\"},{\"n\":\"恐怖片\",\"v\":\"kongbu\"},{\"n\":\"剧情片\",\"v\":\"juqing\"},{\"n\":\"战争片\",\"v\":\"zhanzheng\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"dalu\"},{\"n\":\"香港\",\"v\":\"xianggang\"},{\"n\":\"台湾\",\"v\":\"taiwan\"},{\"n\":\"美国\",\"v\":\"meiguo\"},{\"n\":\"日本\",\"v\":\"riben\"},{\"n\":\"韩国\",\"v\":\"hanguo\"}]},{\"key\":2,\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2025\",\"v\":\"2025\"},{\"n\":\"2024\",\"v\":\"2024\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"}]},{\"key\":3,\"name\":\"排序\",\"value\":[{\"n\":\"最新\",\"v\":\"time\"},{\"n\":\"人气\",\"v\":\"hits\"}]}],\"ju\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"ju\"},{\"n\":\"国产剧\",\"v\":\"guochan\"},{\"n\":\"港台剧\",\"v\":\"gangtai\"},{\"n\":\"韩剧\",\"v\":\"hanju\"},{\"n\":\"美剧\",\"v\":\"meiju\"},{\"n\":\"日剧\",\"v\":\"riju\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"dalu\"},{\"n\":\"香港\",\"v\":\"xianggang\"},{\"n\":\"台湾\",\"v\":\"taiwan\"},{\"n\":\"韩国\",\"v\":\"hanguo\"},{\"n\":\"美国\",\"v\":\"meiguo\"}]},{\"key\":2,\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2025\",\"v\":\"2025\"},{\"n\":\"2024\",\"v\":\"2024\"},{\"n\":\"2023\",\"v\":\"2023\"}]},{\"key\":3,\"name\":\"排序\",\"value\":[{\"n\":\"最新\",\"v\":\"time\"},{\"n\":\"人气\",\"v\":\"hits\"}]}],\"dm\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"dm\"},{\"n\":\"国产动漫\",\"v\":\"guochandm\"},{\"n\":\"日韩动漫\",\"v\":\"rihandm\"},{\"n\":\"欧美动漫\",\"v\":\"oumeidm\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"dalu\"},{\"n\":\"日本\",\"v\":\"riben\"},{\"n\":\"美国\",\"v\":\"meiguo\"}]},{\"key\":2,\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2025\",\"v\":\"2025\"},{\"n\":\"2024\",\"v\":\"2024\"}]},{\"key\":3,\"name\":\"排序\",\"value\":[{\"n\":\"最新\",\"v\":\"time\"},{\"n\":\"人气\",\"v\":\"hits\"}]}],\"zy\":[{\"key\":0,\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"zy\"},{\"n\":\"内地综艺\",\"v\":\"neidi\"},{\"n\":\"港台综艺\",\"v\":\"gangtai\"},{\"n\":\"韩国综艺\",\"v\":\"hanguo\"}]},{\"key\":1,\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"dalu\"},{\"n\":\"香港\",\"v\":\"xianggang\"},{\"n\":\"台湾\",\"v\":\"taiwan\"},{\"n\":\"韩国\",\"v\":\"hanguo\"}]},{\"key\":2,\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2025\",\"v\":\"2025\"},{\"n\":\"2024\",\"v\":\"2024\"}]},{\"key\":3,\"name\":\"排序\",\"value\":[{\"n\":\"最新\",\"v\":\"time\"},{\"n\":\"人气\",\"v\":\"hits\"}]}]}");
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    /**
     * 爬虫请求头配置
     *
     * @param url 请求URL
     * @return 头信息Map
     */
    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("Host", siteHost);
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("DNT", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Referer", siteUrl);
        return headers;
    }

    /**
     * 获取首页分类 + 首页视频列表
     *
     * @param filter 是否返回筛选配置
     * @return JSON字符串
     */
    @Override
    public String homeContent(boolean filter) {
        try {
            // 1. 获取并解析首页HTML
            Document doc = Jsoup.parse(OkHttpUtil.string(siteUrl, getHeaders(siteUrl)));

            // 2. 解析分类导航（电影、连续剧、动漫、综艺）
            Elements navElements = doc.select("ul.type-slide.clearfix");
            JSONArray classes = new JSONArray();

            for (Element ele : navElements) {
                String name = ele.select("li > a").text().trim();
                // 只保留核心分类
                boolean show = name.equals("电影") ||
                        name.equals("连续剧") ||
                        name.equals("动漫") ||
                        name.equals("综艺");

                if (show) {
                    Matcher mather = regexCategory.matcher(ele.select("li > a").attr("href"));
                    if (!mather.find())
                        continue;
                    // 把分类的id和名称取出来加到列表里
                    String id = mather.group(1).trim();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type_id", id);
                    jsonObject.put("type_name", name);
                    classes.put(jsonObject);
                }
            }

            // 3. 构建返回结果
            JSONObject result = new JSONObject();
            if (filter) {
                result.put("filters", filterConfig);
            }
            result.put("class", classes);

            // 4. 解析首页推荐视频列表（最新更新板块）
            try {
                Elements videoItems = doc.select("div.stui-pannel-bg:has(h3:contains(最新)) ul.stui-vodlist li");
                JSONArray videos = new JSONArray();

                for (Element item : videoItems) {
                    Element vodLink = item.selectFirst("a.stui-vodlist__thumb");
                    if (vodLink == null) continue;

                    // 提取视频基础信息
                    String title = vodLink.attr("title").trim();
                    String cover = vodLink.attr("data-original").trim();
                    String remark = vodLink.selectFirst("span.pic-text") != null ?
                            vodLink.selectFirst("span.pic-text").text().trim() : "";
                    String href = vodLink.attr("href").trim();

                    // 提取视频ID
                    Matcher matcher = regexVid.matcher(href);
                    if (!matcher.find()) continue;
                    String vodId = matcher.group(1) + "_" + matcher.group(2); // 拼接分类+ID，避免重复

                    // 封装视频信息
                    JSONObject v = new JSONObject();
                    v.put("vod_id", vodId);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", remark);
                    videos.put(v);
                }

                result.put("list", videos);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取分类下的视频列表（带分页和筛选）
     *
     * @param tid     分类ID
     * @param pg      页码
     * @param filter  是否返回筛选配置
     * @param extend  筛选参数
     * @return JSON字符串
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            // 1. 构建分类列表URL
            String pageNum = TextUtils.isEmpty(pg) ? "1" : pg;
            String url = siteUrl + "/" + tid + "/list-" + pageNum + ".html";

            // 2. 处理筛选参数（如果有）
            if (extend != null && !extend.isEmpty()) {
                StringBuilder filterParams = new StringBuilder();
                for (Map.Entry<String, String> entry : extend.entrySet()) {
                    filterParams.append("-").append(URLEncoder.encode(entry.getValue()));
                }
                url = siteUrl + "/" + tid + "/list" + filterParams + "-" + pageNum + ".html";
            }

            // 3. 解析分类页HTML
            String html = OkHttpUtil.string(url, getHeaders(url));
            Document doc = Jsoup.parse(html);
            JSONObject result = new JSONObject();

            // 4. 解析分页信息
            int page = Integer.parseInt(pageNum);
            int pageCount = 1;

            Elements pageElements = doc.select("ul.pagination li");
            if (!pageElements.isEmpty()) {
                // 提取尾页页码
                Element lastPage = doc.select("ul.pagination li:last-child a").first();
                if (lastPage != null) {
                    Matcher pageMatcher = regexPage.matcher(lastPage.attr("href"));
                    if (pageMatcher.find()) {
                        pageCount = Integer.parseInt(pageMatcher.group(2));
                    }
                }
            }

            // 5. 解析视频列表
            JSONArray videos = new JSONArray();
            if (!html.contains("暂无数据")) {
                Elements list = doc.select("ul.stui-vodlist li a.stui-vodlist__thumb");
                for (Element vod : list) {
                    String title = vod.attr("title").trim();
                    String cover = vod.attr("data-original").trim();
                    String remark = vod.selectFirst("span.pic-text") != null ?
                            vod.selectFirst("span.pic-text").text().trim() : "";
                    String href = vod.attr("href").trim();

                    Matcher matcher = regexVid.matcher(href);
                    if (!matcher.find()) continue;
                    String vodId = matcher.group(1) + "_" + matcher.group(2);

                    JSONObject v = new JSONObject();
                    v.put("vod_id", vodId);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", remark);
                    videos.put(v);
                }
            }

            // 6. 封装返回结果
            result.put("page", page);
            result.put("pagecount", pageCount);
            result.put("limit", 30); // 56dy默认每页30条
            result.put("total", pageCount * 30);
            result.put("list", videos);

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取视频详情信息（含播放源）
     *
     * @param ids 视频ID列表（取第一个）
     * @return JSON字符串
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            // 1. 解析视频ID（拆分分类+数字ID）
            String vodId = ids.get(0);
            String[] idParts = vodId.split("_");
            String type = idParts[0];
            String numId = idParts[1];

            // 2. 获取详情页HTML
            String detailUrl = siteUrl + "/" + type + "/" + numId + ".html";
            Document doc = Jsoup.parse(OkHttpUtil.string(detailUrl, getHeaders(detailUrl)));

            // 3. 解析基础信息
            JSONObject result = new JSONObject();
            JSONObject vodInfo = new JSONObject();

            // 封面、标题
            Element coverImg = doc.selectFirst("div.detail-pic img");
            String cover = coverImg != null ? coverImg.attr("data-original").trim() : "";
            String title = doc.selectFirst("h1.title").text().trim();

            // 详情信息（类型、地区、年份、简介等）
            String category = "", area = "", year = "", remark = "", director = "", actor = "", desc = "";
            Elements infoItems = doc.select("div.detail-info li");

            for (Element item : infoItems) {
                String label = item.selectFirst("span").text().trim();
                String value = item.text().replace(label, "").trim();

                switch (label) {
                    case "类型：":
                        category = value;
                        break;
                    case "地区：":
                        area = value;
                        break;
                    case "年份：":
                        year = value;
                        break;
                    case "状态：":
                        remark = value;
                        break;
                    case "导演：":
                        director = value;
                        break;
                    case "主演：":
                        actor = value;
                        break;
                }
            }

            // 简介
            Element descEle = doc.selectFirst("div.detail-intro-content");
            desc = descEle != null ? descEle.text().trim() : "";

            // 封装基础信息
            vodInfo.put("vod_id", vodId);
            vodInfo.put("vod_name", title);
            vodInfo.put("vod_pic", cover);
            vodInfo.put("type_name", category);
            vodInfo.put("vod_year", year);
            vodInfo.put("vod_area", area);
            vodInfo.put("vod_remarks", remark);
            vodInfo.put("vod_actor", actor);
            vodInfo.put("vod_director", director);
            vodInfo.put("vod_content", desc);

            // 4. 解析播放源列表
            Map<String, String> playSources = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        int sort1 = playerConfig.getJSONObject(o1).getInt("or");
                        int sort2 = playerConfig.getJSONObject(o2).getInt("or");
                        return sort1 - sort2;
                    } catch (JSONException e) {
                        SpiderDebug.log(e);
                        return 1;
                    }
                }
            });

            // 播放线路标签
            Elements sourceTabs = doc.select("div.play-tab a");
            // 播放列表容器
            Elements playLists = doc.select("div.play-list");

            for (int i = 0; i < sourceTabs.size() && i < playLists.size(); i++) {
                String sourceName = sourceTabs.get(i).text().trim();
                // 匹配播放源配置中的标识
                String configKey = "";
                for (Iterator<String> it = playerConfig.keys(); it.hasNext(); ) {
                    String key = it.next();
                    if (playerConfig.getJSONObject(key).getString("sh").contains(sourceName)) {
                        configKey = key;
                        break;
                    }
                }
                if (TextUtils.isEmpty(configKey)) continue;

                // 解析集数列表
                Elements episodeItems = playLists.get(i).select("li a");
                List<String> episodeList = new ArrayList<>();

                for (Element episode : episodeItems) {
                    String episodeName = episode.text().trim();
                    String playHref = episode.attr("href").trim();

                    Matcher playMatcher = regexPlay.matcher(playHref);
                    if (playMatcher.find()) {
                        String playId = playMatcher.group(1) + "-" + playMatcher.group(2) + "-" + playMatcher.group(3);
                        episodeList.add(episodeName + "$" + playId);
                    }
                }

                if (!episodeList.isEmpty()) {
                    String playStr = TextUtils.join("#", episodeList);
                    playSources.put(configKey, playStr);
                }
            }

            // 封装播放源
            if (!playSources.isEmpty()) {
                String playFrom = TextUtils.join("$$$", playSources.keySet());
                String playUrl = TextUtils.join("$$$", playSources.values());
                vodInfo.put("vod_play_from", playFrom);
                vodInfo.put("vod_play_url", playUrl);
            }

            // 5. 构建最终结果
            JSONArray vodList = new JSONArray();
            vodList.put(vodInfo);
            result.put("list", vodList);

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 解析播放地址
     *
     * @param flag     播放源标识
     * @param id       播放ID
     * @param vipFlags VIP播放源列表
     * @return JSON字符串
     */
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            // 1. 构建播放页URL
            String[] playIdParts = id.split("-");
            String playUrl = siteUrl + "/play/" + playIdParts[0] + "/" + playIdParts[1] + "/" + playIdParts[2] + ".html";

            // 2. 解析播放页HTML
            Document doc = Jsoup.parse(OkHttpUtil.string(playUrl, getHeaders(playUrl)));
            JSONObject result = new JSONObject();

            // 3. 提取播放地址（从script中解析）
            Elements scripts = doc.select("script");
            for (Element script : scripts) {
                String scriptContent = script.html().trim();
                if (scriptContent.contains("player_data") || scriptContent.contains("videoUrl")) {
                    // 匹配播放地址（适配56dy的播放地址格式）
                    Matcher urlMatcher = Pattern.compile("videoUrl\\s*=\\s*['\"](.+?)['\"]").matcher(scriptContent);
                    if (urlMatcher.find()) {
                        String videoUrl = urlMatcher.group(1);
                        JSONObject pCfg = playerConfig.getJSONObject(flag);

                        result.put("parse", pCfg.getInt("sn"));
                        result.put("playUrl", pCfg.getString("pu"));
                        result.put("url", videoUrl);
                        result.put("header", new JSONObject(getHeaders(videoUrl)).toString());
                        break;
                    }
                }
            }

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 搜索功能
     *
     * @param key   搜索关键词
     * @param quick 是否快捷搜索
     * @return JSON字符串
     */
    @Override
    public String searchContent(String key, boolean quick) {
        try {
            if (quick) return "";

            // 1. 构建搜索URL
            String searchUrl = siteUrl + "/search?wd=" + URLEncoder.encode(key) + "&submit=1";
            Document doc = Jsoup.parse(OkHttpUtil.string(searchUrl, getHeaders(searchUrl)));

            // 2. 解析搜索结果
            JSONObject result = new JSONObject();
            JSONArray videos = new JSONArray();

            Elements searchItems = doc.select("ul.stui-vodlist li a.stui-vodlist__thumb");
            for (Element item : searchItems) {
                String title = item.attr("title").trim();
                String cover = item.attr("data-original").trim();
                String remark = item.selectFirst("span.pic-text").text().trim();
                String href = item.attr("href").trim();

                Matcher matcher = regexVid.matcher(href);
                if (!matcher.find()) continue;
                String vodId = matcher.group(1) + "_" + matcher.group(2);

                JSONObject v = new JSONObject();
                v.put("vod_id", vodId);
                v.put("vod_name", title);
                v.put("vod_pic", cover);
                v.put("vod_remarks", remark);
                videos.put(v);
            }

            // 3. 封装结果
            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
