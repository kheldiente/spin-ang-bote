package midien.kheldiente.spinangbote.player;

import midien.kheldiente.spinangbote.BasePresenter;
import midien.kheldiente.spinangbote.BaseView;

public interface AddPlayerContract {

    interface View extends BaseView<Presenter> {

        void goToSpinTable();

        void addPlayerView(String player);

        void deletePlayerView(String key);

        void updatePlayerView(String key, String updatedName);

        void loadPlayers();

        void showAddPlayerView();

        void showEditPlayerView(String key, String name);

        void hideEditPlayerView();

        void resetEditPlayerView();

        void showAddMoreView();

        void hideAddMoreView();

        void showMessageInfo(String msg);

    }

    interface Presenter extends BasePresenter {

        boolean addPlayer(String player);

        void deletePlayer(String key);

        void updatePlayerName(String key, String updatedName);

        String getPlayer(String key);

        boolean checkIfNameIsEmpty(String name);

        boolean nameExists(String name);

        boolean nameExceededLength(String name);

        void loadPlayers();

    }

}
