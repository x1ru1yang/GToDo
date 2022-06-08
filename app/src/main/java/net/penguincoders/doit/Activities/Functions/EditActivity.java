package net.penguincoders.doit.Activities.Functions;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.penguincoders.doit.Activities.MainActivity;
import net.penguincoders.doit.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditActivity extends AppCompatActivity {
    private EditText editText;
    private Button editButton;
    private ImageButton backButton;
    private int userid;
    private int id; // task


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.editText);
        editButton = findViewById(R.id.editButton);
        backButton = findViewById(R.id.backButton);

        Bundle extras = getIntent().getExtras();
        userid = parseInt(extras.get("userid").toString());
        id = parseInt(extras.get("id").toString()); // task id
    }

    public void setEditButton(View caller){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("task",editText.getText().toString());
        intent.putExtra("userid",userid);

        intent.putExtra("id",id);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        intent.putExtra("datetime",localTime);
        intent.putExtra("status",0);
        intent.putExtra("edit",1);
        startActivity(intent);
        finish();
    }

    public void setBackButton(View caller){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("userid",userid);
        startActivity(intent);
        finish();
    }

}