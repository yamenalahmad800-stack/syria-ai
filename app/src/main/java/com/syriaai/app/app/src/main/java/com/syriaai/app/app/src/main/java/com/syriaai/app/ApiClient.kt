package com.syriaai.app

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * ⚠️ ملاحظة أمان مهمة:
 * لا تضع مفتاح الـ API الحقيقي هنا مباشرة داخل التطبيق أبداً.
 * أي شخص يفكك التطبيق (decompile) رح يقدر يسرق المفتاح ويستخدمه على حسابك.
 *
 * الطريقة الصحيحة: تبني خادم وسيط بسيط (backend) يخزّن المفتاح بأمان،
 * والتطبيق يتواصل مع الخادم فقط. هاد الملف حالياً مثال تجريبي للبنية،
 * لازم تستبدل BASE_URL بعنوان الخادم الوسيط تبعك قبل النشر الفعلي.
 */
object ApiClient {

    // 🔧 غيّر هذا لعنوان الخادم الوسيط تبعك (backend)
    private const val BASE_URL = "https://YOUR-BACKEND-SERVER.com/api/chat"

    private val client = OkHttpClient()

    fun sendMessage(userMessage: String, callback: (String) -> Unit) {
        val json = JSONObject().apply {
            put("message", userMessage)
        }

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(BASE_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("⚠️ حدث خطأ بالاتصال: ${e.message}")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val responseBody = response.body?.string() ?: "{}"
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val reply = jsonResponse.optString("reply", "لم يصل رد.")
                    callback(reply)
                } catch (e: Exception) {
                    callback("⚠️ تعذّر قراءة الرد.")
                }
            }
        })
    }
}
