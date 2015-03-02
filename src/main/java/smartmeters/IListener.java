package smartmeters;

/**
 * Created by nikolay on 02.03.15.
 */
public interface IListener {
    public void onCreated(String id);
    public void onUpdated(String id, String data);
}
