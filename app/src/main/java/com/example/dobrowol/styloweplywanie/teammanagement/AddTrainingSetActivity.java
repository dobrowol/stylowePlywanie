package com.example.dobrowol.styloweplywanie.teammanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.dobrowol.styloweplywanie.R;
import com.example.dobrowol.styloweplywanie.utils.SwimmingStyles;
import com.example.dobrowol.styloweplywanie.utils.TrainingData;
import com.example.dobrowol.styloweplywanie.utils.TrainingSet;
import com.example.dobrowol.styloweplywanie.utils.TrainingSetAdapter;

import java.util.ArrayList;
import java.util.Date;

import static com.example.dobrowol.styloweplywanie.utils.Utils.swimmingStylesNames;

/**
 * Created by dobrowol on 10.04.17.
 */

public class AddTrainingSetActivity extends AppCompatActivity implements View.OnClickListener, TrainingSetAdapter.TrainingSetSelectedListener {
    public static final String RETURNED_DATA_KEY = "TrainingDataSet";
    private Date trainingDate;
    private ArrayList<TrainingSet> trainingSets;
    private TrainingSetAdapter adapter;
    private FloatingActionButton addButton;
    private EditText distance;
    private TrainingData trainingData;
    RecyclerView setsView;

    private EditText description;
    private AutoCompleteTextView style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrainingset);
        adapter = new TrainingSetAdapter(this);
        setsView = (RecyclerView) findViewById(R.id.training_sets_view);
        setsView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        setsView.setAdapter(adapter);
        trainingSets = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null & intent.hasExtra(AddTrainingActivity.TRAINING_SET_KEY)) {
            trainingDate =  (Date) intent.getExtras().getSerializable(AddTrainingActivity.TRAINING_SET_KEY);
            trainingData = (TrainingData) intent.getExtras().getSerializable(AddTrainingActivity.TRAINING_DATA);
            //trainingSets = trainingData.trainingSets;

        }


        adapter.setItems(trainingSets);
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_trainingSet);
        addButton.setOnClickListener(this);
        distance = (EditText) findViewById(R.id.editText_distance);
        description = (EditText) findViewById(R.id.editText_description);
        style = (AutoCompleteTextView) findViewById(R.id.autoComplete_style);
        String[] swimmingStyles = swimmingStylesNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,swimmingStyles);
        style.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.floatingActionButton_trainingSet:
                Log.d("DUPA", "floating add");
                int dist = Integer.parseInt(distance.getText().toString());
                String desc = description.getText().toString();
                SwimmingStyles st = SwimmingStyles.valueOf(style.getText().toString());
                TrainingSet set = new TrainingSet(dist, st, desc);
                trainingSets.add(set);
                adapter.setItems(trainingSets);
                break;
            default:
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddTrainingActivity.TRAINING_DATA,trainingData);
                returnIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

        }
    }

    @Override
    public void onItemSelected(TrainingSet item) {

    }

   /* class TrainingSetAdapter extends RecyclerView.Adapter<TrainingSetAdapter.TrainingSetViewHolder> {

        private final LayoutInflater inflater;
        private List<TrainingSet> samples;

        private TrainingSetAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void setSamples(List<TrainingSet> samples) {
            this.samples = samples;
        }

        @Override
        public TrainingSetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.trainingsetitem, viewGroup, false);
            return new TrainingSetViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TrainingSetAdapter.TrainingSetViewHolder viewHolder, int i) {
            TrainingSet item = samples.get(i);
            viewHolder.fillView(item);
        }

        @Override
        public int getItemCount() {
            if (samples == null)
                return 0;
            return samples.size();
        }

        class TrainingSetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView distance;
            public final TextView style;
            public final TextView description;

            public TrainingSetViewHolder(View view) {
                super(view);
                this.distance = (TextView) view.findViewById(R.id.textView_distance);
                this.description = (TextView) view.findViewById(R.id.textView_desription);
                this.style = (TextView) view.findViewById(R.id.textView_style);
                view.setOnClickListener(this);
            }
            public void fillView(TrainingSet trainingSet)
            {
                distance.setText(trainingSet.distance);

                description.setText(trainingSet.description);
            }

            @Override
            public void onClick(@NonNull View v) {

            }
        }
    }*/
}
