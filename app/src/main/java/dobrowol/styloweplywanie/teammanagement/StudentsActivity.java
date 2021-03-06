package dobrowol.styloweplywanie.teammanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dobrowol.styloweplywanie.R;
import dobrowol.styloweplywanie.teammanagement.trainingdetails.StudentAchievementChartActivity;
import dobrowol.styloweplywanie.utils.CsvDataUtils;
import dobrowol.styloweplywanie.utils.StudentAchievement;
import dobrowol.styloweplywanie.utils.StudentAdapter;
import dobrowol.styloweplywanie.utils.StudentData;
import dobrowol.styloweplywanie.utils.TeamData;
import dobrowol.styloweplywanie.utils.TeamDataUtils;
import dobrowol.styloweplywanie.utils.WrapContentLinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dobrowol on 02.12.17.
 */

public class StudentsActivity extends AppCompatActivity implements StudentAdapter.StudentSelectedListener  {
    private static final int ADD_TEAM_REQUEST = 1;

    private StudentAdapter adapter;
    private RecyclerView itemsView;
    private List<TeamData> items;
    private TeamDataUtils teamUtils;
    private TeamData teamData;
    private FloatingActionButton fab;
    private static final String KEY = "TeamName";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jointeam);

        //TeamDataUtils teamDataUtils = new TeamDataUtils(getApplicationContext());
        //teamDataUtils.clearCache();
        fab = (FloatingActionButton) findViewById(R.id.fab_check);
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);
        itemsView = (RecyclerView) findViewById(R.id.items_view);
        itemsView.setLayoutManager(new WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new StudentAdapter(this);
        itemsView.setAdapter(adapter);
        teamUtils = new TeamDataUtils(getApplicationContext());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Intent intent = getIntent();
        if (intent != null & intent.hasExtra(KEY)) {
            fetchStudents(intent.getExtras().getString(KEY));
        }

    }
    public static void startActivity(String teamName, Context context) {
        Intent intent = new Intent(context, StudentsActivity.class);
        intent.putExtra(KEY, teamName);

        context.startActivity(intent);
    }
    private void fetchStudents(String teamName)
    {
        teamData = teamUtils.getTeam(teamName);
        if (teamData != null) {
            adapter.setItems(teamData.students);
            //normalizeData();
        }
    }

    private void normalizeData() {
        List<StudentData> students = teamData.students;
        CsvDataUtils csvDataUtils = new CsvDataUtils(getApplicationContext());
        for (StudentData studentData :students)
        {
            ArrayList<StudentAchievement> achievements = csvDataUtils.getStudentAchievements(studentData.dataFile);
            unifyAchievements(achievements);
            csvDataUtils.overwrite(achievements,studentData.dataFile);
        }
    }


    private void unifyAchievements(ArrayList<StudentAchievement> achievements) {
        String []klasyczny = getResources().getStringArray(R.array.klasyczny);
        String []grzbietowy = getResources().getStringArray(R.array.grzbietowy);
        String []dowolny = getResources().getStringArray(R.array.dowolny);
        String []motylkowy = getResources().getStringArray(R.array.motylkowy);

        Set<String> KLAS_VALUES = new HashSet<String>(Arrays.asList(
                klasyczny
        ));
        Set<String> GRZB_VALUES = new HashSet<String>(Arrays.asList(
                grzbietowy
        ));
        Set<String> DOW_VALUES = new HashSet<String>(Arrays.asList(
                dowolny
        ));
        Set<String> MOT_VALUES = new HashSet<String>(Arrays.asList(
                motylkowy
        ));
        String []distances = getResources().getStringArray(R.array.distances);
        for (StudentAchievement achievement : achievements)
        {
            if(KLAS_VALUES.contains(achievement.style))
            {
                achievement.style=klasyczny[0];
            }
            else if(GRZB_VALUES.contains(achievement.style))
            {
                achievement.style=grzbietowy[0];
            }
            else if(DOW_VALUES.contains(achievement.style))
            {
                achievement.style=dowolny[0];
            }
            else if(MOT_VALUES.contains(achievement.style))
            {
                achievement.style=motylkowy[0];
            }
            achievement.distance=achievement.distance.replaceAll("m", "");
        }
    }

    private void fetchStudent(StudentData studentData) {
        Log.d("DUPA", "fetchStudent");
        //TeamDetailsActivity.startDetailsActivity(teamData, JoinTeamActivity.this);
        //StudentAchievementActivity.startActivity(studentDataFile, StudentsActivity.this);
        //StudentAchievementChartActivity.startActivity(studentDataFile, StudentsActivity.this);
        StudentDetailsActivity.startActivity(studentData, StudentsActivity.this);
    }
    @Override
    public void onItemSelected(StudentData item) {
        fetchStudent(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jointeam, menu);
        menu.findItem(R.id.action_addTeam).setVisible(false).setEnabled(false);
        menu.findItem(R.id.action_addTeamMember).setVisible(true).setEnabled(true);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_addTeamMember:
                showAddStudent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddStudent() {
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.putExtra(KEY, teamData.teamName);
        //Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ));
        // Show user only contacts w/ phone numbers
        startActivityForResult(intent, ADD_TEAM_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TEAM_REQUEST) {
            if(resultCode == Activity.RESULT_OK){

                fetchStudents(teamData.teamName);
            }
        }
    }
}
