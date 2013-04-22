package com.newsrss.Feed;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 22.04.13
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class MailSender {

     public static void send(Activity activity, Object target) {
         String subject = "";
         String body = "";

         if (target instanceof Article) {
             subject = ((Article) target).getTitle();
             body = ((Article) target).getDescription();
         }
         else
         if (target instanceof Podcast) {
             subject = ((Podcast) target).getTitle();
             body = ((Podcast) target).getDescription();
         }
         else
         if (target instanceof Job) {
             subject = ((Job) target).getTitle();
             body = ((Job) target).getDescription();
         }
         else {
             // We must send only Article, Podcast or Job
             return;
         }

         Intent iMailSender = new Intent(Intent.ACTION_SEND);
         iMailSender.setType("message/rfc822");
         iMailSender.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
         iMailSender.putExtra(Intent.EXTRA_SUBJECT, subject);
         iMailSender.putExtra(Intent.EXTRA_TEXT   , body);

         try {
             activity.startActivity(Intent.createChooser(iMailSender, "Send mail..."));
         }
         catch (android.content.ActivityNotFoundException ex) {
             Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
         }
     }
}
