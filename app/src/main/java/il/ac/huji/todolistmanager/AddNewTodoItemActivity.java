package il.ac.huji.todolistmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

/**
 * An activity to add items to the list
 */
public class AddNewTodoItemActivity extends Activity {

    private DatePicker datep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_item);

        datep = (DatePicker) findViewById(R.id.datePicker);

        //when OK button is clicked, the given title and date are
        //given back to the main activity (through the result extras)
        final Button b = (Button) findViewById(R.id.btnOK);
        b.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        EditText edit = (EditText) findViewById(R.id.edtNewItem);
                        String input = edit.getText().toString();
                        if (!input.matches(""))
                        {
                            //the result intent that will be given back to the main activity
                            Intent result = new Intent();
                            Date dueDate = new Date(datep.getYear() - 1900, datep.getMonth(), datep.getDayOfMonth());
                            result.putExtra("title", input);
                            result.putExtra("dueDate", dueDate);
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else
                        {
                            // if ok was pressed and text is empty it will show a message and stay in
                            // this activity
                            Toast.makeText(getApplicationContext(), "Empty text",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        // When cancel button is clicked, nothing happens (finishes)
        final Button cancelb = (Button) findViewById(R.id.btnCancel);
        cancelb.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();

                    }
                }
        );
    }
}
