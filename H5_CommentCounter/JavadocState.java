
public class JavadocState extends CharacterState {

    private static JavadocState instance;

    //see: multilineState.java
    private boolean starCheck;

    //we are in a javadoc comment, which operates identically to the multiline
    private JavadocState(){
        starCheck = false;
    }

    public static JavadocState getInstance(){

        if(instance == null){
            instance = new JavadocState();
        }

        return instance;
    }

    public void charEvent(CommentCounter c){
        
        c.incJavadoc();
        starCheck = false;
    }

    public void starEvent(CommentCounter c){
        
        c.incJavadoc();
        starCheck = true;
    }

    public void slashEvent(CommentCounter c){
        
        c.incJavadoc();

        if(starCheck == true){
            
            starCheck = false;
            c.changeState(CodeState.getInstance());
        }
    }

    public void quoteEvent(CommentCounter c){
        
        c.incJavadoc();
        starCheck = false;
    }

    public void newlineEvent(CommentCounter c){
        
        starCheck = false;
    }


}