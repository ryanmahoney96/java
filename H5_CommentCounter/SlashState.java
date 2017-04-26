
public class SlashState extends CharacterState {

    private static SlashState instance;

    //we have encountered a slash in code, and must determine if it is division or a comment
    private SlashState(){

    }

    public static SlashState getInstance(){

        if(instance == null){
            instance = new SlashState();
        }

        return instance;
    }

    //any "regular" character after a slash means it was a division symbol
    public void charEvent(CommentCounter c){

        c.changeState(CodeState.getInstance());
        c.incCode();
        c.incCode();

    }

    //this is the start of either a multiline comment or a javadoc comment
    public void starEvent(CommentCounter c){

        c.changeState(SlashStarState.getInstance());
    }

    //this is the start of a single line comment
    public void slashEvent(CommentCounter c){

        c.changeState(SingleLineState.getInstance());
        c.incSingle();
        c.incSingle();

    }

    //code with quotes in this order will not compile (based on my test)
    public void quoteEvent(CommentCounter c){
        //
    }

    //a newline after a slash is a division symbol, so we bounce back to code
    public void newlineEvent(CommentCounter c){

        c.changeState(CodeState.getInstance());
        c.incCode();
    }

}