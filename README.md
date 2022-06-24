# 校园二手交易 APP
一个使用 Java 编写的简易的校园二手交易 Android APP，后端使用 [LeanCloud](https://leancloud.cn/) .
## 介绍
主要实现：
- 底部导航栏：BottomNavigationView + 自定义 Fragment 逻辑，页面状态保持;
- 注册登录：LeanCloud SDK
- 网络数据请求：RxJava
- 图片加载：Glide
- 余额实时更新：BroadcastReceiver 广播机制
- 页面/应用间跳转：Intent
## 部分界面
![image](https://user-images.githubusercontent.com/59608026/175533818-7f57dc4e-44dc-4449-b0cb-d7353536d7a8.png)
![image](https://user-images.githubusercontent.com/59608026/175535894-b4bef76a-037b-4608-a1da-04f0d736c1a8.png) <br>
![image](https://user-images.githubusercontent.com/59608026/175534727-78270a31-2b45-4085-b18c-b69fa24f841e.png) <br>
![image](https://user-images.githubusercontent.com/59608026/175536828-ff5ac57d-b9c9-461c-86f0-840f7fef518c.png) <br>
## 构建 & 运行
1. 打开项目，等待 Gradle 完成;
2. 打开 MainActivity ，填上 LeanCloud 初始化所需 [参数](https://leancloud.cn/docs/sdk_setup-java.html#hash-702365779) （需要注册LeanCloud）;
```java
//TODO  LeanCloud.initialize(this, "", "", "");
```
3. 运行
