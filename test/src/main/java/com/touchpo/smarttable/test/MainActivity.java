package com.touchpo.smarttable.test;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.frogggias.smarttable.activity.SmartTableActivity;
import com.frogggias.smarttable.provider.SmartTableProvider;

public class MainActivity extends SmartTableActivity {

    @Override
    protected SmartTableProvider getSmartTableProvider() {
        return
            new SmartTableProvider.Builder(ContactsContract.Contacts.CONTENT_URI)
                .addColumn(ContactsContract.PRIMARY_ACCOUNT_NAME, "name")
                .build();
    }
}
