package com.li.androidprojecttext.kotlin.net.exception

/**
 * 服务器返回的错误类型
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}