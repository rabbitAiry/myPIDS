package com.airy.mypids.repository

import com.airy.mypids.data.Station
import com.airy.mypids.data.TranslateResult
import com.airy.mypids.utils.responseViaOkHttp
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.and
import java.security.MessageDigest
import java.util.*

const val httpsSite = "https://fanyi-api.baidu.com/api/trans/vip/translate/"

/**
 * 秘钥需要被隐藏起来
 */
class TranslationRepository {

    /**
     * 假设源语言总是中文
     */
    suspend fun translateWholeStationList(stations: StationListInfo, targetLanguage: String = "en"){
        return withContext(Dispatchers.IO){
            val url = getURL(concatStationTexts(stations.stations))
            val result = Gson().fromJson(responseViaOkHttp(url), TranslateResult::class.java)
        }
    }

    private fun concatStationTexts(list: List<Station>): String {
        return StringBuilder().run {
            list.forEach { append(it.name).append("站;") }
            deleteCharAt(length - 1)
            toString()
        }
    }

    private fun getURL(text: String): String {
        val salt = Random().nextInt(100)
        val sign = getMD5(appId + text + salt + key)
        return "$httpsSite?appid=$appId&q=$text&from=zh&to=en&salt=$salt&sign=$sign"
    }

    private fun getMD5(text: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(text.toByteArray())
        val builder = StringBuilder()
        for (bit in digest) {
            if (bit.and(0xFF) < 0x10) {
                builder.append("0")
            }
            builder.append(Integer.toHexString(bit.and(0xFF)))
        }
        return builder.toString()
    }
}