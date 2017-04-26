
public class SourceText extends Subject {

    private String content;

    public SourceText (){
        this("");
    }
    public SourceText (String s){
        content = new String(s);
    }

    public String getText (){
        return content;
    }

    public void setText (String text){
        
        content = new String(text);
        
        //System.out.println(content);
        this.notifyObservers();
    }

}