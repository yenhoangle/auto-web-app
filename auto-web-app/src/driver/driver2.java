package driver;

import exception.AutoException;
import model.Automotive;
import util.FileIO;

public class driver2 {
    public static void main(String[] args) throws AutoException {
        FileIO fileIO = new FileIO();
        Automotive car = fileIO.buildAutoObject("C:\\Users\\Arteh\\IdeaProjects\\CIS35B_HW5_Server\\textfiles\\ffw.txt");
        System.out.println("Car attributes before serialization:\n");
        car.print();
        fileIO.serialize("C:\\Users\\Arteh\\IdeaProjects\\CIS35B_HW5_Server\\textfiles\\ffw.ser", car);
        Automotive newCar = fileIO.deserialize("C:\\Users\\Arteh\\IdeaProjects\\CIS35B_HW5_Server\\textfiles\\ffw.ser");
        System.out.println();
        System.out.println("Car attributes after serialization and deserialization:\n");
        newCar.print();
    }
}
