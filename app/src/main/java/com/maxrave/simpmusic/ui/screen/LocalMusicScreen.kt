package com.maxrave.simpmusic.ui.screen

import android.net.Uri
import android.content.Context
import android.provider.MediaStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocalMusicScreen(context: Context, onSongClick: (Uri) -> Unit) {
    var songs by remember { mutableStateOf(listOf<Pair<String, Uri>>()) }

    // Carregar músicas do dispositivo
    LaunchedEffect(Unit) {
        val musicList = mutableListOf<Pair<String, Uri>>()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media._ID
        )
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val id = it.getLong(idIndex)
                val uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                musicList.add(title to uri)
            }
        }
        songs = musicList
    }

    // UI
    Scaffold(
        topBar = { TopAppBar(title = { Text("Músicas Locais") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(songs) { song ->
                Text(
                    text = song.first,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSongClick(song.second) }
                        .padding(16.dp)
                )
            }
        }
    }
}