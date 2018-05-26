/**
 * Copyright (C) 2018 by George Vrynios
 * This project was made under the supervision of Udacity
 * in the Android Developer Nanodegree Program
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aiassoft.bakingapp.widgets;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.aiassoft.bakingapp.MyApp;

/**
 * Created by gvryn on 20/05/18.
 */

public class ListviewRemoteViewsService extends RemoteViewsService {

    private static final String LOG_TAG = MyApp.APP_TAG + ListviewRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d(LOG_TAG, "service onGetViewFactory, return the ListviewRemoteViewsFactory");

        return new ListviewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
