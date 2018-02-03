package midien.kheldiente.spinangbote.player;

import midien.kheldiente.spinangbote.BasePresenter;
import midien.kheldiente.spinangbote.BaseView;

public interface AddPlayerContract {

    interface View extends BaseView<Presenter> {

        void goToSpinTable();

        void addPlayerView(String player);

        void deletePlayerView(int index);

        void updatePlayerView(int index, String updatedName);

        void loadPlayers();

        void showAddPlayerView();

        void showEditPlayerView(int index, String name);

        void showAddMoreView();

        void hideAddMoreView();

    }

    interface Presenter extends BasePresenter {

        void addPlayer(String player);

        void deletePlayer(int index);

        void updatePlayerName(int index, String updatedName);

        String getPlayer(int index);

        boolean checkIfNameIsEmpty(String name);

        boolean nameExists(String name);

        boolean nameExceededLength(String name);

        void loadPlayers();

    }

}
