import java.net.InetSocketAddress;

//a simple struct used to hold the client information, used to send update requests
public class UpdateAddress {

    private String ip;
    private int port;
    private InetSocketAddress addr;

    public UpdateAddress (String i, int p, InetSocketAddress a){
        ip = i;
        port = p;
        addr = a;
    }

    public String getIP (){
        return ip;
    }

    public int getPort (){
        return port;
    }

    public InetSocketAddress getFullAddress (){
        return addr;
    }

    @Override
    public String toString (){
        return getFullAddress().toString() + ", listening on port: " + port;
    }

}