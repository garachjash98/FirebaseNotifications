package com.example.firebasenotificationdemo.preference

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    companion object {
        private const val PREF_NAME = "firebase_notification_demo"

        //Preference Keys
        const val SUBS_TOPIC = "topic_subs_done"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Helper function to edit preferences with a clear commit/apply choice
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    // Put methods for various data types
    fun putString(key: String, value: String?) {
        prefs.edit { it.putString(key, value) }
    }

    fun putInt(key: String, value: Int) {
        prefs.edit { it.putInt(key, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit { it.putBoolean(key, value) }
    }

    // Get methods for various data types with default values
    fun getString(key: String, defaultValue: String? = null): String? =
        prefs.getString(key, defaultValue)

    fun getInt(key: String, defaultValue: Int = 0): Int =
        prefs.getInt(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        prefs.getBoolean(key, defaultValue)

    // Clear all preferences
    fun clear() {
        prefs.edit { it.clear() }
    }
}