![avatar](/img/routelimit.jpg)
- 网关分布式限流实现,基于redis+lua
- 1.基于redis令牌桶形式
- 2.限流预警
- 3.异步日志链路追踪
## 使用方式
## maven依赖
```
<dependency>
  <groupId>com.github.sunywdev</groupId>
  <artifactId>routelimit</artifactId>
  <version>1.0.1</version>
</dependency>
```
### 针对接口配置固定限流数量
```
@GetMapping("/logerror")
@Log(name = "限流测试")
@RouteLimiter(acquiredQuantity = 1,burstCapacity = 2,repleniShrate = 1)
public Map<String, Object> logError() {
    Map<String, Object> respMap = new HashMap<>();
    respMap.put("code", "200");
    respMap.put("msg", "success");
    return respMap;
}
```
### 全局限流,根据请求接口地址进行流量限制
```
route:
  #每秒产生的令牌数量
  replenishrate: 1
  #桶内令牌容量
  burstcapacity: 10
  #每次获取令牌数量
  acquiredquantity: 1
```
### 邮件通知
```
mail:
    #协议地址
    host: smtp.163.com
    #发送人邮箱
    username:
    #授权号 authpsd
    password:
 #发送人邮箱
  formmailaddress:
  #服务异常通知人邮箱,多个使用 , 分隔开
  sendmailaddress:
```
### 日志记录
- route.startlog :true(开启),默认关闭,开启时需要初始化根目录下db文件夹内log_info.sql初始化



# routelimit
-Gateway distributed current limiting implementation, based on redis+lua
- 1. Based on redis token bucket form
- 2. Current limit warning
- 3. Asynchronous log link tracking
## How to use
## maven dependency
```
<dependency>
  <groupId>com.github.sunywdev</groupId>
  <artifactId>routelimit</artifactId>
  <version>1.0.1</version>
</dependency>
```
### Based on token bucket
```
@GetMapping("/logerror")
@Log(name = "Current Limit Test")
@RouteLimiter(acquiredQuantity = 1, burstCapacity = 2, repleniShrate = 1)
public Map<String, Object> logError() {
    Map<String, Object> respMap = new HashMap<>();
    respMap.put("code", "200");
    respMap.put("msg", "success");
    return respMap;
}
```
### The number of tokens and the configuration of the token bucket
```
route:
  #Number of tokens generated per second
  replenishrate: 1
  #Token capacity in bucket
  burstcapacity: 10
  #Get the number of tokens each time
  acquiredquantity: 1
```
### E-mail notification
```
mail:
    #Protocol address
    host: smtp.163.com
    #Sender's mailbox
    username:
    #Authorization number authpsd
    password:
 #Sender's mailbox
  formmailaddress:
  #Service exception notification person mailbox, multiple uses, separate
  sendmailaddress:
```
### Logging
-route.startlog: true (open), closed by default, you need to initialize log_info.sql in the db folder under the root directory when it is turned on
