/*
 * Yen Le
 * 20123455
 *
 * FileIO.java
 * Class which handles file input/output for the program. It contains methods to build an Automotive object from text
 * and prop files
 * */

package util;
import java.util.*;
import java.io.*;

import exception.AutoException;
import model.Automotive;
import model.OptionSet;

public class FileIO {
    int filetype;
    public Automotive buildAutoObject(String filename) throws  AutoException {
        try {
            try {
                File testFile = new File(filename);
                if (!testFile.exists() || testFile.length() == 0) {
                    throw new AutoException(1);
                }
            } catch (AutoException ae) {
                ae.log();
                System.out.println("Bad file, please check file and run the program again");
                System.exit(0);
            }

            FileReader fileReader = new FileReader(filename);
            BufferedReader buffer = new BufferedReader(fileReader);
            //creates a boolean value to keep track of end of file
            boolean eof = false;
            //first line has auto make, model, base price, and size of opset array
            String autoString = buffer.readLine();
            //file is empty
            try {
                if (autoString.isEmpty() || autoString.equals(" ")) {
                    throw new AutoException(1);
                }
            } catch (AutoException ae) {

            }
            String[] baseAuto = autoString.split(":");
            String autoMake = baseAuto[0];
            String autoModel = baseAuto[1];
            String autoYear = baseAuto[2];
            Float basePrice = Float.parseFloat((baseAuto[3]));
            int opsetNum = Integer.parseInt((baseAuto[4])); //total option sets
            Automotive auto = new Automotive(autoMake, autoModel, autoYear, basePrice);

            //throw exception if make is empty
            try {
                if (baseAuto[0].equals(" ") || baseAuto[0].isEmpty()) {
                    throw new AutoException(2);
                }
            } catch (AutoException ae) {
                ae.fix(2, auto);
                ae.log();

            }
            //throw exception if model is empty
            try {
                if (baseAuto[1].equals(" ") || baseAuto[1].isEmpty()) {
                    throw new AutoException(6);
                }
            } catch (AutoException ae) {
                ae.fix(6, auto);
                ae.log();

            }

            //throw exception if year is empty
            try {
                if (baseAuto[2].equals(" ") || baseAuto[2].isEmpty()) {
                    throw new AutoException(6);
                }
            } catch (AutoException ae) {
                ae.fix(7, auto);
                ae.log();

            }
            //throw exception if base price is negative
            try {
                if (basePrice < 0) {
                    throw new AutoException(3);
                }
            } catch (AutoException ae) {
                ae.fix(3, auto);
                ae.log();
            }

            while (!eof) {
                for (int i = 0; i < opsetNum; i++) {
                    //get a new optionset
                    String line = buffer.readLine();
                    //split line into name : length
                    String[] pair = line.split(":");
                    int opSize = Integer.parseInt(pair[1]);
                    auto.addOpset(pair[0]);
                    try {
                        if (pair[0].equals("") || pair[0].equals(" ")) {
                            throw new AutoException(4);
                        }
                    } catch (AutoException ae) {
                        ae.fix(4, auto);
                        ae.log();
                    }
                    buffer.readLine(); //skips (
                    //loop for the option array itself
                    for (int j = 0; j < opSize; j++) {
                        String option = buffer.readLine();
                        //split option into name : price
                        String[] namePrice = option.split(":");
                        String opName = namePrice[0];
                        try {

                            if (opName.equals("")) {
                                throw new AutoException(5);
                            }
                        } catch (AutoException ae) {
                            ae.fix(5, auto);
                            ae.log();
                        }
                        float opPrice = Float.parseFloat(namePrice[1]);
                        auto.addOption(i, opName, opPrice);
                    }
                    //next line is ), skip it
                    buffer.readLine();
                }
                eof = true;
            }
            buffer.close();
            fileReader.close();
            return auto;
        } catch (FileNotFoundException fnf) {
            System.out.println("FNF");
            throw new AutoException(1);
        } catch (NullPointerException npe) {
            System.out.println("NPE");
        } catch (IOException e) {
            System.out.println("Error");
        }
        return null;
    }

    //new buildAutoObject method to accommodate prop files
    public Automotive buildAutoObject(String filename, int filetype) throws AutoException {
        this.filetype = filetype;
        switch(filetype) {
            case 1:
                return buildAutoObject(filename);
            case 2:
                //uses our new method to build from prop files
                return buildFromPropFile(filename);
            default:
                throw new AutoException();
        }
    }


    public Automotive buildFromPropFile(String filename) throws AutoException {
        filetype = 2;
        Automotive auto;
        try {
            //convert text file to Properties object
            FileInputStream fis = new FileInputStream((filename));
            Properties props = new Properties();
            props.load(fis);
            auto = buildAutoObject(props);

        } catch (IOException ioe) {
            throw new AutoException();
        }
        return auto;
    }

    public Automotive buildAutoObject(Object propString) {
        Automotive auto;
        Properties props = (Properties) propString;

        String key, make, model, year;
        float baseprice;


        //for parsing props loop
        int letter = 65; //ascii value of A
        int counter = 1;
        String optionSetNameKey, optionNameKey, optionPriceKey;

        make = props.getProperty("Make");
        model = props.getProperty("Model");
        year = props.getProperty("Year");
        //need to convert to float
        baseprice = Float.parseFloat(props.getProperty("Baseprice"));
        auto = new Automotive(make, model, year, baseprice);

        optionSetNameKey = "OptionSet" + (char) letter;
        //loop through the prop files and add option set, option, op price
        while (props.getProperty(optionSetNameKey) != null) {
            String opsetName = props.getProperty(optionSetNameKey);
            auto.addOpset(opsetName); //add the optionSet
            optionNameKey = "Option" + ((char) letter) + counter; // hopefully this is OptionA1 and not OptionB
            optionPriceKey = "OptionValue" + ((char) letter) + counter;

            while (props.getProperty(optionNameKey) != null) {
                String opName = props.getProperty(optionNameKey);
                float opPrice = Float.parseFloat(props.getProperty(optionPriceKey));
                auto.addOption(opsetName, opName, opPrice);
                counter++;

                optionNameKey = "Option" + ((char) letter) + counter; //get the next option keys
                optionPriceKey = "OptionValue" + ((char) letter) + counter;
            }
            letter++;
            counter = 1; //reset counter for next option set
            optionSetNameKey = "OptionSet" + (char) letter; //get the next optionset key
        }
        return auto;

    }



    //not a requirement to parse a file content into a auto
    public Automotive buildFromTextContent(Object fileStringBuffer) {
        String fString = fileStringBuffer.toString();
        Automotive builtAuto;
        String fname = "C:\\Users\\Arteh\\IdeaProjects\\CIS35B_HW5_Server\\textfiles\\sentAuto.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fname));
            writer.write(fString);
            builtAuto = buildAutoObject(fname);
            return builtAuto;

        } catch (IOException io) {

        } catch (AutoException ae) {

        }
        return null;
    }

    public int getFileType() {
        return filetype;
    }

    public void serialize(String filename, Automotive a1) throws AutoException {
        try {
            FileOutputStream foStream = new FileOutputStream(filename);
            ObjectOutputStream ooStream = new ObjectOutputStream(foStream);
            ooStream.writeObject(a1);
            //closing streams after done writing
            ooStream.close();
            foStream.close();
        } catch (FileNotFoundException fnf) {
            throw new AutoException(1);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Automotive deserialize(String filename) throws AutoException {
        if (filename == null) return null;
        Automotive autoObj = null;
        try {
            FileInputStream fiStream = new FileInputStream(filename);
            ObjectInputStream oiStream = new ObjectInputStream(fiStream);
            autoObj = (Automotive) oiStream.readObject();
            //closing streams after done reading
            oiStream.close();
            fiStream.close();
        } catch (FileNotFoundException fnf) {
            throw new AutoException(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return autoObj;
    }
}

