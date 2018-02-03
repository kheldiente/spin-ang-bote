package midien.kheldiente.spinangbote.player;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.spintable.SpinTableActivity;
import midien.kheldiente.spinangbote.views.EditPlayerDialog;
import midien.kheldiente.spinangbote.views.EditPlayerTextView;

public class AddPlayerFragment extends Fragment
        implements AddPlayerContract.View, View.OnClickListener,
                    EditPlayerDialog.OnUpdateListener {

    private static final String TAG = AddPlayerFragment.class.getSimpleName();

    private static final int MAX_PLAYERS = 8;

    private AddPlayerContract.Presenter mPresenter;

    private LinearLayout mPlayerList;
    private ImageView mAddImgView;
    private TextView mAddTxtView;
    private EditPlayerDialog mEditPlayerDialog;
    private Button mNextBtn;

    List<EditPlayerTextView> mPlayerNames = new ArrayList<>(0);

    public static AddPlayerFragment newInstance() {
        return new AddPlayerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_player, container, false);
        mPlayerList = root.findViewById(R.id.ll_player_list);
        mAddImgView = root.findViewById(R.id.add_iv);
        mAddTxtView = root.findViewById(R.id.add_txt);
        mNextBtn = root.findViewById(R.id.btn_next);

        mAddImgView.setOnClickListener(this);
        mAddTxtView.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        loadPlayers();
        return root;
    }

    @Override
    public void setPresenter(AddPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void goToSpinTable() {
        Intent spinTableAct = new Intent(getContext(), SpinTableActivity.class);
        spinTableAct.putStringArrayListExtra(SpinTableActivity.EXTRA_PLAYERS, getAllPlayers(mPlayerNames));
        startActivity(spinTableAct, ActivityOptionsCompat.makeCustomAnimation(getContext(),
                android.R.anim.fade_in,
                android.R.anim.fade_out).toBundle());
    }

    private static ArrayList<String> getAllPlayers(List<EditPlayerTextView> pv) {
        ArrayList<String> p = new ArrayList<>(0);
        for(EditPlayerTextView ptv: pv) {
            p.add(ptv.name); // Extract name from {@link EditPlayerTextView}
        }
        return p;
    }

    @Override
    public void addPlayerView(final String player) {
        if(mPlayerNames.size() < MAX_PLAYERS) {
            // Set up player edit text
            EditPlayerTextView ept = new EditPlayerTextView(getContext());
            ept.setName(player);
            // Add to list for reference
            mPlayerNames.add(ept);
            // Add to list view
            mPlayerList.addView(ept);
            // Then add onClickListener to show player editor
            ept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index = mPlayerNames.size() - 1;
                    showEditPlayerView(index, player);
                }
            });
        }
    }

    @Override
    public void deletePlayerView(int index) {
        mPlayerNames.remove(index);
    }

    @Override
    public void updatePlayerView(int index, String updatedName) {
        mPlayerNames.get(index).setName(updatedName);
    }

    @Override
    public void loadPlayers() {
        mPresenter.loadPlayers();
    }

    @Override
    public void showAddPlayerView() {
        if(mPlayerNames.size() < MAX_PLAYERS) {
            mEditPlayerDialog = new EditPlayerDialog(getContext());
            mEditPlayerDialog.setOnUpdateListener(this);

            mEditPlayerDialog.show();
        }
    }

    @Override
    public void showEditPlayerView(int index, String name) {
        mEditPlayerDialog = new EditPlayerDialog(getContext());
        mEditPlayerDialog.setOnUpdateListener(this);
        mEditPlayerDialog.setValues(index, name);

        mEditPlayerDialog.show();
    }

    @Override
    public void showAddMoreView() {
        mAddImgView.setVisibility(View.VISIBLE);
        mAddTxtView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddMoreView() {
        mAddImgView.setVisibility(View.GONE);
        mAddTxtView.setVisibility(View.GONE);
    }

    @Override
    public void onUpdate(int index, String name) {
        if(index == - 1)
            addPlayerView(name);
        else
            updatePlayerView(index, name);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add_iv:
            case R.id.add_txt:
                showAddPlayerView();
                break;
            case R.id.btn_next:
                goToSpinTable();
                break;
        }
    }
}