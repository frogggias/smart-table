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
                .setDefaultSearchable(true)
                .setDefaultSortable(true)
                .addColumn(ContactsContract.Contacts.DISPLAY_NAME, "name", 0, null, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.DISPLAY_NAME)
                    .addColumn(ContactsContract.Contacts.DISPLAY_NAME, "name-not-sort")
                .build();
    }
}
