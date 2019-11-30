//Classe che tiene traccia delle statistiche dei pacchetti di cui se ne vuole tener traccia
public class PINGStatTracker {

    protected long packetsSendTime[];
    protected long packetsRTT[];
    protected int packetsSent;
    protected int packetsLost;
    protected int packetsReceived;
    protected float percentPacketLoss;
    protected long minRTT;
    protected long maxRTT;
    protected long avgRTT;

    public PINGStatTracker(int packetsNumber){
        packetsSendTime = new long[packetsNumber];
        packetsRTT = new long[packetsNumber];
        packetsSent = packetsNumber;
        percentPacketLoss = 0;
        avgRTT = 0;
        packetsReceived = 0;
    }

    //Metodo da invocare ogni volta che si invia un pacchetto, che registra il momento in cui viene effettuato l' invio
    public String updateSend(int packetID){
        long sentTime = System.currentTimeMillis();
        packetsSendTime[packetID] = sentTime;
        return " " + String.valueOf(sentTime);
    }
    //Metodo da invocare ogni volta che riceviamo un pacchetto, in modo da registrare il tempo che esso ha impiegato per
    //essere spedito dal server al client, con annessa stampa
    public void updateOnReceive(int packetID){
        packetsRTT[packetID] = System.currentTimeMillis() - packetsSendTime[packetID];
        packetsReceived++;
        System.out.println("PING " + packetID + " " + packetsSendTime[packetID] + " RTT: " + packetsRTT[packetID] + " ms");
    }
    //Metodo da invocare ogni volta che un pacchetto è stato perso, con annessa stampa di avviso
    public void updateOnFailedReceive(int packetID){
        packetsRTT[packetID] = -1;
        packetsLost++;
        System.out.println("PING " + packetID + " " + packetsSendTime[packetID] + " RTT: *");
    }
    //Metodo per aggiornare ogni statistica finale dei pacchetti
    private void updateStatistics(){
        percentPacketLoss = ((float) packetsLost / (float) packetsSent) * 100;
        int i;
        minRTT = packetsRTT[0];
        maxRTT = packetsRTT[0];
        for(i = 0; i < packetsSent; i++){
            if(packetsRTT[i] != -1){
                if(packetsRTT[i] < minRTT || minRTT == -1)
                    minRTT = packetsRTT[i];
                else if(packetsRTT[i] > maxRTT)
                    maxRTT = packetsRTT[i];

                avgRTT += packetsRTT[i];
            }
        }
        if(packetsReceived > 0)
            avgRTT = avgRTT / packetsReceived;
        else
            avgRTT = -1;
    }
    //Stampa dei risultati ottenuti
    public void printStatistics(){
        updateStatistics();
        System.out.println("---- PING Statistics ----");
        System.out.println(packetsSent + " packets transmitted, " + packetsReceived + " packets received, " + percentPacketLoss + "% packet loss");
        System.out.println("round trip (ms) min/avg/max = " + minRTT + "/" + avgRTT + "/" + maxRTT);
    }

}


