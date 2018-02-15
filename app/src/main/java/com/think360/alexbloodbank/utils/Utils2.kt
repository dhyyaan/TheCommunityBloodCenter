package com.think360.alexbloodbank.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.customview.BadgeDrawable

/**
 * Created by think360 on 17/11/17.
 */
object Utils2 {
    fun setBadgeCount(context: Context, icon: LayerDrawable, count: Int) {

        val badge: BadgeDrawable

        // Reuse drawable if possible
        val reuse = icon.findDrawableByLayerId(R.id.ic_badge)
        if (reuse != null && reuse is BadgeDrawable) {
            badge = reuse as BadgeDrawable
        } else {
            badge = BadgeDrawable(context)
        }

        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_badge, badge)
    }

    fun shareAppLinkViaFacebook( context : Context,urlToShareb : String ) {
        try {
            val intent1  = Intent()
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler")
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT","https://www.facebook.com/sharer/sharer.php?u=" + urlToShareb)
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent1);
        } catch ( e : Exception) {
            // If we failed (not native FB app installed), try share through SEND
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShareb
            val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context. startActivity(intent);
        }
    }
    fun getTwitterIntent(ctx: Context, shareText: String) {

        try {

        val shareIntent: Intent
        shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setClassName("com.twitter.android",
                    "com.twitter.android.PostActivity")
            shareIntent.type = "text/*"
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            ctx.startActivity(shareIntent);
        } catch ( e : Exception) {
            val tweetUrl = "https://twitter.com/intent/tweet?text=" + shareText
            val uri = Uri.parse(tweetUrl)
           val shareIntent = Intent(Intent.ACTION_VIEW, uri)
            ctx.startActivity(shareIntent);
        }
    }

}