package persistence.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ActionQueue {

    private final Queue<InsertAction> insertActions = new LinkedList<>();
    private final Queue<UpdateAction> updateActions = new LinkedList<>();
    private final Queue<DeleteAction> deleteActions = new LinkedList<>();

    private final List<Queue<?>> ORDERED_ACTIONS = List.of(insertActions, updateActions, deleteActions);

    public void addInsertAction(InsertAction action) {
        insertActions.add(action);
    }

    public void addUpdateAction(UpdateAction action) {
        updateActions.add(action);
    }

    public void addDeleteAction(DeleteAction action) {
        deleteActions.add(action);
    }

    public void executeActions() {
        for(Queue<?> actions : ORDERED_ACTIONS) {
            executeActions(actions);
        }
    }

    private void executeActions(Queue<?> actions) {
        while(!actions.isEmpty()) {
            EntityAction action = (EntityAction) actions.poll();
            action.execute();
        }
    }
}
