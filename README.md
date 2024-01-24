# v2cbb-android
适用于Android的V2Ray客户端，可以实现匿名上网，远程访问，vpn等方案

![256](./assets/256.png)

[English](README_EN.md) | 简体中文

## 使用

在代理配置里面写上[v2ray](https://v2ray.com/)的config配置文件即可，只需要写outbounds和route即可

```json
{
  "log": {
    "loglevel": "debug"
  },
  "outbounds": [
    {
      "tag": "Proxy",
      "protocol": "vmess",
      "settings": {
        "vnext": [
          {
            "address": "xxxxxxxxxxx",
            "port": 443,
            "users": [
              {
                "id": "xxxxxx"
              }
            ]
          }
        ]
      },
      "streamSettings": {
        "network": "tcp",
        "security": "tls",
        "tlsSettings": {
          "serverName": "127.0.0.1",
          "allowInsecure": true,
    "allowInsecureCiphers": false,
    "disableSystemRoot": true
        }
      }
    },
    {
      "protocol": "freedom",
      "settings": {},
      "tag": "Direct"
    }
  ],
  "routing": {
    "rules": [
      {
        "type": "field",
        "outboundTag": "Proxy",
        "ip": [
          "0.0.0.0/0"
        ]
      }
    ]
  }
}
```
## 下载

[下载页面](https://github.com/happycbbboy/v2cbb-android/releases)

## 未来计划

- 页面优化:页面配置更加好看，更加人性化
- 路由支持:IP段走vpn、某些软件走vpn和某些软件不走vpn
- 广告过滤:根据域名、ip、url过滤广告
- 抓包:通过我们的流量我们实现抓包并呈现在页面上
- 日志:可以把日志显示到页面，分享到微信，以便用户排查
- 导入流量策略:支持二维码导入，粘贴板导入，url导入
- ios支持
- macos支持
- windows支持

## 免责声明

使用此程序请遵守当地的法律法规，禁止滥用、恶意使用，触犯法律所造成的问题均由使用者承担。

## 合作

如果您有任何vpn等相关问题，我们都可以帮您解答，同样我们也可以给您提供vpn的sdk，包括定制版本和标准版本

包括标准协议隧道，自定义加密隧道等方案，您可以通过 happycbbboy@gmail.com 邮箱和我们联系

## 支持

我是 happycbbboy 一直利用业余时间做vpn客户端和服务端

如果您想支持我，可以在[buymeacoffee](https://www.buymeacoffee.com/happycbbboy)捐助我，您的任何一点捐助都是对我莫大的支持

buymeacoffee是一个将开发人员、设计师、艺术家等与其社区联系起来的网站，以便人们表达对他们工作的赞赏。

## License

[The MIT License (MIT)](https://github.com/happycbbboy/v2cbb-android/blob/main/LICENSE)

## Star History

<a href="https://star-history.com/#happycbbboy/v2cbb-android&Date">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=happycbbboy/v2cbb-android&type=Date&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=happycbbboy/v2cbb-android&type=Date" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=happycbbboy/v2cbb-android&type=Date" />
  </picture>
</a>

