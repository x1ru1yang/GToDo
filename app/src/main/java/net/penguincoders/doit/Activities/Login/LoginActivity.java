package net.penguincoders.doit.Activities.Login;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.penguincoders.doit.Activities.MainActivity;
import net.penguincoders.doit.Adapters.ToDoAdapter;
import net.penguincoders.doit.Models.ToDoModel;
import net.penguincoders.doit.Models.UserModel;
import net.penguincoders.doit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText useridText;
    private Button loginButton;
    private EditText passwordText;
    private Button createAccount;

    private RequestQueue requestQueue;
    private boolean existed = false;
    private boolean pwdCorrect = false;
    private List<UserModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        useridText = findViewById(R.id.useridText);
        loginButton = findViewById(R.id.loginButton);
        passwordText = findViewById(R.id.password);
        createAccount = findViewById(R.id.createAccount);

        loginButton.setEnabled(false);
        loginButton.setTextColor(Color.GRAY);
        createAccount.setEnabled(false);
        createAccount.setTextColor(Color.GRAY);

        initUserInfo();

        useridText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    passwordText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.toString().equals("")){
                                loginButton.setEnabled(false);
                                loginButton.setTextColor(Color.GRAY);
                                createAccount.setEnabled(false);
                                createAccount.setTextColor(Color.GRAY);
                            }
                            else{
                                loginButton.setEnabled(true);
                                loginButton.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));
                                createAccount.setEnabled(true);
                                createAccount.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));
                                isExisted();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
                else {
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(Color.GRAY);
                    createAccount.setEnabled(false);
                    createAccount.setTextColor(Color.GRAY);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void initUserInfo(){
        userList = new ArrayList<>();
        String requestURL = "https://studev.groept.be/api/a21pt102/get_all_users";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                UserModel userModel = new UserModel(parseInt(curObject.getString( "userid" )),curObject.getString( "password" ));
                                userList.add(userModel);
                            }
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );
        requestQueue.add(submitRequest);
    }

    public void isExisted(){
        for (int i=0;i<userList.size();i++){
            if (userList.get(i).getUserid()==parseInt(useridText.getText().toString())){
                existed = true;
                break;
            }
        }
    }

    public void searchUserPassword(){
        for (int i=0;i<userList.size();i++){
            if (existed){
                if (userList.get(i).getPassword().equals(passwordText.getText().toString())){
                    pwdCorrect = true;
                    break;
                }
            }
        }
    }

    public void createUser(UserModel userModel){
        String requestURL = "https://studev.groept.be/api/a21pt102/create_account/"+userModel.getUserid()+"/"+passwordText.getText().toString();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );
        requestQueue.add(submitRequest);
    }

    public void setLoginButton(View caller){
        if (existed){
            searchUserPassword();
            if (pwdCorrect){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userid",parseInt(useridText.getText().toString()));
                startActivity(intent);
                finish();
            }
            else{
                Toast toast = Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_LONG);
                //可以控制 toast 显示的位置
                toast.setGravity(Gravity.TOP, 0, 10);
                toast.show();
            }
        }
        else {
            Toast toast = Toast.makeText(LoginActivity.this, "User doesn't exist!", Toast.LENGTH_LONG);
            //可以控制 toast 显示的位置
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.show();
        }
    }

    public void setCreateAccount(View caller){
        if (existed){
            Toast toast = Toast.makeText(this, "ID has been already taken!",
                    Toast.LENGTH_LONG);
            //可以控制 toast 显示的位置
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.show();
        }
        else {
            UserModel userModel = new UserModel(parseInt(useridText.getText().toString()),passwordText.getText().toString());
            createUser(userModel);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("userid",userModel.getUserid());
            startActivity(intent);
            finish();
        }
    }


    public static String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] result = md.digest(password.getBytes());
            return byteArrayToHex(result);
        } catch (Exception e) {
            return "";
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}