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
public class Test2 extends Spider {
    private static final String siteHost = "localhost/TVBox";
    private static final String TAG = "TestSpider";

    // 固定播放源配置（影视仓显示用）
    private JSONObject playerConfig;
    // 固定筛选配置（影视仓筛选面板显示用）
    private JSONObject filterConfig;
    private String liveTxt;
    // 外层分类数组（最终返回的JSON数组）
    private JSONArray typeArray;

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
            liveTxt = "央视,#genre#\n" +
                    "CCTV1,http://183.62.8.58:50085/tsfile/live/0001_1.m3u8?key=txiptv\n" +
                    "CCTV1,http://43.156.8.127:5050/ysp?id=2024078201&pid=600001859&q=fhd\n" +
                    "CCTV2,http://183.62.8.58:50085/tsfile/live/0002_1.m3u8?key=txiptv\n" +
                    "CCTV2,http://43.156.8.127:5050/ysp?id=2024075401&pid=600001800&q=fhd\n" +
                    "CCTV3,http://183.62.8.58:50085/tsfile/live/0003_1.m3u8?key=txiptv\n" +
                    "CCTV3,http://43.156.8.127:5050/ysp?id=2024068501&pid=600001801&q=fhd\n" +
                    "CCTV4,http://183.62.8.58:50085/tsfile/live/0004_1.m3u8?key=txiptv\n" +
                    "CCTV4,http://43.156.8.127:5050/ysp?id=2029797103&pid=600001814&q=fhd\n" +
                    "CCTV5,http://183.62.8.58:50085/tsfile/live/0005_1.m3u8?key=txiptv\n" +
                    "CCTV5,http://43.156.8.127:5050/ysp?id=2024078401&pid=600001818&q=fhd\n" +
                    "CCTV5+,http://112.27.235.94:8000/hls/6/index.m3u8\n" +
                    "CCTV5+,http://43.156.8.127:5050/ysp?id=2024078001&pid=600001817&q=fhd\n" +
                    "CCTV6,http://43.156.8.127:5050/ysp?id=2013693901&pid=600108442&q=fhd\n" +
                    "CCTV7,http://183.62.8.58:50085/tsfile/live/0007_1.m3u8?key=txiptv\n" +
                    "CCTV7,http://43.156.8.127:5050/ysp?id=2024072001&pid=600004092&q=fhd\n" +
                    "CCTV8,http://183.62.8.58:50085/tsfile/live/0008_1.m3u8?key=txiptv\n" +
                    "CCTV8,http://43.156.8.127:5050/ysp?id=2029793001&pid=600001803&q=fhd\n" +
                    "CCTV9,http://183.62.8.58:50085/tsfile/live/0009_1.m3u8?key=txiptv\n" +
                    "CCTV9,http://43.156.8.127:5050/ysp?id=2024078601&pid=600004078&q=fhd\n" +
                    "CCTV10,http://183.62.8.58:50085/tsfile/live/0010_1.m3u8?key=txiptv\n" +
                    "CCTV10,http://43.156.8.127:5050/ysp?id=2024078701&pid=600001805&q=fhd\n" +
                    "CCTV11,http://183.62.8.58:50085/tsfile/live/0011_1.m3u8?key=txiptv\n" +
                    "CCTV11,http://43.156.8.127:5050/ysp?id=2027248701&pid=600001806&q=fhd\n" +
                    "CCTV12,http://183.62.8.58:50085/tsfile/live/0012_1.m3u8?key=txiptv\n" +
                    "CCTV12,http://43.156.8.127:5050/ysp?id=2027248801&pid=600001807&q=fhd\n" +
                    "CCTV13,http://183.62.8.58:50085/tsfile/live/0013_1.m3u8?key=txiptv\n" +
                    "CCTV13,http://43.156.8.127:5050/ysp?id=2029797203&pid=600001811&q=fhd\n" +
                    "CCTV14,http://183.62.8.58:50085/tsfile/live/0014_1.m3u8?key=txiptv\n" +
                    "CCTV14,http://43.156.8.127:5050/ysp?id=2027248901&pid=600001809&q=fhd\n" +
                    "CCTV15,http://183.62.8.58:50085/tsfile/live/0015_1.m3u8?key=txiptv\n" +
                    "CCTV15,http://43.156.8.127:5050/ysp?id=2027249001&pid=600001815&q=fhd\n" +
                    "CCTV16,http://43.156.8.127:5050/ysp?id=2027249101&pid=600098637&q=fhd\n" +
                    "CCTV16(4K),http://43.156.8.127:5050/ysp?id=2027249301&pid=600099502&q=fhd\n" +
                    "CCTV17,http://43.156.8.127:5050/ysp?id=2027249401&pid=600001810&q=fhd\n" +
                    "CCTV4K,http://43.156.8.127:5050/ysp?id=2027249501&pid=600002264&q=fhd\n" +
                    "CCTV8K,http://43.156.8.127:5050/ysp?id=2026774101&pid=600156816&q=fhd\n" +
                    "\n" +
                    "卫视,#genre#\n" +
                    "安徽卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/ahws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "安徽卫视,http://43.156.8.127:5050/ysp?id=2024171403&pid=600002532&q=fhd\n" +
                    "北京卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/bjws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "北京卫视,http://43.156.8.127:5050/ysp?id=2024052703&pid=600002309&q=fhd\n" +
                    "广西卫视,http://hlsbkmgsplive.miguvideo.com/wd-guangxiwssd-600/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "广西卫视,http://43.156.8.127:5050/ysp?id=2024060703&pid=600002509&q=fhd\n" +
                    "贵州卫视,http://hlsbkmgsplive.miguvideo.com/wd-guizhouwssd-600/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "贵州卫视,http://43.156.8.127:5050/ysp?id=2024061603&pid=600002490&q=fhd\n" +
                    "广东卫视,http://hlsbkmgsplive.miguvideo.com/ws_w/gdws/gdws3000/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "广东卫视,http://43.156.8.127:5050/ysp?id=2024060903&pid=600002485&q=fhd\n" +
                    "海南卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/hainanws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "海南卫视,http://43.156.8.127:5050/ysp?id=2024055603&pid=600002506&q=fhd\n" +
                    "河北卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/hbws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "河北卫视,http://43.156.8.127:5050/ysp?id=2024171503&pid=600002493&q=fhd\n" +
                    "黑龙江卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/hljws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "黑龙江卫视,http://43.156.8.127:5050/ysp?id=2029797003&pid=600002498&q=fhd\n" +
                    "河南卫视,http://121.12.173.50/tvcdn.stream3.hndt.com/tv/65c4a6d5017e1000b2b6ea2500000000_transios/playlist.m3u8?wsSecret=cc712604c221965ad77eab4a05f9fb88&wsTime=1771846603&wsSession=038d4cc16cb7fae2dee186e5-177183406333511&wsIPSercert=f1ed5e80c31ab65580d88c6bbb451005&wsiphost=local&wsBindIP=1\n" +
                    "河南卫视,http://43.156.8.127:5050/ysp?id=2029797303&pid=600002525&q=fhd\n" +
                    "湖北卫视,http://hlsbkmgsplive.miguvideo.com/migu/kailu/hubeiwshd/57/20220712/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "湖北卫视,http://43.156.8.127:5050/ysp?id=2024171203&pid=600002508&q=fhd\n" +
                    "湖南台,http://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZyYXV0aF9lbmFibGU9b2ZmJmNkbmV4X2lkPXFxX3Bob25lX2xpdmUmY2hzPSZkZWY9MSZzPTQyZjA0MjBiMzUxMjcyNjAxMTAxMGRlN2Y4MjRiNTgwJnVpZD0mdXVpZD02NGI1MzZlYmJmODkyZTdlYjNlZWM1ODFiMzQzNTg2YS02YTBlMjYzNyZ2PTImYXM9MCZlcz0xNzcxODUxMzg5/HNGJMPP360.m3u8\n" +
                    "湖南爱晚,http://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZyYXV0aF9lbmFibGU9b2ZmJmNkbmV4X2lkPXFxX3Bob25lX2xpdmUmY2hzPSZkZWY9MSZzPTlmZjFiYzNjOGE1MTgwMWVmMDdjOWY2ZTM0MGUyMmMyJnVpZD0mdXVpZD02ZDdjYzk3ZmFiNjZkMDIyZjk5ZTJmZDkzZThmNTI0YS02YTBlMjYzNyZ2PTImYXM9MCZlcz0xNzcxODQ4MDA2/HNGGMPP360.m3u8\n" +
                    "湖南都市,http://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZjZG5leF9pZD1xcV9waG9uZV9saXZlJmNocz0mZGVmPTEmcz1kOGQyYTJhOTAxMzI2OGJjNjlkNDBhMTJjNzFjM2VjMSZ1aWQ9JnV1aWQ9ZWI1N2IyZDA2ZDM3MGZjYzdhN2I2M2ZkNzljZWI4NzctNmEwZTI2Mzcmdj0yJmFzPTAmZXM9MTc3MTg0NDE0NQ,,/HNDSMPP360.m3u8\n" +
                    "湖南经视,http://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZyYXV0aF9lbmFibGU9b24mY2RuZXhfaWQ9cXFfcGhvbmVfbGl2ZSZjaHM9JmRlZj0xJnM9Y2YxYTVkYzU1YzkzZWM4NDY5MjkwMTc3MDlkZGI3ZjkmdWlkPSZ1dWlkPTgwMTZhNGU2Yjk3Yjg2NTQyN2IyMTVkNzc3ZjQ4ZWY4LTZhMGUyNjM3JnY9MiZhcz0wJmVzPTE3NzE4NTE1Njk,/HNJSMPP360.m3u8\n" +
                    "湖南电视剧,http://phonehwei.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZjZG5leF9pZD1od19waG9uZSZjaHM9JmRlZj0xJnM9ODEwOTg4N2ZjZGUzODI3ZGZmM2VkYTVlY2EzNzdlMmMmdWlkPSZ1dWlkPTkzN2U5ZmM0YzI3NjU0NzIwZDNhY2Q5NmVjM2NkNmRmLTZhMGUyNjM3JnY9MiZhcz0wJmVzPTE3NzE4NTE2NjY,/HNDSJMPP360.m3u8\n" +
                    "湖南卫视,http://hlsbkmgsplive.miguvideo.com/wd-hunanhd-2500/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "湖南卫视,http://43.156.8.127:5050/ysp?id=2024054803&pid=600002475&q=fhd\n" +
                    "吉林卫视,https://hlsbkmgsplive.miguvideo.com/envivo_v/2018/SD/jilin/1000/01.m3u8?&HlsSubType=&encrypt=\n" +
                    "吉林卫视,http://43.156.8.127:5050/ysp?id=2025561503&pid=600190405&q=fhd\n" +
                    "江苏卫视,https://hlsbkmgsplive.miguvideo.com/migu/kailu/jswshd265/55/20200407/01.m3u8?&HlsSubType=&encrypt=\n" +
                    "江苏卫视,http://43.156.8.127:5050/ysp?id=2024171103&pid=600002521&q=fhd\n" +
                    "江西卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/jxws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "江西卫视,http://43.156.8.127:5050/ysp?id=2024061703&pid=600002503&q=fhd\n" +
                    "辽宁卫视,http://hlsbkmgsplive.miguvideo.com/wd_r2/ocn/liaoningwshd/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "辽宁卫视,http://43.156.8.127:5050/ysp?id=2024171303&pid=600002505&q=fhd\n" +
                    "内蒙古卫视,http://hlsbkmgsplive.miguvideo.com/envivo_w/2018/SD/neimeng/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "内蒙古卫视,http://43.156.8.127:5050/ysp?id=2025561203&pid=600190401&q=fhd\n" +
                    "宁夏卫视,http://hlsbkmgsplive.miguvideo.com/envivo_x/2018/SD/ningxia/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "青海卫视,http://hlsbkmgsplive.miguvideo.com/envivo_w/2018/SD/qinghai/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "山东卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/yg/shandongwshd/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "山东卫视,http://43.156.8.127:5050/ysp?id=2029787903&pid=600002513&q=fhd\n" +
                    "陕西卫视,http://stream.snrtv.com/sxbc-star-zDGSWY.m3u8\n" +
                    "陕西卫视,http://43.156.8.127:5050/ysp?id=2029795103&pid=600190400&q=fhd\n" +
                    "深圳卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/szws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "深圳卫视,http://43.156.8.127:5050/ysp?id=2024061303&pid=600002481&q=fhd\n" +
                    "东南卫视,https://hlsbkmgsplive.miguvideo.com/migu/kailu/dongnanws/51/20230724/01.m3u8?&HlsSubType=&encrypt=\n" +
                    "东南卫视,http://43.156.8.127:5050/ysp?id=2024061503&pid=600002484&q=fhd\n" +
                    "天津卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/tjws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "天津卫视,http://43.156.8.127:5050/ysp?id=2019927003&pid=600152137&q=fhd\n" +
                    "西藏卫视,http://hlsbkmgsplive.miguvideo.com/envivo_x/2018/SD/xizang/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "新疆卫视,https://slstplay.xjtvs.com.cn/xjtv1/xjtv1stream.m3u8?auth_key=1771837706-2-2-816119728e2ae87ef4bf5931e09c6892&aliyun_uuid=f9104ad2-7eb9-4d4a-929d-c9bc77bdfac0\n" +
                    "云南卫视,http://hlsbkmgsplive.miguvideo.com/envivo_x/2018/SD/yunnan/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "云南卫视,http://43.156.8.127:5050/ysp?id=2025561303&pid=600190402&q=fhd\n" +
                    "浙江卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/zjws/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "浙江卫视,http://43.156.8.127:5050/ysp?id=2024054703&pid=600002520&q=fhd\n" +
                    "中国教育1台,http://43.156.8.127:5050/ysp?id=2022823801&pid=600171827&q=fhd\n" +
                    "东方卫视,http://hlsbkmgsplive.miguvideo.com/wd_r4/dfl/dongfangwshd/3000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "东方卫视,http://43.156.8.127:5050/ysp?id=2024054503&pid=600002483&q=fhd\n" +
                    "大湾区卫视,http://hlsbkmgsplive.miguvideo.com/wd_r3/2018/nfmedia/nfws/1000/01.m3u8?msisdn=&Channel_ID=&ContentId=&HlsSubType=&HlsProfileId=&encrypt=\n" +
                    "甘肃卫视,https://liveout.gstv.com.cn/skt649/p9xpey.m3u8\n" +
                    "海峡卫视,https://hlsbkmgsplive.miguvideo.com/migu/kailu/haixiaws/51/20230724/01.m3u8?&HlsSubType=&encrypt=\n" +
                    "金鹰卡通,http://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZjZG5leF9pZD1xcV9waG9uZV9saXZlJmNocz0mZGVmPTEmcz00YWQzM2E5MGUxMTRlMzRmYTRhNzQ2NjI5NWJlYTQzMiZ1aWQ9JnV1aWQ9ZTIxYmJhZjkyMDA0MjkyYWE2YjIzZTkwM2FkMmRjNWYtNmEwZTI2Mzcmdj0yJmFzPTAmZXM9MTc3MTgzNzI0OQ,,/JYKTMPP360.m3u8\n" +
                    "金鹰纪实频道,https://phoneqq.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwNi4xNC4zOC41NSZ0ZXJtPTUmcWlkPSZyYXV0aF9lbmFibGU9b2ZmJmNkbmV4X2lkPXFxX3Bob25lX2xpdmUmY2hzPSZkZWY9MSZzPTliZDA4MTJkOTlmMmIzZTI5NTUzODczZmI2MDQ5ZjY0JnVpZD0mdXVpZD1kNjdiNWQxMmI3NGUyZmNjNDNkMDY0NTM1NTdkMDRiNC02YTBlMjYzNyZ2PTImYXM9MCZlcz0xNzcxODQ3Nzgx/JYJSMPP360.m3u8\n" +
                    "三沙卫视,https://srs.ssws.tv/video/sstv-10/index.m3u8\n" +
                    "兵团卫视,https://hlsbkmgsplive.miguvideo.com/migu/kailu/btws/51/20250804/01.m3u8?&HlsSubType=&encrypt=\n" +
                    "\n" +
                    "港澳台,#genre#\n" +
                    "Beautiful Life TV,https://5ddce30eb4b55.streamlock.net/bltvhd/bltv1/playlist.m3u8\n" +
                    "\n" +
                    "历年春晚,#genre#\n" +
                    "1983,https://alimov2.a.kwimgs.com/upic/2022/01/31/15/BMjAyMjAxMzExNTU5MTRfNDAzMDAxOTlfNjYyNzMxNjcwMjBfMF8z_b_Beb3bda599f76c60c463c433ca7460153.mp4\n" +
                    "1984,https://alimov2.a.kwimgs.com/upic/2022/01/31/15/BMjAyMjAxMzExNTU5NTRfNDAzMDAxOTlfNjYyNzMyMzg3MTRfMF8z_b_B192356dadbc90d207ba16964d4c2914c.mp4\n" +
                    "1985,https://alimov2.a.kwimgs.com/upic/2022/01/31/16/BMjAyMjAxMzExNjAwMDFfNDAzMDAxOTlfNjYyNzMyNTAwMzJfMF8z_b_Be73c5abcbc0eeb2ec9fce6842e1362a4.mp4\n" +
                    "1986,https://alimov2.a.kwimgs.com/upic/2022/01/31/16/BMjAyMjAxMzExNjAwMDhfNDAzMDAxOTlfNjYyNzMyNjMyMDNfMF8z_b_B570493ed8f7200d4013a66b2d21b2de9.mp4\n" +
                    "1987,https://alimov2.a.kwimgs.com/upic/2022/01/31/16/BMjAyMjAxMzExNjAwMTJfNDAzMDAxOTlfNjYyNzMyNjkxNjBfMF8z_b_B8c835b83a92d25bde81ba22c5cd9521e.mp4\n" +
                    "1988,https://alimov2.a.kwimgs.com/upic/2022/01/31/16/BMjAyMjAxMzExNjAwMTVfNDAzMDAxOTlfNjYyNzMyNzQ2OTlfMF8z_b_Be477b27b9ce655d2372df56a5a3d96ef.mp4\n" +
                    "1989,https://alimov2.a.kwimgs.com/upic/2022/01/31/16/BMjAyMjAxMzExNjAwMTVfNDAzMDAxOTlfNjYyNzMyNzQ2OTlfMF8z_b_Be477b27b9ce655d2372df56a5a3d96ef.mp4\n" +
                    "1990,https://cdn8.yzzy-online.com/20220704/597_e0d90c37/1000k/hls/index.m3u8\n" +
                    "1992,https://txmov2.a.kwimgs.com/bs3/video-hls/5256826755663896297_hlshd15.m3u8\n" +
                    "1993,https://alimov2.a.kwimgs.com/upic/2023/01/13/22/BMjAyMzAxMTMyMjEwMDNfNDAzMDAxOTlfOTM1MTIzMzYwODJfMF8z_b_B647d10e431b4cc5e48e6c77347d69021.mp4\n" +
                    "1994,https://alimov2.a.kwimgs.com/upic/2023/01/13/22/BMjAyMzAxMTMyMjEwMDNfNDAzMDAxOTlfOTM1MTIzMzYxMjNfMF8z_b_B3dde97f36273f04403d4dc5eec611a35.mp4\n" +
                    "1995,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQwNzVfMF8z_b_B811c0dec6b9a3d3074a18522c185010a.mp4\n" +
                    "1996,https://alimov2.a.kwimgs.com/upic/2023/01/13/22/BMjAyMzAxMTMyMjEwMDNfNDAzMDAxOTlfOTM1MTIzMzYxNTJfMF8z_b_Bd841eae10ab1c9955ef55fbedfae6c45.mp4\n" +
                    "1997,https://txmov2.a.kwimgs.com/bs3/video-hls/5230649583590411879_hlshd15.m3u8\n" +
                    "1998,https://txmov2.a.kwimgs.com/bs3/video-hls/5225864507896315430_hlshd15.m3u8\n" +
                    "1999,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQxNTRfMF8z_b_B0b5e52bc003285ef66ec0cbb2be08556.mp4\n" +
                    "2000,https://alimov2.a.kwimgs.com/upic/2023/01/13/21/BMjAyMzAxMTMyMTE4MzRfNDAzMDAxOTlfOTM1MDY4ODIxMTNfMF8z_b_Bdddf4e7ef0ff6cfd477857bb40e78419.mp4\n" +
                    "2001,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQyMDFfMF8z_b_B70592cb7c4054e9cabb675e849bbf4bd.mp4\n" +
                    "2002,https://alimov2.a.kwimgs.com/upic/2023/01/13/21/BMjAyMzAxMTMyMTE4MzRfNDAzMDAxOTlfOTM1MDY4ODIxNDdfMF8z_b_Ba6271d10b7e6cfae83759033a091f257.mp4\n" +
                    "2003,https://alimov2.a.kwimgs.com/upic/2023/01/14/23/BMjAyMzAxMTQyMzQxNDdfNDAzMDAxOTlfOTM2MTU0MTk1NDFfMF8z_b_B182749d2cd2ea9323639254af385f24b.mp4\n" +
                    "2004,https://alimov2.a.kwimgs.com/upic/2023/01/13/21/BMjAyMzAxMTMyMTE4MzRfNDAzMDAxOTlfOTM1MDY4ODIxOTVfMF8z_b_B86c4430b82ff5a7f4e8132f6ee558536.mp4\n" +
                    "2005,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQyMzhfMF8z_b_B35ad7cc86aec8fc9e5ddfb31fc7bed63.mp4\n" +
                    "2006,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQyNzlfMF8z_b_Bbc3703fc331dc994c50859c19aad28ff.mp4\n" +
                    "2007,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQzMjNfMF8z_b_B00b069c7899976459ceeaa99353dfefe.mp4\n" +
                    "2008,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQzNTNfMF8z_b_Bd7346962e61bd7b84e11a1fa6e4616f9.mp4\n" +
                    "2009,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQzOTBfMF8z_b_B29a36a85e0277f6c2a1f033ef7c10708.mp4\n" +
                    "2010,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQ0MjlfMF8z_b_B8818807a00eed329a69fb494f405bd43.mp4\n" +
                    "2011,https://alimov2.a.kwimgs.com/upic/2023/01/16/11/BMjAyMzAxMTYxMTA3MjFfNDAzMDAxOTlfOTM3MjcyMjA3ODhfMF8z_b_B8214200efc869dc6fcf99dad619fa4c1.mp4\n" +
                    "2012,https://cdn8.yzzy-online.com/20220704/591_82b72f82/1000k/hls/index.m3u8\n" +
                    "2013,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQ1NjNfMF8z_b_B4fea55408dca4471a68a963ae096be59.mp4\n" +
                    "2014,https://alimov2.a.kwimgs.com/upic/2023/01/06/16/BMjAyMzAxMDYxNjMxMTNfNDAzMDAxOTlfOTI4OTY2ODAzNjlfMF8z_b_Bdee65c77f9e7b2120a185c919dad81d2.mp4\n" +
                    "2015,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQ2MTZfMF8z_b_B4851f43f5a2bc2871a9b0ec87294a6e7.mp4\n" +
                    "2016,https://cdn8.yzzy-online.com/20220704/577_cda9c8d1/1000k/hls/index.m3u8\n" +
                    "2017,https://alimov2.a.kwimgs.com/upic/2023/01/13/20/BMjAyMzAxMTMyMDA5MjJfNDAzMDAxOTlfOTM0OTkwNDQ2NDhfMF8z_b_B6527b0c2ce3dda1d9b3f34edd4fdb9aa.mp4\n" +
                    "2018,https://alimov2.a.kwimgs.com/upic/2023/01/06/16/BMjAyMzAxMDYxNjMxMTRfNDAzMDAxOTlfOTI4OTY2ODE2MTBfMF8z_b_B11a778e34390a21de42d407e94f45b91.mp4\n" +
                    "2020,https://alimov2.a.kwimgs.com/upic/2022/01/30/17/BMjAyMjAxMzAxNzA5NDdfNDAzMDAxOTlfNjYxNzQ2MDAyMTFfMF8z_b_B5d51d9564c5670dc66faeba20aa7af3f.mp4\n" +
                    "2021,https://alimov2.a.kwimgs.com/upic/2022/01/30/17/BMjAyMjAxMzAxNzE4NTJfNDAzMDAxOTlfNjYxNzUzOTg3NjlfMF8z_b_Be41d9503181d7b0608a839ed401e02c2.mp4\n" +
                    "2022,https://alimov2.a.kwimgs.com/upic/2022/02/01/11/BMjAyMjAyMDExMTEwMjNfNDAzMDAxOTlfNjYzNzA4MTk4NzNfMF8z_b_B898cc7ddd0025bf54ddb18ec1f723c84.mp4\n" +
                    "2023,https://txmov2.a.kwimgs.com/bs3/video-hls/5251197255879398624_hlshd15.m3u8\n" +
                    "\n" +
                    "MP4韩国歌团,#genre#\n" +
                    "电音,https://vd3.bdstatic.com/mda-mev3hw0htz28h5wn/1080p/cae_h264/1622343504467773766/mda-mev3hw0htz28h5wn.mp4\n" +
                    "韩国歌团001,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240095359203.mp4\n" +
                    "韩国歌团002,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239978750464.mp4\n" +
                    "韩国歌团003,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239858729476.mp4\n" +
                    "韩国歌团004,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239755956819.mp4\n" +
                    "韩国歌团005,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239987758613.mp4\n" +
                    "韩国歌团006,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239880949246.mp4\n" +
                    "韩国歌团007,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239903717006.mp4\n" +
                    "韩国歌团008,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239903321355.mp4\n" +
                    "韩国歌团009,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239799872402.mp4\n" +
                    "韩国歌团010,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239799088974.mp4\n" +
                    "韩国歌团011,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240024786285.mp4\n" +
                    "韩国歌团012,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240142715042.mp4\n" +
                    "韩国歌团013,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240025046562.mp4\n" +
                    "韩国歌团014,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240145171654.mp4\n" +
                    "韩国歌团015,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240147051191.mp4\n" +
                    "韩国歌团016,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239805200933.mp4\n" +
                    "韩国歌团017,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239910253332.mp4\n" +
                    "韩国歌团018,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239806164759.mp4\n" +
                    "韩国歌团019,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239807872136.mp4\n" +
                    "韩国歌团020,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240032526123.mp4\n" +
                    "\n" +
                    "MP4歌团★,#genre#\n" +
                    "歌团★021,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239808028600.mp4\n" +
                    "歌团★022,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240031614983.mp4\n" +
                    "歌团★023,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240150331617.mp4\n" +
                    "歌团★024,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239809100782.mp4\n" +
                    "歌团★025,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240151167718.mp4\n" +
                    "歌团★026,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240033362815.mp4\n" +
                    "歌团★027,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240151167938.mp4\n" +
                    "歌团★029,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239811800375.mp4\n" +
                    "歌团★030,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239916285148.mp4\n" +
                    "歌团★031,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239927589941.mp4\n" +
                    "歌团★032,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239931661209.mp4\n" +
                    "歌团★033,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240171579858.mp4\n" +
                    "歌团★034,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239831144046.mp4\n" +
                    "歌团★035,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240056530470.mp4\n" +
                    "歌团★036,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239832040344.mp4\n" +
                    "歌团★037,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240173879894.mp4\n" +
                    "歌团★038,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240057078179.mp4\n" +
                    "歌团★040,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240059018784.mp4\n" +
                    "歌团★041,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239834324813.mp4\n" +
                    "歌团★042,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239834716201.mp4\n" +
                    "歌团★043,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239837532125.mp4\n" +
                    "歌团★044,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240179867562.mp4\n" +
                    "歌团★045,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240063650207.mp4\n" +
                    "歌团★046,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240181243061.mp4\n" +
                    "歌团★047,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240181363115.mp4\n" +
                    "歌团★048,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239944465251.mp4\n" +
                    "歌团★049,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240065122134.mp4\n" +
                    "歌团★050,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239840536452.mp4\n" +
                    "歌团★051,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240065838644.mp4\n" +
                    "歌团★052,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239945877111.mp4\n" +
                    "歌团★053,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240184339138.mp4\n" +
                    "歌团★054,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239842640589.mp4\n" +
                    "歌团★055,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240186067562.mp4\n" +
                    "歌团★056,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240187071401.mp4\n" +
                    "歌团★057,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240069974546.mp4\n" +
                    "歌团★058,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240070346911.mp4\n" +
                    "歌团★059,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240070818783.mp4\n" +
                    "歌团★060,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239846692034.mp4\n" +
                    "歌团★061,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239951329234.mp4\n" +
                    "歌团★062,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240191295627.mp4\n" +
                    "歌团★063,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240026585459.mp4\n" +
                    "歌团★064,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240192067467.mp4\n" +
                    "歌团★065,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239911732892.mp4\n" +
                    "歌团★066,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240196491782.mp4\n" +
                    "歌团★067,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239960909980.mp4\n" +
                    "歌团★068,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240017737344.mp4\n" +
                    "歌团★069,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240202339353.mp4\n" +
                    "歌团★070,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240203243765.mp4\n" +
                    "歌团★071,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240205555546.mp4\n" +
                    "歌团★072,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239983417489.mp4\n" +
                    "歌团★074,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240221687198.mp4\n" +
                    "歌团★075,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240222023079.mp4\n" +
                    "歌团★076,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240107150280.mp4\n" +
                    "歌团★077,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240224523227.mp4\n" +
                    "歌团★078,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239987569147.mp4\n" +
                    "歌团★079,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240225803033.mp4\n" +
                    "歌团★080,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239989445779.mp4\n" +
                    "歌团★081,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240229579224.mp4\n" +
                    "歌团★082,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239993533054.mp4\n" +
                    "歌团★083,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239994225085.mp4\n" +
                    "歌团★084,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239994741288.mp4\n" +
                    "歌团★085,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239995197198.mp4\n" +
                    "歌团★086,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240232939168.mp4\n" +
                    "歌团★087,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239890536417.mp4\n" +
                    "歌团★088,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239890568711.mp4\n" +
                    "歌团★089,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240233783820.mp4\n" +
                    "歌团★090,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239894180409.mp4\n" +
                    "歌团★092,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239895496483.mp4\n" +
                    "歌团★093,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240119938989.mp4\n" +
                    "歌团★094,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240002397273.mp4\n" +
                    "歌团★095,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240241527208.mp4\n" +
                    "歌团★096,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239899840062.mp4\n" +
                    "歌团★097,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240243499351.mp4\n" +
                    "歌团★098,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240127638122.mp4\n" +
                    "歌团★099,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240030505796.mp4\n" +
                    "歌团★100,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240245283772.mp4\n" +
                    "歌团★101,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240247623420.mp4\n" +
                    "歌团★102,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240043672242.mp4\n" +
                    "歌团★103,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240339124000.mp4\n" +
                    "歌团★104,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240221702622.mp4\n" +
                    "歌团★105,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239993732827.mp4\n" +
                    "歌团★106,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239994460907.mp4\n" +
                    "歌团★107,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240340899550.mp4\n" +
                    "歌团★108,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239995692215.mp4\n" +
                    "歌团★109,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240341971789.mp4\n" +
                    "歌团★110,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239996664565.mp4\n" +
                    "歌团★111,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240342839842.mp4\n" +
                    "歌团★112,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240225254466.mp4\n" +
                    "歌团★113,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240225226897.mp4\n" +
                    "歌团★114,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239998000351.mp4\n" +
                    "歌团★115,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240105989528.mp4\n" +
                    "歌团★116,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/239998340711.mp4\n" +
                    "歌团★117,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240106477140.mp4\n" +
                    "歌团★118,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240107389699.mp4\n" +
                    "歌团★119,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240345787129.mp4\n" +
                    "歌团★120,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240227966801.mp4\n" +
                    "歌团★121,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240228462625.mp4\n" +
                    "歌团★122,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240108721427.mp4\n" +
                    "歌团★123,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240001176191.mp4\n" +
                    "歌团★125,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240001228776.mp4\n" +
                    "歌团★126,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240109533631.mp4\n" +
                    "歌团★127,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240347663598.mp4\n" +
                    "歌团★128,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240001932458.mp4\n" +
                    "歌团★129,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240002044738.mp4\n" +
                    "歌团★130,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240111085001.mp4\n" +
                    "歌团★131,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240350575186.mp4\n" +
                    "歌团★132,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240350771160.mp4\n" +
                    "歌团★133,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240113261859.mp4\n" +
                    "歌团★134,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240352039996.mp4\n" +
                    "歌团★135,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240236014123.mp4\n" +
                    "歌团★136,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240008036293.mp4\n" +
                    "歌团★137,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240354863286.mp4\n" +
                    "歌团★138,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240008780109.mp4\n" +
                    "歌团★139,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240009608741.mp4\n" +
                    "歌团★140,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240379515679.mp4\n" +
                    "歌团★141,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240262842385.mp4\n" +
                    "歌团★142,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240264262344.mp4\n" +
                    "歌团★143,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240384227055.mp4\n" +
                    "歌团★145,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240267170778.mp4\n" +
                    "歌团★146,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240386743317.mp4\n" +
                    "歌团★147,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240268654616.mp4\n" +
                    "歌团★148,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240387107547.mp4\n" +
                    "歌团★149,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240150573492.mp4\n" +
                    "歌团★150,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240388683474.mp4\n" +
                    "歌团★151,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240270774376.mp4\n" +
                    "歌团★152,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240151273206.mp4\n" +
                    "\n" +
                    "MP4韩国太妍,#genre#\n" +
                    "韩国太妍02,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240167997205.mp4\n" +
                    "韩国太妍03,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240059400880.mp4\n" +
                    "韩国太妍04,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240407847242.mp4\n" +
                    "韩国太妍05,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240062596020.mp4\n" +
                    "韩国太妍06,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240170661907.mp4\n" +
                    "韩国太妍07,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240411259014.mp4\n" +
                    "韩国太妍08,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240174309994.mp4\n" +
                    "韩国太妍09,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240175225325.mp4\n" +
                    "韩国太妍10,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240066736888.mp4\n" +
                    "韩国太妍11,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240175161903.mp4\n" +
                    "韩国太妍12,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240295526170.mp4\n" +
                    "韩国太妍13,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240295818399.mp4\n" +
                    "韩国太妍14,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240177321736.mp4\n" +
                    "韩国太妍15,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240177941288.mp4\n" +
                    "韩国太妍16,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240070652257.mp4\n" +
                    "韩国太妍17,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240298266546.mp4\n" +
                    "韩国太妍18,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240070884570.mp4\n" +
                    "韩国太妍19,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240298694512.mp4\n" +
                    "韩国太妍20,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240418087243.mp4\n" +
                    "韩国太妍21,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240299394846.mp4\n" +
                    "韩国太妍22,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240181409471.mp4\n" +
                    "韩国太妍23,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240182993056.mp4\n" +
                    "韩国太妍24,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240301854532.mp4\n" +
                    "韩国太妍25,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240075164377.mp4\n" +
                    "韩国太妍26,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240349762400.mp4\n" +
                    "韩国太妍27,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240121912724.mp4\n" +
                    "韩国太妍28,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240126480392.mp4\n" +
                    "韩国太妍29,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240355262537.mp4\n" +
                    "韩国太妍30,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240355734488.mp4\n" +
                    "韩国太妍31,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240237453313.mp4\n" +
                    "韩国太妍32,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240130092025.mp4\n" +
                    "韩国太妍33,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240478207039.mp4\n" +
                    "韩国太妍34,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240361330093.mp4\n" +
                    "韩国太妍35,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240139316317.mp4\n" +
                    "韩国太妍36,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240248465975.mp4\n" +
                    "韩国太妍37,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240139720035.mp4\n" +
                    "韩国太妍38,https://cloud.video.taobao.com//play/u/57349687/p/1/e/6/t/1/240368550193.mp4\n" +
                    "\n";
            typeArray = new JSONArray();

            // 临时变量：当前分类JSON对象
            JSONObject currentTypeObj = null;
            // 临时变量：当前分类下的视频（key=视频名称，value=视频JSON对象）
            Map<String, JSONObject> videoMap = new HashMap<>();
            // ID计数器
            int typeIdCounter = 1;   // 分类ID：TID001开始
            int videoIdCounter = 1;  // 视频ID：每个分类内重置

            // 分割行：兼容\r\n（Windows）和\n（Linux）换行符
            String[] lines = liveTxt.split("\\r?\\n");
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // 跳过空行
                }

                // 1. 解析分类行：格式 分类名,#genre#
                if (line.endsWith(",#genre#")) {
                    // 先处理上一个分类（避免遗漏）
                    if (currentTypeObj != null && !videoMap.isEmpty()) {
                        try {
                            // 构建videos数组并添加到分类对象
                            JSONArray videosArray = new JSONArray();
                            for (JSONObject videoObj : videoMap.values()) {
                                videosArray.put(videoObj);
                            }
                            currentTypeObj.put("videos", videosArray);
                            // 将分类对象添加到外层数组
                            typeArray.put(currentTypeObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 重置视频Map和计数器
                        videoMap.clear();
                        videoIdCounter = 1;
                    }

                    // 构建新的分类JSON对象
                    String typeName = line.split(",")[0];
                    String typeId = String.format("TID%03d", typeIdCounter++);
                    try {
                        currentTypeObj = new JSONObject();
                        currentTypeObj.put("type_id", typeId);
                        currentTypeObj.put("type_name", typeName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // 2. 解析视频行：格式 视频名,播放地址
                else if (currentTypeObj != null && line.contains(",")) {
                    String[] parts = line.split(",", 2); // 按第一个逗号分割（地址含逗号不影响）
                    if (parts.length != 2) {
                        continue; // 格式错误，跳过
                    }
                    String vodName = parts[0].trim();
                    String playUrl = parts[1].trim();

                    // 检查是否已存在该视频，不存在则新建
                    JSONObject videoObj = videoMap.get(vodName);
                    if (videoObj == null) {
                        try {
                            videoObj = new JSONObject();
                            // 生成视频ID：VID001/VID002...
                            String vodId = String.format("VID%03d", videoIdCounter++);
                            videoObj.put("vod_id", vodId);
                            videoObj.put("vod_name", vodName);
                            // 初始化播放地址数组
                            videoObj.put("vod_play_url", new JSONArray());
                            videoMap.put(vodName, videoObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 添加播放地址到数组（合并多地址）
                    try {
                        JSONArray playUrlArray = videoObj.getJSONArray("vod_play_url");
                        playUrlArray.put(playUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 处理最后一个分类（避免遗漏）
            if (currentTypeObj != null && !videoMap.isEmpty()) {
                try {
                    JSONArray videosArray = new JSONArray();
                    for (JSONObject videoObj : videoMap.values()) {
                        videosArray.put(videoObj);
                    }
                    currentTypeObj.put("videos", videosArray);
                    typeArray.put(currentTypeObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
                    videos.put(new JSONObject()
                            .put("vod_id", typeObj.get("type_id"))
                            .put("vod_name", typeObj.get("type_name"))
                            .put("vod_pic", "")
                            .put("vod_remarks", typeObj.get("type_name")));
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
                        // 固定首页视频列表
                        videos.put(new JSONObject()
                                .put("vod_id", typeObj.get("type_id"))
                                .put("vod_name", typeObj.get("type_name"))
                                .put("vod_pic", "")
                                .put("vod_remarks", typeObj.get("type_name")));
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
     * key 代表 vod_name
     */
    @Override
    public String searchContent(String key, boolean quick) {
        try {
            JSONObject result = new JSONObject();
            JSONArray videos = new JSONArray();
            for (int i = 0; i < typeArray.length(); i++) {
                try {
                    JSONObject typeObj = typeArray.getJSONObject(i);
                    if (typeObj.get("type_name").equals(key)) {
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
