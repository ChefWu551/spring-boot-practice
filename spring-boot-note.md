# ayspringboot学习笔记

## 一、配置文件

### 1. 配置文件

#### 1.1. properties文件配置

```yaml
server.port=8081
server.address=127.0.0.1

student.name=Mountain-dev
student.age=14
student.achievement.english=98
student.achievement.math=88
student.lesson=english, math, chinese
student.dog.dogName=xiaoTianQuan
student.dog.dogColor=yellow
```

#### 1.2. yaml文件配置

```yaml
server:
  address: 127.0.0.2
  port: 8088
student:
  name: mountain-yaml-prod
  age: 20
#  ""双引号内的内容表示，不转义，根据内容自动处理
#  lesson: ["a", "b \n c"]
# ''单引号内容表示，转义，全部按照正常字符串来处理
  lesson: ["a", 'b \n c']
  achievement: {"geogrophy": 88, "history": 97}
  dog:
#    dogName: Jane
    dogColor: gray
#    属性本来命名是驼峰法dogName，yaml文件中支持松散语法绑定
    dog-name: July
#    dog_name: July
```

#### 1.3. @Value与@ConfigurationProperties

|                    | @ConfigurationProperties | @Value     |
| ------------------ | ------------------------ | ---------- |
| 功能               | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定(松散语法) | 支持                     | 不支持     |
| SpEL               | 不支持                   | 支持       |
| JSR303数据校验     | 支持                     | 不支持     |

