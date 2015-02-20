package smartmeters;

/**
 * Created by nikolay on 19.02.15.
 */

public class TestimonialStore {

    private static TestimonialStore instance = new TestimonialStore();
    private String data;

    public TestimonialStore() {

    }
    public static TestimonialStore getInstance() {
        return instance;
    }

    public String getData() {
        return data;
    }
    public void setData(String _data) {
        data = _data;
    }
}
