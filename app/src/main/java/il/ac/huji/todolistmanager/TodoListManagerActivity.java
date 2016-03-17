package il.ac.huji.todolistmanager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ListItem - each item in the list contains its title and date
 */
class ListItem{
    public final Date _date;
    public final String _itemtitle;
    public ListItem(Date date, String _itemtitle)
    {
        this._date = date;
        this._itemtitle = _itemtitle;
    }
}

public class TodoListManagerActivity extends AppCompatActivity {
    //the list
    private List<ListItem> todoList = new ArrayList<ListItem>();
    TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        adapter = new TodoAdapter(this, todoList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // defining the listview
        final ListView listView = (ListView) findViewById(R.id.lstTodoItems);
        //setting the adapter to the listview
        listView.setAdapter(adapter);
        // registering the listview to the context menu
        registerForContextMenu(listView);
        final MenuItem menuitem = (MenuItem)
                findViewById(R.id.additem);
        onOptionsItemSelected(menuitem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    /**
     * the function that starts the add new item activity when the add button
     * in the menu is clicked
     */
//    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null)
        {
            Intent intent = new Intent(this, AddNewTodoItemActivity.class);
            startActivityForResult(intent, 1);
            adapter.notifyDataSetChanged();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * the context menu that opens when an item in the list is pressed for a long time
     * there is a different context menu for items that begin with "Call " that includes
     * the call option
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        // showing the item chosen in the title of the context menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(todoList.get(info.position)._itemtitle);

        //if it begins with "Call " then open the context_menu_call
        if  ((todoList.get(info.position)._itemtitle.length()>4) &&
        ((todoList.get(info.position)._itemtitle.substring(0, 5)).matches("Call ")))
        {
            inflater.inflate(R.menu.context_menu_call, menu);
            MenuItem item = menu.findItem(R.id.menuItemCall);
            item.setTitle(todoList.get(info.position)._itemtitle);
        }
        else
        {
            //else, open the regular context_menu
            inflater.inflate(R.menu.context_menu, menu);
        }

    }
    /**
     * when in context menu, choosing to delete deletes from list
     * or Call .. opens the dialer with the given number (the rest of the string after call)
     * or cancel (does nothing and closes the context menu)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case (R.id.menuItemDelete) :
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int pos = info.position;
                todoList.remove(pos);
                adapter.notifyDataSetChanged();
                break;
            case (R.id.menuItemCall) :
                // calling
                Intent intent = new Intent(Intent.ACTION_DIAL);
                AdapterView.AdapterContextMenuInfo infocall = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poscall = infocall.position;
                String number = todoList.get(poscall)._itemtitle.substring(5);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                break;
            case (R.id.cancel_delete) :
                return false;
        }
        return true;
    }

    /**
     * getting the results back from the add new item activity
     */
    @Override
    protected void onActivityResult(int reqcode, int rescode, Intent data)
    {
        if (reqcode == 1) {
            // Make sure the request was successful
            if (rescode == RESULT_OK) {
                String itemTitle = data.getExtras().getString("title");
                //checking the string input isnt empty
                if (!itemTitle.matches(""))
                {
                    Date dueDate = (Date)data.getSerializableExtra("dueDate");
                    ListItem newItem = new ListItem(dueDate, itemTitle);
                    todoList.add(newItem);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}

