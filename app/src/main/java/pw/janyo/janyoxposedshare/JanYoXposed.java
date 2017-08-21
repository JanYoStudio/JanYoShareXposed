package pw.janyo.janyoxposedshare;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author kiva
 */

public class JanYoXposed implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.e("JanYoXposed", "Checking for package: " + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals(HookConstant.TARGET_PACKAGE_NAME)) {
            Log.e("JanYoXposed", "Found QQ: Start hooking.");
            doHook(loadPackageParam);
            Log.e("JanYoXposed", "Hook Done.");
        }
    }

    private void doHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookConstructor(HookConstant.TARGET_CLASS_IDENTIFIER,
                loadPackageParam.classLoader, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        for (Object arg : param.args) {
                            Log.e("JanYoXposed", "beforeHookedMethod: " +
                                    "argClass: " + arg.getClass().getName() + ", " +
                                    "argValue: " + arg.toString());
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        try {
                            Object fileName = XposedHelpers.getObjectField(param.thisObject, "str_file_name");
                            Log.e("JanYoXposed", "Got file name: " + fileName.getClass().getName() + ", " + fileName.toString());
                        } catch (Throwable e) {
                            Log.e("JanYoXposed", "Failed to get object field");
                        }
                    }
                });
    }
}
