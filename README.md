# 休闲SDK接入文档

## 引入SDK
### 1. 添加网星 maven 仓库地址

```groovy
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()

        maven {
            url "http://inappregistry.iscrv.com/repository/maven-public/"
        }
    }
}
```

### 2. 引入SDK aar

1. 方式一， 引入完整SDK
```groovy
implementation 'com.starmedia:leisure:1.1.2'
```
2. 方式二， 引入最小SDK， 首次使用SDK时， 会由网络加载
```groovy
implementation 'com.starmedia:leisure-tiny:1.1.2'
```

指定使用java8

```
 compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

### 3. 接入Demo

> https://github.com/work-zdx/leisure-demo

## 接入API

### 初始化

```kotlin
 /**
 * 初始化接口
 * @param id 网星后台申请的AppId
 */
StarMedia.init(application: Application, id: String)

```


### 信息流

#### 获取信息流内容

```kotlin
/**
* @param params 频道分类属性 //key 的取值有: 0:新闻、1:图集、2:视频
* value list 的item 的取值见类目编码
* val params = HashMap<Long, List<Int>>()
* params[0] = arrayListOf(1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009)
* params[1] = arrayListOf(1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019)
* params[2] = arrayListOf(1033, 1034, 1036, 1037, 1039, 1040, 1041, 1042)
* @param page 分页数据，每页 30条
*/
StarMedia.loadContent(
activity: Activity,
params: Map<Long, List<Int>>,
page: Int,
listener: IContent.Listener)

interface Listener {
	fun onSuccess(list: List<ContentItem>)
	fun onError(msg: String)
}
```

**params 类目编码**

| 类目ID |   类目   |             0:新闻             |             1:图集             | 2:视频 |
| :----: | :------: | :----------------------------: | :----------------------------: | :----: |
|  1001  |   娱乐   |             **√**              |             **√**              | **√**  |
|  1002  |   体育   |             **√**              |             **√**              | **√**  |
|  1003  |   图片   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1004  |    IT    | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1005  |   手机   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1006  |   财经   |             **√**              |             **√**              | **√**  |
|  1007  |   汽车   |             **√**              |             **√**              | **√**  |
|  1008  |   房产   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1009  |   时尚   |             **√**              |             **√**              | **√**  |
|  1011  |   文化   |             **√**              |             **√**              | **√**  |
|  1012  |   军事   |             **√**              |             **√**              | **√**  |
|  1013  |   科技   |             **√**              |             **√**              | **√**  |
|  1014  |   健康   |             **√**              |             **√**              | **√**  |
|  1015  |   母婴   |             **√**              |             **√**              | **√**  |
|  1016  |   社会   |             **√**              |             **√**              | **√**  |
|  1017  |   美食   |             **√**              |             **√**              | **√**  |
|  1018  |   家居   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1019  |   游戏   |             **√**              |             **√**              | **√**  |
|  1020  |   历史   |             **√**              |             **√**              | **√**  |
|  1021  |   时政   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1024  |   美女   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1025  |   搞笑   |             **√**              |             **√**              | **√**  |
|  1026  |   猎奇   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1027  |   旅游   |             **√**              |             **√**              | **√**  |
|  1028  |   动物   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1030  |   摄影   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1031  |   动漫   |             **√**              |             **√**              | **√**  |
|  1032  |   女人   | <font color='red'>**×**</font> |             **√**              | **√**  |
|  1033  |   生活   |             **√**              |             **√**              | **√**  |
|  1034  |   表演   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1036  |   音乐   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1037  | 影视周边 | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1039  | 相声小品 | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1040  |   舞蹈   | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1041  | 安全出行 | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  1042  |  大自然  | <font color='red'>**×**</font> | <font color='red'>**×**</font> | **√**  |
|  9999  |   其他   |             **√**              |             **√**              | **√**  |

#### 数据信息
```kotlin
class ContentItem {

    interface Type {
        companion object {
            const val NEWS = "news"
            const val IMAGE = "image"
            const val VIDEO = "video"
        }
    }

    @SerializedName("type")
    var type: String = ""

    fun getContentNews(): ContentNews?

    fun getContentVideo(): ContentVideo?

    fun getContentImage(): ContentImage?

	/**
	* 上报条目展示
	*/
    fun onShow(){
    }

	/**
	* 上报条目点击， 会自动打开内容详情页面
	*/
    fun onClick(activity: Activity){
    }

}


class ContentImage {

    //内容id
    var id: Long? = null

    //标题
    var title: String? = null

    //大题图片列表
    var imageList: List<ContentPicture>? = null

    //更新时间
    var updateTime: String? = null

    //出处
    var source: ContentSource? = null

    //图集中的图片数量
    var colImageCount: Int? = null

    //是否授权,0:未授权,1:已授权
    var authorized: Int? = null

    //标签列表
    var tagList: List<ContentPictureTag>? = null

    //类目信息
    var catInfo: ContentCatInfo? = null

    //是否置顶,1:表示置顶
    var isTop: Int? = null

    //相似簇编号
    var clusterNo: Long? = null

    //小题图片列表
    var smallImageList: List<ContentPicture>? = null

    //详情页面url
    internal var detailUrl: String? = null

    //阅读次数
    var readCounts: Int? = null

    //DislikeReasonInfo数组
    var dislikeReasons: List<ContentDislike>? = null

    //摘要
    var brief: String? = null

    //作者姓名
    var author: String? = null

    //举报地址
    var reportUrl: String? = null

    //只有站内推荐使用，媒体自有类目名称
    var outerCate: String? = null

    /**
     * 获取大图
     */
    fun loadImage(): String?
}

class ContentVideo : Serializable {
    //内容id
    var id: Long? = null

    //标题
    var title: String? = null

    //更新时间
    var updateTime: String? = null

    //来源
    var source: ContentSource? = null

    //作者头像
    var avatar: String? = null

    //显示类型
    var presentationType: Int? = null

    //播放时长，单位:秒
    var duration: Int? = null

    //封面图
    var thumbUrl: String? = null

    //视频大小，默认标清。单位:KB
    var videoSize: Int? = null

    //各清晰度视频大小
    var videoSizeInfo: ContentVideoSize? = null

    //详情页面url
    internal var detailUrl: String? = null

    //播放次数
    var playCounts: Int? = null

    //标签列表
    var tags: List<ContentTag>? = null

    //类目信息
    var catInfo: ContentCatInfo? = null

    //DislikeReasonInfo数组
    var dislikeReasons: List<ContentDislike>? = null

    //点击打点URL
    var clickDc: String? = null

    //入屏打点
    var inview: String? = null

    //曝光打点url
    var showDc: String? = null

    //阅读曝光
    var readDc: String? = null

    //作者主页
    var authorPage: String? = null

    //摘要
    var brief: String? = null

    //清晰度: 超清,标清,普清;
    var definition: String? = null

    //举报地址
    var reportUrl: String? = null

    //只有站内推荐使用，媒体自有类目名称
    var outerCate: String? = null

    /**
     * 获取大图
     */
    fun loadImage(): String?
}


class ContentNews() {
    //内容id
    var id: String? = null

    //标题
    var title: String? = null

    //图片列表
    var images: List<String>? = null

    //更新时间
    var updateTime: String? = null

    //置顶标识，1为需要置顶
    var isTop: Int? = null

    //推荐标志位，1:推荐
    var recommend: Int? = null

    //详情页面url
    internal var detailUrl: String? = null

    //类目信息
    var catInfo: ContentCatInfo? = null

    //DislikeReasonInfo数组
    var dislikeReasons: List<ContentDislike>? = null

    //点击打点URL
    var clickDc: String? = null

    //入屏打点
    var inview: String? = null

    //曝光打点url
    var showDc: String? = null

    //曝光打点url列表
    var showDcList: List<String>? = null

    //阅读曝光
    var readDc: String? = null

    //标签
    var tags: List<ContentTag>? = null

    //阅读次数
    var readCounts: Int? = null

    //大图地址
    var bigPicUrl: String? = null

    //是否为图片资讯，1:是图片资讯
    var ifImageNews: Int? = null

    //内容创建时间
    var createTime: String? = null

    //举报地址
    var reportUrl: String? = null

    //摘要
    var brief: String? = null

    //只有站内推荐使用，媒体自有类目名称
    var outerCate: String? = null

    //来源
    var source: String? = null

    /**
     * 获取大图
     */
    fun loadImage(): String?

    /**
     * 获取小图的列表
     */
    fun loadSmallImageList(): List<String>?
}
```
#### 上报信息流条目曝光和点击事件

```kotlin
// 当条目展示时， 上报条目曝光事件， 可以在 RecyclerView Holder 绑定数据时触发
contentItem.onShow()

// 当条目被点击， 上报条目点击事件
contentItem.onClick(activity)
```

### 打开游戏中心

```kotlin
/**
* 打开游戏中心 / 特定游戏
* @param param json 结构， 用于特定场景下使用， null值 打开游戏中心， 具体参数见下面参数说明， 目前支持 game_id 和 menu 字段
* @param callback 打开结果回调，可能存在异步耗时操作，开发者可以选择先展示loading动画，收到回调时结束动画
*/
@JvmStatic
StarMedia.openStarGame(
activity: Activity,
param: String? = null,
callback: ((Boolean) -> Unit)? = null
)
```

### 打开特定游戏

| param key | 类型 | 说明                                                         |
| --------- | ---- | ------------------------------------------------------------ |
| game_id   | Long | 指定打开的游戏ID， 默认值null， 打开游戏中心                 |
| menu      | bool | （可选参数， 默认值 true）是否显示悬浮按钮，点击悬浮按钮可返回到游戏中心， 如果不想展示悬浮按钮， 请传递 false |


游戏中心支持打开特定某一款游戏， 用 game_id 作为 key，值为Long类型
> 例如： 打开 开心钓钓乐， param 参数传递 {"game_id": 10001}


如果想打开特定的游戏， 并且不显示悬浮按钮，传递 game_id 字段的同时， 还需要传递 menu 字段
> 例如:  打开 连连看, 同时不显示悬浮菜单， param 参数传递 {"game_id": 10002， "menu": false}

#### 目前支持的游戏ID
|        小游戏名称        | 游戏ID |
| :----------------------: | :----: |
|        开心钓钓乐        | 10001  |
|          连连看          | 10002  |
|         六角消除         | 10003  |
|        开心小农场        | 10004  |
|        成语大官人        | 10005  |
|  消灭僵尸（庄园保卫战）  | 10006  |
|        开心斗地主        | 10007  |
|         水果杀手         | 10008  |
|         左右消滑         | 10009  |
|         养猪大亨         | 10010  |
|        快来当岛主        | 10011  |
|        扫雷大作战        | 10012  |
|        娘娘养成记        | 10013  |
|        全明星足球        | 10014  |
|         水果忍者         | 10015  |
|       滚动的天空2        | 10016  |
| 经典打砖块（砖块消消消） | 10017  |
|        疯狂消星星        | 10018  |
|       开心六角消除       | 10019  |
| 欢乐碰碰球（吃鸡碰碰碰） | 10020  |
|         欢乐猜字         | 10021  |
|         黄金矿工         | 10022  |
|          打球球          | 10023  |
|          泡泡龙          | 10024  |
|         跳跃忍者         | 10025  |
|        我是神枪手        | 10026  |
|         塔防三国         | 10027  |
|         热血篮球         | 10028  |
|         航海传奇         | 10029  |
|         街头篮球         | 10030  |
|          五子棋          | 10031  |
|           乒乓           | 10032  |
|         跳舞的线         | 10033  |
|   幸福餐厅（幸福厨房）   | 10034  |
|          斗兽棋          | 10035  |
|         快乐拼拼         | 10036  |

### 设置支持 切屏广告 和 激励视频广告

小游戏内 打开关卡 或 获得奖励 时， 存在可以展现激励视频广告的场景， 如果开发者想展示这些广告， 可以在初始化前设置 这些广告的回调

#### 设置调用激励视频广告的回调

```kotlin
StarMedia.setRewardedVideoListener {
    //loadRewardVideo()
}
```

