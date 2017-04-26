
public class Server extends Subject implements Observer, ServerInterface {

    //reference to SourceText, the base string displayed on all GUIs
    private SourceText st;

    public Server (SourceText temp){
        st = temp;
        st.attach(this);
    }

    public void update (){
        
        //send updates up to ClientProxy
        //System.out.println("Update Mandate in Server");

        //notifies all the client proxies 'looking' at this server (and the source text, by extension)
        notifyObservers();
    }

    @Override
    public void setText (String setString){
        
        //update Source Text using the string obtained up the chain
        st.setText(setString);
    }

    public String getText (){
        //return the source text's string
        return st.getText();
    }

    public void closeConnection(){
    }

}