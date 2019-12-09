package adapter;
import exception.AutoException;
import java.util.Properties;

public interface CreateAuto {
    public void buildAuto(String filename) throws AutoException;
    public void buildAuto(String filename, int fileType) throws AutoException;
    public void buildAuto(Properties props) throws AutoException;
    public void printAuto(String filename);
}
