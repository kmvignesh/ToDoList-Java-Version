package com.example.vicky.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.*;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.vicky.todolist.DTO.ToDoItem;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.vicky.todolist.Const.*;

public class ItemActivity extends AppCompatActivity {

    Toolbar item_toolbar;
    RecyclerView rv_item;
    FloatingActionButton fab_item;

    long todoId = -1;
    ItemActivity activity;
    DBHandler dbHandler;
    ItemTouchHelper touchHelper;
    ItemAdapter adapter;
    ArrayList<ToDoItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        item_toolbar = findViewById(R.id.item_toolbar);
        rv_item = findViewById(R.id.rv_item);
        fab_item = findViewById(R.id.fab_item);
        setSupportActionBar(item_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(INTENT_TODO_NAME));
        todoId = getIntent().getLongExtra(INTENT_TODO_ID, -1);
        activity = this;
        dbHandler = new DBHandler(activity);
        rv_item.setLayoutManager(new LinearLayoutManager(activity));

        fab_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo Item");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDoItem item = new ToDoItem();
                            item.setItemName(toDoName.getText().toString());
                            item.setToDoId(todoId);
                            item.setCompleted(false);
                            dbHandler.addToDoItem(item);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
        touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder p1, @NonNull RecyclerView.ViewHolder p2) {
                int sourcePosition = p1.getAdapterPosition();
                int targetPosition = p2.getAdapterPosition();
                Collections.swap(list, sourcePosition, targetPosition);
                adapter.notifyItemMoved(sourcePosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        });

        touchHelper.attachToRecyclerView(rv_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    void refreshList() {
        list = dbHandler.getToDoItems(todoId);
        adapter = new ItemAdapter(activity);
        rv_item.setAdapter(adapter);
    }


    void updateItem(final ToDoItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo Item");
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(item.getItemName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    item.setItemName(toDoName.getText().toString());
                    item.setToDoId(todoId);
                    item.setCompleted(false);
                    dbHandler.updateToDoItem(item);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        ItemActivity activity;

        ItemAdapter(ItemActivity activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
            holder.itemName.setText(list.get(i).getItemName());
            holder.itemName.setChecked(list.get(i).isCompleted());
            holder.itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.get(i).setCompleted(!list.get(i).isCompleted());
                    activity.dbHandler.updateToDoItem(list.get(i));
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Are you sure");
                    dialog.setMessage("Do you want to delete this item ?");
                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            activity.dbHandler.deleteToDoItem(list.get(i).getId());
                            activity.refreshList();
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.updateItem(list.get(i));
                }
            });
            holder.move.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        activity.touchHelper.startDrag(holder);
                    }
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox itemName;
            ImageView edit;
            ImageView delete;
            ImageView move;

            ViewHolder(View v) {
                super(v);
                itemName = v.findViewById(R.id.cb_item);
                edit = v.findViewById(R.id.iv_edit);
                delete = v.findViewById(R.id.iv_delete);
                move = v.findViewById(R.id.iv_move);
            }
        }
    }
}
