package net.penguincoders.doit.Models;

public class ToDoModel {

    private int id, status;
    private String task;
    private String datetime;
    private int userid;

    public ToDoModel(int id, String task, int status, String datetime, int userid){
        this.id = id;
        this.task = task;
        this.status = status;
        this.datetime = datetime;
        this.userid = userid;
    }


    public int getUser() {
        return userid;
    }

    public void setUser(int userid) {
        this.userid = userid;
    }

    public ToDoModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }



}

