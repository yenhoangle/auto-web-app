/*
 * Yen Le
 * 20123455
 *
 * BuildCarModelOptions.java
 * Class which processes requests from the client in regarding to building an Automotive object and configuring an
 * Automotive object
 * */

package server;
import adapter.*;

public class BuildCarModelOptions extends ProxyAutomotive {
    ////////// PROPERTIES //////////
    private static final int WAITING = 0;
    private static final int REQUEST_BUILD_AUTO = 1;
    private static final int REQUEST_CONFIGURE_AUTO = 2;

    private int state = WAITING;

    ////////// CONSTRUCTORS //////////

    public BuildCarModelOptions() {

    }

    ////////// INSTANCE METHODS //////////

    public Object processRequest(Object obj) {
        Object toClient = null;
        String objString;
        int filetype = 0;
            objString = obj.toString();

            if (state == REQUEST_BUILD_AUTO) {
                //set file type
                if (objString.contains(":")) {
                    filetype = 1;
                }
                if (objString.contains("=")) {
                    filetype = 2;
                }

                //build auto and add to database - atm: prop file only
                addAuto(obj, filetype);
                toClient = "Automotive object successfully added to database\n"
                        + "Press any key to return to main menu";

            } else if (state == REQUEST_CONFIGURE_AUTO) {
                //code for configureauto
                toClient = getAuto(objString);

            } else {
                System.out.println("Cannot process request");
            }

            this.state = WAITING;
        return toClient;
    }

    public int getRequest() {
        return state;
    }

    public String setRequest(int i) {
        String output = null;

        if (i == 1) {
            this.state = REQUEST_BUILD_AUTO;
            output = "Upload a file to create an Automotive";
        }
        else if (i == 2) {
            this.state = REQUEST_CONFIGURE_AUTO;
            output = "Select an Automotive from the following list to configure: \n" + listAutos();
        }
        else {
            output = "Invalid request";
        }

        return output;
    }
}
