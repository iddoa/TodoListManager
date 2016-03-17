package il.ac.huji.todolistmanager;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * to do adapter is an adapter that works with the ListItem items from our list
 * and presents the date under the title in every item
 */
public class TodoAdapter extends BaseAdapter{
    List<ListItem> result;
    Context context;
    private static LayoutInflater inflater=null;
    public TodoAdapter(TodoListManagerActivity mainActivity, List<ListItem> prgmNameList) {
        result=prgmNameList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //implementing the needed overrides for the adapter
    @Override
    public int getCount() {

        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //a holder class that holds the data
    public class Holder
    {
        TextView title;
        TextView date;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;

        // fill in the text with the item and the date
        rowView = inflater.inflate(R.layout.list_adapter, null);
        holder.title=(TextView) rowView.findViewById(R.id.txtTodoTitle);
        holder.date=(TextView) rowView.findViewById(R.id.txtTodoDueDate);
        holder.title.setText(result.get(position)._itemtitle);
        // checking that the due date isnt empty
        // (even though it should never happen in this app)
        if (holder.date == null)
        {
            holder.date.setText("No due date");
        }else{
            //I only included the date (no hour needed because there is no time field to fill)
            holder.date.setText(result.get(position)._date.toString().substring(0, 10));
        }

        Date selectedDate = result.get(position)._date;
        Date today = new Date();
        // if due date has passed - color the text in red
        if (selectedDate.getYear() <  today.getYear())
        {
            holder.title.setTextColor(Color.RED);
            holder.date.setTextColor(Color.RED);
        }
        else
        {
            if (selectedDate.getMonth() <  today.getMonth())
            {
                holder.title.setTextColor(Color.RED);
                holder.date.setTextColor(Color.RED);
            }
            else
            {
                if (selectedDate.getDay() <  today.getDay())
                {
                    holder.title.setTextColor(Color.RED);
                    holder.date.setTextColor(Color.RED);
                }
            }
        }
        return rowView;
    }

}