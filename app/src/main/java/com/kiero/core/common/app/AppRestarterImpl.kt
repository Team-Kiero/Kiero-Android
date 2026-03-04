package com.kiero.core.common.app

import android.content.Context
import android.content.Intent
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.system.exitProcess

class AppRestarterImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppRestarter {

    override fun restartApp() {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            } ?: return

        context.startActivity(intent)
        killCurrentProcess()
    }

    private fun killCurrentProcess() {
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}
