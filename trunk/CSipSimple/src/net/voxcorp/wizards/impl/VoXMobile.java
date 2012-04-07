package net.voxcorp.wizards.impl;

import java.util.HashMap;

import net.voxcorp.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.EditTextPreference;

import net.voxcorp.api.SipConfigManager;
import net.voxcorp.api.SipProfile;
import net.voxcorp.utils.CustomDistribution;
import net.voxcorp.utils.PreferencesWrapper;
import net.voxcorp.voxmobile.types.SipUser;
import net.voxcorp.voxmobile.utils.Consts;
import net.voxcorp.voxmobile.utils.SimpleCrypto;
import net.voxcorp.voxmobile.utils.VoXSettings;

public class VoXMobile extends BaseImplementation {

	public static final int ACCOUNT_CREATED = 2000;
	
	protected static String DISPLAY_NAME = "display_name";
	protected static String USER_NAME = "username";

	private EditTextPreference accountDisplayName;
	private EditTextPreference accountUsername;
	
	private void bindFields() {
		accountDisplayName = (EditTextPreference) parent.findPreference(DISPLAY_NAME);
		accountUsername = (EditTextPreference) parent.findPreference(USER_NAME);
	}
	
	private static String VOX_MOBILE = "voxmobile";
	private static String UUID = "uuid";
	
	public static String getUuid(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(VOX_MOBILE, Context.MODE_PRIVATE);
		String uuid = prefs.getString(UUID, "");
		return SimpleCrypto.decrypt(uuid);
	}

	public static void setUuid(Context context, String uuid) {
		SharedPreferences prefs = context.getSharedPreferences(VOX_MOBILE, Context.MODE_PRIVATE);
		String encryptedUuid = SimpleCrypto.encrypt(uuid);
		Editor editor = prefs.edit();
		editor.putString(UUID, encryptedUuid);
		editor.commit();
		return;
	}

	public static void clearUuid(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(VOX_MOBILE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.remove(UUID);
		editor.commit();
		return;
	}
	
	public static boolean isVoXMobile(String [] proxies) {
        boolean result = false;
        for (int i = 0; i < proxies.length; i++) {
        	if (proxies[i].toLowerCase().contains("voxcorp.net")) {
        		result = true;
        			break;
        	}
        }
        return result;
	}
	
	@Override
	public void fillLayout(final SipProfile account) {
		bindFields();

		accountDisplayName.setText(account.display_name);
		accountUsername.setText(account.username);
	}

	private static HashMap<String, Integer>SUMMARIES = new  HashMap<String, Integer>() {

		private static final long serialVersionUID = 1L;

	{
		put(DISPLAY_NAME, R.string.w_common_display_name_desc);
		put(USER_NAME, R.string.w_common_phone_number);
	}};
	
	@Override
	public String getDefaultFieldSummary(String fieldName) {
		Integer res = SUMMARIES.get(fieldName);
		if(res != null) {
			return parent.getString( res );
		}
		return "";
	}

	@Override
	public void updateDescriptions() {
		setStringFieldSummary(DISPLAY_NAME);
		setStringFieldSummary(USER_NAME);
	}

	public boolean canSave() {
		boolean isValid = true;
		
		isValid &= checkField(accountDisplayName, isEmpty(accountDisplayName));
		isValid &= checkField(accountUsername, isEmpty(accountUsername));

		return isValid;
	}
	
	public String getDomain() {
		if (VoXSettings.getMode() == Consts.MODE_PRODUCTION)
			return "sip.voxcorp.net";
		else
			return "sipstage.voxcorp.net";
	}
	
	protected String getDefaultName() {
		return "VoX Mobile";
	}
	
	private SipProfile updateAccount(SipProfile account, String displayName) {
		account.display_name = displayName;
		return account;
	}
	
	private SipProfile createAccount(SipProfile account, String sipUid, String sipPwd, String displayName) {
		account.display_name = "(" + sipUid.substring(0, 3) + ")" +
	                           sipUid.substring(3, 6) + "-" +
				               sipUid.substring(6);

		account.acc_id = "<sip:" + Uri.encode(sipUid) + "@"+getDomain()+">";
		
		String regUri = "sip:"+getDomain();
		account.reg_uri = regUri;
		account.proxies = new String[] { "sip:" + getDomain() } ;
		
		account.realm = "*";
		account.username = sipUid;
		account.data = sipPwd;
		account.scheme = SipProfile.CRED_SCHEME_DIGEST;
		account.datatype = SipProfile.CRED_DATA_PLAIN_PASSWD;

		account.reg_timeout = 900;	
		account.transport = SipProfile.TRANSPORT_UDP;
		account.vm_nbr = sipUid;
		account.wizard = "VOXMOBILE";
		
		return account;
	}

	@Override
	public SipProfile buildAccount(SipProfile account) {
		return updateAccount(account, accountDisplayName.getText());
	}
	
	public SipProfile buildAccount(SipProfile account, SipUser sipUser) {
		return createAccount(account, 
				sipUser.username, 
				sipUser.password, 
				sipUser.displayname);
	}

	@Override
	public int getBasePreferenceResource() {
		return R.xml.w_voxmobile_preferences;
	}

	@Override
	public boolean needRestart() {
		return false;
	}

	@Override
	public void setDefaultParams(PreferencesWrapper prefs) {
		super.setDefaultParams(prefs);
		prefs.setPreferenceStringValue(SipConfigManager.USER_AGENT, CustomDistribution.getUserAgent());
	}
		
}