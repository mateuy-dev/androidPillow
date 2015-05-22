/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.view.forms.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.util.BundleUtils;

public class ShowModelActivity <T extends IdentificableModel>  extends ActionBarActivity  {
	FormFragment<T> formFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.form_activity);
		if (savedInstanceState == null) {
			formFragment = new FormFragment<T>();
			Bundle bundle = BundleUtils.copyBundle(getIntent().getExtras());
			bundle.putBoolean(FormFragment.EDIT_MODE_PARAM, false);
			formFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().add(R.id.container, formFragment).commit();
		}
	}
}