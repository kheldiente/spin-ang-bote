package midien.kheldiente.spinangbote.player;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import midien.kheldiente.spinangbote.R;
import midien.kheldiente.spinangbote.spintable.SpinTableActivity;
import midien.kheldiente.spinangbote.views.EditPlayerDialog;
import midien.kheldiente.spinangbote.views.EditPlayerTextView;

public class AddPlayerFragment extends Fragment
        implements AddPlayerContract.View, View.OnClickListener,
                    EditPlayerDialog.OnUpdateListener {

    private static final String TAG = AddPlayerFragment.class.getSimpleName();

    private static final int MAX_PLAYERS = 8;
    private static final int MIN_PLAYERS = 2;

    private AddPlayerContract.Presenter mPresenter;

    private LinearLayout mPlayerList;
    private ImageView mAddImgView;
    private TextView mAddTxtView;
    private EditPlayerDialog mEditPlayerDialog;
    private Button mNextBtn;

    Map<String, EditPlayerTextView> mPlayerNames = new HashMap<>(0);

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

    private static ArrayList<String> getAllPlayers(Map<String, EditPlayerTextView> pv) {
        ArrayList<String> p = new ArrayList<>(0);
        for(Map.Entry<String, EditPlayerTextView> ptv: pv.entrySet()) {
            p.add(ptv.getValue().name); // Extract name from {@link EditPlayerTextView}
        }
        return p;
    }

    @Override
    public void addPlayerView(final String p) {
        if(mPlayerNames.size() < MAX_PLAYERS) {
            // Set up player edit text
            EditPlayerTextView ept = new EditPlayerTextView(getContext());
            ept.setName(p);
            // Add to list for reference
            mPlayerNames.put(p, ept);
            // Add to list view
            mPlayerList.addView(ept);
            // Then add onClickListener to show player editor
            ept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String player = mPlayerNames.get(p).name;
                    showEditPlayerView(p, player);
                }
            });
        }
    }

    @Override
    public void deletePlayerView(String key) {
        mPlayerList.removeView(mPlayerNames.get(key));
        mPlayerNames.remove(key);
    }

    @Override
    public void updatePlayerView(String key, String updatedName) {
        mPlayerNames.get(key).setName(updatedName);
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
    public void showEditPlayerView(String key, String name) {
        mEditPlayerDialog = new EditPlayerDialog(getContext());
        mEditPlayerDialog.setOnUpdateListener(this);
        mEditPlayerDialog.setValues(key, name);
        if(mPlayerNames.size() <= MIN_PLAYERS) // If true, user cannot delete anymore players
            mEditPlayerDialog.cannotBeDeleted(false);

        mEditPlayerDialog.show();
    }

    @Override
    public void hideEditPlayerView() {
        if(mEditPlayerDialog != null)
            mEditPlayerDialog.dismiss();
    }

    @Override
    public void resetEditPlayerView() {
        if(mEditPlayerDialog != null)
            mEditPlayerDialog.reset();
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
    public void showMessageInfo(String msg) {
        if(mEditPlayerDialog != null)
            mEditPlayerDialog.setMessageInfo(msg);
    }

    @Override
    public void onUpdate(String key, String name) {
        if(TextUtils.isEmpty(name))
            return;

        if(TextUtils.isEmpty(key)) {
            boolean added = mPresenter.addPlayer(name);
            if(added) {
                showMessageInfo(getString(R.string.added_player_msg, name));
                // If player list is not yet on {@value MAX_PLAYERS}, clear edit text and add again
                resetEditPlayerView();
                if(mPlayerNames.size() == MAX_PLAYERS)
                    hideEditPlayerView();
            } else
                showMessageInfo(getString(R.string.added_player_fail));
        } else {
            mPresenter.updatePlayerName(key, name);
        }
    }

    @Override
    public void onDelete(String key) {
        mPresenter.deletePlayer(key);
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
