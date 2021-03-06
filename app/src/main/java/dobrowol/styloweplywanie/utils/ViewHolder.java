package dobrowol.styloweplywanie.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dobrowol.styloweplywanie.R;
import com.squareup.picasso.Picasso;

/**
 * Created by dobrowol on 29.03.17.
 */
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    private final TextView teamLabel;
    private final TextView descriptionLabel;
    private final ImageView profileImage;
    private ItemsAdapter.ItemsSelectedListener listener;
    private TeamData userDetails;
    private View.OnCreateContextMenuListener contextMenuListener;

    public ViewHolder(View itemView, ItemsAdapter.ItemsSelectedListener listener) {

        super(itemView);
        Log.d("DUPA", "view holder create");
        teamLabel = (TextView) itemView.findViewById(R.id.team_label);
        descriptionLabel = (TextView) itemView.findViewById(R.id.description_label);
        profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void fillView(TeamData userDetails, Picasso imageLoader) {
        this.userDetails = userDetails;
        teamLabel.setText(this.userDetails.getTeamName());
        descriptionLabel.setText(this.userDetails.getCoachName());
        //imageLoader.load(this.userDetails.getImageUrl()).into(profileImage);
    }

    @Override
    public void onClick(View v) {
        Log.d("DUPA", "click");
        listener.onItemSelected(userDetails);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onLongClick(View v) {
        listener.OnItem(getAdapterPosition());
        return false;
    }



}

