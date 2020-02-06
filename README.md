# NGINX动态发现服务

为了使服务动态的扩容，而不用频繁修改nginx的配置文件，寻求解决办法。

找到了nginx的第三方模块 [ngx_http_dyups_module](https://github.com/yzprofile/ngx_http_dyups_module) ，可以做到动态更新upstream，并且不用reload。

然后需要一个发现服务的功能，监听到新服务上线/下线，及时更新nginx的upstream，就有了这个工程。

有些解决方案是采用了etcd/consul+nginx第三方模块(nginx-upsync-module)的方式来实现nginx零重启更新upstream的操作。

因为项目都是基于zookeeper，所以就使用了zk作为注册中心。

如果觉得安装nginx第三方模块麻烦，可以使用[tengine](http://tengine.taobao.org/) 。集成了dyups模块。

### 参考：
- [Nginx动态发现方案与实践](https://mp.weixin.qq.com/s/AOUaeq3glhJrb_NeRzXjbA)

- [基于Nginx dyups模块的站点动态上下线并实现简单服务治理](https://www.cnblogs.com/beyondbit/p/6063132.html)



#### TODO
- upstream数据持久化
