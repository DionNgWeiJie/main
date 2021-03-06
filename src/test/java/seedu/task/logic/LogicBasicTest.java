package seedu.task.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.events.ui.JumpToTaskListRequestEvent;
import seedu.task.commons.events.ui.ShowHelpEvent;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.TaskBook;
import seedu.task.storage.StorageManager;

//@@author A0144702N-reused
public class LogicBasicTest {
	 /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    protected Model model;
    protected Logic logic;

    //These are for checking the correctness of the events raised
    protected ReadOnlyTaskBook latestSavedTaskBook;
    protected boolean helpShown;
    protected int targetedJumpIndex;

    /******************************Event Subscription********************/
    
    @Subscribe
    private void handleLocalModelChangedEvent(TaskBookChangedEvent abce) {
        latestSavedTaskBook = new TaskBook(abce.data);
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToTaskListRequestEvent(JumpToTaskListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }
    
    @Subscribe
    private void handleJumpToEventListRequestEvent(JumpToTaskListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }
    
    
    /******************************Pre and Post set up*****************************/
    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskBookFile = saveFolder.getRoot().getPath() + "TempTaskBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskBook = new TaskBook(model.getTaskBook()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

}
