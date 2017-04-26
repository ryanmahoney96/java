
public class SlashStarState extends CharacterState {

private static SlashStarState instance;

    //we may be in a multiline comment or a javadoc comment
    private SlashStarState(){

    }

    public static SlashStarState getInstance(){

        if(instance == null){
            instance = new SlashStarState();
        }

        return instance;
    }

    //any "regular" character means we must be in a multiline comment
    // i.e /*aaa*/
    public void charEvent(CommentCounter c){

        c.changeState(MultilineState.getInstance());
        c.incMulti();
        c.incMulti();
        c.incMulti();
    }

    //another star means we are in a javadoc comment
    // i.e. /**aaa*/
    public void starEvent(CommentCounter c){

        c.changeState(JavadocState.getInstance());
        c.incJavadoc();
        c.incJavadoc();
        c.incJavadoc();
    }

    //this is essentially the same as any character other than a star
    public void slashEvent(CommentCounter c){

        charEvent(c);
    }

    public void quoteEvent(CommentCounter c){

        charEvent(c);
    }

    //this is different, because we do not count newline characters
    public void newlineEvent(CommentCounter c){

        c.changeState(MultilineState.getInstance());
        c.incMulti();
        c.incMulti();
    }

}