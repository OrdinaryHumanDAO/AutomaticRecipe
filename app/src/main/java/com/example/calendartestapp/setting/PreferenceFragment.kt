package com.example.calendartestapp.setting

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.calendartestapp.R
import android.widget.TextView
import androidx.preference.PreferenceCategory


class PreferenceFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // res/xml/preferences.xml ファイルに従って設定画面を構成
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val nf: SwitchPreferenceCompat? = findPreference("notificationFlag")
        val nt: SwitchPreferenceCompat? = findPreference("notificationTime")

        nt?.isVisible = nf?.isChecked == true

        findPreference<SwitchPreferenceCompat>("notificationFlag")?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                nt?.isVisible = newValue == true
                true
            }
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val nf: SwitchPreferenceCompat? = findPreference("notificationFlag")
        val nt: SwitchPreferenceCompat? = findPreference("notificationTime")
        nt?.isVisible = preference == nf && newValue == true
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.setting).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

}
