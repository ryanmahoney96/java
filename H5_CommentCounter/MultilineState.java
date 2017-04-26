
public class MultilineState extends CharacterState {

    private static MultilineState instance;

    //this is used to determine the end of the comment. If the starCheck is true (we just previously saw a star as the last char) and then we get a slash, we exit the comment. Until then, starCheck gets set to false
    private boolean starCheck;

    //we are inside a multiline comment
    private MultilineState(){
        starCheck = false;
    }

    public static MultilineState getInstance(){

        if(instance == null){
            instance = new MultilineState();
        }

        return instance;
    }

    public void charEvent(CommentCounter c){
        
        c.incMulti();
        starCheck = false;
    }

    public void starEvent(CommentCounter c){
        
        c.incMulti();
        starCheck = true;
    }

    //we put a check here to determine if the last character was a star
    public void slashEvent(CommentCounter c){
        
        c.incMulti();

        if(starCheck == true){
            
            starCheck = false;
            c.changeState(CodeState.getInstance());
        }
    }

    public void quoteEvent(CommentCounter c){
        
        c.incMulti();
        starCheck = false;
    }

    public void newlineEvent(CommentCounter c){
        
        starCheck = false;
    }

}