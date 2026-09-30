// ! Bu araç @keyiflerolsun tarafından | @KekikAkademi için yazılmıştır.

package com.dolus.flixie.extractors

open class OkRuSSL : Odnoklassniki() {
    override var name    = "OkRuSSL"
    override var mainUrl = "https://ok.ru"
}

open class OkRuHTTP : Odnoklassniki() {
    override var name    = "OkRuHTTP"
    override var mainUrl = "http://ok.ru"
}

class OkRuSSLMobile : OkRuSSL() {
    override var mainUrl = "https://m.ok.ru"
}

class OkRuHTTPMobile : OkRuHTTP() {
    override var mainUrl = "http://m.ok.ru"
}
