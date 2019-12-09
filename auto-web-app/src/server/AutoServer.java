/*
 * Yen Le
 * 20123455
 *
 * AutoServer.java
 * Interface which declares server methods
 * */

package server;
import model.Automotive;
public interface AutoServer {
    public Automotive getAuto(Object received);
    public String listAutos();
    public void addAuto(Object input, int filetype);
}
