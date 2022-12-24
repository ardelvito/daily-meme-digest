package com.example.dailymemedigest

import java.util.Date

data class Meme(val id: Int,
                val url: String,
                val top_text: String,
                val bot_text: String,
                val users_id: Int,
                val total_likes: Int,
                val created_at: String,
                val top_text_color: String,
                val bot_text_color: String,
                val total_komen: Int,
                var expandable: Boolean = false,
                val username: String,
                val day: Int,
                val hours: Int
)