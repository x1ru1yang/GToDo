package net.penguincoders.doit.Activities.Functions;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import net.penguincoders.doit.Activities.MainActivity;
import net.penguincoders.doit.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateActivity extends AppCompatActivity {
    private EditText createText;
    private Button createButton;
    private ImageButton imageButton;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createText = findViewById(R.id.createText);
        createButton = findViewById(R.id.createButton);
        imageButton = findViewById(R.id.imageButton);

        Bundle extras = getIntent().getExtras();
        userid = parseInt(extras.get("userid").toString());
    }

    public void setCreateButton(View caller){
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("task",createText.getText().toString());
        intent.putExtra("userid",userid);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        intent.putExtra("datetime",localTime);
        intent.putExtra("create",1);
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