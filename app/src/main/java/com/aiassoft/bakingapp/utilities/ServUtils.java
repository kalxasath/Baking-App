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

package com.aiassoft.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;

import com.aiassoft.bakingapp.MyApp;

import static com.aiassoft.bakingapp.utilities.IntentUtils.convertImplicitIntentToExplicitIntent;

/**
 * Created by gvryn on 25/05/18.
 */

public final class ServUtils {

    private ServUtils() {
        throw new AssertionError("No Instances for you!");
    }

    public static void startService(String intentUri) {
        Intent implicitIntent = new Intent();
        implicitIntent.setAction(intentUri);
        Context context = MyApp.getContext();
        Intent explicitIntent = convertImplicitIntentToExplicitIntent(context, implicitIntent);
        if(explicitIntent != null){
            context.startService(explicitIntent);
        }
    }
}
