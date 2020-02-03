package com.jordansilva.map4.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpFoursquareInterceptor : Interceptor {

    companion object {
        const val VERSION = "20180323"
        const val FOURSQUARE_CLIENT_ID = "BXRUJ2F5NHKRUOKG4KZFVOT2VLAJ2GLDVG3SP50DQQFC2C32"
        const val FOURSQUARE_CLIENT_SECRET = "H0AAVX1J4ENUVK3H5HDAFC4AGUP1AF30LKHXQWN4GS5E2EOD"

        const val FOURSQUARE_CLIENT_ID2 = "LDJ555F3TFALNLL4BL4HFTNOAIVZ1EUIZNYTMCCJ2BSKNSTC"
        const val FOURSQUARE_CLIENT_SECRET2 = "DM3YQKFDIP441WMRPE1JLPAECLQBTQNDIN4QCGCBTPQ1MWWX"

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = newRequest(original)
        return chain.proceed(request)
    }

    private fun newRequest(original: Request): Request {
        val originalUrl = original.url

        //Replacing Foursquare URL with API version and Client keys
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("client_id", FOURSQUARE_CLIENT_ID)
            .addQueryParameter("client_secret", FOURSQUARE_CLIENT_SECRET)
            .addQueryParameter("v", VERSION)
            .build()

        return original.newBuilder()
            .url(newUrl)
            .build()
    }

}