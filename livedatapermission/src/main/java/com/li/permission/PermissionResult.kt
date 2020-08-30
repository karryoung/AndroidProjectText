package com.li.permission

/**
 *
 * @CreateDate:     2020/6/4 10:29
 * @Description:     权限请求结果
 * @Author:         李想
 */
sealed class PermissionResult {
    object Grant : PermissionResult()
    class Deny(val permission: Array<String>) : PermissionResult()
    class Rationale(val permission: Array<String>) : PermissionResult()
}