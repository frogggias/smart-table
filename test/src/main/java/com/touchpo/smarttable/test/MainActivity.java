package com.touchpo.smarttable.test;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.frogggias.smarttable.activity.SmartTableActivity;
import com.frogggias.smarttable.filter.ColumnFilterCursorProvider;
import com.frogggias.smarttable.filter.ColumnFilterProvider;
import com.frogggias.smarttable.filter.SimpleColumnCursorFilterProvider;
import com.frogggias.smarttable.provider.SmartTableColumn;
import com.frogggias.smarttable.provider.SmartTableProvider;

public class MainActivity extends SmartTableActivity {

    @Override
    protected SmartTableProvider getSmartTableProvider() {
        SmartTableColumn.Factory factory = new SmartTableColumn.Factory();
        factory.setDefaultSearchable(true);
        factory.setDefaultSortable(true);

        // Filtrable column
        SmartTableColumn name1 = factory.create(ContactsContract.Contacts.DISPLAY_NAME);
        ColumnFilterCursorProvider name1FilterProvider = new SimpleColumnCursorFilterProvider(
                ContactsContract.Contacts.CONTENT_URI,
                ContactsContract.Contacts.DISPLAY_NAME
        );
        name1.setFilterProvider(name1FilterProvider);

        SmartTableColumn name2 = factory.create(ContactsContract.Contacts.DISPLAY_NAME);
        name2.setSortable(false);

        return
            new SmartTableProvider.Builder(ContactsContract.Contacts.CONTENT_URI)
                .addColumn(name1)
                .addColumn(name2)
                .build();
    }
}
