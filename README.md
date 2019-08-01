# 单点登录系统实现
## 介绍

一套基于shiro的伪单点登录的模板简易实现, 使用redis实现shiro-session的分布式共享, 思路可供参考。

---

## 概念区分:

### 一、真伪单点登录：

何为真单点，何为伪单点呢？其实理解起来也很简单。

伪单点，举个例子，比如一般中小公司里的内部系统，这类系统一般只有一套用户认证和权限系统，各子系统之间也都是相互信任的，对用户的验证方式都是统一的，这类就是伪单点，一般这类系统都是部署在同一个ip地址上, 即cookie是项目间共享的。

像两个比较大型的系统之间的整合，比如A公司的a系统要整合到B公司的b系统中，两个系统之前肯定不是统一的验证方式，信任什么的更不用提了，这类单点就稍微复杂一些，也就是真单点。

### 二、为什么需要共享Session

以`shiro` 框架作为`登录验证权限框架` 为例，这里还不写这个，先说为什么需要`共享session` ,正常情况的时候，是如下图： 

![](F:\IdeaProjects\SSO-project\images\1.png)

如上图描述，每个项目中`shiro` 都维护了自己的`sessionid与session的关系` ，它们之间不共享。

上图两个项目中切换请求必然会有如下的步骤：

1、sessionId串的生成
  浏览器首次对项目1发起请求时，项目1 会为当前请求创建一个session ，根据session 生成一个sessionId 字符串，该sessionId串 与创建的session 维护着一个关联关系。

2、sessionId串存到cookie中
  后台将该sessionId串返回给浏览器，浏览器将sessionId串 拿到后，存储到浏览器中的cookie 中。如下图： 

![](F:\IdeaProjects\SSO-project\images\2.png)

3、再去请求项目2
  当然，当前的这个sessionId串 仅仅对项目1 来说是认识的。如果这时候浏览器接着访问项目2，（在http请求中，发起请求的一方总会把自身所带的所有cookie 打包到请求头中传给服务）。如下边两张图所示：

​        第一张为在去请求项目2前，可以看到浏览器中的cookie ，此时共有三个，其中sessionId 对应的字符串是来自于项目1 登录成功后返回的 ： 

![](F:\IdeaProjects\SSO-project\images\3.png)

 当我对`项目2` 发起请求，浏览器将现有的所有`cookie` 串统统放到了`request headers` 里，传了过去。 

![](F:\IdeaProjects\SSO-project\images\4.png)

4、请求必然失败

​       这时候项目2 中已有的session 集合中无法根据当前传入进来的sessionId串 匹配成功的。因此登录不成功。 

    问题就看出来，是因为项目2 里的session 集合无法认识当前传入进来的sessionId ，匹配不到session 导致的访问失败。

---

## 实现方式

 上边已大致说明为什么需要共享`session` 。那共享`session` 后，是什么逻辑，也备了一张图, 下面说一下实现的理解, 如下图:

![](F:\IdeaProjects\SSO-project\images\5.png)

上图中`master` 项目为主项目，登录页即在这个项目中，`suiteone` 、`suitetwo` 为两个从项目，当两个从项目有请求时，如果没有登录的时候，都会打到`master` 项目的登录页上。共享`session` 采用的是`redis` 存储。

### 实现步骤

1. 浏览器请求master 项目，第一次请求的时候，也是会带着浏览器中的cookie 去请求，当然第一次去redis 里肯定找不到对应的session，会通过⑤进入到登录页。
2. 当在登录页输入完正确的账号密码后，才能登录成功，否则仍会回到⑤。
3. 在这一步的时候，会将登录成功后的session ，根据它，将生成sessionId串 ，并传到前端浏览器中，浏览器以cookie 存储。
4. 同时将第③步中生成的session 存储到redis 中。
5. 当前这里，不只是当登录失败的时候，会进入到登录页中，当浏览器长时间没有访问后台（每次浏览器访问后台，其实都会刷新session 的过期时间expireTime），导致session 超过时，也会进入到该步中。
6. 当浏览器请求suiteone 、suteTwo 这两个从项目时，肯定也是将当前浏览器中的所有的cookie 设置到request headers 请求头中。
7. 根据传入的sessionId串 到共享的redis 存储中匹配。
8. 如果匹配不到，则会跳转到`master` 项目的登录页，如果匹配成功，则会访问通过。

---

## 必要条件

1, redis服务。

2、搭建各项目的shiro集群, 登录系统负责提供登录入口和项目路由分发。

[感谢大神思路链接](https://blog.csdn.net/wohaqiyi/article/details/81342741)