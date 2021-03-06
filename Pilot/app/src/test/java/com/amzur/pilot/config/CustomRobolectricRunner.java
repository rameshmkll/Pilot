package com.amzur.pilot.config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.amzur.pilot.BuildConfig;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

public class CustomRobolectricRunner extends RobolectricGradleTestRunner {

    private static final int MAX_SDK_LEVEL = 21;

    private int[] sdk = {21};

    public CustomRobolectricRunner(Class<?> klass) throws InitializationError {

        super(klass);

    }

    @Override

    public Config getConfig(Method method) {

        Config config = super.getConfig(method);

        /*

        Fixing up the Config:

        * SDK can not be higher than 21

        * constants must point to a real BuildConfig class

         */

        config = new Config.Implementation(sdk,

                config.manifest(),

                config.qualifiers(),

                config.packageName(),

                config.resourceDir(),

                config.assetDir(),

                config.shadows(),

                config.application(),

                config.libraries(),

                ensureBuildConfig(config.constants()));


    return config;
}

private Class<?> ensureBuildConfig(Class<?> constants) {
    if (constants == Void.class) return BuildConfig.class;
    return constants;
}

private int ensureSdkLevel(int sdkLevel) {
    if (sdkLevel > MAX_SDK_LEVEL) return MAX_SDK_LEVEL;
    if (sdkLevel <= 0) return MAX_SDK_LEVEL;
    return sdkLevel;
}
    public static void startFragment( Fragment fragment )
    {
        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
    }

}