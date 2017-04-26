
public interface ServerInterface {

    public void setText(String sendString);
    public void attach(Observer o);
    public String getText();
    public void closeConnection();

}