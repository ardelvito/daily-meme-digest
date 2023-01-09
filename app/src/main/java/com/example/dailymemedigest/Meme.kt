package com.example.dailymemedigest

import java.util.Date

data class Meme(var id: Int,
                var url: String,
                var top_text: String,
                var bot_text: String,
                var users_id: Int,
                var total_likes: Int,
                var created_at: String,
                var top_text_color: String,
                var bot_text_color: String,
                var total_komen: Int,
                var expandable: Boolean = false,
                var username: String,
                var private: Int,
                var day: Int,
                var hours: Int
)