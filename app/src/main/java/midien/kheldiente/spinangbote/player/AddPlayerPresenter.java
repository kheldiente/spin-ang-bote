package midien.kheldiente.spinangbote.player;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPlayerPresenter implements AddPlayerContract.Presenter {

    @NonNull
    AddPlayerContract.View mAddPlayerView;

    private static final int MAX_PLAYERS = 8;

    private static Map<String, String> mPlayers = new HashMap<>(0);

    static {
        mPlayers.put("Player 2", "Player 2");
        mPlayers.put("Player 1","Player 1");
    }

    public AddPlayerPresenter(@NonNull AddPlayerContract.View addPlayerView) {
        mAddPlayerView = addPlayerView;
        mAddPlayerView.setPresenter(this);
    }

    @Override
    public void onCreate() {}

    @Override
    public boolean addPlayer(String player) {
        if(!nameExists(player) && !nameExceededLength(player)) {
            // Add to list for reference
            mPlayers.put(player, player);
            // Add to ui
            mAddPlayerView.addPlayerView(player);
            // After adding the player view onto the ui, check if it reached the allowed max players
            if (mPlayers.size() == MAX_PLAYERS) {
                mAddPlayerView.hideAddMoreView();
            }
            return true;
        }
        return false;
    }

    @Override
    public void deletePlayer(String key) {
        mPlayers.remove(key);
        mAddPlayerView.deletePlayerView(key);
        // After removing player, check if still did not reached the max player count
        if(mPlayers.size() < MAX_PLAYERS) {
            mAddPlayerView.showAddMoreView();
        }
    }

    @Override
    public void updatePlayerName(String key, String updatedName) {
        mPlayers.put(updatedName, updatedName);
        mAddPlayerView.updatePlayerView(key, updatedName);
        mAddPlayerView.hideEditPlayerView();
    }

    @Override
    public String getPlayer(String key) {
        return mPlayers.get(key);
    }

    @Override
    public boolean checkIfNameIsEmpty(String name) {
        return name.isEmpty() || name.length() == 0;
    }

    @Override
    public boolean nameExists(String name) {
        return mPlayers.containsKey(name);
    }

    @Override
    public boolean nameExceededLength(String name) {
        return name.length() > 8;
    }

    @Override
    public void loadPlayers() {
        for(Map.Entry<String, String> p: mPlayers.entrySet()) {
            mAddPlayerView.addPlayerView(p.getValue());
        }
    }

}
