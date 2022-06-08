package net.penguincoders.doit.Utils;

import static java.lang.Integer.parseInt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import net.penguincoders.doit.Models.ToDoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DatabaseHandler {

    private List<ToDoModel> taskList;
    private RequestQueue requestQueue;
    private Context context;

    public DatabaseHandler(Context context, List<ToDoModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public JsonArrayRequest insertTask(ToDoModel task){
        String requestURL = "https://studev.groept.be/api/a21pt102/insert_new_task/"+
                task.getId()+"/"+task.getTask()+"/"+task.getStatus()+"/"+task.getDatetime()+"/"+task.getUser();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        taskList.add(task);
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

        return submitRequest;
    }

    public List<ToDoModel> getAllTasks(){
        return taskList;
    }

    public JsonArrayRequest updateStatus(int id, int status){
        String requestURL = "https://studev.groept.be/api/a21pt102/update_status/"+status+"/"+id;

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
        return submitRequest;
    }

    public JsonArrayRequest updateTask(int id, String task) {
        String requestURL = "https://studev.groept.be/api/a21pt102/update_task/"+task+"/"+id;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        for (int i=0;i<taskList.size();i++){
                            if (taskList.get(i).getId()==id){
                                taskList.get(i).setTask(task);
                            }
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
        return submitRequest;
    }

    public JsonArrayRequest deleteTask(int id){
        String requestURL = "https://studev.groept.be/api/a21pt102/delete_task/"+id;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        for (int i=0;i<taskList.size();i++){
                            if (taskList.get(i).getId()==id){
                                taskList.remove(i);
                            }
                        }
                        for (int i=0;i<taskList.size();i++){
                            taskList.get(i).setId(i+1);
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
        return submitRequest;
    }

    public JsonArrayRequest resetID(){
        String requestURL = "https://studev.groept.be/api/a21pt102/reset_taskid";

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
        return submitRequest;
    }
}
