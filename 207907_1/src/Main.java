package src;
/*  KE Lab MCA II Year, Semester 4 
    author : Amrit Pandey, MCA, 207907
    date: 07-01-2022
    Assignment 1 : KE Lab

    How to run? -> Please read attention.txt file.
*/

import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        // main method to run the application
        System.out.println("ASSIGNMENT 1");
        while (true) {
            System.out.println("[1] Creation of Transaction Database (with sample data)");
            System.out.println("[2] Creation of Transaction Database (user input)");
            System.out.println("[3] Read Transactions from file");
            System.out.println("[4] Find the support");
            System.out.println("[5] Extract item - transaction list");
            System.out.println("[6] Delete database");
            System.out.println("[0] Exit");
            System.out.print("[IN] >> ");
            int n = sc.nextInt();
            // clearScreen();
            System.out.println("\n<out>");
            switch (n) {
                case 0:
                    System.exit(0);
                case 1:
                    _1_TransactionDB.create();
                    break;
                case 2:
                    _1_TransactionDB.create(false);
                    break;
                case 3:
                    _2_ReadTransaction.read();
                    break;
                case 4:
                    _3_FindSupport.execute();
                    break;
                case 5:
                    _4_ExtractItems.extract();
                    break;
                case 6:
                    _1_TransactionDB.delete();
                    break;
            }
            System.out.println("</out>\n");
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}