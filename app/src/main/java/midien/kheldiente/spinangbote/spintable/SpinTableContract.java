package midien.kheldiente.spinangbote.spintable;


import midien.kheldiente.spinangbote.BasePresenter;
import midien.kheldiente.spinangbote.BaseView;

public interface SpinTableContract {

    interface View extends BaseView<Presenter> {

        void showPointedPlayer(String player);

        void spinBottle();

        void addPlayer(String player);

    }

    interface Presenter extends BasePresenter {

        void loadPlayers();

    }
}
