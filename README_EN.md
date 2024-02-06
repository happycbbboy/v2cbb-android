# v2cbb-android
V2Ray client for Android, capable of implementing anonymous internet access, remote access, VPN, and other solutions

![256](./assets/256.png)

English | [简体中文](README_EN.md)


## use

Write [v2ray](https://v2ray.com/) config in the proxy configure,You only need to write outbound and route to the config configuration file
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

## Download

[Download page](https://github.com/happycbbboy/v2cbb-android/releases)

## Features

- Import traffic strategy: configure page optimization
- Import traffic strategy: supports QR code import, pasteboard import, and url import
- Ad filtering: filter ads based on domain name, IP, and URL
- Packet capture: We capture the passing traffic and display it on the page
- Log: You can display the log on the page and export it to assist in troubleshooting.
- ios support
- macos support
- windows support

## Disclaimers

Please comply with local laws and regulations when using this program. Misuse and malicious use are prohibited, and any problems caused by violating the law will be borne by the user.

## cooperation

you can use happycbbboy@gmail.com Contact us via email

## support

I am Happy Babboy and I have been using my spare time to work on VPN clients and servers

If you want to support me, you can go to [buymeacoffee]( https://www.buymeacoffee.com/happycbbboy) Donate to me. Any donation you make is a great support for me

Buymeacoffee is a website that connects developers, designers, artists, and their communities to express appreciation for their work.

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

