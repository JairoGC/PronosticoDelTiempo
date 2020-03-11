package com.example.sunshinekotlin

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.sunshinekotlin.data.SunshinePreferences
import com.example.sunshinekotlin.sync.SunshineSyncTask
import com.example.sunshinekotlin.sync.SunshineSyncUtils

class SettingsFragment : PreferenceFragmentCompat(),
    OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.pref_general)
        val sharedPreferences = preferenceScreen.sharedPreferences
        val count: Int = preferenceScreen.preferenceCount
        for (i in 0 until count) {
            val p: Preference = preferenceScreen.getPreference(i)
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            }
        }
    }

    private fun setPreferenceSummary(preference: Preference, value: Any?) {
        val stringValue = value.toString()
        if (preference is ListPreference) {
            val listPreference: ListPreference = preference
            val prefIndex: Int = listPreference.findIndexOfValue(stringValue)
            if (prefIndex >= 0) {
                preference.summary = listPreference.entries[prefIndex]
            }
        } else preference.summary = stringValue
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        key: String
    ) {
        if (key == getString(R.string.pref_location_key)) {
            SunshinePreferences.resetLocationCoordinates(this.requireContext())
            SunshineSyncUtils.startImmediateSync(this.requireContext());
        }else if (key == getString(R.string.pref_units_key)) {
            SunshineSyncTask.refresh(this.requireContext())
        }

        val preference: Preference? = findPreference(key)
        if (null != preference) {
            if (preference !is CheckBoxPreference) {
                val value: String = sharedPreferences.getString(preference.key, "")?:""
                setPreferenceSummary(preference, value)
            }
        }
    }
}