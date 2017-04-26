
public class CodeState extends CharacterState {

    private static CodeState instance;

    private CodeState(){
    }

    public static CodeState getInstance(){

        if(instance == null){
            instance = new CodeState();
        }

        return instance;
    }

    public void charEvent(CommentCounter c){
        c.incCode();
    }

    public void starEvent(CommentCounter c){
        c.incCode();
    }

    //we are in code, this may be a division or the start of a type of comment
    public void slashEvent(CommentCounter c){
        c.changeState(SlashState.getInstance());
    }

    //the start of a string, may be important
    public void quoteEvent(CommentCounter c){
        c.changeState(StringState.getInstance());
        c.incCode();
    }

    //newlines are not counted and are not state changing for code
    public void newlineEvent(CommentCounter c){
        //
    }

}