package se.kth.iv1350.sem3.startup;

import se.kth.iv1350.sem3.controller.Controller;
import se.kth.iv1350.sem3.integration.SystemDelegator;
import se.kth.iv1350.sem3.view.View;

/**
 * Starts application by starting view which in turn allows for program to
 * initiate.
 */
public class Main {
    /**
     * The main method is used to start the entire application
     * 
     * @params args
     */
    public static void main(String[] args) throws Exception {
        SystemDelegator delegator = new SystemDelegator();
        Controller contr = new Controller(delegator);
        View view = new View(contr);
        view.runFakeExecution();
    }

}