package org.bazhlekova.todoanddont;

import com.parse.*;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.*;

public class ToDoAndDontActivity extends Activity implements OnItemClickListener {

	private EditText mTaskInput;
	private ListView mListView;
	private TaskAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_and_dont);
		
		Parse.initialize(this, "RSQQp3jOr3Y4n1q1VeXI9TgtBjCLvQj0PPyBxu55", "EwdhvwBqOTvQWSXZIMbtWsf8VJw3TLXZP24StpK2");
		ParseAnalytics.trackAppOpened(getIntent());
		ParseObject.registerSubclass(Task.class);
		
		mTaskInput = (EditText) findViewById(R.id.task_input);
		mListView = (ListView) findViewById(R.id.task_list);
		
		mAdapter = new TaskAdapter(this, new ArrayList<Task>());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		updateData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_and_dont, menu);
		return true;
	}
	/**
	 * User creates a new task
	 * @param v	 view that is used	
	 */
	public void createTask(View v){
		if (mTaskInput.getText().length() > 0) // checks if input is blank
			{
				Task t = new Task(); //adds new task to the task table
				t.setDescription(mTaskInput.getText().toString()); //gets description from user
				t.setCompleted(false); //sets completed boolean as false initially
				t.saveEventually(); //if user is not online, queues up object to be saved when user is connected to a network
				mTaskInput.setText(""); //clears text input field
				mAdapter.insert(t, 0);
			}
	}
	
	public void updateData(){
		  ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
		  query.findInBackground(new FindCallback<Task>() {
		          
		      @Override
		      public void done(List<Task> tasks, ParseException error) {
		          if(tasks != null){
		              mAdapter.clear();
		              mAdapter.addAll(tasks);
		          }
		      }
		  });
		}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Task task = mAdapter.getItem(position);
		TextView taskDescription = (TextView) view.findViewById(R.id.task_description);
		
		task.setCompleted(!task.isCompleted());
		
		if(task.isCompleted()){
		      taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		  }else{
		      taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
		  }

		  task.saveEventually();
	}

}
