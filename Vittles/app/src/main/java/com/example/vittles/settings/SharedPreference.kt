package com.example.vittles.settings


import android.content.Context
import android.content.SharedPreferences

/**
 * Shared preference class used to store simple data in the internal storage privately for each application
 * that will be used to save setting preferences.
 *
 *@author Fethi Tewelde
 *
 * @property context application context
 */

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "Settings"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Saves shared preference
     *
     * @param KEY_NAME The key name of the preference.
     * @param text The string value of the key.
     */
     fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor!!.commit()
    }

    /**
     * Saves shared preference
     *
     * @param KEY_NAME The key name of the preference.
     * @param value The Int value of the key.
     */
    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }

    /**
     * Saves shared preference
     *
     * @param KEY_NAME The key name of the preference.
     * @param status The boolean value of the key.
     */
    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putBoolean(KEY_NAME, status!!)

        editor.commit()
    }

    /**
     * Gets the string value of the shared preference
     *
     * @param KEY_NAME The key name of the preference.
     */
    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }

    /**
     * Gets the Int value of the shared preference
     *
     * @param KEY_NAME The key name of the preference.
     */
    fun getValueInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, 0)
    }

    /**
     * Gets the boolean value of the shared preference
     *
     * @param KEY_NAME The key name of the preference.
     */
    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {

        return sharedPref.getBoolean(KEY_NAME, defaultValue)

    }

    /**
     * Clears the entire shared preferences
     * To remove a specific data
     *
     */
    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        editor.clear()
        editor.commit()
    }

    /**
     * Removes a specific data
     *
     * @param KEY_NAME The key name of the preference.
     *
     */
    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }
}