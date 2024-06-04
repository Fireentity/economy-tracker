package it.unipd.dei.music_application.interfaces

import it.unipd.dei.music_application.models.Category

interface OnItemClickListener {
    fun onItemClick(category: Category)
}