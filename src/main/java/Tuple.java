/**
 * A map-like structure to make code cleaner
 */
public class Tuple {

    private String myString;
    private Integer myNumber;

    public Tuple(String myString, Integer myNumber) {
        this.myString = myString;
        this.myNumber = myNumber;
    }

    public void setMyNumber(Integer number) {
        this.myNumber = number;
    }

    public Integer getMyNumber() {
        return myNumber;
    }

    public String getMyString() {
        return myString;
    }

    public String toString() {
        return myString + " : " + myNumber;
    }

}
