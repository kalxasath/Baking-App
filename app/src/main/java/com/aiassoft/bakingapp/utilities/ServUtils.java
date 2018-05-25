package com.aiassoft.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;

import com.aiassoft.bakingapp.MyApp;

import static com.aiassoft.bakingapp.utilities.IntentUtils.convertImplicitIntentToExplicitIntent;

/**
 * Created by gvryn on 25/05/18.
 */

public class ServUtils {

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
