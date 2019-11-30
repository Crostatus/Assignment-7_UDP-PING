import java.net.*;
//Classe Client che spedisce 10 pacchetti al server, utilizzando un oggetto di tipo PINGStatTracker per tenere
//traccia delle statistiche necessarie per fornire un resoconto generale sull' andamento dei pacchetti spediti
public class PingClient {

    public static void main(String[] args) throws Exception{
        //Richiede 2 parametri:
        //args[0] : serve per indicare l' indirizzo del server, e può essere fornito come indirizzo ip o nome simbolico
        //args[1] : (int) numero di porta su cui spedire i pacchetti
        PINGStatTracker statTracker = new PINGStatTracker(10);
        DatagramSocket clientSocket = new DatagramSocket(4444);
        clientSocket.setSoTimeout(2000);
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(args[0]);
        }
        catch (Exception e0){
            try{
                ipAddress = InetAddress.getByAddress(args[0].getBytes());
            }
            catch (Exception e1){
                System.out.println("ERR - arg 1");
                System.exit(1);
            }
        }

        byte[] data = new byte[128];
        int i;
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(data, data.length, ipAddress, Integer.valueOf(args[1]));
        }
        catch (Exception e){
            System.out.println("ERR - arg 2");
            System.exit(1);
        }
        for(i = 0; i < 10; i++){
            String PINGMsg = formatMessage(i, statTracker);
            data = PINGMsg.getBytes();
            packet.setData(data);
            packet.setLength(data.length);
            clientSocket.send(packet);
            try {
                clientSocket.receive(packet);
                statTracker.updateOnReceive(i);
            }
            catch (SocketTimeoutException e) {
                statTracker.updateOnFailedReceive(i);
            }
        }
        statTracker.printStatistics();

    }

    private static String formatMessage(int PINGnumber, PINGStatTracker tracker){
        String message = "PING " + String.valueOf(PINGnumber);
        message += tracker.updateSend(PINGnumber);

        return message;
    }

}
