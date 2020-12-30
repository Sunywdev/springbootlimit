# springbootlimit
基于springboot+redis+lua实现的限流,一个是基于注解的形式一个是基于包路径扫描的
## 使用方式
### 如果使用注解的形式需要添加
```
 /**
     * 基于注解形式的限流
     *
     * @param param
     * @return
     */
    @GetMapping("/send/{param}")
    @Limiter(count = 1, method = "send", time = 30)
    public String send(@PathVariable String param) {
        return param;
    }
```
### 如果使用包扫描拦截,修改想要拦截的包位置
```
 @Pointcut("execution(public * com.sunyw.xyz.controller.*.*(..))")
    public void cut() {
    }
```
### 令牌数量以及令牌桶的配置
```
route:
  #每秒产生的令牌数量
  replenishrate: 1
  #桶内令牌容量
  burstcapacity: 10
  #每次获取令牌数量
  acquiredquantity: 1
```
### 此外还搭配了邮件通知
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
### 全局异常处理
```
@ControllerAdvice
@Slf4j
public class LimitExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public Object invoke(Exception https) {
        log.info ("开始执行[Exception]异常捕获");
        ModelAndView modelAndView = new ModelAndView (new MappingJackson2JsonView ());
        modelAndView.addObject ("code", HttpStatus.INTERNAL_SERVER_ERROR.toString ());
        modelAndView.addObject ("msg", https.getMessage ());
        return modelAndView;
    }

    /**
     * LimitException 全局异常捕获
     *
     * @param https
     * @return
     */
    @ExceptionHandler(value = LimitException.class)
    public Object invoke(LimitException https) {
        log.info ("开始执行[LimitException]异常捕获");
        ModelAndView modelAndView = new ModelAndView (new MappingJackson2JsonView ());
        modelAndView.addObject ("code", https.getCode ());
        modelAndView.addObject ("msg", https.getDesc ());
        return modelAndView;
    }
}
```
