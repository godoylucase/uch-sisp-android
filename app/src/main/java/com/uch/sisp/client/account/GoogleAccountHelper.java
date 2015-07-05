package com.uch.sisp.client.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by lucas on 05/07/15.
 */
public class GoogleAccountHelper {

    public static String getPrincipalEmailAccount(Context context){
        String possibleEmail = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        return possibleEmail;
    }
}
