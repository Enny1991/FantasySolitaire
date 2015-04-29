package com.eneaceolini.fantasysolitaire;

import java.util.List;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	protected static final boolean ALWAYS_SIMPLE_PREFS = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */


    private SharedPreferences prefs;
	void setupSimplePreferencesScreen() {
		if (isSimplePreferences(this)) {
			return;
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'general' preferences.
        //noinspection deprecation
        addPreferencesFromResource(R.xml.pref_general);
		// Add 'notifications' preferences, and a corresponding header.
        PreferenceCategory appsCategory = new PreferenceCategory(this);
        appsCategory.setTitle("POWERS");

        LSice ice = new LSice(this);
        CharSequence[] entries = {"ICE1","ICE2","ICE3"} ;
        CharSequence[] entriesValues = {"-1","0","1"} ;
        ice.setEntries(entries);
        ice.setEntryValues(entriesValues);
        ice.setTitle("ICE");
        ice.setKey("donachiave");


        LSfire fire = new LSfire(this);
        CharSequence[] entries5 = {"FIRE1","FIRE2","FIRE3"} ;
        CharSequence[] entriesValues5 = {"-1","0","1"} ;
        fire.setEntries(entries5);
        fire.setEntryValues(entriesValues5);
        fire.setTitle("FIRE");
        fire.setKey("fire");


        LSearth earth = new LSearth(this);
        CharSequence[] entries4 = {"EARTH1","EARTH2","EARTH3"} ;
        CharSequence[] entriesValues4 = {"-1","0","1"} ;
        earth.setEntries(entries4);
        earth.setEntryValues(entriesValues4);
        earth.setTitle("EARTH");
        earth.setKey("earth");

        LStime time = new LStime(this);
        CharSequence[] entries3 = {"TIME1","TIME2","TIME3"} ;
        CharSequence[] entriesValues3 = {"-1","0","1"} ;
        time.setEntries(entries3);
        time.setEntryValues(entriesValues3);
        time.setTitle("TIME");
        time.setKey("time");


        LSthunder thunder = new LSthunder(this);
        CharSequence[] entries2 = {"THUNDER1","THUNDER2","THUNDER3"} ;
        CharSequence[] entriesValues2 = {"-1","0","1"} ;
        thunder.setEntries(entries2);
        thunder.setEntryValues(entriesValues2);
        thunder.setTitle("THUNDER");
        thunder.setKey("thunder");
        getPreferenceScreen().addPreference(appsCategory);


        appsCategory.addPreference(ice);
        appsCategory.addPreference(fire);
        appsCategory.addPreference(thunder);
        appsCategory.addPreference(earth);
        appsCategory.addPreference(time);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//		PreferenceCategory fakeHeader = new PreferenceCategory(this);
//		fakeHeader.setTitle("Powers Description");
//		getPreferenceScreen().addPreference(fakeHeader);
//		addPreferencesFromResource(R.xml.powers);
		
		 
//
//		// Add 'data and sync' preferences, and a corresponding header.
//		fakeHeader = new PreferenceCategory(this);
//		fakeHeader.setTitle(R.string.pref_header_data_sync);
//		getPreferenceScreen().addPreference(fakeHeader);
//		addPreferencesFromResource(R.xml.pref_data_sync);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		
		
		//bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
		//bindPreferenceSummaryToValue(findPreference("sync_frequency"));
	}

	public class LSice extends ListPreference{
		 
	    public LSice(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	 
	    public LSice(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void setValue(String value) {
	        super.setValue(value);
            switch (value) {
                case "-1":
                    super.setTitle("ICE1");
                    break;
                case "0":
                    super.setTitle("ICE2");
                    break;
                case "1":
                    super.setTitle("ICE3");
                    break;
                default:
                    super.setTitle(value);
                    break;
            }
	        setSummary(value);
	    }
	 
	    @Override
	    public void setSummary(CharSequence summary) {
	    	Log.d("summary",summary.toString());
	    	if (summary.equals("-1")) super.setSummary(R.string.ice1_1_1);
	    	else  if (summary.equals("0")) super.setSummary(R.string.ice2_1_1);
	    	else if (summary.equals("1")) super.setSummary(R.string.ice3_1_1);
	    	else super.setSummary(summary);
	    }
	}
	
	public class LSfire extends ListPreference{
		 
	    public LSfire(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	 
	    public LSfire(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void setValue(String value) {
	        super.setValue(value);
            switch (value) {
                case "-1":
                    super.setTitle("FIRE1");
                    break;
                case "0":
                    super.setTitle("FIRE2");
                    break;
                case "1":
                    super.setTitle("FIRE3");
                    break;
                default:
                    super.setTitle(value);
                    break;
            }
	        setSummary(value);
	    }
	 
	    @Override
	    public void setSummary(CharSequence summary) {
	    	Log.d("summary",summary.toString());
	    	if (summary.equals("-1")) super.setSummary(R.string.fire1_1_1);
	    	else  if (summary.equals("0")) super.setSummary(R.string.fire1_1_1);
	    	else if (summary.equals("1")) super.setSummary(R.string.fire1_1_1);
	    	else super.setSummary(summary);
	    }
	}
	
	public class LSearth extends ListPreference{
		 
	    public LSearth(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	 
	    public LSearth(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void setValue(String value) {
	        super.setValue(value);
            switch (value) {
                case "-1":
                    super.setTitle("EARTH1");
                    break;
                case "0":
                    super.setTitle("EARTH2");
                    break;
                case "1":
                    super.setTitle("EARTH3");
                    break;
                default:
                    super.setTitle(value);
                    break;
            }
	        setSummary(value);
	    }
	 
	    @Override
	    public void setSummary(CharSequence summary) {
	    	Log.d("summary",summary.toString());
	    	if (summary.equals("-1")) super.setSummary(R.string.earth1_1_1);
	    	else  if (summary.equals("0")) super.setSummary(R.string.earth2_1_1);
	    	else if (summary.equals("1")) super.setSummary(R.string.earth3_1_1);
	    	else super.setSummary(summary);
	    }
	}
	
	
	
	
	
	public class LStime extends ListPreference{
		 
	    public LStime(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	 
	    public LStime(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void setValue(String value) {
	        super.setValue(value);
            switch (value) {
                case "-1":
                    super.setTitle("TIME1");
                    break;
                case "0":
                    super.setTitle("TIME2");
                    break;
                case "1":
                    super.setTitle("TIME3");
                    break;
                default:
                    super.setTitle(value);
                    break;
            }
	        setSummary(value);
	    }
	 
	    @Override
	    public void setSummary(CharSequence summary) {
	    	Log.d("summary",summary.toString());
	    	if (summary.equals("-1")) super.setSummary(R.string.time1_1_1);
	    	else  if (summary.equals("0")) super.setSummary(R.string.time1_1_1);
	    	else if (summary.equals("1")) super.setSummary(R.string.time1_1_1);
	    	else super.setSummary(summary);
	    }
	}
	
	public class LSthunder extends ListPreference{
		 
	    public LSthunder(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	 
	    public LSthunder(Context context) {
	        super(context);
	    }
	 
	    @Override
	    public void setValue(String value) {
	        super.setValue(value);
            switch (value) {
                case "-1":
                    super.setTitle("THUNDER1");
                    break;
                case "0":
                    super.setTitle("THUNDER2");
                    break;
                case "1":
                    super.setTitle("THUNDER3");
                    break;
                default:
                    super.setTitle(value);
                    break;
            }
	        setSummary(value);
	    }
	 
	    @Override
	    public void setSummary(CharSequence summary) {
	    	Log.d("summary",summary.toString());
	    	if (summary.equals("-1")) super.setSummary(R.string.thunder1_1_1);
	    	else  if (summary.equals("0")) super.setSummary(R.string.thunder2_1_1);
	    	else if (summary.equals("1")) super.setSummary(R.string.thunder3_1_1);
	    	else super.setSummary(summary);
	    }
	}
	
	
	
	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                && isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else if (preference instanceof RingtonePreference) {
				// For ringtone preferences, look up the correct display value
				// using RingtoneManager.
				if (TextUtils.isEmpty(stringValue)) {
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent);

				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if (ringtone == null) {
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);
					} else {
						// Set the summary to reflect the new ringtone display
						// name.
						String name = ringtone
								.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("example_text"));
			bindPreferenceSummaryToValue(findPreference("example_list"));
		}
	}

	/**
	 * This fragment shows notification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends
			PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_notification);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
		}
	}

	/**
	 * This fragment shows data and sync preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_data_sync);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("sync_frequency"));
		}
	}
}
