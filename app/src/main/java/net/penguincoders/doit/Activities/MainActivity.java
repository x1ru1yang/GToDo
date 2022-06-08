package net.penguincoders.doit.Activities;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.Activities.Functions.CreateActivity;
import net.penguincoders.doit.Activities.Login.LoginActivity;
import net.penguincoders.doit.Activities.Login.SplashActivity;
import net.penguincoders.doit.Adapters.ToDoAdapter;
//import net.penguincoders.doit.Fragments.AddNewTask;
import net.penguincoders.doit.Models.ToDoModel;
import net.penguincoders.doit.R;
import net.penguincoders.doit.Utils.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    private List<ToDoModel> taskList = new ArrayList<>();
    private DatabaseHandler db;
    private RequestQueue requestQueue;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private ImageButton logout;

    private int allSize;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        fab = findViewById(R.id.fab);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        logout = findViewById(R.id.logout);
        requestQueue = Volley.newRequestQueue( this );

        Bundle extras = getIntent().getExtras();
        // load login information
        userid = parseInt(extras.get("userid").toString());

        initUserData();
        initAllSize();

        db = new DatabaseHandler(this,taskList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter,this));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (extras.containsKey("create")){
                    ToDoModel task = new ToDoModel();
                    task.setUser(userid);
                    task.setId(allSize+1);
                    task.setTask(extras.get("task").toString());
                    task.setDatetime(extras.get("datetime").toString());
                    task.setStatus(0);
                    insertTask(task);
                }
//                else if (extras.containsKey("edit")){
//                    editTask(parseInt(extras.getString("id")),extras.getString("task"));
//                }
                extras.clear();
                initUserData();

                new Handler().postDelayed(() -> {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    public void setLogout(View caller){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    private void editTask(int id, String task) {
//        requestQueue.add(db.updateTask(id,task));
////        for (int i=0;i<taskList.size();i++){
////            if (taskList.get(i).getId()==id){
////                taskList.get(i).setTask(task);
////            }
////        }
//        taskList = db.getAllTasks();
//        tasksAdapter.setTasks(taskList);
//    }

    private void initAllSize() {
        allSize = 0;
        String requestURL = "https://studev.groept.be/api/a21pt102/get_all_task";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            for( int i = 0; i < response.length(); i++ )
                            {
                                allSize++;
                            }
                        }
                        catch( Exception e )
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

    public void initUserData(){
        taskList = new ArrayList<>();
        String requestURL = "https://studev.groept.be/api/a21pt102/get_task/"+userid;

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
                                ToDoModel toDoModel = new ToDoModel();
                                toDoModel.setId(parseInt(curObject.getString( "id" )));
                                toDoModel.setTask(curObject.getString( "task" ));
                                toDoModel.setStatus(parseInt(curObject.getString( "status" )));
                                toDoModel.setDatetime(curObject.getString( "datetime" ));
                                toDoModel.setUser(userid);
                                taskList.add(toDoModel);
                            }
                            tasksAdapter = new ToDoAdapter(taskList, db,MainActivity.this);
                            tasksAdapter.setTasks(taskList);
                            tasksRecyclerView.setAdapter(tasksAdapter);
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

    public void updateStatus(int id, int status){
        for (int i=0;i<taskList.size();i++){
            if (taskList.get(i).getId()==id){
                taskList.get(i).setStatus(status);
            }
        }
        tasksAdapter.setTasks(taskList);
        requestQueue.add(db.updateStatus(id,status));
    }

    public void insertTask(ToDoModel task){
        requestQueue.add(db.insertTask(task));
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
    }


    public void setFab(View caller){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("userid",userid);
        startActivity(intent);
        finish();
    }


    // inner class
    public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

        private ToDoAdapter adapter;
        private MainActivity mainActivity;

        public RecyclerItemTouchHelper(ToDoAdapter adapter, MainActivity mainActivity) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.mainActivity = mainActivity;
        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                AlertDialog.Builder builder = new AlertDialog.Builder(tasksAdapter.getContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this Task?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } //else {
//                editItem(position);
//            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            Drawable icon;
            ColorDrawable background;

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;

            if (dX > 0) {
                icon = ContextCompat.getDrawable(mainActivity, R.drawable.ic_baseline_edit);
                background = new ColorDrawable(ContextCompat.getColor(mainActivity, R.color.colorPrimaryDark));
            } else {
                icon = ContextCompat.getDrawable(mainActivity, R.drawable.ic_baseline_delete);
                background = new ColorDrawable(Color.RED);
            }

            assert icon != null;
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
        }
    }

//    private void editItem(int position) {
//        ToDoModel item = taskList.get(position);
//        Intent intent = new Intent(this,EditActivity.class);
//        intent.putExtra("userid",userid);
//        intent.putExtra("id",item.getId());   // task id
//        startActivity(intent);
//    }

    private void deleteItem(int position) {
        ToDoModel item = taskList.get(position);
        requestQueue.add(db.deleteTask(item.getId()));
        requestQueue.add(db.resetID());
        for (int i=0;i<taskList.size();i++){
            if (taskList.get(i).getId()==item.getId()){
                taskList.remove(i);
            }
        }
        for (int i=0;i<taskList.size();i++){
            taskList.get(i).setId(i+1);
        }
    }



}