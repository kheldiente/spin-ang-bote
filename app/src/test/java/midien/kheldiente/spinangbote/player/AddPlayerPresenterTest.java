package midien.kheldiente.spinangbote.player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the implementation of {@link AddPlayerPresenter}
 */
public class AddPlayerPresenterTest {

    private static List<String> PLAYERS;

    @Mock
    private AddPlayerContract.View mAddPlayerView;

    private AddPlayerPresenter mAddPlayerPresenter;

    @Before
    public void setupAddPlayerPresenter() {
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddPlayerPresenter = new AddPlayerPresenter(mAddPlayerView);
    }

    @Test
    public void createPresenter_setsThePresenterView() {
        // Get a reference to the class under test
        mAddPlayerPresenter = new AddPlayerPresenter(mAddPlayerView);

        // The the presenter is set to the view
        verify(mAddPlayerView).setPresenter(mAddPlayerPresenter);
    }

    @Test
    public void createPlayers_showAddPlayerView() {
        mAddPlayerPresenter.addPlayer("Michael");
        mAddPlayerPresenter.addPlayer("Joem");
        mAddPlayerPresenter.addPlayer("Jr");
        // Then add more view set to hidden
        verify(mAddPlayerView, times(3)).showAddPlayerView();
    }

    @Test
    public void createPlayerView_reachedMaxPlayers() {
        mAddPlayerPresenter.addPlayer("Michael");
        mAddPlayerPresenter.addPlayer("Joem");
        mAddPlayerPresenter.addPlayer("Jr");
        mAddPlayerPresenter.addPlayer("Imelda");
        mAddPlayerPresenter.addPlayer("Joelito");
        mAddPlayerPresenter.addPlayer("Ate");
        mAddPlayerPresenter.addPlayer("Laylay");
        mAddPlayerPresenter.addPlayer("Ester");
        verify(mAddPlayerView, times(1)).hideAddMoreView();
    }

}
