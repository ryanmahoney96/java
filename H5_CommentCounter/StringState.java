
public class StringState extends CharacterState {

    private static StringState instance;

    private boolean backslashCheck;

    //we have encountered a quote within code, so we have entered a string
    private StringState(){
        backslashCheck = false;
    }

    public static StringState getInstance(){

        if(instance == null){
            instance = new StringState();
        }

        return instance;
    }

    //a character in a string
    public void charEvent(CommentCounter c){
        c.incCode();
        backslashCheck = false;
    }

    public void starEvent(CommentCounter c){
        c.incCode();
        backslashCheck = false;
    }

    public void slashEvent(CommentCounter c){
        c.incCode();
        backslashCheck = false;
    }

    //this is the end of a string
    //implement backslash bool
    public void quoteEvent(CommentCounter c){

        if(backslashCheck != true){
            c.changeState(CodeState.getInstance());
        }

        c.incCode();
        backslashCheck = false;
    }

    public void backslashEvent(CommentCounter c){
        c.incCode();
        backslashCheck = true;
    }
}