# lifeshow-server
毕设代码备份。短视频小程序服务端，包含管理端和小程序后端（Spring Boot项目）



### 简短介绍：

基于微信小程序的短视频社交软件设计与实现

随着微信的普及和短视频的流行，设计一个微信小程序端的短视频社交软件，提供短视频的观看、点赞、评论、分享以及短视频的上传发布等功能给用户使用；用户观看视频时界面上会有其他用户留言以弹幕的形式滚动，让用户有和其他用户同时观看视频的感觉，并且用户可以对其他用户的留言进行点赞和回复；提供多个内容板块供用户选择，可以定期举办内容创作比赛，由用户和短视频上传用户共同参与。后端使用基于Java的短视频后台管理系统，包括用户管理、短视频的管理、背景音乐的管理、用户留言的管理、登录注册、权限验证等等功能。



### 后端界面预览：

<img src="https://raw.githubusercontent.com/chaooWoo/lifeshow-server/master/document/images/2.png" alt="预览图" style="zoom:50%;" />



### 需求量化：

#尽可能地实现了大部分功能

#相较于【[需求量化.xlsx](https://github.com/chaooWoo/lifeshow-minip/blob/master/document/需求量化.xlsx)】新增功能包括：

1. 删除管理员本地上传音频，改为QQ音乐接口搜索和下载（仅可试听和下载免费歌曲）
2. 管理员音频剪切功能

#未实现功能包括：

1. 后台与小程序的websoket连接
2. ~~管理员dashboard面板数据为假数据~~ （已有相关代码完成该功能）
3. 暂不支持管理员对短视频信息的修改
4. 管理员操作日志未实现



### 如何使用？

* 前提（以下两个部署之前都需要做的事情）：
  1. 在配置文件【application.yml】中spring.profiles.active改为对应的环境即可（dev对应开发环境，pro对应生产环境）
  2. 创建一个名为 **life_show** 的本地数据库
* 本地使用
  1. 配置文件【application-dev.yml】更改数据库用户名【data-username】和密码【data-password】为自己的
  2. 配置文件【application-dev.yml】修改Redis相关配置，主要是【host】改为虚拟机ip
  3. 配置文件【application-dev.yml】修改文件存放的位置，主要是盘符
  4. 配置文件【application-dev.yml】修改ffmpeg工具地址，建议使用静态编译版本，不需要其他动态库，这里推荐ffmpeg的[静态编译版](https://ffmpeg.zeranoe.com/builds/)下载，根据需要下载即可（Linking为static版本）
* 服务器部署（采用docker一键部署SpringBoot打包为【jar】包的项目）
  1. 由于小程序需要https、tls、ssl等才能够访问服务器资源，因此【application-pro.yml】中对SpringBoot内置Tomcat进行了SSL配置，相关配置教程，可以从[这里](https://blog.csdn.net/weixin_41463193/article/details/89220092)看
  2. 配置文件【application-pro.yml】更改数据库用户名【username】和密码【password】为自己的
  3. 配置文件【application-pro.yml】修改文件存放的位置，可修改为linux服务器任意有权限的位置，需要和后续docker部署时的文件挂载相同
  4. 配置文件【application-pro.yml】中服务器地址【server.url】（这是自定义的）修改为自己服务器的域名地址
  5. Maven的项目周期管理中先后运行【clean】【install】命令完成项目jar包的打包
  6. 编写docker部署时需要的Dockerfile，可以查看[这里](https://blog.csdn.net/zhangcc233/article/details/96706157)的教程，同时该帖子也包含了后续的一些操作
  7. **下面开始是linux上的操作**（使用的是redhat的CentOS 7）
  8. 在linux上安装Redis并启动，最好更改Redis配置文件，将其进行后台运行（若是你使用docker进行redis安装，相应问题请自行解决）
  9. 将打包后的jar包和编写好的Dockerfile上传到linux任意文件夹中（最好是按照配置文件中的路径进行文件夹创建，这样你可以减少更改），建议该文件夹中只有这两个文件以及建议再将之前下载好的SSL证书也放在该文件夹下（后缀为.pfx的文件，如果下载的为Tomcat是该后缀）
  10. 将linux版本的静态编译版本ffmpeg工具上传并放置在指定位置（文件下载在FAQ中）
  11. 按照[教程](https://blog.csdn.net/zhangcc233/article/details/96706157)所示在linux中安装docker和MySQL，【前提】中创建的数据库需要在这里进行创建，MySQL使用Navicat连接出现错误时可以查看下面的FAQ进行问题查找，注意！！！【我使用的MySQL版本为8.0.19】
  12. 在存放jar包的当前文件夹执行命令 ：【**docker build -t lifeshow .**】 制作docker镜像
  13. 运行项目，可执行该命令在运行项目同时文件挂载：【**docker run -it --net=host -v /root/lifeshow/tool:/root/lifeshow/tool -v /root/lifeshow/bgm/audio:/root/lifeshow/bgm/audio -v /root/lifeshow/bgm/cover:/root/lifeshow/bgm/cover -v /root/lifeshow/video/cover:/root/lifeshow/video/cover -v /root/lifeshow/video/video:/root/lifeshow/video/video -v /root/lifeshow/log:/root/lifeshow/log --privileged=true --name lifeshow -p 80:80 lifeshow**】
  14. 等待片刻后即可在浏览器进行访问



### 功能设计：

1. 短视频管理（增 / 删 / 改 / 查）
2. 用户管理（增 / 删 / 改 / 查）
3. 背景音乐管理（增 / 删 / 改 / 查）
   * 本地音乐的完整试听 / 本地音频剪切
   * 在线音乐搜索 / 试听 / 下载
4. 活动标签管理（增 / 删 / 改 / 查）
5. 举报模块（对被举报短视频进行相应操作）
6. 管理员管理（只有admin用户可以使用）



### 技术栈：

#### 后端

| 名称                   | 描述                     | 官网                                         |
| ---------------------- | ------------------------ | -------------------------------------------- |
| Spring Framework       | 容器                     | https://spring.io/projects/spring-framework  |
| Spring Boot            | 微服务框架               | https://spring.io/projects/spring-boot       |
| MyBatis Plus           | ORM框架，MyBatis增强版本 | https://mp.baomidou.com                      |
| MyBatis Plus Generator | 代码生成                 | https://mp.baomidou.com/guide/generator.html |
| PageHelper             | MyBatis Plus分页         | https://mp.baomidou.com/guide/page.html      |
| Maven                  | 项目构建管理             | http://maven.apache.org/                     |
| MySQL                  | 关系型数据库             | https://www.mysql.com                        |
| Redis                  | 费关系型数据库           | https://redis.io                             |
| FFmpeg                 | 音视频服务工具           | http://ffmpeg.org                            |
| swagger2               | API接口文档生成工具      | https://swagger.io                           |



#### 前端

| 名称        | 描述                                 | 官网                                    |
| ----------- | ------------------------------------ | --------------------------------------- |
| Thymeleaf   | 模板引擎                             | https://www.thymeleaf.org               |
| echarts     | 图表                                 | https://www.echartsjs.com/zh/index.html |
| jQuery      | 函数库                               | http://jquery.com/                      |
| wavesurfer  | 音频可视化插件                       | https://wavesurfer-js.org               |
| Semantic UI | 快速创建好看、响应式、可读性好的HTML | https://semantic-ui.com/                |



### FAQ：

**#项目连接mysql数据库是出现无法连接怎么办呀？**

可能是因为配置文件【application-dev.yml】中数据库用户名【data-username】和密码【data-password】，改为【username】和【password】，在我的windows10 -> IDEA 上使用前一种可以连接，但是在linux（CentOS）上就无法连接，所以配置文件【application-pro.yml】使用的是后一种



**#服务器部署时，docker下载镜像好慢怎么办呀？**

可以设置docker的镜像下载加速为阿里云的镜像加速，教程在[这里](https://blog.csdn.net/weixin_43569697/article/details/89279225)



**#使用虚拟机时，windows中的项目如何连接linux上的redis？**

1. 如果你使用的虚拟机为【VMware】，并且linux系统为CentOS 7时，可以像我这样做

2. 将【虚拟机设置】中【网络适配器】设置为NAT模式

3. 启动linux系统，并将该系统的ip地址设置为一个固定值，[如何修改？](https://blog.csdn.net/wudinaniya/article/details/93342297?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2)

   **注：** 第 **3.** 步中的一些注意点：修改的配置名字不一定是【ifcfg-ens32】，我的是【ifcfg-ens33】，所以不用一定找到博客中的文件，只要里面的内容对就行，下面是我的配置截图

   ![配置](https://raw.githubusercontent.com/chaooWoo/lifeshow-server/master/document/images/1.png)

4. 将上一步中设置的【IPADDR】写到【application-dev.yml】中Redis配置中

5. 到这里你可能还是无法连接，因为CentOS系统自带的防火墙机制（其他linux版本可以自行解决），那么你可以关闭防火墙，或是永久开放Redis的永久端口【6379】，[如何开关？](https://blog.csdn.net/ls1645/article/details/78750561)

6. 最后一点：Redis本身是不允许外部连接的，可以在配置文件中进行设置，将其注释，变得任意ip都可进行连接




**#linux版本的静态编译版ffmpeg在哪里下载？**

这里准备了 **ffmpeg** 和 **ffprobe** 的linux静态编译版本：

ffmpeg：https://pan.baidu.com/s/1JBJoS0TSx-myrBNsM9ci3A 提取码：u8tg

ffprobe：https://pan.baidu.com/s/1llVK7QK3mn_AZXkNy0jzlw 提取码：g31a

使用原因：在某个网页上下载的，可能某些功能无法使用，可使用功能够我们使用了。主要是解决了：将ffmpeg安装在linux上时，使用ffmpeg需要调用动态链接库，而在docker容器中的项目无法访问这些动态链接库（可能有错，我理解不是很深，有什么错误请告诉我，谢谢），所以使用的是静态编译版本



**#服务器部署时，为什么Navicat无法连接已经开启的docker容器中的mysql，出现2059错误？**

可能有多个原因，需要你自己排除：

1. 以阿里云ESC服务器为例，实例的安全组规则中，需要将MySQL的3306接口添加进去，供外部访问
2. 因为我使用的MySQL版本为8.0.19，所以可能原因为MySQL8之前版本的加密规则为mysql_native_password，之后加密规则为caching_sha2_password，所以需要进行更改，如何更改，这里有相应的[教程](https://www.cnblogs.com/lifan1998/p/9177731.html)
3. 第 **2.** 步中无法连接怎么执行相应命令呢？通过docker进入MySQL容器，之后再做操作，如何进入容器，可以百度搜索



**#阿里云上部署的Tomcat访问慢怎么办呀？**

项目部署之后，可能访问很慢，网页需要加载很长一段时间才能出来，[这里](https://blog.csdn.net/qq_40386113/article/details/84837881)可以帮助你



### 其他：

1. 代码风格可能不是很好，有一些混乱，相应注释在后续提交中逐步增加
2. 如有其他问题而在FAQ中无法找到答案，可以自行解决时，请给这个项目提交issues，让我补充到FAQ中供其他使用者查看。若是有其他无法解决的问题，也可以提交issues。项目稳定性不是特别好，请海涵。



#**2020/04/27更新**

1. 删除原来的master分支
2. 删除原有的两张表：tb_sys_stat和tb_stat_type
3. 管理员dashboard面板数据：已增加相应代码，反应的数据为真实数据库数据，使用echarts完成数据可视化，可以前往中文官网进行学习
4. **特别注意！！！！：** 如果需要在本地运行该项目时，需要去除SSL的Bean配置，否则本地访问都会将链接端口指向https的443端口，导致无法访问，（无需删除带有ssl配置的【application-pro.yml】），**解决方法**：在该包下【package per.chao.lifeshow.config】，选择以下任一一种方法可以解决：
   * 将类【ConnectorConfig.java】直接删除
   * 将类【ConnectorConfig.java】的【servletWebServerFactory】方法上的注解【@Bean】去除，使Spring不能创建相应的Bean

#**2020/05/07更新**

1. 修复管理员自动登录Cookie的路径错误
2. 完善在管理员封禁用户情况下，小程序端用户不可登录的功能
3. 修改【UserCommentVO属性结构】和【CommentsMapper的SQL语句】，支持用户可以通过点击留言内用户头像进入用户信息页
4. 修复dashboard面板显示：有关时间（7日内等）的统计图形显示不正确的问题
5. 小程序访问量统计问题：
   * 系统初始化时自动初始化Redis中总访问量和当日访问量
   * Spring Boot设置定时任务，每天0点自动将当日访问量清零
6. 补充说明（上次commit就改了但是没有说）：日志组件AOP【per.chao.lifeshow.aop.LogAspect.java】中，将原有的切入点表达式【@Pointcut("execution(* per.chao.lifeshow.controller.\*.\*(..))")】改为【@Pointcut("execution(* per.chao.lifeshow.controller..\*.\*.*(..))")】，使得controller包下的admin子包下的类也可以被扫描到
7. 添加生日彩蛋



 --> readme后续将会逐步完善...