package midien.kheldiente.spinangbote.player;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AddPlayerPresenter implements AddPlayerContract.Presenter {

    @NonNull
    AddPlayerContract.View mAddPlayerView;

    private static final int MAX_PLAYERS = 8;

    private static List<String> mPlayers = new ArrayList<>(0);

    static {
        mPlayers.add("Player 1");
        mPlayers.add("Player 2");
    }

    public AddPlayerPresenter(@NonNull AddPlayerContract.View addPlayerView) {
        mAddPlayerView = addPlayerView;
        mAddPlayerView.setPresenter(this);
    }

    @Override
    public void onCreate() {}

    @Override
    public void addPlayer(String player) {
        mPlayers.add(player);
        mAddPlayerView.showAddPlayerView();
        // After adding the player view onto the ui, check if it reached the allowed max players
        if(mPlayers.size() == MAX_PLAYERS) {
            mAddPlayerView.hideAddMoreView();
        }
    }

    @Override
    public void deletePlayer(int index) {
        mPlayers.remove(index);
        mAddPlayerView.deletePlayerView(index);
        // After removing player, check if still did not reached the max player count
        if(mPlayers.size() < MAX_PLAYERS) {
            mAddPlayerView.showAddPlayerView();
        }
    }

    @Override
    public void updatePlayerName(int index, String updatedName) {
        mPlayers.set(index, updatedName);
        mAddPlayerView.updatePlayerView(index, updatedName);
    }

    @Override
    public String getPlayer(int index) {
        return mPlayers.get(index);
    }

    @Override
    public boolean checkIfNameIsEmpty(String name) {
        return name.isEmpty() || name.length() == 0;
    }

    @Override
    public boolean nameExists(String name) {
        return mPlayers.contains(name);
    }

    @Override
    public boolean nameExceededLength(String name) {
        return name.length() > 8;
    }

    @Override
    public void loadPlayers() {
        for(String p: mPlayers) {
            mAddPlayerView.addPlayerView(p);
        }
    }

}
