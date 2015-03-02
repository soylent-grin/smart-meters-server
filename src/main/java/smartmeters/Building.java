package smartmeters;

/**
 * Created by nikolay on 02.03.15.
 */

public class Building {
    private int id;
    private String address;
    private int quarters;

    public Building(int _id, String _address, int _quarters) {
        id = _id;
        address = _address;
        quarters = _quarters;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public int getQuarters() {
        return quarters;
    }
}
