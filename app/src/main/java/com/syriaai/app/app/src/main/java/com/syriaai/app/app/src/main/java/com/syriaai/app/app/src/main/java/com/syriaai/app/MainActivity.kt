package com.syriaai.app

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerMessages: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        findViewById<android.widget.ImageButton>(R.id.btn_menu).setOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }

        recyclerMessages = findViewById(R.id.recycler_messages)
        recyclerMessages.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messageList)
        recyclerMessages.adapter = chatAdapter

        val greetingView = findViewById<android.widget.TextView>(R.id.txt_greeting)
        val editMessage = findViewById<android.widget.EditText>(R.id.edit_message)
        val btnSend = findViewById<android.widget.ImageButton>(R.id.btn_send)
        val btnAttach = findViewById<android.widget.ImageButton>(R.id.btn_attach)
        val btnMic = findViewById<android.widget.ImageButton>(R.id.btn_mic)

        btnSend.setOnClickListener {
            val text = editMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            greetingView.visibility = android.view.View.GONE
            chatAdapter.addMessage(Message(text, isUser = true))
            recyclerMessages.scrollToPosition(messageList.size - 1)
            editMessage.text.clear()

            ApiClient.sendMessage(text) { reply ->
                runOnUiThread {
                    chatAdapter.addMessage(Message(reply, isUser = false))
                    recyclerMessages.scrollToPosition(messageList.size - 1)
                }
            }
        }

        btnAttach.setOnClickListener {
            Toast.makeText(this, "إرفاق ملف - قيد التطوير", Toast.LENGTH_SHORT).show()
        }

        btnMic.setOnClickListener {
            Toast.makeText(this, "التسجيل الصوتي - قيد التطوير", Toast.LENGTH_SHORT).show()
        }

        findViewById<android.widget.ImageButton>(R.id.btn_new_chat_top).setOnClickListener {
            startNewChat()
        }

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_new_chat -> startNewChat()
                R.id.nav_settings -> Toast.makeText(this, "الإعدادات - قيد التطوير", Toast.LENGTH_SHORT).show()
                R.id.nav_about -> Toast.makeText(this, "Syria AI - الإصدار 1.0", Toast.LENGTH_SHORT).show()
                R.id.nav_chat_recent -> Toast.makeText(this, "فتح المحادثة الأخيرة", Toast.LENGTH_SHORT).show()
                R.id.nav_chat_before_recent -> Toast.makeText(this, "فتح المحادثة قبل الأخيرة", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun startNewChat() {
        messageList.clear()
        chatAdapter.notifyDataSetChanged()
        findViewById<android.widget.TextView>(R.id.txt_greeting).visibility = android.view.View.VISIBLE
        drawerLayout.closeDrawers()
    }
}
