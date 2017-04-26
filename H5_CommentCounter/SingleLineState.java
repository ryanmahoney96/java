

public class SingleLineState extends CharacterState {

    private static SingleLineState instance;

    //we are in the middle of a single line comment
    private SingleLineState(){

    }

    public static SingleLineState getInstance(){

        if(instance == null){
            instance = new SingleLineState();
        }

        return instance;
    }

    public void charEvent(CommentCounter c){
        c.incSingle();
    }

    public void starEvent(CommentCounter c){
        c.incSingle();
    }

    public void slashEvent(CommentCounter c){
        c.incSingle();
    }

    public void quoteEvent(CommentCounter c){
        c.incSingle();
    }

    //newlines in a single line comment end the comment, and we return to code
    public void newlineEvent(CommentCounter c){
        
        c.changeState(CodeState.getInstance());
    }

}