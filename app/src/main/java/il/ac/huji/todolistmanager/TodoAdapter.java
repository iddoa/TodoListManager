package il.ac.huji.todolistmanager;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * to do adapter is an adapter that works with the ListItem items from firebase
 * and presents the date under the title in every item
 */
public class TodoAdapter extends BaseAdapter{
    // the list of the ListItems, updates according to the list on firebase
    List<ListItem> valList;
    // the list of the keys, updates according to the list on firebase in the same
    // order of the matching values
    private List<String> keysList;
    Context context;
    private ChildEventListener mListener;
    private static LayoutInflater inflater=null;

    /**
     * the todoadapter gets a firebase query and populates a listview with the data
     * @param mainActivity
     * @param fbDb
     */
    public TodoAdapter(TodoListManagerActivity mainActivity, Query fbDb) {
        valList = new ArrayList<ListItem>();
        keysList = new ArrayList<String>();
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // every time the data changes in the firebase db it executes this function
        fbDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> list = (HashMap<String, Object>) snapshot.getValue();
                if (list!=null)
                {
                    // every time something in the list changes, the key and values lists are
                    // reset and refilled with all the items in the list
                    valList.clear();
                    keysList.clear();
                    for ( Map.Entry<String, Object> entry : list.entrySet()) {
                        String key = entry.getKey();
                        keysList.add(key);
                        Object curritem = entry.getValue();
                        HashMap<String, Object> item = (HashMap<String, Object>) curritem;
                        String date = (String) item.remove("_date");
                        String title = (String) item.remove("_itemtitle");
                        ListItem newItem = new ListItem(date, title);
                        valList.add(newItem);
                        notifyDataSetChanged();
                    }
                }
                else
                {
                    valList.clear();
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                String message = "Server error. Refresh page";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    //implementing the needed overrides for the adapter
    @Override
    public int getCount() {

        return valList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    public List<ListItem> getList()
    {
        return valList;
    }
    public List<String> getKeysList()
    {
        return keysList;
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    //a holder class that holds the data
    public class Holder
    {
        TextView title;
        TextView date;
    }

    /**
     * A function that converts a string to a date for date comparison
     * (firebase cant hold date objects therefore the dates are strings in the list)
     * @param s the string to convert
     * @return the date as a Date object
     */
    public Date stringToDate(String s)
    {
        DateFormat df = new SimpleDateFormat("E MMM dd yyyy");
        try{
            return df.parse(s);
        }catch (Exception ex ){
            System.out.println(ex);
            return null;
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;

        // fill in the text with the item and the date
        rowView = inflater.inflate(R.layout.list_adapter, null);
        holder.title=(TextView) rowView.findViewById(R.id.txtTodoTitle);
        holder.date=(TextView) rowView.findViewById(R.id.txtTodoDueDate);
        holder.title.setText(valList.get(position)._itemtitle);
        // checking that the due date isnt empty
        // (even though it should never happen in this app)
        if (holder.date == null)
        {
            holder.date.setText("No due date");
        }else{
            //I only included the date (no hour needed because there is no time field to fill)
            holder.date.setText(valList.get(position)._date);//.toString().substring(0, 10));
        }

        Date selectedDate = stringToDate(valList.get(position)._date);
        if (selectedDate != null)
        {
            Date today = new Date();
            // if due date has passed - color the text in red
            if (selectedDate.getYear() <  today.getYear())
            {
                holder.title.setTextColor(Color.RED);
                holder.date.setTextColor(Color.RED);
            }
            else
            {
                if ((selectedDate.getMonth() <  today.getMonth()) &&
                        (selectedDate.getYear() == today.getYear()))
                {
                    holder.title.setTextColor(Color.RED);
                    holder.date.setTextColor(Color.RED);
                }
                else
                {
                    if ((selectedDate.getDay() <  today.getDay())
                            && (selectedDate.getYear() == today.getYear())
                            && (selectedDate.getMonth() == today.getMonth()))
                    {
                        holder.title.setTextColor(Color.RED);
                        holder.date.setTextColor(Color.RED);
                    }
                }
            }
        }
        return rowView;
    }

}