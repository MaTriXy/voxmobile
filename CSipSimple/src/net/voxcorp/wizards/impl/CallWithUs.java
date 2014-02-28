/**
 * Copyright (C) 2010-2012 Regis Montoya (aka r3gis - www.r3gis.fr)
 * This file is part of CSipSimple.
 *
 *  CSipSimple is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  If you own a pjsip commercial license you can also redistribute it
 *  and/or modify it under the terms of the GNU Lesser General Public License
 *  as an android library.
 *
 *  CSipSimple is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CSipSimple.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.voxcorp.wizards.impl;

import android.text.InputType;

import net.voxcorp.api.SipConfigManager;
import net.voxcorp.api.SipProfile;
import net.voxcorp.utils.PreferencesWrapper;


public class CallWithUs extends SimpleImplementation {


	@Override
	protected String getDomain() {
		return "sip.callwithus.com";
	}
	
	@Override
	protected String getDefaultName() {
		return "CallWithUs";
	}
	
	@Override
	public void fillLayout(SipProfile account) {
		super.fillLayout(account);
		accountUsername.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
	}
	
	@Override
	public SipProfile buildAccount(SipProfile account) {
		SipProfile acc = super.buildAccount(account);
		acc.reg_timeout = 120;
		acc.allow_contact_rewrite = false;
		acc.sip_stun_use = 1;
		acc.media_stun_use = 1;
		acc.ice_cfg_use = 1;
		acc.ice_cfg_enable = 0;
		acc.vm_nbr = "086";
		return acc;
	}

    @Override
    public void setDefaultParams(PreferencesWrapper prefs) {
        super.setDefaultParams(prefs);
        prefs.addStunServer("stun.sipcel.com");
        
        prefs.setPreferenceBooleanValue(SipConfigManager.ENABLE_STUN, true);
    }
	
    /* (non-Javadoc)
     * @see net.voxcorp.wizards.impl.SimpleImplementation#needRestart()
     */
    @Override
    public boolean needRestart() {
        return true;
    }
}
