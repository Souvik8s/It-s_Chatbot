package com.example.day2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AiChatBot : AppCompatActivity() {

    private val conversationHistory = mutableListOf<M>()
    private lateinit var mainAdapter: Adp
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInputField: EditText
    private lateinit var actionSendBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ai_chat_bot)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { containerView, windowInsets ->
            val insetDimensions = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            containerView.setPadding(insetDimensions.left, insetDimensions.top, insetDimensions.right, insetDimensions.bottom)
            windowInsets
        }

        bindUIComponents()
        configureChatRecycler()
        attachClickListeners()
    }

    private fun bindUIComponents() {
        chatRecyclerView = findViewById(R.id.rvMessages)
        messageInputField = findViewById(R.id.etInput)
        actionSendBtn = findViewById(R.id.btnSend)
    }

    private fun configureChatRecycler() {
        mainAdapter = Adp(conversationHistory)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = mainAdapter
    }

    private fun attachClickListeners() {
        val geminiEngine = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "YOUR_API_KEY"
        )

        actionSendBtn.setOnClickListener {
            val queryText = messageInputField.text.toString().trim()
            if (queryText.isNotEmpty()) {
                pushNewMessage(M(messageText = queryText, fromUser = true))
                messageInputField.text.clear()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val aiResult = geminiEngine.generateContent(queryText)
                        val replyContent = aiResult.text ?: "No response"
                        withContext(Dispatchers.Main) {
                            pushNewMessage(M(messageText = replyContent, fromUser = false))
                        }
                    } catch (err: Exception) {
                        val failMsg = err.localizedMessage ?: "An error occurred"
                        withContext(Dispatchers.Main) {
                            pushNewMessage(M(messageText = failMsg, fromUser = false))
                        }
                    }
                }
            }
        }
    }

    private fun pushNewMessage(msgItem: M) {
        conversationHistory.add(msgItem)
        mainAdapter.notifyItemInserted(conversationHistory.size - 1)
        chatRecyclerView.scrollToPosition(conversationHistory.size - 1)
    }
}