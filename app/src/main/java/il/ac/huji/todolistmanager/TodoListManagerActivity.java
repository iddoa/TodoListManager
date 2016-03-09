package il.ac.huji.todolistmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoListManagerActivity extends AppCompatActivity {

    private List<String> values = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // defining the listview
        final ListView listView = (ListView) findViewById(R.id.lstTodoItems);
        // the array of the list that keeps the strings

        // creating the adapter
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                TextView textView=(TextView) view;
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    textView.setTextColor(Color.BLUE);
                }
                else
                {
                    // Set the background color for alternate row/item
                    textView.setTextColor(Color.RED);
                }
                return view;
            }
        };
        //setting the adapter to the listview
        listView.setAdapter(adapter);
        // registering the listview to the context menu
        registerForContextMenu(listView);
        final MenuItem menuitem = (MenuItem)
                findViewById(R.id.action_settings);
        onOptionsItemSelected(menuitem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    /**
     * the function that adds the items to the list when the add button
     * in the menu is clicked (and the input isnt empty)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        EditText editTextName = (EditText) findViewById(R.id.edtNewItem);
        String itemInput = editTextName.getText().toString();
        if (!itemInput.matches("")) {
            values.add(itemInput);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    /**
     * the context menu that opens when an item in the list is pressed for a long time
     * and opens an option to delete the item from the list
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        // showing the item chosen in the title of the context menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(values.get(info.position));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.delete_item){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int pos = info.position;
            values.remove(pos);
            adapter.notifyDataSetChanged();
        }
        else {
            return false;
        }

        return true;
    }
}
