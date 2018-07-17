[TOC]

# 0. 基础SDK以及音视频

CC视频推拉流以及各个组件都依赖CCAtlasClient这个核心类, 获取核心类的实例:CCAtlasClient mAtlasClient = CCAtlasClient.getInstance();

获取一个context实例：CCInteractSDK.init(this.getApplicationContext(), true);

## 0.1 获取sessionid
登入房间需要获取sessionid才可以登入房间
```javascript
public void login(String roomId, String userId, @CCAtlasClient.Role int role,
                      String username, String password,final CCAtlasCallBack<String> callBack) {
```
object 参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| roomId  | String | 房间ID   | 必选     |
| userId| String | 用户ID   | 必选     |
| role| int | 用户角色   | 必选     |
| username  | String |  用户名  | 必选     |
| password  | String | 用户密码   | 必选     |
| callBack| CCAtlasCallBack | 回调   | 必选     |

### 0.1.1 登录成功

join登录成功以后，给客户返回配置信息data
```javascript
public void join(String sessionId, String userAcount, String areaCode, final CCAtlasCallBack<CCInteractBean> callBack) {
```
object 参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| sessionId | String | sessionid   | 必选     |
| userAcount| String | 用户ID   | 必选     |
| areaCode| String | 节点区域，可以传null   | 可选     |
| callBack| CCAtlasCallBack | 回调   | 必选     |

返回 CCInteractBean部分数据格式如下

```javascript
{

     result:"OK",
     data:{
    
            "desc": "9DC1A878A164F696",   //房间描述
    
            "talker_bitrate": 200,      //学生推流码率
    
            "publisher_bitrate": 200,    //老师推流码率
    
           "live": {
          		"id": 1231,   // 直播id
              	"status": 0,   // 1为开始
              	"last": "2000" // 如果status为1，则last为直播持续时长，单位毫秒
    			"startTime":"1503908480000"  //直播开始时间戳
            }
    
            "max_streams": 6,   //允许最大视频流数
    
            "name": "ha",      //用户昵称
    
            "result": "OK",
    
            "user": {
    
    	         "id": "6e373a456cd04b45b00f7b97986a45fc",   //用户唯一id
    
    	         "name": "123123123",   //用户name
    
    	         "role": "talker",    //'talker'学生   'presenter'老师
    
    	         "roomid": "EC05E15A770D84AC9C33DC5901307461", // 房间id
    
            },
    
            "video_mode": 1,   // 视频模式1为音视频模式  ， 2为仅音频模式
    
            "is_follow": '',  // 默认为空，如果是跟随模式，则为流id
    
            "max_users": 1, // 最大支持连麦人数
    
            "allow_chat": true  // 是否允许发言，默认为True, 房间级配置
    
            "allow_audio": true   // 是否允许麦克风 默认为True, 房间级配置
    
            "allow_speak": true  // 是否允许上麦，默认为True 房间级配置
    
            "rtspurl": "" //第三方推流地址
    	}
    }

```

# 1. 流相关方法

## 1.1 本地流相关方法

### 1.1.1 创建本地流
创建本地流，需要
```javascript
 public CCStream createLocalStream(@NonNull LocalStreamConfig config) throws StreamException {
```

参数说明：

| 参数名称   | 参数类型 | 说明                         | 是否必须 |
| ---------- | -------- | ---------------------------- | -------- |
| config    | LocalStreamConfig | 创建本地流自定义参数         | 必选     |

### 1.1.2 关闭远程流声音

```javascript
public void pauseAudio(@NonNull CCStream stream, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明       | 是否必须 |
| ---------- | -------- | ---------- | -------- |
| stream   | CCStream | 远程流 | 必选     |
| success    | function | 成功回调   | 可选     |
| fail       | function | 失败回调   | 可选     |


### 1.1.3 开启远程流声音

```javascript
  public void playAudio(@NonNull CCStream stream, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明       | 是否必须 |
| ---------- | -------- | ---------- | -------- |
| stream     | CCStream | 本地流名称 | 必选     |
| success    | function | 成功回调   | 可选     |
| fail       | function | 失败回调   | 可选     |

### 1.1.4 关闭本地流视频画面

```javascript
 public void pauseVideo(@NonNull CCStream stream, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明       | 是否必须 |
| ---------- | -------- | ---------- | -------- |
| stream     | CCStream | 本地流名称 | 必选     |
| success    | function | 成功回调   | 可选     |
| fail       | function | 失败回调   | 可选     |

### 1.1.5 开启本地流视频画面

```javascript
public void playVideo(@NonNull CCStream stream, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明       | 是否必须 |
| ---------- | -------- | ---------- | -------- |
| stream     | CCStream | 本地流名称 | 必选     |
| success    | function | 成功回调   | 可选     |
| fail       | function | 失败回调   | 可选     |


### 1.1.6 销毁本地流的资源

```javascript
public void destoryLocalStream() {
```

### 1.1.7  推送本地流

```javascript
  public synchronized void publish(final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明               | 是否必须 |
| ---------- | -------- | ------------------ | -------- |
| success    | function | 成功回调（含参数） | 可选     |
| fail       | function | 失败回调（含参数） | 可选     |


### 1.1.8  取消推送本地流

```javascript
 private synchronized void _publish(final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称   | 参数类型 | 说明               | 是否必须 |
| ---------- | -------- | ------------------ | -------- |
| success    | function | 成功回调（含参数） | 可选     |
| fail       | function | 失败回调（含参数） | 可选     |

## 1.2 远程流方法

### 1.2.1 订阅远程流

```javascript
public synchronized void SubscribeStream(CCStream remoteStream, CCAtlasCallBack<CCStream> callBack)
            throws StreamException {
```

参数说明：

| 参数名称  | 参数类型 | 说明                                 | 是否必须 |
| --------- | -------- | ------------------------------------ | -------- |
| remoteStream | CCStream | 要订阅的流对象                    | 必选     |
| success   | CCStream | 成功回调（含参数）                   | 可选     |
| fail      | function | 失败回调（含参数）                   | 可选     |

### 1.2.2 取消订阅远程流

```javascript
 public synchronized void unSubscribeStream(CCStream remoteStream, CCAtlasCallBack<Void>
            callBack) throws StreamException {
```

参数说明：

| 参数名称    | 参数类型 | 说明               | 是否必须 |
| ----------- | -------- | ------------------ | -------- |
| unSubStream | object   | 要取消订阅的流对象 | 必选     |
| success     | function | 成功回调（含参数） | 可选     |
| fail        | function | 失败回调（含参数） | 可选     |


## 1.3 监听流相关事件

事件监听，建议在初始化sdk后做监听

### 1.3.1 监听流服务事件通知

监听atlas的流服务事件，不使用排麦组件则监听该事件，使用排麦组件监听排麦流服务事件。

首先获取流服务的监听事件：mAtlasClient.setOnNotifyStreamListener(mClientObserver);

使用例子：（demo里有实现的代码逻辑，可供参考）

```javascript
 private CCAtlasClient.OnNotifyStreamListener mClientObserver = new CCAtlasClient.OnNotifyStreamListener() {

        @Override
        public void onStreamAllowSub(CCStream stream) {
           //这块监听是监听到有流可订阅，逻辑可以根据需要设置。
        }

        @Override
        public void onStreamRemoved(CCStream stream) {
            //这块监听是监听到流移除事件，逻辑可以根据需要设置
        }

        @Override
        public void onStreamError() {
            //这块监听是流错误事件，可以根据自己需要设置
        }
    };
```

## 1.4  部分高级功能

### 1.4.1 推流至cdn平台

直播开启后，讲师端推流成功后，可以调用该方法，将房间内的流推入cdn平台

```javascript
 public void addExternalOutput(@NonNull String serverUrl, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| url      | String   | 推流地址           | 可选     |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |


### 1.4.2 取消推流至cdn平台

讲师端调用推流至cdn平台成功后，如果要取消推送，可以调用该方法

```javascript
 public void removeExternalOutput(@NonNull String serverUrl, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| url      | String   | 推流地址           | 可选     |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |


### 1.4.3 更新推流平台地址

```javascript
 public void updateExternalOutput(@NonNull String serverUrl, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| url      | String   | 推流地址           | 必选     |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |


### 1.4.4 mix 流

此方法只有讲师角色有权限调用，将本地流合并入混流

```javascript
public void mix(final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |

### 1.4.5 unMix 流

此方法只有讲师角色有权限调用，将已加入混流的流取消混流

```javascript
public void unmix(final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |

### 1.4.6 设置混流位置

此方法只有讲师角色有权限调用，为已加入混流的流设置显示区域

```javascript
public void setRegion(@NonNull CCStream stream, final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称  | 参数类型 | 说明               | 是否必须 |
| --------- | -------- | ------------------ | -------- |
| stream | stream | 设置混流参数       | 必选     |
| success   | function | 成功回调（含参数） | 可选     |
| fail      | function | 失败回调（含参数） | 可选     |


### 1.4.7 获取混流位置

此方法只有讲师角色有权限调用，获取已加入混流的流的显示区域

```javascript
 public void getRegion(@NonNull CCStream stream, final CCAtlasCallBack<String> callBack) {
```

参数说明：

| 参数名称  | 参数类型 | 说明               | 是否必须 |
| --------- | -------- | ------------------ | -------- |
| stream    | stream   | 获取混流位置参数   | 必选     |
| success   | function | 成功回调（含参数） | 可选     |
| fail      | function | 失败回调（含参数） | 可选     |

### 1.4.8 获取单条流状态

此方法用于获取已经订阅到或已经推出去的流对象的音视频状态

```javascript
 public void getConnectionStats(@NonNull CCStream stream, final CCAtlasCallBack<ConnectionStatsWrapper> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明                           | 是否必须 |
| -------- | -------- | ------------------------------ | -------- |
| stream   | stream   | 已经订阅到或已经推出去的流对象 | 必选     |
| success  | function | 成功回调（含参数）             | 可选     |
| fail     | function | 失败回调（含参数）             | 可选     |


### 1.4.9 获取城市节点列表

此方法主要是获取到城市节点的列表
```javascript
 public void dispatch(String roomid,String userid,final CCAtlasCallBack<CCCityBean> callBack){
```
参数说明：

| 参数名称 | 参数类型 | 说明                           | 是否必须 |
| -------- | -------- | ------------------------------ | -------- |
| roomid   | String    | 房间ID           | 必选     |
| userid   | String  | 用户ID             | 必选     |
| callBack | CCAtlasCallBack  | 回调      | 可选     |

# 2. 公有的SDK接口

## 2.1 主动调用事件

### 2.1.1  开启直播

直播开启接口由老师角色去控制

```javascript
 public void startLive(final CCAtlasCallBack<Void> callBack) {
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| success  | function | 成功回调（含参数） | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |


### 2.1.2 关闭直播

直播关闭接口由老师角色去控制

```javascript
public void stopLive(final CCAtlasCallBack<Void> callBack)
```

参数说明：

| 参数名称 | 参数类型 | 说明               | 是否必须 |
| -------- | -------- | ------------------ | -------- |
| success  | function | 成功回调           | 可选     |
| fail     | function | 失败回调（含参数） | 可选     |


### 2.1.3 判断直播是否开启

老师端是否开启了直播，返回true开始直播，false结束直播

```javascript
 public boolean isRoomLive() {
```

### 2.1.4 获取本地流ID

```javascript
 public String getLocalStreamId() {
```

### 2.1.4 获取直播间用户列表

```javascript
  public @Nullable ArrayList<CCUser> getUserList() {
```
### 2.1.5 资源释放接口，退出房间，需要释放资源

```javascript
   public void disconnectSocket() {
```

## 2.2 被动监听事件

事件监听，建议在初始化sdk后做监听

### 2.2.1 监听开启/结束直播事件通知

当老师角色调用开启/结束直播接口后，房间内所有人都会监听到该事件

首先获取开启/结束直播监听事件： mAtlasClient.setOnClassStatusListener(mClassStatusListener);

使用例子：（demo里有实现的代码逻辑，可供参考）

```javascript
 private CCAtlasClient.OnClassStatusListener mClassStatusListener = new CCAtlasClient.OnClassStatusListener() {

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }
    };
```