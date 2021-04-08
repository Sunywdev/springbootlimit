package com.sunyw.xyz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2021/4/8 10:30
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorEnum {
    CODE("code","响应码"),
    DESC("desc","响应信息"),
    SERVER_ERROR("9999","服务暂时不可用"),
    TOO_FAST("9998","访问次数太快了,请稍后再访问!"),
    SUCCESS("0000","请求成功"),
    TIME_OUT("1250","服务请求超时");
    private String code;
    private String desc;
}
